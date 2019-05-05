package com.weather.bigdata.it.cluster.htmlController;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * thymeleaf模板
 * @author liweifeng
 *
 */
@Controller
@RequestMapping("/deployMyself")
public class deployMyself {
	
	@GetMapping("")
	public String deployMyself() {
	    return "deployMyself";
	}
	
	@GetMapping("helloworld")
	@ResponseBody
	public String hello() {
	    return "helloworld";
	}

}
