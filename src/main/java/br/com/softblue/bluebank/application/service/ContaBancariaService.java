package br.com.softblue.bluebank.application.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import br.com.softblue.bluebank.domain.conta.ContaBancaria;
import br.com.softblue.bluebank.domain.conta.TiposDeConta;
import br.com.softblue.bluebank.domain.usuario.Usuario;

@Service
public class ContaBancariaService {
    
   public List<ContaBancaria> novaConta (Usuario usuario){
       
       List<ContaBancaria> contas = new ArrayList<>();
       
       ContaBancaria contaCorrente = new ContaBancaria();
       contaCorrente.setNumero(111111);
       contaCorrente.setTipo(TiposDeConta.Corrente);
       contaCorrente.setSaldo(BigDecimal.valueOf(0));
       contaCorrente.setUsuario(usuario);
       
       ContaBancaria contaPoupanca = new ContaBancaria();
       contaPoupanca.setNumero(22222);
       contaPoupanca.setTipo(TiposDeConta.Poupanca);
       contaPoupanca.setSaldo(BigDecimal.valueOf(0));
       contaPoupanca.setUsuario(usuario);
       
       contas.add(contaCorrente);
       contas.add(contaPoupanca);
       
       return contas;
        
   }
}
