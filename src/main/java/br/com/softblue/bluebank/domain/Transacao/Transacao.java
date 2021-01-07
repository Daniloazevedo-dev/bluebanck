package br.com.softblue.bluebank.domain.Transacao;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Transacao {
    
    private String numeroDaConta;
    
    private String tipoDaConta;
    
    private String tipoDaTransacao;
    
    private BigDecimal valor;
    
}
