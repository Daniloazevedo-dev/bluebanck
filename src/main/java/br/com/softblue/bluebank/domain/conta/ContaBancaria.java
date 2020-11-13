package br.com.softblue.bluebank.domain.conta;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import br.com.softblue.bluebank.domain.usuario.Usuario;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class ContaBancaria {
    
    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TiposDeConta tipo;
    
    @NotEmpty(message = "O número da conta não pode ser vazio!")
    @Column(nullable = false, unique = true)
    private String numero;
    
    @Min(0)
    private BigDecimal saldo;
    
    @NotNull
    @ManyToOne(optional = false)
    private Usuario usuario;
        
}
