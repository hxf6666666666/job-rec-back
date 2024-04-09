package org.example.jobrecback.controller;

import org.example.jobrecback.bean.ResultBean;
import org.example.jobrecback.service.PushService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/xfModel")
public class XFMessageController {
    @Autowired
    private PushService pushService;

    @GetMapping("/test")
    public ResultBean test(String uid, String text) {
        if (StringUtils.isEmpty(uid) || StringUtils.isEmpty(text)) {
            log.error("uid或text不能为空");
            return ResultBean.fail("uid或text不能为空");
        }
        return pushService.pushMessageToXFServer(uid, text);
    }
}
