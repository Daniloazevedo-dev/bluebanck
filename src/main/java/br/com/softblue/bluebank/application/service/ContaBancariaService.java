package br.com.softblue.bluebank.application.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.softblue.bluebank.application.util.GerarNumero;
import br.com.softblue.bluebank.domain.contaBancaria.ContaBancaria;
import br.com.softblue.bluebank.domain.contaBancaria.ContaBancariaRepository;
import br.com.softblue.bluebank.domain.contaBancaria.TiposDeConta;
import br.com.softblue.bluebank.domain.usuario.Usuario;

@Service
public class ContaBancariaService {

	@Autowired
	private ContaBancariaRepository contaBancariaRepository;

	public List<ContaBancaria> novaConta(Usuario usuario) {

		List<ContaBancaria> contasBancarias = new ArrayList<>();

		ContaBancaria contaCorrente = new ContaBancaria();
		contaCorrente.setNumero(gerarNumeroDaConta());
		contaCorrente.setTipo(TiposDeConta.corrente.toString());
		contaCorrente.setSaldo(BigDecimal.valueOf(200));
		contaCorrente.setUsuario(usuario);
		contaCorrente.setAtivo(true);

		ContaBancaria contaPoupanca = new ContaBancaria();
		contaPoupanca.setNumero(gerarNumeroDaConta());
		contaPoupanca.setTipo(TiposDeConta.poupanca.toString());
		contaPoupanca.setSaldo(BigDecimal.valueOf(400));
		contaPoupanca.setUsuario(usuario);
		contaPoupanca.setAtivo(false);

		contasBancarias.add(contaCorrente);
		contasBancarias.add(contaPoupanca);

		return contasBancarias;

	}

	public ContaBancaria save(ContaBancaria contaBancaria) {
		return contaBancariaRepository.save(contaBancaria);
	}

	public ContaBancaria pesquisaPorNumeroDaConta(String numero) {
		return contaBancariaRepository.findByNumero(numero);
	}

	public ContaBancaria pesquisaPorNumeroETipo(String tipo, String numero) {
		return contaBancariaRepository.findByNumeroAndTipo(tipo, numero);
	}

	private String gerarNumeroDaConta() {

		String numeroDaconta = null;
		boolean contaExistente = true;

		do {
			numeroDaconta = GerarNumero.gerar();

			if (pesquisaPorNumeroDaConta(numeroDaconta) == null) {
				contaExistente = false;
			}

		} while (contaExistente);

		return numeroDaconta;
	}
}
