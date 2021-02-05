package br.com.softblue.bluebank.domain.usuario;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

@Repository
@RepositoryRestResource(exported = false)
public interface UsuarioRepository extends CrudRepository<Usuario, Long> {

    Usuario findByEmail(Usuario usuario);

    Usuario findByEmail(String email);

    Usuario findByCpf(String cpf);

    Usuario findByTitular(String titular);

    @Transactional
    @Modifying
    @Query(value = "update usuario set senha = ?1 where id = ?2", nativeQuery = true)
    void updateSenha(String senha, Long idUsuario);

}
