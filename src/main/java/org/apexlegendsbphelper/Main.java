package org.apexlegendsbphelper;


import net.sourceforge.tess4j.ITessAPI;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.File;
import javax.imageio.*;

public class Main {
    public static String imagePath = "/Users/michaelkomarov/Downloads/image_2023-10-24_19-32-46.png";
    public static String folderPath;
    public static String grayscaleImagePath;
    public static void main(String[] args) throws TesseractException, IOException {

        int lastStop = imagePath.lastIndexOf('.');
        if (lastStop >= 0) {
            grayscaleImagePath = imagePath.substring(0, lastStop) + "_grayscale.png";
        }

        int lastSlash = imagePath.lastIndexOf('/');
        if (lastSlash >= 0) {
            folderPath = imagePath.substring(0, lastSlash) + "/";
        }

        BufferedImage inputImage = loadImage(imagePath);
        BufferedImage grayscaleImage = imageToGrayscale(inputImage, grayscaleImagePath);


        setImageThreshold(grayscaleImage);

        int[] firstQuestCoords = findFirstQuest(grayscaleImage);

        cropImageByPixels(grayscaleImage, firstQuestCoords[0], firstQuestCoords[1], firstQuestCoords[2], firstQuestCoords[3]);
        Tesseract tesseract = new Tesseract();
        tesseract.setDatapath("/opt/homebrew/Cellar/tesseract/5.3.3/share/tessdata/");
        tesseract.setLanguage("eng+eng_old");
        tesseract.setPageSegMode(1);
        tesseract.setOcrEngineMode(1);
        tesseract.setPageSegMode(ITessAPI.TessPageSegMode.PSM_AUTO);
        String result = tesseract.doOCR(new File(folderPath + "tempimage.png"));
        System.out.println(result);

        cropImageByPixels(grayscaleImage, firstQuestCoords[4], firstQuestCoords[5], firstQuestCoords[6], firstQuestCoords[7]);
        result = tesseract.doOCR(new File(folderPath + "tempimage.png"));
        System.out.println(result);
    }

    public static BufferedImage loadImage(String pathToImage) throws IOException {
        return ImageIO.read(new File(pathToImage));
    }

    public static BufferedImage imageToGrayscale (BufferedImage image, String pathToGrayscale) throws IOException {
        BufferedImage result;
        try {
            int width = image.getWidth();
            int height = image.getHeight();

            BufferedImage grayscaleImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
            Graphics g = grayscaleImage.getGraphics();
            g.drawImage(image, 0, 0, null);


            ImageIO.write(grayscaleImage, "png", new File(pathToGrayscale));
            result = grayscaleImage;
        } catch (Exception e) {
            e.printStackTrace();
            result = null;
        }
        return result;
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

    public static int[] findFirstQuest (BufferedImage image) {
        int[] firstQuestCoords = new int[8];

        //Find the quest name from stratch
        outerLoop:
        for(int i = 0; i < 50; i++) {
            for(int j = 0; j < 50; j++) {
                if(image.getRGB(i, j) == -16777216) { // -16777216 = black; -1 = white
                    firstQuestCoords[0] = i;
                    firstQuestCoords[1] = j;
                    firstQuestCoords[2] = i + 800;
                    firstQuestCoords[3] = j + 32;
                    break outerLoop;
                }
            }
        }

        //Current quest progress
        firstQuestCoords[4] = firstQuestCoords[0] + 22;
        firstQuestCoords[5] = firstQuestCoords[3] + 3;
        firstQuestCoords[6] = firstQuestCoords[0] + 300;
        firstQuestCoords[7] = firstQuestCoords[3] + 25;

        //Next Quest name
//        firstQuestCoords[4] = firstQuestCoords[0] + 30;
//        firstQuestCoords[5] = firstQuestCoords[3] + 40;
//        firstQuestCoords[6] = firstQuestCoords[0] + 300;
//        firstQuestCoords[7] = firstQuestCoords[3] + 40 + 30;

//        for(int i = firstQuestCoords[0] + 20; i < firstQuestCoords[0] + 200; i++) {
//            for(int j = firstQuestCoords[3]; j < firstQuestCoords[3] + 28; j++) {
//                if(image.getRGB(i, j) == -16777216) {
//                    firstQuestCoords[0] = i;
//                    firstQuestCoords[1] = j;
//                    firstQuestCoords[2] = i + 800;
//                    firstQuestCoords[3] = j + 32;
//                    return firstQuestCoords;
//                }
//            }
//        }

        return firstQuestCoords;
    }

    public static BufferedImage cropImageByPixels(BufferedImage image, int startX, int startY, int endX, int endY) throws IOException {
        int width = endX - startX;
        int height = endY - startY;

        BufferedImage croppedImage = new BufferedImage(width, height, image.getType());

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int sourceX = x + startX;
                int sourceY = y + startY;

                int rgb = image.getRGB(sourceX, sourceY);
                croppedImage.setRGB(x, y, rgb);
            }
        }
        ImageIO.write(croppedImage, "png", new File(folderPath + "tempimage.png"));
        return croppedImage;
    }
}