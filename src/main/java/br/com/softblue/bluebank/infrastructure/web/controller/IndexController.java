package br.com.softblue.bluebank.infrastructure.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/public/")
public class IndexController {
	
	@GetMapping("/index")
	public String login() {
		return "index";
	}
	
	@GetMapping("/senha-recuperacao")
	public String senhaRecuperacao() {
		return "senha-recuperacao";
	}
	
	@GetMapping("/nova-conta")
	public String novaConta() {
		return "nova-conta";
	}
}
