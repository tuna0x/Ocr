package com.example.orctest.service;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.springframework.stereotype.Service;

@Service
public class ExportService {
       public byte[] exportTxt(String content) {
        return content.getBytes(StandardCharsets.UTF_8);
    }

    public byte[] exportDocx(String title, String content) throws Exception {
        XWPFDocument document = new XWPFDocument();

        // Title
        XWPFParagraph titlePara = document.createParagraph();
        XWPFRun titleRun = titlePara.createRun();
        titleRun.setBold(true);
        titleRun.setFontSize(14);
        titleRun.setText(title);

        // Content
        XWPFParagraph contentPara = document.createParagraph();
        XWPFRun contentRun = contentPara.createRun();
        contentRun.setFontSize(11);

        for (String line : content.split("\n")) {
            contentRun.setText(line);
            contentRun.addBreak();
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        document.write(out);
        document.close();

        return out.toByteArray();
    }
}
