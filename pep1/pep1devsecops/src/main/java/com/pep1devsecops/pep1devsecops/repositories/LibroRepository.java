package com.pep1devsecops.pep1devsecops.repositories;

import com.pep1devsecops.pep1devsecops.entities.LibroEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LibroRepository extends JpaRepository<LibroEntity, Long> {
    public LibroEntity findByTitulo(String titulo);

    @Query(value = "DELETE FROM libros WHERE titulo = :titulo", nativeQuery = true)
    @Modifying
    @Transactional
    void deleteLibroByTitulo(@Param("titulo") String titulo);
}
