package br.com.softblue.bluebank.domain.conta;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContaBancariaRepository extends CrudRepository<ContaBancaria, Long>{

    public ContaBancaria findByNumero(String numero);

}
