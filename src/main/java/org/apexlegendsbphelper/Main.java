package org.apexlegendsbphelper;


import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import java.io.File;

public class Main {
    public static void main(String[] args) throws TesseractException {
        File image = new File("/Users/michaelkomarov/Downloads/photo_2023-10-24_19-20-40.jpg");
        Tesseract tesseract = new Tesseract();
        tesseract.setDatapath("/opt/homebrew/Cellar/tesseract/5.3.3/share/tessdata/");
        tesseract.setLanguage("eng");
        tesseract.setPageSegMode(1);
        tesseract.setOcrEngineMode(1);
        String result = tesseract.doOCR(image);
        System.out.println(result);
    }
}