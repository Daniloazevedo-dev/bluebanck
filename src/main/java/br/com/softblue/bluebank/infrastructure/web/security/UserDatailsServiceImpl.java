package br.com.softblue.bluebank.infrastructure.web.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.softblue.bluebank.domain.contaBancaria.ContaBancaria;
import br.com.softblue.bluebank.domain.contaBancaria.ContaBancariaRepository;

@Service
public class UserDatailsServiceImpl implements UserDetailsService {
    
    private ContaBancariaRepository contaBancariaRepository;
    
    @Autowired
    public UserDatailsServiceImpl(ContaBancariaRepository contaBancariaRepository) {
	this.contaBancariaRepository = contaBancariaRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String numeroDaConta) throws UsernameNotFoundException {
	
	ContaBancaria contaBancaria = contaBancariaRepository.findByNumero(numeroDaConta);
	
	if(contaBancaria == null) {
	    throw new UsernameNotFoundException(numeroDaConta);
	}
	
	return new UserDetailsImpl(contaBancaria);
    }
}
