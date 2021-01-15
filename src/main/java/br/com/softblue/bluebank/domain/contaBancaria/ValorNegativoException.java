package br.com.softblue.bluebank.domain.contaBancaria;

@SuppressWarnings("serial")
public class ValorNegativoException extends Exception {

    public ValorNegativoException(String message) {
	super(message);
    }
}
