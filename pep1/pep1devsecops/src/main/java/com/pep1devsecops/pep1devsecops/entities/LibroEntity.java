package com.pep1devsecops.pep1devsecops.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "libros")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LibroEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;

    private String titulo;
    private String autor;
    private int precio;
    private int stock;
    private int anio;
    private String descripcion;

    //Getters
    public Long getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getAutor() { return autor; }
    public int getPrecio() { return precio; }
    public int getStock() { return stock; }
    public String getDescripcion() { return descripcion; }
    public int getAnio() { return anio; }

    //Setters
    public void setId(Long id) { this.id = id; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public void setAutor(String autor) { this.autor = autor; }
    public void setPrecio(int precio) { this.precio = precio; }
    public void setStock(int stock) { this.stock = stock; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public void setAnio(int anio) { this.anio = anio; }

}
