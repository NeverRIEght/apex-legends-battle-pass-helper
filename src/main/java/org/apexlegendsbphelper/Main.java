package org.apexlegendsbphelper;


import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.File;
import javax.imageio.*;

public class Main {
    public static String imagePath = "/Users/michaelkomarov/Downloads/image_2023-10-24_19-32-46.png";
    public static String grayscaleImagePath;
    public static void main(String[] args) throws TesseractException, IOException {

        int lastStop = imagePath.lastIndexOf('.');

        if (lastStop >= 0) {
            grayscaleImagePath = imagePath.substring(0, lastStop) + "_grayscale.png";
        } else {
            grayscaleImagePath = null;
        }


        BufferedImage inputImage = loadImage(imagePath);
        BufferedImage grayscaleImage = imageToGrayscale(inputImage, grayscaleImagePath);


        setImageThreshold(grayscaleImage);

        Tesseract tesseract = new Tesseract();
        tesseract.setDatapath("/opt/homebrew/Cellar/tesseract/5.3.3/share/tessdata/");
        tesseract.setLanguage("eng+eng_digit");
        tesseract.setPageSegMode(1);
        tesseract.setOcrEngineMode(1);
        String result = tesseract.doOCR(new File(grayscaleImagePath));
        System.out.println(result);
    }

    public static BufferedImage loadImage(String pathToImage) throws IOException {
        try {
            return ImageIO.read(new File(pathToImage));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static BufferedImage imageToGrayscale (BufferedImage image, String pathToGrayscale) throws IOException {
        try {
            int width = image.getWidth();
            int height = image.getHeight();

            BufferedImage grayscaleImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
            Graphics g = grayscaleImage.getGraphics();
            g.drawImage(image, 0, 0, null);


            ImageIO.write(grayscaleImage, "png", new File(pathToGrayscale));
            return grayscaleImage;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static BufferedImage setImageThreshold (BufferedImage image) throws IOException {
        int width = image.getWidth();
        int height = image.getHeight();
        int threshold = 170;  // Пороговое значение для бинаризации

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color color = new Color(image.getRGB(x, y));
                int grayscale = (int) (0.299 * color.getRed() + 0.587 * color.getGreen() + 0.114 * color.getBlue());

                if (grayscale > threshold) {
                    image.setRGB(x, y, Color.WHITE.getRGB());
                } else {
                    image.setRGB(x, y, Color.BLACK.getRGB());
                }
            }
        }
        ImageIO.write(image, "png", new File(grayscaleImagePath));
        return image;
    }

}