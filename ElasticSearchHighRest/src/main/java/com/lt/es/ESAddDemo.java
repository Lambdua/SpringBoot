package com.lt.es;

import com.lt.es.common.ESConstans;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.rest.RestStatus;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author liangtao
 * @Date 2020/6/24
 * es库的常用增删改查操作
 **/
public class ESAddDemo {
    private RestHighLevelClient client = EsClient.getInstance();
    public static String pattern="yyyy-MM-dd HH:mm:ss";

    public static void main(String[] args) throws IOException {
        ESAddDemo esAddDemo = new ESAddDemo();
//        esAddDemo.addByJson();
        esAddDemo.addByMap();
        esAddDemo.addByXContentBuilder();
        esAddDemo.client.close();
    }

    private IndexRequest initRequest() {
        //设置id
        String id = UUID.randomUUID().toString().replaceAll("-", "");
        //初始化request
        return new IndexRequest(ESConstans.INDEX, ESConstans.TYPE_DOC, id);
    }

    /**
     * @param
     * @return void
     * @description 新增数据，通过jsonString方式实现
     * @author liangtao
     * @date 2020/6/24
     **/
    public void addByJson() throws IOException {
        IndexRequest request = initRequest();
        //设置json数据
        String jsonData = "{\n" +
//                "    \"_id\": \"5ef2e38cc86f687057c60336\",\n" +
                "    \"index\": 0,\n" +
                "    \"guid\": \"3d43e713-bdf0-4304-8191-092f37298335\",\n" +
                "    \"isActive\": true,\n" +
                "    \"balance\": \"$2,681.34\",\n" +
                "    \"picture\": \"http://placehold.it/32x32\",\n" +
                "    \"age\": 22,\n" +
                "    \"eyeColor\": \"green\",\n" +
                "    \"name\": \"Shaw Fleming\",\n" +
                "    \"gender\": \"male\",\n" +
                "    \"company\": \"COMTEXT\",\n" +
                "    \"email\": \"shawfleming@comtext.com\",\n" +
                "    \"phone\": \"+1 (960) 435-2908\",\n" +
                "    \"address\": \"561 Monitor Street, Diaperville, Northern Mariana Islands, 8987\",\n" +
                "    \"about\": \"Deserunt labore esse anim qui ipsum minim veniam proident. Amet culpa nostrud sint adipisicing esse nisi amet duis fugiat incididunt esse. Consequat ut laborum ipsum incididunt excepteur voluptate duis. Irure aute commodo culpa eiusmod enim mollit enim proident voluptate.\\r\\n\",\n" +
                "    \"registered\": \"2014-01-25T01:18:42 -08:00\",\n" +
                "    \"latitude\": 29.260762,\n" +
                "    \"longitude\": 122.215068\n" +
                "  }";
        //文档源设置为index。
        request.source(jsonData, XContentType.JSON);
        IndexResponse response = client.index(request, RequestOptions.DEFAULT);
        RestStatus status = response.status();
        System.out.println("===================>id: " + response.getId() + " status: " + response.status().getStatus());
    }

    /**
     * @param
     * @return void
     * @description 新增数据，以map方式实现
     * @author liangtao
     * @date 2020/6/24
     **/
    public void addByMap() throws IOException {
        IndexRequest request = initRequest();
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("uid", 1234);
        jsonMap.put("phone", 12345678909L);
        jsonMap.put("msgcode", 1);
        jsonMap.put("sendtime", LocalDateTime.now().format(DateTimeFormatter.ofPattern(ESAddDemo.pattern)));
        jsonMap.put("message", "lt study Elasticsearch");
        request.source(jsonMap);
        IndexResponse response = client.index(request, RequestOptions.DEFAULT);
        System.out.println("===================>id: " + response.getId() + " status: " + response.status().getStatus());
    }


    public void addByMapBulk() throws IOException{
        IndexRequest request=new IndexRequest(ESConstans.ES_IP,ESConstans.TYPE_DOC);


    }

    /**
     * @param
     * @return void
     * @description 新增数据，通过XContentBuilder方式
     * @author liangtao
     * @date 2020/6/24
     **/
    public void addByXContentBuilder() throws IOException {
        IndexRequest request = initRequest();
        XContentBuilder builder = XContentFactory.jsonBuilder();
        //方式1
/*
        builder.startObject();
        {
            builder.field("uid", 1234);
            builder.field("phone", 12345678909L);
            builder.field("msgcode", 1);
            builder.timeField("sendtime", "2019-03-14 01:57:04");
            builder.field("message", "lt study Elasticsearch");
        }
        builder.endObject();
*/
        //方式2
        builder.startObject()
                .field("uid",1234)
                .field("phone", 12345678909L)
                .field("msgcode", 1)
                .timeField("sendtime", LocalDateTime.now().format(DateTimeFormatter.ofPattern(ESAddDemo.pattern)))
                .field("message", "lt study Elasticsearch");

        request.source(builder);
        IndexResponse response = client.index(request, RequestOptions.DEFAULT);
        System.out.println("===================>id: " + response.getId() + " status: " + response.status().getStatus());
    }
}
