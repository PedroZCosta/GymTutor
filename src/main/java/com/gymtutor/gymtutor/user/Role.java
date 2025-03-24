package com.gymtutor.gymtutor.user;

import jakarta.persistence.*;

// A anotação @Entity indica que esta classe é uma entidade JPA, representando uma tabela no banco de dados.
@Entity
@Table(name = "tb_role")
public class Role {

    // A anotação @Id indica que este campo é a chave primária da tabela.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Define a estratégia de geração de valor para a chave primária.
    private int roleId;

    // A anotação @Enumerated indica que o campo roleName será armazenado como uma string,
    // com valores predefinidos de RoleName.
    @Enumerated(EnumType.STRING)
    private RoleName roleName;

    // Construtor que recebe um RoleName para inicializar a instância de Role.
    public Role(RoleName roleName) {
        this.roleName = roleName;
    }

    // Construtor padrão necessário para o JPA (e outras operações como desserialização).
    public Role() {
    }

    // Getters e setters para acesso e modificação dos campos.
    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public RoleName getRoleName() {
        return roleName;
    }

    public void setRoleName(RoleName roleName) {
        this.roleName = roleName;
    }
}