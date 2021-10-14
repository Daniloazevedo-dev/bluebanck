package br.com.softblue.bluebank.infrastructure.web.restController;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
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
import br.com.softblue.bluebank.domain.contaBancaria.ContaBancaria;
import br.com.softblue.bluebank.domain.contaBancaria.ContaInexistenteException;
import br.com.softblue.bluebank.domain.contaBancaria.SaldoInsufucienteException;
import br.com.softblue.bluebank.domain.contaBancaria.ValorNegativoException;
import br.com.softblue.bluebank.domain.extrato.Extrato;
import br.com.softblue.bluebank.domain.usuario.CpfExistenteException;
import br.com.softblue.bluebank.domain.usuario.EmailExistenteException;
import br.com.softblue.bluebank.domain.usuario.TitularExistenteException;
import br.com.softblue.bluebank.domain.usuario.Usuario;
import br.com.softblue.bluebank.domain.usuario.UsuarioInexistenteException;
import br.com.softblue.bluebank.infrastructure.web.security.SecurityUtils;

@RestController
@RequestMapping("/usuario")
public class UsuarioRestController {

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
	public ResponseEntity<ContaBancaria> transferencia(@PathVariable String tipoDaContaRemetente,
			@PathVariable String numeroDaContaRemetente, @PathVariable String tipoDaContaDestinatario,
			@PathVariable String numeroDaContaDestinatario, @PathVariable BigDecimal valorTransferencia

	) throws ContaInexistenteException, ValorNegativoException, SaldoInsufucienteException {
		
		ContaBancaria contaBancariaRemetenteAtl = new ContaBancaria();

		ContaBancaria contaBDRemetente = contaBancariaService.pesquisaPorNumeroETipo(tipoDaContaRemetente,
				numeroDaContaRemetente);

		if (contaBDRemetente == null) {
			throw new ContaInexistenteException("Conta do remetente inexistente");
		}

		BigDecimal saldoAtualRemetente = contaBDRemetente.getSaldo();

		ContaBancaria contaBDDestinatario = contaBancariaService.pesquisaPorNumeroETipo(tipoDaContaDestinatario,
				numeroDaContaDestinatario);

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

			contaBancariaRemetenteAtl = contaBancariaService.save(contaBDRemetente);
			contaBancariaService.save(contaBDDestinatario);

			extratoService.save(contaBDRemetente.getUsuario(), "Transfêrencia", valorTransferencia,
					tipoDaContaRemetente);
			extratoService.save(contaBDDestinatario.getUsuario(), "Depósito recebido", valorTransferencia,
					tipoDaContaDestinatario);
		}

		return new ResponseEntity<>(contaBancariaRemetenteAtl, HttpStatus.OK);

	}

	@GetMapping(value = "/extrato/search/{dataInicial}/{dataFinal}", produces = "application/json")
	public List<Extrato> buscarExtratos(
			@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dataInicial,
			@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dataFinal
			
			) throws DataInicialException {

		if (dataInicial == null) {
			throw new DataInicialException("Insira uma data Inicial");
		}

		if (dataFinal == null) {
			dataFinal = LocalDate.now();
		}

		String emailUsuario = SecurityUtils.userDetailsImpl();

		Usuario usuario = usuarioService.buscarUsuarioPorEmail(emailUsuario);

		List<Extrato> extratos = extratoService.pesquisaExtratoPorData(usuario.getId(), dataInicial,
				dataFinal);

		return extratos;

	}

	@PutMapping(value = "/altera-tipo-conta", produces = "application/json")
	public ResponseEntity<List<ContaBancaria>> alteraContaSelecionada(
			@RequestBody List<ContaBancaria> contasBancarias) {

		List<ContaBancaria> contasAtualizadas = new ArrayList<>();

		for (ContaBancaria contaBancaria : contasBancarias) {

			ContaBancaria contaDoBanco = contaBancariaService.pesquisaPorNumeroDaConta(contaBancaria.getNumero());

			if (contaDoBanco.getAtivo() == true) {
				contaDoBanco.setAtivo(false);
			} else {
				contaDoBanco.setAtivo(true);
			}
			contasAtualizadas.add(contaDoBanco);
			contaBancariaService.save(contaDoBanco);
		}
		return new ResponseEntity<>(contasAtualizadas, HttpStatus.OK);
	}

	@PutMapping(value = "/altetar-dados", produces = "application/json")
	public ResponseEntity<Usuario> alterarCadastro(@RequestBody Usuario usuario) throws TitularExistenteException,
			EmailExistenteException, CpfExistenteException, UsuarioInexistenteException {

		Usuario usuarioAtualizado = new Usuario();

		String emailUsuario = SecurityUtils.userDetailsImpl();

		Usuario usuarioBanco = usuarioService.buscarUsuarioPorEmail(emailUsuario);

		if (usuarioBanco != null) {

			if (!usuarioBanco.getTitular().equalsIgnoreCase(usuario.getTitular())) {
				if (usuarioService.buscarUsuarioPorTitular(usuario.getTitular()) != null) {
					throw new TitularExistenteException("Já existe um usuário com esse nome.");
				}
			}

			if (!usuarioBanco.getEmail().equalsIgnoreCase(usuario.getEmail())) {

				if (usuarioService.buscarUsuarioPorEmail(usuario.getEmail()) != null) {
					throw new EmailExistenteException("Já existe um usuário com este email cadastrado.");
				}
			}

			if (!usuarioBanco.getCpf().equalsIgnoreCase(usuario.getCpf())) {
				if (usuarioService.buscarUsuarioPorCpf(usuario.getCpf()) != null) {
					throw new CpfExistenteException("Já existe um usuário com este cpf.");
				}
			}

			usuarioBanco.setTitular(usuario.getTitular());
			usuarioBanco.setEmail(usuario.getEmail());
			usuarioBanco.setCpf(usuario.getCpf());
			usuarioAtualizado = usuarioService.atualizaUsuario(usuarioBanco);

		} else {
			throw new UsuarioInexistenteException("Usuario Inexistente.");
		}

		return new ResponseEntity<>(usuarioAtualizado, HttpStatus.OK);

	}

	@GetMapping(value = "/{numeroConta}/{tipo}", produces = "application/json")
	public ResponseEntity<ContaBancaria> buscaConta(
			@PathVariable String numeroConta,
			@PathVariable String tipo) throws ContaInexistenteException {
		
		ContaBancaria contaBancaria = contaBancariaService.pesquisaPorNumeroETipo(tipo,numeroConta);
		
		
		if (contaBancaria == null) {
			throw new ContaInexistenteException("Conta bancaria inexistente.");
		}
		
		contaBancaria.setTitular(contaBancaria.getUsuario().getTitular());

		return new ResponseEntity<>(contaBancaria, HttpStatus.OK);
	}
}
