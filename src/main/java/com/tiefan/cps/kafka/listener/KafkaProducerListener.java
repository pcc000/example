package com.tiefan.cps.kafka.listener;

import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.support.ProducerListener;


/**
 * kafkaProducer监听器，在producer配置文件中开启
 *
 * @author dingwen
 */
@SuppressWarnings("rawtypes")
public class KafkaProducerListener implements ProducerListener {

    private static Logger logger = LoggerFactory.getLogger(KafkaProducerListener.class);

    @Override
    public void onError(String topic, Integer partition, Object key,
                        Object value, Exception exception) {
        logger.error("kafka发送失败，key={}, value={}", key, value);
    }

    @Override
    public void onSuccess(String topic, Integer partition, Object key,
                          Object value, RecordMetadata recordMetadata) {
        logger.info("kafka发送成功，key={}, value={}", key, value);
    }

    @Override
    public boolean isInterestedInSuccess() {
        return true;
    }

}
