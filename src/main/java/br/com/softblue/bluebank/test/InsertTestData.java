package br.com.softblue.bluebank.test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import br.com.softblue.bluebank.application.service.ContaBancariaService;
import br.com.softblue.bluebank.application.service.ExtratoService;
import br.com.softblue.bluebank.application.service.UsuarioService;
import br.com.softblue.bluebank.domain.contaBancaria.ContaBancaria;
import br.com.softblue.bluebank.domain.contaBancaria.TiposDeConta;
import br.com.softblue.bluebank.domain.usuario.Roles;
import br.com.softblue.bluebank.domain.usuario.Usuario;

@Component
public class InsertTestData {

	private UsuarioService usuarioService;
	private ExtratoService extratoService;
	private ContaBancariaService contaBancariaService;

	@Autowired
	public InsertTestData(UsuarioService usuarioService, ExtratoService extratoService,
			ContaBancariaService contaBancariaService) {
		this.usuarioService = usuarioService;
		this.extratoService = extratoService;
		this.contaBancariaService = contaBancariaService;
	}

	@EventListener
	public void onApplicationEvent(ContextRefreshedEvent event) {

		createUsers("João Silva", "joaosilva@email.com", "15963487965", "abc", "123456", "654321",
				Roles.ADMIN.toString());
		deposito("123456", BigDecimal.valueOf(500));
		deposito("654321", BigDecimal.valueOf(1250));

		createUsers("Maria Santos", "mariasantos@email.com", "147852369785", "abc", "147258", "258741",
				Roles.ADMIN.toString());
		deposito("147258", BigDecimal.valueOf(700));
		deposito("258741", BigDecimal.valueOf(2000));

		createUsers("Pedro Lima", "pedrolima@email.com", "32165498732", "abc", "369258", "963852",
				Roles.ADMIN.toString());
		deposito("369258", BigDecimal.valueOf(1900));
		deposito("963852", BigDecimal.valueOf(750));
	}

	private void createUsers(String nome, String email, String cpf, String senha, String numeroPoupanca,
			String numeroCorrente, String role) {

		PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

		Usuario usuario = new Usuario();
		usuario.setTitular(nome);
		usuario.setEmail(email);
		usuario.setCpf(cpf);
		usuario.setSenha(encoder.encode(senha));
		usuario.setRole(role);

		List<ContaBancaria> contasBancarias = createContaBancaria(usuario, numeroPoupanca, numeroCorrente);

		for (ContaBancaria novaConta : contasBancarias) {
			usuarioService.saveUsuario(usuario, novaConta);
			extratoService.save(usuario, "Conta Criada", novaConta.getSaldo(), novaConta.getTipo());
		} 
	}

	private List<ContaBancaria> createContaBancaria(Usuario usuario, String numeroPoupanca, String numeroCorrente) {

		ContaBancaria contaPoupanca = new ContaBancaria();
		contaPoupanca.setNumero(numeroPoupanca);
		contaPoupanca.setTipo(TiposDeConta.poupanca.toString());
		contaPoupanca.setSaldo(BigDecimal.valueOf(0));
		contaPoupanca.setUsuario(usuario); 
		contaPoupanca.setAtivo(true); 

		ContaBancaria contaCorrente = new ContaBancaria();
		contaCorrente.setNumero(numeroCorrente);
		contaCorrente.setTipo(TiposDeConta.corrente.toString());
		contaCorrente.setSaldo(BigDecimal.valueOf(0));
		contaCorrente.setUsuario(usuario);
		contaCorrente.setAtivo(false);

		List<ContaBancaria> contasBancarias = new ArrayList<>();
		contasBancarias.add(contaPoupanca);
		contasBancarias.add(contaCorrente);

		return contasBancarias;
	}

	private void deposito(String numeroDaConta, BigDecimal valor) {

		ContaBancaria contaBD = contaBancariaService.pesquisaPorNumeroDaConta(numeroDaConta);
		BigDecimal saldoAtual = contaBD.getSaldo();
		contaBD.setSaldo(saldoAtual.add(valor));

		contaBancariaService.save(contaBD);
		extratoService.save(contaBD.getUsuario(), "Depósito", valor, contaBD.getTipo());

	}

}
