package com.weather.bigdata.it.cluster.htmlController;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/mvregion")
public class mvregion {
    @GetMapping("")
    public String mvregion() {
        return "mvregion";
    }

    @GetMapping("helloworld")
    @ResponseBody
    public String hello() {
        return "helloworld";
    }
}
