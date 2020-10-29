package com.lt.es;

import com.lt.es.common.ESConstans;
import org.elasticsearch.action.search.*;
import org.elasticsearch.action.support.IndicesOptions;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.ScoreSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.jupiter.api.Test;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

/**
 * @author liangtao
 * @Date 2020/6/24
 * 等值（term查询：QueryBuilders.termQuery(name,value);
 * 多值(terms)查询:QueryBuilders.termsQuery(name,value,value2,value3...);
 * 范围（range)查询：QueryBuilders.rangeQuery(name).gte(value).lte(value);
 * 存在(exists)查询:QueryBuilders.existsQuery(name);
 * 模糊(wildcard)查询:QueryBuilders.wildcardQuery(name,+value+);
 * 组合（bool）查询: BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
 **/

public class ESQueryDemo {
    private RestHighLevelClient client = EsClient.getInstance();


    private SearchRequest initRequest() {
        SearchRequest searchRequest = new SearchRequest(ESConstans.INDEX);
        searchRequest.types(ESConstans.TYPE_DOC);
        return searchRequest;
    }

    private SearchRequest initRequestByAll() {
        return new SearchRequest();
    }


    /**
     * 查询全部
     */
    @Test
    public void allSearch() throws IOException {
//        SearchRequest searchRequest = initRequestByAll();
        SearchRequest searchRequest = initRequest();
        SearchSourceBuilder builder = new SearchSourceBuilder();
        builder.size(100);
        builder.query(QueryBuilders.matchAllQuery());
        searchRequest.source(builder);
//        Scroll scroll = new Scroll(TimeValue.MINUS_ONE);
//        searchRequest.scroll(scroll);

        SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
        printf(response);
    }

    /**
     * 排序:
     * 四种排序规则：
     * field:
     * score：
     * GeoDistance:
     * scriptSortBuilder:
     */
    public SearchResponse sordSearch() throws IOException {
        SearchRequest request = initRequestByAll();
        SearchSourceBuilder builder = new SearchSourceBuilder();
        builder.sort(new ScoreSortBuilder().order(SortOrder.DESC));
        builder.sort(new FieldSortBuilder("uid").order(SortOrder.ASC));
        //细粒度的设置返回数据的过滤和包含
        String[] includeFields = new String[]{"title", "user", "innerObject.*"};
        String[] excludeFields = new String[]{"_type"};
        builder.fetchSource(includeFields, excludeFields);
        SearchResponse response = EsClient.getInstance().search(request, RequestOptions.DEFAULT);
        return response;
    }

    /**
     * 聚合查询
     */
    @Test
    public void aggreation() throws IOException {
        SearchRequest request = initRequest();
        //聚合查询条件构造
        TermsAggregationBuilder aggregationBuilder = AggregationBuilders.terms("group_by_phone").field("phone");
        SearchSourceBuilder builder = new SearchSourceBuilder();
        builder.aggregation(aggregationBuilder);
        builder.size(0);
        request.source(builder);
        SearchResponse response = EsClient.getInstance().search(request, RequestOptions.DEFAULT);

        //聚合查询数据展示
        Aggregations aggregations = response.getAggregations();
        Terms groupByPhone = aggregations.get("group_by_phone");
        List<? extends Terms.Bucket> buckets = groupByPhone.getBuckets();
        buckets.stream().forEach(bucket -> {
            System.out.println("key: " + bucket.getKeyAsString() + " docCount: " + bucket.getDocCount());
        });
    }


    /**
     * 分页查询
     */
    @Test
    public void pageQueryByScroll() throws IOException {
        SearchRequest request = initRequest();
        SearchSourceBuilder builder = new SearchSourceBuilder();
        //查询全部
        builder.query(QueryBuilders.matchAllQuery());
        //一次查询50条数据
        builder.size(50);
        request.source(builder);
        //设置scroll时间间隔
        request.scroll(TimeValue.timeValueMinutes(1L));
        RestHighLevelClient client = EsClient.getInstance();
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        printf(response);
        //获取scrollid用于后续scroll请求
        String scrollId = response.getScrollId();
        while (response.getHits().getHits() != null && response.getHits().getHits().length > 0) {
            SearchScrollRequest scrollRequest = new SearchScrollRequest(scrollId);
            scrollRequest.scroll(TimeValue.timeValueSeconds(30L));
            response = client.scroll(scrollRequest, RequestOptions.DEFAULT);
            printf(response);
            scrollId = response.getScrollId();
        }
        //清理scroll
        ClearScrollRequest clearRequest = new ClearScrollRequest();
        clearRequest.addScrollId(scrollId);
        ClearScrollResponse clearScrollResponse = client.clearScroll(clearRequest, RequestOptions.DEFAULT);
        System.out.println("清理结果：" + clearScrollResponse.isSucceeded());

    }

    /**
     * 一般查询
     *
     * @throws IOException
     */
    public SearchResponse generateSearch() throws IOException {
        SearchRequest searchRequest = initRequest();

        //设置索引表达式
        searchRequest.indicesOptions(IndicesOptions.lenientExpandOpen());
        //查询选择本地分片,默认是集群分片
        searchRequest.preference("_local");
        //设置路由
//        searchRequest.routing("routing....");

        //查询条件构造
        SearchSourceBuilder builder = new SearchSourceBuilder();
        builder.query(QueryBuilders.termQuery("uid", "1234"))
                //分页
                .from(0).size(4)
                //超时
                .timeout(TimeValue.timeValueSeconds(2))
                //关闭source查询时，只获取数据对应的es库信息，不包含具体数据
//                .fetchSource(false);
                //排序，根据默认值进行排序
                .sort(new ScoreSortBuilder().order(SortOrder.DESC));
        //根据字段进行升序排序
//                .sort(new FieldSortBuilder("id").order(SortOrder.ASC))

        searchRequest.source(builder);
        System.out.println(" 普通查询的DSL语句:" + builder.toString());

        //进行同步查询
        SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
        //打印结果
        printf(response);

        return response;
    }


    /**
     * 或查询
     * SELECT * FROM test1 where (uid = 1234 or uid =12345)  and phone = 12345678909
     */
    public SearchResponse orSearch() throws IOException {
        SearchRequest searchRequest = initRequest();
        SearchSourceBuilder builder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        BoolQueryBuilder boolQueryBuilder2 = new BoolQueryBuilder();

        //or
        boolQueryBuilder2.should(QueryBuilders.termQuery("uid", 1234));
        boolQueryBuilder2.should(QueryBuilders.termQuery("uid", 12345));

        //and
        boolQueryBuilder.must(boolQueryBuilder2);
        boolQueryBuilder.must(QueryBuilders.termQuery("phone", "12345678909"));

        builder.query(boolQueryBuilder);
        System.out.println(" 普通查询的DSL语句:" + builder.toString());
        searchRequest.source(builder);
        SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
        printf(response);
        return response;
    }


    /**
     * 模糊查询
     * SELECT * FROM p_test where  message like '%lt%';
     *
     * @throws IOException
     */
    public SearchResponse likeSearch() throws IOException {
        SearchRequest searchRequest = initRequest();
        SearchSourceBuilder builder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();

        //★like
        boolQueryBuilder.must(QueryBuilders.wildcardQuery("message", "*search*"));
        builder.query(boolQueryBuilder);
        searchRequest.source(builder);
        System.out.println(" 普通查询的DSL语句:" + builder.toString());
        SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
        printf(response);
        return response;
    }


    /**
     * 多指查询 in
     * SELECT * FROM p_test where uid in (1,2)
     */
    public SearchResponse inSearch() throws IOException {
        SearchRequest searchRequest = initRequest();
        SearchSourceBuilder builder = new SearchSourceBuilder();

        //★ in: term【s】Query
        builder.query(QueryBuilders.termsQuery("uid", "1234", "13231"
        ));

        searchRequest.source(builder);

        SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
        printf(response);
        return response;
    }


    /**
     * 存在查询
     *
     * @throws IOException
     */
    public SearchResponse existSearch() throws IOException {
        SearchRequest searchRequest = initRequest();
        SearchSourceBuilder builder = new SearchSourceBuilder();

        //★ exist
        builder.query(QueryBuilders.existsQuery("msgcode"));
        System.out.println("存在查询的DSL语句:" + builder.toString());
        // 同步查询
        searchRequest.source(builder);
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        printf(searchResponse);
        return searchResponse;
    }

    /**
     * 范围
     */
    public SearchResponse rangeSearch() throws IOException {
        SearchRequest searchRequest = initRequest();
        SearchSourceBuilder builder = new SearchSourceBuilder();

        //range
        builder.query(QueryBuilders.rangeQuery("sendtime")
                .gte("2019-01-01 00:00:00")
                .lte(LocalDateTime.now().format(DateTimeFormatter.ofPattern(ESAddDemo.pattern))));
        searchRequest.source(builder);
        System.out.println("存在查询的DSL语句:" + builder.toString());
        searchRequest.source(builder);
        SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
        printf(response);
        return response;
    }

    /**
     * 正则
     */
    public SearchResponse regexSearch() throws IOException {
        SearchRequest searchRequest = initRequest();
        SearchSourceBuilder builder = new SearchSourceBuilder();

        builder.query(QueryBuilders.regexpQuery("message", "^lt"));
        searchRequest.source(builder);
        SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
        printf(response);
        return response;
    }


    public static void main(String[] args) throws IOException {
        ESQueryDemo esQueryDemo = new ESQueryDemo();
        esQueryDemo.allSearch();
//        esQueryDemo.generateSearch();
//        esQueryDemo.orSearch();
//        esQueryDemo.likeSearch();
//        esQueryDemo.inSearch();
//        esQueryDemo.rangeSearch();
//        esQueryDemo.regexSearch();
        EsClient.close();
    }

    public void printf(SearchResponse response) {
        RestStatus status = response.status();
        System.out.println("status = " + status);
        TimeValue took = response.getTook();
        System.out.println("took = " + took);
        Boolean terminatedEarly = response.isTerminatedEarly();
        System.out.println("terminatedEarly = " + terminatedEarly);
        Boolean timedOut = response.isTimedOut();
        System.out.println("timedOut = " + timedOut);
        int totalShards = response.getTotalShards();
        int successfulShards = response.getSuccessfulShards();
        int failedShards = response.getFailedShards();
        System.out.println("totalShards:" + totalShards + "  successfulShards: " + successfulShards + "  failedShards = " + failedShards);

        // 失败的原因
        for (ShardSearchFailure failure : response.getShardFailures()) {
            System.out.println("failure.reason() = " + failure.reason());
        }
        SearchHits hits = response.getHits();
        System.out.println("查询数据量：" + hits.totalHits);
        System.out.println("******************");

        hits.forEach(hit -> {
            System.out.println("id: " + hit.getId());
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            if (!CollectionUtils.isEmpty(sourceAsMap)) {
                sourceAsMap.forEach((k, v) -> System.out.println(k + ": " + v));
                System.out.println("=====================");
            }
        });
    }
}
