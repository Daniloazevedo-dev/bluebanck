package br.com.softblue.bluebank.infrastructure.web.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.softblue.bluebank.application.service.ContaBancariaService;
import br.com.softblue.bluebank.application.service.DataInicialException;
import br.com.softblue.bluebank.application.service.ExtratoService;
import br.com.softblue.bluebank.application.service.UsuarioService;
import br.com.softblue.bluebank.domain.Extrato.Extrato;
import br.com.softblue.bluebank.domain.contaBancaria.ContaBancaria;
import br.com.softblue.bluebank.domain.contaBancaria.ContaInexistenteException;
import br.com.softblue.bluebank.domain.contaBancaria.SaldoInsufucienteException;
import br.com.softblue.bluebank.domain.contaBancaria.ValorNegativoException;
import br.com.softblue.bluebank.domain.search.Search;
import br.com.softblue.bluebank.domain.usuario.Usuario;
import br.com.softblue.bluebank.infrastructure.web.security.SecurityUtils;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private ContaBancariaService contaBancariaService;

    @Autowired
    private ExtratoService extratoService;
    
    @Autowired
    private UsuarioService usuarioService;

    @PutMapping(value = "/saque/{tipoDaConta}/{numeroDaConta}/{valor}", produces = "application/json")
    public ResponseEntity<String> sacar(@PathVariable String tipoDaConta, @PathVariable String numeroDaConta,
	    @PathVariable BigDecimal valor)
	    throws ContaInexistenteException, ValorNegativoException, SaldoInsufucienteException {

	ContaBancaria contaBD = contaBancariaService.pesquisaPorNumeroETipo(tipoDaConta, numeroDaConta);

	if (contaBD == null) {
	    throw new ContaInexistenteException("Conta Inexistente.");
	}

	BigDecimal saldoAtual = contaBD.getSaldo();

	if (valor.compareTo(BigDecimal.ZERO) <= 0) {

	    throw new ValorNegativoException("Informe um valor acima de zero!");

	} else if (valor.compareTo(saldoAtual) == 1) {

	    throw new SaldoInsufucienteException("Saldo Insuficiente");

	} else {

	    contaBD.setSaldo(saldoAtual.subtract(valor));
	    contaBancariaService.save(contaBD);
	    extratoService.save(contaBD.getUsuario(), "Saque", valor, tipoDaConta);
	}

	return new ResponseEntity<>("Saque Realizado com sucesso!", HttpStatus.OK);
    }

    @GetMapping(value = "/saldo/{tipoDaConta}/{numeroDaConta}", produces = "application/json")
    public ResponseEntity<BigDecimal> saldo(@PathVariable String tipoDaConta, @PathVariable String numeroDaConta)
	    throws ContaInexistenteException {

	ContaBancaria contaBD = contaBancariaService.pesquisaPorNumeroETipo(tipoDaConta, numeroDaConta);

	if (contaBD == null) {
	    throw new ContaInexistenteException("Conta Inexistente");
	}

	BigDecimal saldoAtual = contaBD.getSaldo();

	return new ResponseEntity<>(saldoAtual, HttpStatus.OK);
    }

    @PutMapping(value = "/transferencia/{tipoDaContaRemetente}/{numeroDaContaRemetente}/{tipoDaContaDestinatario}/{numeroDaContaDestinatario}/{valorTransferencia}", produces = "application/json")
    public ResponseEntity<String> transferencia(@PathVariable String tipoDaContaRemetente,
	    @PathVariable String numeroDaContaRemetente, @PathVariable String tipoDaContaDestinatario,
	    @PathVariable String numeroDaContaDestinatario, @PathVariable BigDecimal valorTransferencia

    ) throws ContaInexistenteException, ValorNegativoException, SaldoInsufucienteException {

	ContaBancaria contaBDRemetente = contaBancariaService.pesquisaPorNumeroETipo(tipoDaContaRemetente, numeroDaContaRemetente);

	if (contaBDRemetente == null) {
	    throw new ContaInexistenteException("Conta do remetente inexistente");
	}

	BigDecimal saldoAtualRemetente = contaBDRemetente.getSaldo();

	ContaBancaria contaBDDestinatario = contaBancariaService.pesquisaPorNumeroETipo(tipoDaContaDestinatario, numeroDaContaDestinatario);

	if (contaBDDestinatario == null) {
	    throw new ContaInexistenteException("Conta do destinatáiro inexistente");
	}

	BigDecimal saldoAtualDestinatario = contaBDDestinatario.getSaldo();

	if (valorTransferencia.compareTo(BigDecimal.ZERO) <= 0) {

	    throw new ValorNegativoException("Informe um valor acima de zero!");

	} else if (valorTransferencia.compareTo(saldoAtualRemetente) == 1) {

	    throw new SaldoInsufucienteException("Saldo Insuficiente");

	} else {
	    
	    contaBDRemetente.setSaldo(saldoAtualRemetente.subtract(valorTransferencia));
	    contaBDDestinatario.setSaldo(saldoAtualDestinatario.add(valorTransferencia));

	    contaBancariaService.save(contaBDRemetente);
	    contaBancariaService.save(contaBDDestinatario);
	    
	    extratoService.save(contaBDRemetente.getUsuario(), "Transfêrencia", valorTransferencia, tipoDaContaRemetente);
	    extratoService.save(contaBDDestinatario.getUsuario(), "Depósito recebido", valorTransferencia, tipoDaContaDestinatario);
	}

	return new ResponseEntity<>("Transferência realizada com sucesso!", HttpStatus.OK);

    }
    
    @GetMapping(value = "/extrato/search", produces = "application/json")
    public List<Extrato> buscarExtratos(@RequestBody Search search) throws DataInicialException {
	
	
	if(search.getDataInicial() == null) {
		throw new DataInicialException("Insira uma data Inicial");
	}
	
	if(search.getDataFinal() == null) {
	    search.setDataFinal(LocalDate.now());
	}
	
	String emailUsuario = SecurityUtils.userDetailsImpl();
	
	Usuario usuario = usuarioService.buscarUsuarioPorEmail(emailUsuario);
	
	List<Extrato> extratos = extratoService.pesquisaExtratoPorData(usuario.getId(), search.getDataInicial(), search.getDataFinal());
	
	return extratos;
	
    }
}
