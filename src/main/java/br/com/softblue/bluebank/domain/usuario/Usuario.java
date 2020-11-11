package br.com.softblue.bluebank.domain.usuario;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class Usuario {
    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @NotEmpty(message = "O titular não pode ser vazio.")
    @Size(max = 80, message = "O nome é muito grande.")
    @Column(nullable = false, unique = true)
    private String titular;
    
    @NotEmpty(message = "O email não pode ser vazio")
    @Size(max = 60, message = "O e-mail é muito grande")
    @Email(message = "O e-mail é inválido")
    @Column(nullable = false,unique = true)
    private String email;
    
    @NotEmpty(message = "O CPF não pode ser vazio") 
    @Column(nullable = false, unique = true)
    private String cpf;
    
    @NotEmpty(message = "A senha não pode ser vazio")
    private String senha;
    
}
