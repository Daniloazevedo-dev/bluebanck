package br.com.softblue.bluebank.domain.usuario;

@SuppressWarnings("serial")
public class EmailExistenteException extends Exception {

    public EmailExistenteException(String message) {
	super(message);
    }
}
