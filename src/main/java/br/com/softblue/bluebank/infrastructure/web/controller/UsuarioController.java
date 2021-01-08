package br.com.softblue.bluebank.infrastructure.web.controller;

import java.math.BigDecimal;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.softblue.bluebank.application.service.ContaBancariaService;
import br.com.softblue.bluebank.application.service.ExtratoService;
import br.com.softblue.bluebank.domain.contaBancaria.ContaBancaria;
import br.com.softblue.bluebank.domain.contaBancaria.ContaInexistenteException;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {
    
    @Autowired
    private ContaBancariaService contaBancariaService;
    
    @Autowired
    private ExtratoService extratoService;
    
    @PutMapping(value = "/saque", produces = "application/json")
    public ResponseEntity<String> sacar(HttpServletRequest request) throws ContaInexistenteException {
	
	String numeroDaConta = request.getParameter("numeroDaConta");
	String tipoDaConta = request.getParameter("tipoDaConta");
	BigDecimal saque = new BigDecimal(request.getParameter("saque"));
	
	ContaBancaria contaBD = contaBancariaService.pesquisaPorNumeroETipo(numeroDaConta, tipoDaConta);
	
	if(contaBD == null) {
	    throw new ContaInexistenteException("Conta Inexistente");
	}
	
	//TODO: Lançar excessao caso a conta destino não exista
	
	BigDecimal saldoAtual = contaBD.getSaldo();
	contaBD.setSaldo(saldoAtual.subtract(saque));
	
	contaBancariaService.save(contaBD);
	extratoService.save(contaBD.getUsuario(), "Saque", saque);
	
	return new ResponseEntity<>("Saque Realizado com sucesso!", HttpStatus.OK);
    }
    
    @GetMapping(value = "/saldo", produces = "application/json")
    public ResponseEntity<BigDecimal> saldo(HttpServletRequest request) throws ContaInexistenteException {
	
	String numeroDaConta = request.getParameter("numeroDaConta");
	String tipoDaConta = request.getParameter("tipoDaConta");
	
	ContaBancaria contaBD = contaBancariaService.pesquisaPorNumeroETipo(numeroDaConta, tipoDaConta);
	
	if(contaBD == null) {
	    throw new ContaInexistenteException("Conta Inexistente");
	}
	
	//TODO: Lançar excessao caso a conta destino não exista
	
	BigDecimal saldoAtual = contaBD.getSaldo();
	
	return new ResponseEntity<>(saldoAtual, HttpStatus.OK);
    }
    
    @PutMapping(value = "/transferencia", produces = "application/json")
    public ResponseEntity<String> transferencia(HttpServletRequest request) {
	
	String numeroDaContaRemetente = request.getParameter("numeroDaContaRemetente");
	String tipoDaContaRemetente = request.getParameter("tipoDaContaRemetente");
	
	String numeroDaContaDestinatario = request.getParameter("numeroDaContaDestinatario");
	String tipoDaContaDestinatario = request.getParameter("tipoDaContaDestinatario");
	
	BigDecimal valorTransferencia = new BigDecimal(request.getParameter("valorTransferencia"));
	
	ContaBancaria contaBDRemetente = contaBancariaService.pesquisaPorNumeroETipo(numeroDaContaRemetente, tipoDaContaRemetente);
	BigDecimal saldoAtualRemetente = contaBDRemetente.getSaldo();
	
	ContaBancaria contaBDDestinatario = contaBancariaService.pesquisaPorNumeroETipo(numeroDaContaDestinatario, tipoDaContaDestinatario);
	BigDecimal saldoAtualDestinatario = contaBDDestinatario.getSaldo();
	
	//TODO: Lançar excessões
	
	contaBDRemetente.setSaldo(saldoAtualRemetente.subtract(valorTransferencia));
	contaBDDestinatario.setSaldo(saldoAtualDestinatario.add(valorTransferencia));
	
	contaBancariaService.save(contaBDRemetente);
	contaBancariaService.save(contaBDDestinatario);
	extratoService.save(contaBDRemetente.getUsuario(), "Transfêrencia", valorTransferencia);
	extratoService.save(contaBDDestinatario.getUsuario(), "Depósito recebido", valorTransferencia);
	
	return new ResponseEntity<>("Transferência realizada com sucesso!", HttpStatus.OK);
	
    }
    
    @GetMapping(value = "/teste", produces = "application/json")
    public void teste() {
	System.out.println("Teste");
    }

}
