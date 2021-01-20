package br.com.softblue.bluebank.infrastructure.web.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.softblue.bluebank.domain.usuario.Usuario;
import br.com.softblue.bluebank.domain.usuario.UsuarioRepository;

@Service
public class UserDatailsServiceImpl implements UserDetailsService {
    
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    public UserDatailsServiceImpl(UsuarioRepository usuarioRepository) {
	this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
	
	Usuario usuario = usuarioRepository.findByEmail(email);
	
	if(usuario == null) {
	    throw new UsernameNotFoundException(email);
	}
	
	return new UserDetailsImpl(usuario);
    }
}
