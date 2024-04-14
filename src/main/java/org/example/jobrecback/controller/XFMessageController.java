package org.example.jobrecback.controller;

import org.example.jobrecback.bean.ResultBean;
import org.example.jobrecback.service.PushService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/xfModel")
public class XFMessageController {
    @Autowired
    private PushService pushService;

    @PostMapping("/test")
    public ResultBean test(@RequestParam("uid") String uid, @RequestParam("text") String text) {
        if (StringUtils.isEmpty(uid) || StringUtils.isEmpty(text)) {
            log.error("uid或text不能为空");
            return ResultBean.fail("uid或text不能为空");
        }
        return pushService.pushMessageToXFServer(uid, text);
    }
}
