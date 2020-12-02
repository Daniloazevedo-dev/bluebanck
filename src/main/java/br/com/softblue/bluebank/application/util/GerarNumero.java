package br.com.softblue.bluebank.application.util;

public class GerarNumero {

    public static String gerar() {

	StringBuilder numeroGerado = new StringBuilder();

	for (int i = 0; i < 6; i++) {
	    int numAleatorio = (int) (Math.random() * 9) + 1;
	    numeroGerado.append(numAleatorio);
	}

	return numeroGerado.toString();
    }

}
