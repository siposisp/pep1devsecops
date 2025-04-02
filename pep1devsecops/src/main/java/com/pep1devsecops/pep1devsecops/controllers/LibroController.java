package com.pep1devsecops.pep1devsecops.controllers;

import com.pep1devsecops.pep1devsecops.entities.LibroEntity;
import com.pep1devsecops.pep1devsecops.services.LibroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/libreria")
@CrossOrigin("*")
public class LibroController {
    @Autowired
    LibroService libroService;

    // Endpoint para vender libros y reducir el stock
    @PostMapping("/vender/{id}")
    public ResponseEntity<LibroEntity> venderLibro(@PathVariable Long id, @RequestParam int cantidadVendida) {
        // Llamamos al servicio para actualizar el stock
        LibroEntity libroActualizado = libroService.venderLibro(id, cantidadVendida);
        // Retornamos el libro actualizado
        return ResponseEntity.ok(libroActualizado);
    }

    @GetMapping("/")
    public ResponseEntity<List<LibroEntity>> librosList() {
        List<LibroEntity> libros = libroService.getLibros();
        return ResponseEntity.ok(libros);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LibroEntity> getLibroById(@PathVariable Long id) {
        LibroEntity libro = libroService.getLibroById(id);
        return ResponseEntity.ok(libro);
    }


    @GetMapping("/titulo/{titulo}")
    public ResponseEntity<LibroEntity> getLibroByTitulo(@PathVariable String titulo) {
        LibroEntity libro = libroService.getLibroByTitulo(titulo);
        return ResponseEntity.ok(libro);
    }


    @PostMapping("/")
    public ResponseEntity<LibroEntity> saveLibro(@RequestBody LibroEntity libro) {
        LibroEntity newLibro = libroService.saveLibro(libro);
        return ResponseEntity.ok(newLibro);
    }

    @PutMapping("/")
    public ResponseEntity<LibroEntity> updateLibro(@RequestBody LibroEntity libro){
        LibroEntity libroUpdated = libroService.updateLibro(libro);
        return ResponseEntity.ok(libroUpdated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteLibroById(@PathVariable Long id) throws Exception {
        var isDeleted = libroService.deleteLibro(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/deletePatent/{patent}")
    public ResponseEntity<Boolean> deleteLibroByTitulo(@PathVariable String titulo) throws Exception {
        var isDeleted = libroService.deleteLibroByTitulo(titulo);
        return ResponseEntity.noContent().build();
    }
}
