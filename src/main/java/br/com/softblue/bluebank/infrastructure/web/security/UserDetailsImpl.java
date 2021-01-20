package br.com.softblue.bluebank.infrastructure.web.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import br.com.softblue.bluebank.domain.usuario.Usuario;

@SuppressWarnings("serial")
public class UserDetailsImpl implements UserDetails {
    
    private String email;
    private String senha;
    private String displayname;
    
    public UserDetailsImpl(Usuario usuario) {
	this.email = usuario.getEmail();
	this.senha = usuario.getSenha();
	this.displayname = usuario.getTitular();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
	return AuthorityUtils.NO_AUTHORITIES;
    }

    @Override
    public String getPassword() {
	return senha;
    }

    @Override
    public String getUsername() {
	return email;
    }

    @Override
    public boolean isAccountNonExpired() {
	return true;
    }

    @Override
    public boolean isAccountNonLocked() {
	return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
	return true;
    }

    @Override
    public boolean isEnabled() {
	return true;
    }
    
    public String getDisplayname() {
	return displayname;
    }
}
