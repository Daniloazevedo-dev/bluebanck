package br.com.softblue.bluebank.domain.usuario;

@SuppressWarnings("serial")
public class CpfExistenteException extends Exception {

    public CpfExistenteException(String message) {
	super(message);
    }
}
