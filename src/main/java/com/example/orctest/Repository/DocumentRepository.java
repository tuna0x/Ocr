package com.example.orctest.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.orctest.domain.Document;
@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {
        @Query("SELECT d FROM Document d WHERE d.content LIKE %:keyword%")
    List<Document> search(@Param("keyword") String keyword);
}
