package org.apexlegendsbphelper;

import net.sourceforge.tess4j.ITessAPI;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import net.sourceforge.tess4j.ITessAPI.TessPageIteratorLevel;
import org.w3c.dom.ls.LSOutput;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.File;
import java.util.List;
import javax.imageio.*;

public abstract class ImageUtil {
    private static final String tesseractDatapath = System.getProperty("user.dir") + File.separator + "lib"
                                                  + File.separator + "tesseract" + File.separator + "share"
                                                  + File.separator + "tessdata";
    private static final String tesseractSetLanguage = "eng+eng_old";
    private static final String tesseractSetDigits = "digits";
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

    public static BufferedImage cropImageByPixels(BufferedImage image, String outputImagePath, int startX, int startY, int endX, int endY) throws IOException {
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

    public static String recogniseText(String pathToImage, boolean isDigitsOnly) throws TesseractException {
        Tesseract tesseract = new Tesseract();
        tesseract.setDatapath(tesseractDatapath);

        if (isDigitsOnly) {
            tesseract.setLanguage(tesseractSetDigits);
        } else {
            tesseract.setLanguage(tesseractSetLanguage);
        }

        tesseract.setPageSegMode(tesseractSetPageSegMode);
        tesseract.setOcrEngineMode(tesseractSetOcrEngineMode);
        tesseract.setPageSegMode(ITessAPI.TessPageSegMode.PSM_AUTO);

        return tesseract.doOCR(new File(pathToImage));
    }

    public static int[] searchFirstColoredPixel(BufferedImage image, int colorCode, int startX, int startY) {
        int coords[] = new int[] {-1, -1};
        for (int x = startX; x < image.getWidth(); x++) {
            for (int y = startY; y < image.getHeight(); y++) {

                int color = image.getRGB(x, y);
                if(color == colorCode) {
                    coords[0] = x;
                    coords[1] = y;
                    return coords;
                }
            }
        }
        return coords;
    }

    public static int[] searchLastColoredPixel(BufferedImage image, int colorCode, int startX, int startY, int endX, int endY) {
        int coords[] = new int[] {-1, -1};

        if(endX > image.getWidth() || endY > image.getHeight()) {
            return coords;
        }

        for (int x = startX; x < endX; x++) {
            for (int y = startY; y < endY; y++) {

                int color = image.getRGB(x, y);
                if(color == colorCode) {
                    coords[0] = x;
                    coords[1] = y;
                }
            }
        }
        return coords;
    }

    public static int determineQuestHeight(BufferedImage image) throws IOException {
        int coords1[] = new int[2];
        int coords2[] = new int[2];

        coords1 = searchFirstColoredPixel(image, -12544866, 0, 0);
        coords2 = searchFirstColoredPixel(image, -12544866, coords1[0], coords1[1] + 50);

        int questHeight = coords2[1] - coords1[1];
        return questHeight;
    }
}