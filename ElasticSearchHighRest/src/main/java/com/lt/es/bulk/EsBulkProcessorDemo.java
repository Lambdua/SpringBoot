package com.lt.es.bulk;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import com.lt.es.EsClient;
import com.lt.es.common.ESConstans;
import org.elasticsearch.action.bulk.BackoffPolicy;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author liangtao
 * @Date 2020/7/2
 * Es批处理简化操作
 * BulkProcessor 简化bulk API的使用，并且使整个批量操作透明化。
 * BulkProcessor 的执行需要三部分组成：
 * RestHighLevelClient :执行bulk请求并拿到响应对象。
 * BulkProcessor.Listener：在执行bulk request之前、之后和当bulk response发生错误时调用。
 * ThreadPool：bulk request在这个线程池中执行操作，这使得每个请求不会被挡住，在其他请求正在执行时，也可以接收新的请求。
 **/
public class EsBulkProcessorDemo {
    public void bulkUser() {


        EsBulkProcessorListener listener = new EsBulkProcessorListener();
        BulkProcessor.Builder builder = BulkProcessor.builder(EsClient.getInstance()::bulkAsync, listener);
        //在这里调用build()方法构造bulkProcessor,在底层实际上是用了bulk的异步操作
        BulkProcessor bulkProcessor = builder.build();

        //执行多少次动作后刷新bulk.默认1000，-1禁用
        builder.setBulkActions(500);
        //执行的动作大小超过多少时，刷新bulk。默认5M，-1禁用
        builder.setBulkSize(new ByteSizeValue(1L, ByteSizeUnit.MB));
        //最多允许多少请求同时执行。默认是1，0是只允许一个。
        builder.setConcurrentRequests(2);
        //设置刷新bulk的时间间隔。默认是不刷新的。
        builder.setFlushInterval(TimeValue.timeValueSeconds(10L));
        //设置补偿机制参数。由于资源限制（比如线程池满），批量操作可能会失败，在这定义批量操作的重试次数。
        builder.setBackoffPolicy(BackoffPolicy.constantBackoff(TimeValue.timeValueSeconds(1L), 3));

        //新建三个 index 请求
        IndexRequest one = new IndexRequest(ESConstans.INDEX, ESConstans.TYPE_DOC, "1").
                source(XContentType.JSON, "title", "In which order are my Elasticsearch queries executed?", "age", 12);
        IndexRequest two = new IndexRequest(ESConstans.INDEX, ESConstans.TYPE_DOC, "2")
                .source(XContentType.JSON, "title", "Current status and upcoming changes in Elasticsearch", "age", 13);
        IndexRequest three = new IndexRequest(ESConstans.INDEX, ESConstans.TYPE_DOC, "3")
                .source(XContentType.JSON, "title", "The Future of Federated Search in Elasticsearch", "age", 14);

        //加入bulk批处理中
        bulkProcessor.add(one).add(two).add(three);

        //所有需要加入的添加完成后，关闭
        bulkProcessor.close();
        try {
            bulkProcessor.awaitClose(30l, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


    private static String[] phones=new String[]{"13904449339","3233434242413","35484395345043","4358454034"};
    public static void bulkAddDataToEs(int dataCount) {
        BulkProcessor build = BulkProcessor.builder(EsClient.getInstance()::bulkAsync, new EsBulkProcessorListener()).build();
        for (int i = 0; i < dataCount; i++) {
            IndexRequest request = new IndexRequest(ESConstans.INDEX, ESConstans.TYPE_DOC);
            Map<String,Object> jsonMap=new HashMap<>();
            jsonMap.put("uid", RandomUtil.randomInt());
            jsonMap.put("phone", phones[RandomUtil.randomInt(4)]);
            jsonMap.put("msgcode",RandomUtil.randomInt(10));
            jsonMap.put("sendTime", DateUtil.now());
            jsonMap.put("msg","批量新增数据");
            request.source(jsonMap);
            build.add(request);
        }

        build.close();
        try {
            build.awaitClose(30l,TimeUnit.SECONDS);
        } catch (InterruptedException e) {
//            e.printStackTrace();
        }
    }

    public static void main(String[] args)  {
        bulkAddDataToEs(900);
    }
}
