package com.example.orctest.controller;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.orctest.domain.Document;
import com.example.orctest.service.AsyncOcrService;
import com.example.orctest.service.DocumentService;
import com.example.orctest.service.ExportService;
import com.example.orctest.service.OcrService;

import lombok.AllArgsConstructor;


@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
@CrossOrigin("*")
public class OrcController {
    private final OcrService ocrService;
    private final DocumentService documentService;
    private final AsyncOcrService asyncOcrService;
    private final ExportService exportService;

    @PostMapping("/upload-async")
        public ResponseEntity<?> uploadAsync(@RequestParam MultipartFile file) {
            this.asyncOcrService.process(file);
            return ResponseEntity.accepted().body("File is processing");
        }


    @PostMapping("/upload")
    public ResponseEntity<Document> upload(@RequestParam MultipartFile file) throws Exception {

    String contentType = file.getContentType();
    String text="";

    text = text.replaceAll("\\s{2,}", " ");


    if (contentType != null && contentType.equals("application/pdf")) {
        text = ocrService.ocrPdfSmart(file);
    } else {
        text = ocrService.ocrImage(file);
    }

    return ResponseEntity.ok(
        documentService.save(file.getOriginalFilename(), text)
    );
    }

    @GetMapping("/search")
    public ResponseEntity<List<Document>> search(@RequestParam String keyword) {
        return ResponseEntity.ok(documentService.search(keyword));
    }

        @GetMapping("/export/txt/{id}")
    public ResponseEntity<byte[]> exportTxt(@PathVariable Long id) {

        Document doc = this.documentService.getDocumentById(id);
        byte[] data = this.exportService.exportTxt(doc.getContent());

    return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=result.txt")
            .contentType(MediaType.TEXT_PLAIN)
            .body(data);
    }

    @GetMapping("/export/docx/{id}")
    public ResponseEntity<byte[]> exportDocx(@PathVariable Long id) throws Exception {

        Document doc = this.documentService.getDocumentById(id);
        byte[] data = exportService.exportDocx(
                doc.getFileName(),
                doc.getContent()
        );


    return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=result.docx")
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .body(data);
    }

    @DeleteMapping("/document/{id}")
    public ResponseEntity<Void> deleteDocument(@PathVariable("id") Long id){
        this.documentService.deleteDocument(id);
        return ResponseEntity.ok().body(null);
    }
}
