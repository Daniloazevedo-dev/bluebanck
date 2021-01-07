package br.com.softblue.bluebank.domain.Extrato;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

@Repository
@RepositoryRestResource(exported = false)
public interface ExtratoRepository extends CrudRepository<Extrato, Long> {

}
