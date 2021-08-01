package br.com.softblue.bluebank.domain.usuario;

@SuppressWarnings("serial")
public class UsuarioInexistenteException extends Exception {

    public UsuarioInexistenteException(String message) {
	super(message);
    }
}
