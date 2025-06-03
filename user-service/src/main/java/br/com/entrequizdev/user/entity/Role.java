package br.com.entrequizdev.user.entity;

import br.com.entrequizdev.user.enums.RoleName;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


//Aqui irei criar o bd e a entidade onde iremos cadastrar a role
@Entity
@Table(name="roles")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private RoleName name;

}
