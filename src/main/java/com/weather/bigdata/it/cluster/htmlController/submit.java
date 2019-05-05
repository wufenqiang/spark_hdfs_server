package com.weather.bigdata.it.cluster.htmlController;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/submit")
public class submit {
    @GetMapping("")
    public String submit() {
        return "submit";
    }

    @GetMapping("helloworld")
    @ResponseBody
    public String hello() {
        return "helloworld";
    }
}
