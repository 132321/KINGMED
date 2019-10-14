package com.kingmed.applets.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 简化路径
 * @author BIN
 *
 */
@Controller
public class JumpController {
	
	@RequestMapping("/")
	public String index() {
		return "redirect:index.html";
	}

}
