package br.com.softblue.bluebank.application.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.softblue.bluebank.domain.extrato.Extrato;
import br.com.softblue.bluebank.domain.extrato.ExtratoRepository;
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

    public List<Extrato> pesquisaExtratoPorData(Long usuarioId, LocalDate dataInicial, LocalDate dataFinal) {
	
	List<Extrato> extratos = extratoRepository.findByDateInterval(usuarioId, dataInicial, dataFinal);
	
	return extratos;
	
    }
    
}
