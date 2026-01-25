package com.example.orctest.service;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import net.sourceforge.tess4j.Tesseract;

@Service
@AllArgsConstructor
public class OcrService {

    private final Tesseract tesseract;
    private final ImagePreprocessService preprocessService;

public String ocrImage(MultipartFile file) throws Exception {

    BufferedImage image = ImageIO.read(file.getInputStream());

    if (image == null) {
        throw new IllegalArgumentException("Unsupported image format");
    }

    BufferedImage processed = preprocessService.preprocessForOCR(image);

    return tesseract.doOCR(processed);
}


public String ocrPdf(MultipartFile file) throws Exception {

    PDDocument document = PDDocument.load(file.getInputStream());
    PDFRenderer renderer = new PDFRenderer(document);

    StringBuilder text = new StringBuilder();

    for (int i = 0; i < document.getNumberOfPages(); i++) {

        BufferedImage image = renderer.renderImageWithDPI(i, 400);

        BufferedImage processed = preprocessService.preprocessForOCR(image);

        text.append(tesseract.doOCR(processed)).append("\n");
    }

    document.close();
    return text.toString();
}

public String ocrPdfSmart(MultipartFile file) throws Exception {

    if (isTextBasedPdf(file)) {
        try (PDDocument doc = PDDocument.load(file.getInputStream())) {
            return new PDFTextStripper().getText(doc);
        }
    }

    return ocrPdf(file);
}

public boolean isTextBasedPdf(MultipartFile file) throws IOException {
    try (PDDocument doc = PDDocument.load(file.getInputStream())) {
        PDFTextStripper stripper = new PDFTextStripper();
        String text = stripper.getText(doc);
        return text != null && text.trim().length() > 50;
    }
}


}
