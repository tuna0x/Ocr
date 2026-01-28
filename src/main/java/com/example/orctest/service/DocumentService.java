package com.example.orctest.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.orctest.Repository.DocumentRepository;
import com.example.orctest.domain.Document;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class DocumentService {
    private final DocumentRepository documentRepository;

        public Document save(String fileName, String content) {
        Document doc = new Document();
        doc.setFileName(fileName);
        doc.setContent(content);
        return this.documentRepository.save(doc);
    }

    public Document update (Document doc){
        Document document=this.documentRepository.findById(doc.getId()).orElse(null);
        document.setContent(doc.getContent());
        doc=this.documentRepository.save(document);
        return doc;

    }

    public List<Document> search(String keyword) {
        return this.documentRepository.search(keyword);
    }
    public Document getDocumentById(Long id){
        return this.documentRepository.findById(id).orElseThrow(() -> new RuntimeException("Document not found"));
    }

    public void deleteDocument(Long id){
        this.documentRepository.deleteById(id);
    }
}
