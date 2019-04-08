package com.tiefan.cps.web.controller;

import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Controller;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Controller
@RequestMapping(value = "/")
public class TestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestController.class);

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    /**
     * @return
     */
    @RequestMapping(value = "/")
    public ModelAndView index() {
        return new ModelAndView("index");
    }

    /**
     * @return
     */
    @RequestMapping(value = "/nopermission")
    public ModelAndView nopermission() {
        return new ModelAndView("nopermission");
    }


    static int i = 0;

    @RequestMapping(value = "/test/0")
    @ResponseBody
    public Map test() throws ExecutionException, InterruptedException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Map resMap = new HashMap();
        ListenableFuture<SendResult<String, String>> res = kafkaTemplate.send("xxxxx", 0, "key-" + i++, "123");
        if (res.isDone()) {
            SendResult<String, String> result = res.get();
            RecordMetadata recordMetadata = result.getRecordMetadata();
            resMap.put("topic", recordMetadata.topic());
            resMap.put("partition", recordMetadata.partition());
            resMap.put("offset", recordMetadata.offset());
            resMap.put("timestamp", sdf.format(recordMetadata.timestamp()));
        }
        return resMap;
    }


}
