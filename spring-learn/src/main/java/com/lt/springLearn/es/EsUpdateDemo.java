package com.lt.springLearn.es;

import com.lt.springLearn.common.ESConstans;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * @author liangtao
 * @Date 2020/6/24
 **/
public class EsUpdateDemo {
    private RestHighLevelClient client = EsClient.getInstance();
    public static void main(String[] args) throws IOException {
        EsUpdateDemo esUpdateDemo = new EsUpdateDemo();
        esUpdateDemo.update();
        EsClient.close();
    }

    public void update() throws IOException {
        //需要库中存有
        String id = "1691b682b5f445849d3f3108ebb20d31";
        UpdateRequest updateRequest = new UpdateRequest(ESConstans.INDEX, ESConstans.TYPE_DOC, id);
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("uid", 13231);
        jsonMap.put("phone", 123235678909L);
        jsonMap.put("msgcode", 1);
        jsonMap.put("sendtime", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        jsonMap.put("message", "lt update Elasticsearch");
        updateRequest.doc(jsonMap);
        //upsert 方法表示如果数据不存在，那么就新增一条
        updateRequest.docAsUpsert(true);
        UpdateResponse response = client.update(updateRequest, RequestOptions.DEFAULT);
        System.out.println("============更新=========>id: " + response.getId() + " status: " + response.status().getStatus());

    }
}
