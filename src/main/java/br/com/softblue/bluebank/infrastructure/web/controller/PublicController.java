package br.com.softblue.bluebank.infrastructure.web.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.softblue.bluebank.application.service.ContaBancariaService;
import br.com.softblue.bluebank.application.service.ExtratoService;
import br.com.softblue.bluebank.application.service.UsuarioService;
import br.com.softblue.bluebank.domain.contaBancaria.ContaBancaria;
import br.com.softblue.bluebank.domain.contaBancaria.ContaInexistenteException;
import br.com.softblue.bluebank.domain.usuario.Usuario;

@RestController
@RequestMapping("/public")
public class PublicController {
    
    @Autowired
    private UsuarioService usuarioService;
    
    @Autowired
    private ContaBancariaService contaBancariaService;
    
    @Autowired
    private ExtratoService extratoService;

    @PostMapping(value = "/nova-conta", produces = "application/json")
    public ResponseEntity<String> novaConta(@RequestBody Usuario usuario) {
	
	List<ContaBancaria> ContasBancarias = contaBancariaService.novaConta(usuario);
	
	for (ContaBancaria novaConta : ContasBancarias) {
	    usuarioService.saveUsuario(usuario, novaConta);
	}
	
	return new ResponseEntity<>("Conta Criada com sucesso!", HttpStatus.OK);

    }
    
    @PutMapping(value = "/deposito/{valor}", produces = "application/json")
    public ResponseEntity<String> deposito (@RequestBody ContaBancaria contaBancaria, @PathVariable BigDecimal valor) throws ContaInexistenteException {
	
	BigDecimal saldoAtual = contaBancaria.getSaldo();
		
	contaBancaria.setSaldo(saldoAtual.add(valor));
	
	contaBancariaService.save(contaBancaria);
	extratoService.save(contaBancaria.getUsuario(), "Depósito", valor);
	
	return new ResponseEntity<>("Depósito Realizado com sucesso!", HttpStatus.OK);
    }
    
    @GetMapping(value = "/buscar/{numeroDaConta}/{tipoDaConta}", produces = "application/json")
    public ResponseEntity<ContaBancaria> buscaContaBancaria(@PathVariable String  numeroDaConta, @PathVariable String tipoDaConta) throws ContaInexistenteException {
	
	ContaBancaria contaBD = contaBancariaService.pesquisaPorNumeroETipo(numeroDaConta, tipoDaConta);
	
	if(contaBD == null) {
	    throw new ContaInexistenteException("Conta Inexistente");
	}
	
	return new ResponseEntity<>(contaBD, HttpStatus.OK);
    }
}
