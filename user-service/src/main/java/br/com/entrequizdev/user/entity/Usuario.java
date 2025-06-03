package br.com.entrequizdev.user.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

//Entity usuario, onde é obrigatoria ter uma role e os dados do usuario

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "nome")
    private String nome;


    @Column(name = "email")
    private String email;


    @Column(name = "senha")
    private String senha;

//ele ira criar uma tabela onde ira relacionar o usuario a role, sem ter que criar uma coluna
// nova na entity usuario
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinTable(name="usuario_roles",
            joinColumns = @JoinColumn(name = "usuario_id"),
            inverseJoinColumns = @JoinColumn(name="role_id"))
    private List<Role> roles;

}
