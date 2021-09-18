package br.com.softblue.bluebank.infrastructure.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class indexController {
	
	@GetMapping(path = "home")
	public String home() {
		return "home";
	}
	
	@GetMapping(path = "extrato")
	public String extrato() {
		return "extrato";
	}
	
	@GetMapping(path = "transferencia")
	public String transferencia() {
		return "transferencia";
	}

}
