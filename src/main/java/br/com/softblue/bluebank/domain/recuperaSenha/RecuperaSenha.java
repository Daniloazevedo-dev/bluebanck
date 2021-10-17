package br.com.softblue.bluebank.domain.recuperaSenha;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecuperaSenha {
    
    
    private String msgRecuperaSenha;
    
   public RecuperaSenha(String msg) {
    	this.msgRecuperaSenha = msg;
    }
    
}
