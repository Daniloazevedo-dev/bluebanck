package br.com.softblue.bluebank.application.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.softblue.bluebank.application.util.GerarNumero;
import br.com.softblue.bluebank.domain.conta.ContaBancaria;
import br.com.softblue.bluebank.domain.conta.ContaBancariaRepository;
import br.com.softblue.bluebank.domain.conta.TiposDeConta;
import br.com.softblue.bluebank.domain.usuario.Usuario;

@Service
public class ContaBancariaService {
    
    @Autowired
    private ContaBancariaRepository contaBancariaRepository;
    
   public List<ContaBancaria> novaConta (Usuario usuario){
       
       List<ContaBancaria> contasBancarias = new ArrayList<>();       
       
       ContaBancaria contaCorrente = new ContaBancaria();
       contaCorrente.setNumero(gerarNumeroDaConta());
       contaCorrente.setTipo(TiposDeConta.Corrente);
       contaCorrente.setSaldo(BigDecimal.valueOf(0));
       contaCorrente.setUsuario(usuario);
       
       ContaBancaria contaPoupanca = new ContaBancaria();
       contaPoupanca.setNumero(gerarNumeroDaConta());
       contaPoupanca.setTipo(TiposDeConta.Poupanca);
       contaPoupanca.setSaldo(BigDecimal.valueOf(0));
       contaPoupanca.setUsuario(usuario);
       
       contasBancarias.add(contaCorrente);
       contasBancarias.add(contaPoupanca);
       
       return contasBancarias;
        
   }
   
   public ContaBancaria pesquisaPorNumeroDaConta(String numero) {
      return contaBancariaRepository.findByNumero(numero);
   }
   
   private String gerarNumeroDaConta() {
       
       String numeroDaconta = null;
       boolean existe = false;
       
       do {
	   numeroDaconta =  GerarNumero.gerar();
	   
	   if(pesquisaPorNumeroDaConta(numeroDaconta) == null) {
	       existe = true;
	   }
	   
       } while (!existe);
       
       return numeroDaconta;
   }
  
}
