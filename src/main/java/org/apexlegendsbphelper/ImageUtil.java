package org.apexlegendsbphelper;

import net.sourceforge.tess4j.ITessAPI;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.w3c.dom.ls.LSOutput;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.File;
import javax.imageio.*;

public abstract class ImageUtil {
    private static final String tesseractDatapath = System.getProperty("user.dir") + File.separator + "lib"
                                                  + File.separator + "tesseract" + File.separator + "share"
                                                  + File.separator + "tessdata";
    private static final String tesseractSetLanguage = "eng+eng_old";
    static int tesseractSetPageSegMode = 1;
    static int tesseractSetOcrEngineMode = 1;
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

    public static BufferedImage imageToBlackWhite(BufferedImage image, String outputPath, int threshold) throws IOException {
        int width = image.getWidth();
        int height = image.getHeight();

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
        ImageIO.write(image, "png", new File(outputPath));
        return image;
    }

    public static int[] findFirstQuest (BufferedImage image) {
//        int[] firstQuestCoords = new int[12];
//
//        //Current quest name
//        outerLoop:
//        for(int i = 0; i < 50; i++) {
//            for(int j = 0; j < 50; j++) {
//                if(image.getRGB(i, j) == -16777216) { // -16777216 = black; -1 = white
//                    firstQuestCoords[0] = i;
//                    firstQuestCoords[1] = j;
//                    firstQuestCoords[2] = i + 800;
//                    firstQuestCoords[3] = j + 32;
//                    break outerLoop;
//                }
//            }
//        }
//
//        //Current quest progress
//        firstQuestCoords[4] = firstQuestCoords[0] + 22;
//        firstQuestCoords[5] = firstQuestCoords[3] + 3;
//        firstQuestCoords[6] = firstQuestCoords[0] + 300;
//        firstQuestCoords[7] = firstQuestCoords[3] + 25;
//
//        //Stars for current quest
//        firstQuestCoords[8] = image.getWidth() - 120;
//        firstQuestCoords[9] = firstQuestCoords[1] + 18;
//        firstQuestCoords[10] = image.getWidth() - 60;
//        firstQuestCoords[11] =  firstQuestCoords[9] + 30;
//
//
//        return firstQuestCoords;


        int[] firstQuestCoords = {0, 0, 0, 0};

        outerLoop:
        for(int i = 0; i < 50; i++) {
            for(int j = 0; j < 50; j++) {
                if(image.getRGB(i, j) == -16777216) { // -16777216 = black; -1 = white
                    firstQuestCoords[0] = i;
                    firstQuestCoords[1] = j;
                    break outerLoop;
                }
            }
        }



        for(int j = firstQuestCoords[1]; j < 100; j++) {
            if(image.getRGB(firstQuestCoords[0], j) == -1) { // -16777216 = black; -1 = white
                firstQuestCoords[3] = j;
                break;
            }
        }

        if(firstQuestCoords[0] != 0 && firstQuestCoords[1] != 0 && firstQuestCoords[3] != 0) {
            firstQuestCoords[2] = firstQuestCoords[0] + 990;
        }

        return firstQuestCoords;
    }

    public static BufferedImage cropImageByPixels(BufferedImage image, String outputImagePath, int startX, int startY, int endX, int endY) throws IOException {
        int width = endX - startX;
        int height = endY - startY;

        //System.out.println(outputImagePath);

        BufferedImage croppedImage = new BufferedImage(width, height, image.getType());

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int sourceX = x + startX;
                int sourceY = y + startY;

                //System.out.println("sourceX: " + sourceX + "; sourceY: " + sourceY);

                int rgb = image.getRGB(sourceX, sourceY);
                croppedImage.setRGB(x, y, rgb);
            }
        }
        ImageIO.write(croppedImage, "png", new File(outputImagePath));
        return croppedImage;
    }

    public static BufferedImage cropImageByPixels(BufferedImage image, String outputImagePath, int[] coords) throws IOException {

        int startX = coords[0];
        int startY = coords[1];
        int endX = coords[2];
        int endY = coords[3];


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
        ImageIO.write(croppedImage, "png", new File(outputImagePath));
        return croppedImage;
    }

    public static String recogniseText(String pathToImage) throws TesseractException {
        Tesseract tesseract = new Tesseract();
        tesseract.setDatapath(tesseractDatapath);
        tesseract.setLanguage(tesseractSetLanguage);
        tesseract.setPageSegMode(tesseractSetPageSegMode);
        tesseract.setOcrEngineMode(tesseractSetOcrEngineMode);
        tesseract.setPageSegMode(ITessAPI.TessPageSegMode.PSM_AUTO);

        return tesseract.doOCR(new File(pathToImage));
    }

    public static boolean imageIsOR(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        int ORCounter = 0;
        boolean ifOR = false;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (image.getRGB(x, y) == -1) {
                    int whitePixelCount = 0;
                    for (int j = x + 1; j < width; j++) {
                        if (image.getRGB(j, y) == -1) {
                            whitePixelCount++;
                        } else {
                            break;
                        }
                        if (whitePixelCount == 9) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    public static boolean checkForBROption(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (image.getRGB(x, y) == -12544866) {
                    return true;
                }
            }
        }

        return false;
    }

    public static boolean checkForNBROption(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (image.getRGB(x, y) == -16758925) {
                    return true;
                }
            }
        }

        return false;
    }
}