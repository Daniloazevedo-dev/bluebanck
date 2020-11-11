package br.com.softblue.bluebank.infrastructure.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.softblue.bluebank.application.service.UsuarioService;
import br.com.softblue.bluebank.domain.usuario.Usuario;

@RestController
public class PublicController {
    
    @Autowired
    private UsuarioService usuarioService;
    
    @PostMapping(value = "/nova-conta", produces = "application/json")
    public ResponseEntity<String> novaConta(@RequestBody Usuario usuario) {
	
	usuarioService.cirarNovaConta(usuario);

	return new ResponseEntity<>("Conta Criada com sucesso!", HttpStatus.OK);

    }

}
