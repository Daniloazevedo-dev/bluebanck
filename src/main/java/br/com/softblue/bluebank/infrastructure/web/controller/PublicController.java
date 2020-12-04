package br.com.softblue.bluebank.infrastructure.web.controller;

import java.math.BigDecimal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.softblue.bluebank.application.service.ContaBancariaService;
import br.com.softblue.bluebank.application.service.UsuarioService;
import br.com.softblue.bluebank.domain.contaBancaria.ContaBancaria;
import br.com.softblue.bluebank.domain.usuario.Usuario;

@RestController
@RequestMapping("/public")
public class PublicController {
    
    @Autowired
    private UsuarioService usuarioService;
    
    @Autowired
    private ContaBancariaService contaBancariaService;

    @PostMapping(value = "/nova-conta", produces = "application/json")
    public ResponseEntity<String> novaConta(@RequestBody Usuario usuario) {
	
	List<ContaBancaria> ContasBancarias = contaBancariaService.novaConta(usuario);
	
	for (ContaBancaria novaConta : ContasBancarias) {
	    usuarioService.saveUsuario(usuario, novaConta);
	}
	
	return new ResponseEntity<>("Conta Criada com sucesso!", HttpStatus.OK);

    }
    
    @PutMapping(value = "/deposito", produces = "application/json")
    public ResponseEntity<String> deposito (HttpServletRequest request) {
	
	String numeroDaConta = request.getParameter("numeroDaConta");
	String tipoDaConta = request.getParameter("tipoDaConta");
	BigDecimal deposito = new BigDecimal(request.getParameter("deposito"));
		
	ContaBancaria contaBD = contaBancariaService.pesquisaPorNumeroETipo(numeroDaConta, tipoDaConta);
	
	//TODO: Lançar excessao caso a conta destino não exista
	
	BigDecimal saldoAtual = contaBD.getSaldo();
	
	contaBD.setSaldo(saldoAtual.add(deposito));
	
	contaBancariaService.save(contaBD);
	
	return new ResponseEntity<>("Depósito Realizado com sucesso!", HttpStatus.OK);
    }
    
}
