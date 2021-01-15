package br.com.softblue.bluebank.application.service;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.softblue.bluebank.domain.Extrato.Extrato;
import br.com.softblue.bluebank.domain.Extrato.ExtratoRepository;
import br.com.softblue.bluebank.domain.usuario.Usuario;

@Service
public class ExtratoService {
    
    @Autowired
    private ExtratoRepository extratoRepository;
    
    public void save(Usuario usuario, String descricao, BigDecimal valor, String tipoDaConta) {
	
	Extrato extrato = new Extrato();
	extrato.setData(LocalDate.now());
	extrato.setDescricao(descricao);
	extrato.setValor(valor);
	extrato.setTipoDaConta(tipoDaConta);
	extrato.setUsuario(usuario);
	usuario.getExtratos().add(extrato);
	
	extratoRepository.save(extrato);
    }

}
