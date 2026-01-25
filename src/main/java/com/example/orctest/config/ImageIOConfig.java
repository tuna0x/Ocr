package com.example.orctest.config;

import javax.imageio.ImageIO;

import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;

@Configuration
public class ImageIOConfig {

    @PostConstruct
    public void registerWebP() {
        ImageIO.scanForPlugins();
    }
}
