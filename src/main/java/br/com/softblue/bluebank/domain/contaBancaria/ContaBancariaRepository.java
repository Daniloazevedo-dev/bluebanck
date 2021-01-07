package br.com.softblue.bluebank.domain.contaBancaria;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

@Repository
@RepositoryRestResource(exported = false)
public interface ContaBancariaRepository extends CrudRepository<ContaBancaria, Long>{

    public ContaBancaria findByNumero(String numero);
    
   @Query("SELECT c FROM ContaBancaria c WHERE c.numero = ?1 AND c.tipo = ?2")
    public ContaBancaria findByNumeroAndTipo(String numero, String tipo);

}
