package com.lt.es;

import com.lt.es.common.ESConstans;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;

import java.io.IOException;

/**
 * @author liangtao
 * @Date 2020/6/24
 **/
public class EsClient {
    private static class Instance{
        /**
         * 用户名密码校验的
         */
        private static RestHighLevelClient client=new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost(ESConstans.ES_IP,ESConstans.ES_PORT)
                ).setHttpClientConfigCallback(new RestClientBuilder.HttpClientConfigCallback() {
                    @Override
                    public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder httpClientBuilder) {
                        httpClientBuilder.disableAuthCaching();
                        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
                        credentialsProvider.setCredentials(AuthScope.ANY,
                                new UsernamePasswordCredentials("elastic", "agile12345"));  //es
                        return httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
                    }
                })
        );
    }

//    public static void main(String[] args) {
//        RestHighLevelClient client = getInstance();
//    }

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
