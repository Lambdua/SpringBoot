package com.lt.springLearn.es;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;

import java.io.IOException;

import static com.lt.springLearn.common.ESConstans.ES_IP;
import static com.lt.springLearn.common.ESConstans.ES_PORT;

/**
 * @author liangtao
 * @Date 2020/6/24
 **/
public class EsClient {
    private static class Instance{
        private static RestHighLevelClient client=new RestHighLevelClient(RestClient.builder(
                new HttpHost(ES_IP, ES_PORT)));
    }

    public static RestHighLevelClient getInstance(){
        return Instance.client;
    }



    public static void close(){
        if (getInstance()!=null){
            try {
                getInstance().close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
