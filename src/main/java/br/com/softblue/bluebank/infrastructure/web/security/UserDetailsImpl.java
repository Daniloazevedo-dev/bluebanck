package br.com.softblue.bluebank.infrastructure.web.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import br.com.softblue.bluebank.domain.contaBancaria.ContaBancaria;
import br.com.softblue.bluebank.domain.usuario.Usuario;

@SuppressWarnings("serial")
public class UserDetailsImpl implements UserDetails {
    
    private String email;
    private String senha;
    private String displayname;
    private String cpf;
    private List<ContaBancaria> contasBancarias = new ArrayList<>();
    
    public UserDetailsImpl(Usuario usuario) {
	this.email = usuario.getEmail();
	this.senha = usuario.getSenha();
	this.displayname = usuario.getTitular();
	this.cpf = usuario.getCpf();
	this.contasBancarias = usuario.getContas();
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
    
    public String getCpf() {
		return cpf;
	}

    public List<ContaBancaria> getContasBancarias() {
	return contasBancarias;
    }
}
