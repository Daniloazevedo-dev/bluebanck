package br.com.softblue.bluebank.domain.contaBancaria;

@SuppressWarnings("serial")
public class SaldoInsufucienteException extends Exception {

    public SaldoInsufucienteException(String message) {
	super(message);
    }
}
