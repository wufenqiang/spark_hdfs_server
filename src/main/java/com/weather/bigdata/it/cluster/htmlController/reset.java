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
@RequestMapping("/reset")
public class reset {
	
	@GetMapping("")
	public String reset() {
	    return "reset";
	}
	
	@GetMapping("helloworld")
	@ResponseBody
	public String hello() {
	    return "helloworld";
	}

}
