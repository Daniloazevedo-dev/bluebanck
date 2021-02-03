package br.com.softblue.bluebank.domain.extrato;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

@Repository
@RepositoryRestResource(exported = false)
public interface ExtratoRepository extends CrudRepository<Extrato, Long> {
    
    @Query("SELECT e FROM Extrato e WHERE e.usuario.id = ?1 AND e.data BETWEEN ?2 AND ?3")
    public List<Extrato> findByDateInterval(Long usuarioId, LocalDate dataInicial, LocalDate dataFinal);

}
