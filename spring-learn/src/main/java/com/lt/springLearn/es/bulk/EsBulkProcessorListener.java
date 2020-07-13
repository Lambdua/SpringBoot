package com.lt.springLearn.es.bulk;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;

/**
 * @author liangtao
 * @Date 2020/7/2
 **/
@Slf4j
public class EsBulkProcessorListener implements BulkProcessor.Listener {
//    Logger log= LoggerFactory.getLogger(getClass());
    @Override
    public void beforeBulk(long executionId, BulkRequest request) {
        //在每次bulk之前，进行request的数量统计
        int count = request.numberOfActions();
        log.info("{} 请求数量：{}", executionId, count);
    }

    @Override
    public void afterBulk(long executionId, BulkRequest request, BulkResponse response) {
        if (response.hasFailures()) {
            log.warn("Bulk [{}] executed with failures", executionId);
        } else {
            log.debug("Bulk [{}] completed in {} milliseconds", executionId, response.getTook().getMillis());
        }
    }

    @Override
    public void afterBulk(long executionId, BulkRequest request, Throwable failure) {
        //发生错误时调用
        log.error("failed to bulk {} with {}", executionId, failure);

    }

}
