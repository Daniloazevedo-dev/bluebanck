package br.com.softblue.bluebank.application.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.softblue.bluebank.domain.usuario.Usuario;
import br.com.softblue.bluebank.domain.usuario.UsuarioRepository;

@Service
public class UsuarioService {
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    public void cirarNovaConta(Usuario usuario) {
	usuarioRepository.save(usuario);
    }
    
}
