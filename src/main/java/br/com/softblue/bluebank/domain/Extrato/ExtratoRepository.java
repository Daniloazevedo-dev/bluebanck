package br.com.softblue.bluebank.domain.Extrato;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExtratoRepository extends CrudRepository<Extrato, Long> {

}
