package com.gymtutor.gymtutor.activities;

import jakarta.persistence.*;

// A anotação @Entity indica que esta classe é uma entidade JPA, representando uma tabela no banco de dados.
@Entity
@Table(name = "tb_muscular_group")
public class MuscularGroupModel {

    // A anotação @Id indica que este campo é a chave primária da tabela.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int muscularGroupId;

    // A anotação @Enumerated indica que o campo muscularGroupName será armazenado como uma string,
    @Enumerated(EnumType.STRING)
    private MuscularGroup muscularGroupName;


    // Construtor que recebe um MuscularGroupName para inicializar a instância de MuscularGroup.
    public MuscularGroupModel(MuscularGroup muscularGroupName) {
        this.muscularGroupName = muscularGroupName;
    }

    // Construtor padrão necessário para o JPA (e outras operações como desserialização).
    public MuscularGroupModel() {
    }


    // Getters e setters para acesso e modificação dos campos.
    public int getMuscularGroupId() {
        return muscularGroupId;
    }

    public void setMuscularGroupId(int muscularGroupId) {
        this.muscularGroupId = muscularGroupId;
    }

    public MuscularGroup getMuscularGroupName() {
        return muscularGroupName;
    }

    public void setMuscularGroupName(MuscularGroup muscularGroupName) {
        this.muscularGroupName = muscularGroupName;
    }
}
