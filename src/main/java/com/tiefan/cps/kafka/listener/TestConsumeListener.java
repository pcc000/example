package com.tiefan.cps.kafka.listener;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.stereotype.Component;

/**
 * 测试消费者监听器
 */
@Component
public class TestConsumeListener<K, V> implements MessageListener<K, V> {

    private static Logger logger = LoggerFactory.getLogger(TestConsumeListener.class);

    /**
     * 监听器自动执行该方法 消费消息 自动提交offset 执行业务代码 （high level api
     * 不提供offset管理，不能指定offset进行消费）
     */
    @Override
    public void onMessage(ConsumerRecord<K, V> record) {
        String key = record.key() == null ? "" : record.key().toString();
        String value = record.value() == null ? "" : record.value().toString();
        logger.info("kafkaConsumer开始消费, topic={}, offset={}, key={}, value={}",
                record.topic(), record.offset(), key, value);
    }

}
