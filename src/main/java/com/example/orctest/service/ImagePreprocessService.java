package com.example.orctest.service;

import java.awt.Graphics;
import java.awt.Image.*;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;

import org.springframework.stereotype.Service;

@Service
public class ImagePreprocessService {

public BufferedImage preprocessForOCR(BufferedImage src) {

    // 1️⃣ Chuyển grayscale
    BufferedImage gray = new BufferedImage(
        src.getWidth(),
        src.getHeight(),
        BufferedImage.TYPE_BYTE_GRAY
    );

    Graphics g = gray.getGraphics();
    g.drawImage(src, 0, 0, null);
    g.dispose();

    // 2️⃣ Tăng contrast NHẸ (không threshold)
    RescaleOp rescale = new RescaleOp(1.2f, 10, null);
    gray = rescale.filter(gray, null);

    return gray;
}
}
