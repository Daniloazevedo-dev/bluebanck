package br.com.softblue.bluebank.domain.conta;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContaRepository extends CrudRepository<ContaBancaria, Long>{

}
