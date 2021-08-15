package br.com.softblue.bluebank.application.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.softblue.bluebank.domain.contaBancaria.ContaBancaria;
import br.com.softblue.bluebank.domain.usuario.Roles;
import br.com.softblue.bluebank.domain.usuario.Usuario;
import br.com.softblue.bluebank.domain.usuario.UsuarioRepository;

@Service
public class UsuarioService {

	@Autowired
	private UsuarioRepository usuarioRepository;

	public Usuario saveUsuario(Usuario usuario, ContaBancaria conta) {
		usuario.setRole(Roles.ADMIN.toString());
		usuario.getContas().add(conta);
		return usuarioRepository.save(usuario);
	}
	
	public Usuario atualizaUsuario(Usuario usuario) {
		return usuarioRepository.save(usuario);
	}

	public Usuario buscarUsuarioPorEmail(String email) {

		return usuarioRepository.findByEmail(email);
	}

	public Usuario buscarUsuarioPorCpf(String cpf) {

		return usuarioRepository.findByCpf(cpf);
	}

	public Usuario buscarUsuarioPorTitular(String titular) {

		return usuarioRepository.findByTitular(titular);
	}

	public void atualizarSenha(String senha, Long idUsuario) {
		usuarioRepository.updateSenha(senha, idUsuario);
	}
}
