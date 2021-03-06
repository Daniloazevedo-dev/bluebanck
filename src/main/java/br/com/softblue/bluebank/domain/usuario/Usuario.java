package br.com.softblue.bluebank.domain.usuario;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import br.com.softblue.bluebank.domain.contaBancaria.ContaBancaria;
import br.com.softblue.bluebank.domain.extrato.Extrato;
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
    
    @NotEmpty(message = "O titular não pode ser vazio")
    @Size(max = 80, message = "O nome é muito grande.")
    @Column(nullable = false)
    private String titular;
    
    @NotEmpty(message = "O email não pode ser vazio")
    @Size(max = 60, message = "O e-mail é muito grande")
    @Email(message = "O e-mail é inválido")
    @Column(nullable = false)
    private String email;
    
    @NotEmpty(message = "O CPF não pode ser vazio") 
    @Column(nullable = false)
    private String cpf;
    
    @NotEmpty(message = "A senha não pode ser vazio")
    private String senha;
    
    @Column(updatable = false)
    private String role;
    
    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private List<ContaBancaria> contas = new ArrayList<>(); 
    
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Extrato> extratos = new ArrayList<>();
}
