package br.com.softblue.bluebank.domain.extrato;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

import br.com.softblue.bluebank.domain.usuario.Usuario;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class Extrato {
    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull(message = "A data é obrigatória!")
    @FutureOrPresent(message = "A data não pode está no passado!")
    private LocalDate data;
    
    @NotEmpty(message = "O nome não pode ser vazio!")
    private String descricao;
    
    @NotEmpty(message = "O Tipo da Conta não pode ser vazio!")
    private String tipoDaConta;
    
    @NotNull
    private BigDecimal valor;
    
    @JsonIgnore
    @NotNull
    @ManyToOne(optional = false)
    private Usuario usuario;
    
}
