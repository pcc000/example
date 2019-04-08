package com.tiefan.cps.web.controller;

import java.util.Arrays;

import com.tiefan.cps.intf.dto.sample.SampleNameItem;
import com.tiefan.cps.intf.page.Pager;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.tiefan.keel.fsp4server.FspService;

/**
 * 基于管理后台的接口
 * Created by chengyao on 2016/7/27.
 */
@Controller
@RequestMapping(value = "/sample")
@Profile({"local","cie","sit"})
public class SampleController {


    @FspService
    @RequestMapping(value = "/info/{id}", method = RequestMethod.GET)
    public SampleNameItem info(@PathVariable Integer id) {
        return SampleNameItem.random();
    }

    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @FspService
    public Pager<SampleNameItem> list(@RequestBody SampleNameItem param) {
        return new Pager<>(2, Arrays.asList(SampleNameItem.random(), param));
    }

}
