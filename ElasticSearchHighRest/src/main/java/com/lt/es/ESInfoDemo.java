package com.lt.es;

import org.elasticsearch.action.main.MainResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.junit.Test;

import java.io.IOException;

/**
 * @author liangtao
 * @Date 2020/7/3
 * ES库信息
 **/
public class ESInfoDemo {
    private RestHighLevelClient client=EsClient.getInstance();


    @Test
    public void showInfo() throws IOException {
        MainResponse info = client.info(RequestOptions.DEFAULT);
        System.out.println("info.getClusterName() = " + info.getClusterName());
        System.out.println("info.getClusterUuid() = " + info.getClusterUuid());
        System.out.println("info.getBuild() = " + info.getBuild());
        System.out.println("info.getNodeName() = " + info.getNodeName());
        System.out.println("info.getVersion() = " + info.getVersion());
    }
}
