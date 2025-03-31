package com.gymtutor.gymtutor.activities;

import jakarta.persistence.*;

// A anotação @Entity indica que esta classe é uma entidade JPA, representando uma tabela no banco de dados.
@Entity
@Table(name = "tb_muscular_group")
public class MuscularGroupModel {

    // A anotação @Id indica que este campo é a chave primária da tabela.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private byte muscularGroupId;

    // A anotação @Enumerated indica que o campo muscularGroupName será armazenado como uma string,
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private MuscularGroup muscularGroup;


    // Construtor que recebe um MuscularGroupName para inicializar a instância de MuscularGroup.
    public MuscularGroupModel(MuscularGroup muscularGroupName) {
        this.muscularGroup = muscularGroup;
    }

    // Construtor padrão necessário para o JPA (e outras operações como desserialização).
    public MuscularGroupModel() {}

    // Getters e setters para acesso e modificação dos campos.
    public byte getMuscularGroupId() {
        return muscularGroupId;
    }

    public void setMuscularGroupId(byte muscularGroupId) {
        this.muscularGroupId = muscularGroupId;
    }

    public MuscularGroup getMuscularGroup() {
        return muscularGroup;
    }

    public void setMuscularGroupName(MuscularGroup muscularGroupName) {
        this.muscularGroup = muscularGroupName;
    }
}
