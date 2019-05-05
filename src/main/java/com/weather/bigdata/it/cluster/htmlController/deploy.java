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
@RequestMapping("/deploy")
public class deploy {
	
	@GetMapping("")
	public String deploy() {
	    return "deploy";
	}
	
	@GetMapping("helloworld")
	@ResponseBody
	public String hello() {
	    return "helloworld";
	}

}
