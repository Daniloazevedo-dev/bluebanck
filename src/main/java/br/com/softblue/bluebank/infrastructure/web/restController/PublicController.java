package br.com.softblue.bluebank.infrastructure.web.restController;

import java.math.BigDecimal;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.softblue.bluebank.application.service.ContaBancariaService;
import br.com.softblue.bluebank.application.service.EnviaEmailService;
import br.com.softblue.bluebank.application.service.ExtratoService;
import br.com.softblue.bluebank.application.service.UsuarioService;
import br.com.softblue.bluebank.application.util.GerarNumero;
import br.com.softblue.bluebank.domain.contaBancaria.ContaBancaria;
import br.com.softblue.bluebank.domain.contaBancaria.ContaInexistenteException;
import br.com.softblue.bluebank.domain.contaBancaria.ValorNegativoException;
import br.com.softblue.bluebank.domain.usuario.CpfExistenteException;
import br.com.softblue.bluebank.domain.usuario.EmailExistenteException;
import br.com.softblue.bluebank.domain.usuario.TitularExistenteException;
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
    
    @Autowired
    private EnviaEmailService enviaEmailService;

    @PostMapping(value = "/nova-conta", produces = "application/json")
    public ResponseEntity<String> novaConta(@RequestBody @Valid Usuario usuario) throws TitularExistenteException, EmailExistenteException, CpfExistenteException {
	
	if(usuarioService.buscarUsuarioPorTitular(usuario.getTitular()) != null) {
	    throw new TitularExistenteException("Já existe um usuário com esse nome.");
	}
	
	if(usuarioService.buscarUsuarioPorEmail(usuario.getEmail()) != null) {
	    throw new EmailExistenteException("Já existe um usuário com este email cadastrado.");
	}
	
	if(usuarioService.buscarUsuarioPorCpf(usuario.getCpf()) != null) {
	    throw new CpfExistenteException("Já existe um usuário com este cpf.");
	}

	PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
	usuario.setSenha(encoder.encode(usuario.getSenha()));

	List<ContaBancaria> contasBancarias = contaBancariaService.novaConta(usuario);

	for (ContaBancaria novaConta : contasBancarias) {
	    usuarioService.saveUsuario(usuario, novaConta);
	}
	
	return new ResponseEntity<>("Conta Criada com sucesso!", HttpStatus.OK);

    }

    @PutMapping(value = "/deposito/{tipoDaConta}/{numeroDaConta}/{valor}", produces = "application/json")
    public ResponseEntity<String> deposito(@PathVariable String tipoDaConta, @PathVariable String numeroDaConta,
	    @PathVariable BigDecimal valor) throws ContaInexistenteException, ValorNegativoException {

	ContaBancaria contaBD = contaBancariaService.pesquisaPorNumeroETipo(tipoDaConta, numeroDaConta);

	if (contaBD == null) {
	    throw new ContaInexistenteException("Conta Inexistente");
	}
	
	BigDecimal saldoAtual = contaBD.getSaldo();

	if (valor.compareTo(BigDecimal.ZERO) <= 0) {

	    throw new ValorNegativoException("Informe um valor acima de zero!");

	} else {

	    contaBD.setSaldo(saldoAtual.add(valor));

	    contaBancariaService.save(contaBD);
	    extratoService.save(contaBD.getUsuario(), "Depósito", valor, tipoDaConta);
	}

	return new ResponseEntity<>("Depósito Realizado com sucesso!", HttpStatus.OK);
    }

    @GetMapping(value = "/buscar/{tipoDaConta}/{numeroDaConta}", produces = "application/json")
    public ResponseEntity<ContaBancaria> buscaContaBancaria(@PathVariable String tipoDaConta,
	    @PathVariable String numeroDaConta) throws ContaInexistenteException {

	ContaBancaria contaBD = contaBancariaService.pesquisaPorNumeroETipo(tipoDaConta, numeroDaConta);

	if (contaBD == null) {
	    throw new ContaInexistenteException("Conta Inexistente");
	}

	return new ResponseEntity<>(contaBD, HttpStatus.OK);
    }
    
    @PostMapping(value = "/recuperar/{email}", produces = "application/json")
    public ResponseEntity<String> recuperar(@PathVariable String email) throws Exception {
	
	Usuario usuarioBD = usuarioService.buscarUsuarioPorEmail(email);
	
	if(usuarioService.buscarUsuarioPorEmail(email) == null) {
	    throw new EmailExistenteException("Email não cadastrado.");
	}
	
	String novaSenha = GerarNumero.gerar();
	
	PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
	usuarioService.atualizarSenha(encoder.encode(novaSenha), usuarioBD.getId());
	
	enviaEmailService.enviarEmail("Recuperação de senha", email, "Sua nova senha é: " + novaSenha);
	
	return new ResponseEntity<>("Senha enviada para o seu email.", HttpStatus.OK); 
	
    }
    
}
