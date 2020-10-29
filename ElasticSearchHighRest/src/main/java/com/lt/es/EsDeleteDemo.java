package com.lt.es;

import com.lt.es.common.ESConstans;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;

import java.io.IOException;

/**
 * @author liangtao
 * @Date 2020/6/24
 **/
public class EsDeleteDemo {
    private RestHighLevelClient client = EsClient.getInstance();

    public static void main(String[] args) throws IOException {
        EsDeleteDemo esDeleteDemo = new EsDeleteDemo();
//        esDeleteDemo.deleteById();
        esDeleteDemo.deleteByQuery();
        EsClient.close();
    }

    public void deleteById() throws IOException {
        String id = "1";
        DeleteRequest deleteRequest = new DeleteRequest(ESConstans.INDEX, ESConstans.TYPE_DOC, id);
        //设置超时时间
        deleteRequest.timeout(TimeValue.timeValueMillis(2));
        // 设置刷新策略"wait_for"
        // 保持此请求打开，直到刷新使此请求的内容可以搜索为止。此刷新策略与高索引和搜索吞吐量兼容，但它会导致请求等待响应，直到发生刷新
        deleteRequest.setRefreshPolicy(WriteRequest.RefreshPolicy.WAIT_UNTIL);
        // 同步删除
        DeleteResponse response = client.delete(deleteRequest, RequestOptions.DEFAULT);
        System.out.println("======删除============>id: " + response.getId() + " status: " + response.status().getStatus());
    }


    /**
     * 根据条件进行删除
     */
    public void deleteByQuery() throws IOException {
        DeleteByQueryRequest queryRequest = new DeleteByQueryRequest(ESConstans.INDEX);
        queryRequest.setQuery(QueryBuilders.termQuery("uid", new Integer(1234)));
        //同步执行
        BulkByScrollResponse response = client.deleteByQuery(queryRequest, RequestOptions.DEFAULT);
        System.out.println("=========删除==========>batches: " + response.getBatches() + " status: " + response.getStatus());
    }

}
