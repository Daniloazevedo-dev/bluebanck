package br.com.softblue.bluebank.domain.usuario;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

@Repository
@RepositoryRestResource(exported = false)
public interface UsuarioRepository extends CrudRepository<Usuario, Long> {

}
