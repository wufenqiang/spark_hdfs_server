package com.weather.bigdata.it.cluster.controller;

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
@RequestMapping("/tl")
public class ThymeleafController {
	
	@GetMapping("")
	public String index() {
		return "index";
	}
	
	@GetMapping("helloworld")
	@ResponseBody
	public String hello() {
		return "helloworld";
	}

}
