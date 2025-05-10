package com.pep1devsecops.pep1devsecops.services;

import com.pep1devsecops.pep1devsecops.entities.LibroEntity;
import com.pep1devsecops.pep1devsecops.repositories.LibroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service

public class LibroService {
    @Autowired
    LibroRepository libroRepository;

    // --------------------------CREATE--------------------------

    public LibroEntity saveLibro(LibroEntity libro){
        return libroRepository.save(libro);
    }


    // ---------------------------READ---------------------------

    public ArrayList<LibroEntity> getLibros(){
        return (ArrayList<LibroEntity>) libroRepository.findAll();
    }

    public LibroEntity getLibroById(Long id){
        return libroRepository.findById(id).get();
    }

    public LibroEntity getLibroByTitulo(String titulo) {
        return libroRepository.findByTitulo(titulo);
    }


    // --------------------------UPDATE--------------------------

    public LibroEntity updateLibro(LibroEntity libro) {
        return libroRepository.save(libro);
    }


    //Vender libros (el mismo) y reducir el stock
    public LibroEntity venderLibro(Long id, int cantidadVendida) {
        // Buscar el libro por ID
        LibroEntity libro = libroRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Libro no encontrado"));

        // Verificar si hay suficiente stock
        if (libro.getStock() < cantidadVendida) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No hay suficiente stock");
        }

        // Reducir el stock
        libro.setStock(libro.getStock() - cantidadVendida);

        // Guardar el libro con el stock actualizado
        return libroRepository.save(libro);
    }


    // --------------------------DELETE--------------------------

    public boolean deleteLibro(Long id) throws Exception {
        try {
            libroRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }

    }

    public boolean deleteLibroByTitulo(String titulo) throws Exception {
        try {
            libroRepository.deleteLibroByTitulo(titulo);
            return true;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }

    }

    // --------------------------SEARCH--------------------------

    // Método para buscar libros por diferentes campos
    public List<LibroEntity> buscarLibros(String search) {
        List<LibroEntity> libros = (List<LibroEntity>) libroRepository.findAll();  // Obtener todos los libros de la base de datos

        if (search != null && !search.isEmpty()) {
            // Filtrar libros por título, autor, descripción, etc.
            libros = libros.stream()
                    .filter(libro -> libro.getTitulo().toLowerCase().contains(search.toLowerCase()) ||
                            libro.getAutor().toLowerCase().contains(search.toLowerCase()) ||
                            libro.getDescripcion().toLowerCase().contains(search.toLowerCase()) ||
                            String.valueOf(libro.getAnio()).contains(search) ||  // Conversión explícita a String
                            String.valueOf(libro.getStock()).contains(search) ||  // Conversión explícita a String
                            String.valueOf(libro.getPrecio()).contains(search)) // Conversión explícita a String
                    .collect(Collectors.toList());

        }

        return libros;
    }
}
