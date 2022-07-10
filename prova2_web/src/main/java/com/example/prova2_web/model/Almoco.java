package com.example.prova2_web.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class Almoco {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private Date deleted;
    private String imageUri;

    @NotBlank
    private String nome;
    @NotBlank
    private String tamanho;
    @NotBlank
    private String composicao;
    @NotNull
    @Min(0)
    private Double preco;




}
