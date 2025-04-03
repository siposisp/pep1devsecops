package com.pep1devsecops.pep1devsecops;

import com.pep1devsecops.pep1devsecops.entities.LibroEntity;
import com.pep1devsecops.pep1devsecops.repositories.LibroRepository;
import com.pep1devsecops.pep1devsecops.services.LibroService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LibroServiceTest {

    @Mock
    private LibroRepository libroRepository;

    @InjectMocks
    private LibroService libroService;

    private LibroEntity libro;

    @BeforeEach
    void setUp() {
        libro = new LibroEntity();
        libro.setId(1L);
        libro.setTitulo("El Hobbit");
        libro.setAutor("J.R.R. Tolkien");
        libro.setPrecio(15000);
        libro.setStock(10);
        libro.setAnio(1937);
        libro.setDescripcion("Una novela de fantasía");
    }

    @Test
    void testSaveLibro() {
        when(libroRepository.save(any(LibroEntity.class))).thenReturn(libro);
        LibroEntity savedLibro = libroService.saveLibro(libro);
        assertNotNull(savedLibro);
        assertEquals("El Hobbit", savedLibro.getTitulo());
    }

    @Test
    void testGetLibros() {
        List<LibroEntity> libros = new ArrayList<>();
        libros.add(libro);
        when(libroRepository.findAll()).thenReturn(libros);
        assertEquals(1, libroService.getLibros().size());
    }

    @Test
    void testGetLibroById() {
        when(libroRepository.findById(anyLong())).thenReturn(Optional.of(libro));
        LibroEntity foundLibro = libroService.getLibroById(1L);
        assertEquals("El Hobbit", foundLibro.getTitulo());
    }

    @Test
    void testGetLibroByTitulo() {
        when(libroRepository.findByTitulo(anyString())).thenReturn(libro);
        LibroEntity foundLibro = libroService.getLibroByTitulo("El Hobbit");
        assertEquals("El Hobbit", foundLibro.getTitulo());
    }

    @Test
    void testUpdateLibro() {
        when(libroRepository.save(any(LibroEntity.class))).thenReturn(libro);
        LibroEntity updatedLibro = libroService.updateLibro(libro);
        assertEquals("El Hobbit", updatedLibro.getTitulo());
    }

    @Test
    void testVenderLibro_Success() {
        when(libroRepository.findById(anyLong())).thenReturn(Optional.of(libro));
        when(libroRepository.save(any(LibroEntity.class))).thenReturn(libro);

        LibroEntity updatedLibro = libroService.venderLibro(1L, 2);
        assertEquals(8, updatedLibro.getStock());
    }

    @Test
    void testVenderLibro_NotEnoughStock() {
        when(libroRepository.findById(anyLong())).thenReturn(Optional.of(libro));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            libroService.venderLibro(1L, 20);
        });
        assertEquals("400 BAD_REQUEST \"No hay suficiente stock\"", exception.getMessage());
    }

    @Test
    void testVenderLibro_NotFound() {
        when(libroRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            libroService.venderLibro(2L, 1);
        });
        assertEquals("404 NOT_FOUND \"Libro no encontrado\"", exception.getMessage());
    }

    @Test
    void testDeleteLibro() throws Exception {
        doNothing().when(libroRepository).deleteById(anyLong());
        assertTrue(libroService.deleteLibro(1L));
    }

    @Test
    void testDeleteLibroByTitulo() throws Exception {
        doNothing().when(libroRepository).deleteLibroByTitulo(anyString());
        assertTrue(libroService.deleteLibroByTitulo("El Hobbit"));
    }

    @Test
    void testDeleteLibroByTitulo_Exception() {
        doThrow(new RuntimeException("Database error")).when(libroRepository).deleteLibroByTitulo("El Hobbit");

        Exception exception = assertThrows(Exception.class, () -> {
            libroService.deleteLibroByTitulo("El Hobbit");
        });

        assertEquals("Database error", exception.getMessage());
    }

    @Test
    void testDeleteLibro_Exception() {
        doThrow(new RuntimeException("Database error")).when(libroRepository).deleteById(anyLong());

        Exception exception = assertThrows(Exception.class, () -> {
            libroService.deleteLibro(1L);
        });

        assertEquals("Database error", exception.getMessage());
    }


}

