package com.example.orctest.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AsyncOcrService {
      private final OcrService ocrService;
    private final DocumentService documentService;


    @Async
    public void process(MultipartFile file) {
        try {
            String text;

            if (file.getOriginalFilename().endsWith(".pdf")) {
                text = ocrService.ocrPdf(file);
            } else {
                text = ocrService.ocrImage(file);
            }

            documentService.save(file.getOriginalFilename(), text);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
