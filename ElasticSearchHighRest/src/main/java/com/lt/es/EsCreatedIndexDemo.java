package com.lt.es;

import com.lt.es.common.ESConstans;
import org.elasticsearch.action.admin.indices.alias.Alias;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.get.GetIndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.lt.es.common.ESConstans.INDEX;


/**
 * @author liangtao
 * @Date 2020/6/24
 * es新建索引demo
 **/
public class EsCreatedIndexDemo {
    private RestHighLevelClient client = EsClient.getInstance();

    public static void main(String[] args) throws IOException {
        EsCreatedIndexDemo demo = new EsCreatedIndexDemo();
        demo.createIndex();
        EsClient.close();
    }

    public void createIndex() throws IOException {
        //setting的值
        Map<String, Object> setMapping = new HashMap<>();
        //设置分区数、副本数、缓存刷新时间
        setMapping.put("number_of_shards", 10);
        setMapping.put("number_of_replicas", 1);
        setMapping.put("refresh_interval", "5s");

        //设置类型
        Map<String, Object> keyword = new HashMap<>();
        keyword.put("type", "keyword");

        //设置类型
        Map<String, Object> lon = new HashMap<>();
        lon.put("type", "long");

        //设置类型
        Map<String, Object> date = new HashMap<>();
        date.put("type", "date");
        date.put("format", ESAddDemo.pattern);

        Map<String, Object> jsonMap2 = new HashMap<>();
        Map<String, Object> properties = new HashMap<>();

        //设置字段message信息
        properties.put("uid", lon);
        properties.put("phone", lon);
        properties.put("msgcode", lon);
        properties.put("message", keyword);
        properties.put("sendtime", date);

        Map<String, Object> mapping = new HashMap<>();
        mapping.put("properties", properties);
        jsonMap2.put(ESConstans.TYPE_DOC, mapping);

        GetIndexRequest getRequest = new GetIndexRequest();
        getRequest.indices(INDEX);
        getRequest.local(false);
        getRequest.humanReadable(true);
        boolean exists2 = client.indices().exists(getRequest, RequestOptions.DEFAULT);


        //如果存在就不创建了
        if (exists2) {
            System.out.println(INDEX + "索引库已经存在!");
            return;
        }
        // 开始创建库
        CreateIndexRequest request = new CreateIndexRequest(INDEX);
        // 加载数据类型
        request.settings(setMapping);
        //设置mapping参数
        request.mapping(ESConstans.TYPE_DOC, jsonMap2);
        //设置别名
        request.alias(new Alias("pancm_alias"));
        CreateIndexResponse createIndexResponse = client.indices().create(request, RequestOptions.DEFAULT);
        boolean falg = createIndexResponse.isAcknowledged();
        if (falg) {
            System.out.println("创建索引库:" + INDEX + "成功！");
        }
    }
}
