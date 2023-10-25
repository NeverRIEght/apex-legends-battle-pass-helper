package org.apexlegendsbphelper;
import static org.apexlegendsbphelper.ImageUtil.*;

import net.sourceforge.tess4j.TesseractException;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;



public class Main {
    public static String folderPath;
    public static String imagePath = "C:\\Users\\LFKom\\Downloads\\image_2023-10-24_19-32-46.png";
    public static String grayscaleImagePath;
    public static String tempImagePath;
    public static void main(String[] args) throws TesseractException, IOException {

        int lastStop = imagePath.lastIndexOf('.');
        if (lastStop >= 0) {
            grayscaleImagePath = imagePath.substring(0, lastStop) + "_grayscale.png";
        }

        int lastSlash = imagePath.lastIndexOf(File.separator);
        if (lastSlash >= 0) {
            folderPath = imagePath.substring(0, lastSlash) + File.separator;
            tempImagePath = folderPath + "tempimage.png";
            System.out.println(folderPath + "; " + lastSlash + "; " + tempImagePath);
        } else {
            System.out.println(folderPath + "; " + lastSlash + "; " + tempImagePath);
        }

        BufferedImage inputImage = loadImage(imagePath);
        BufferedImage grayscaleImage = imageToGrayscale(inputImage, grayscaleImagePath);
        setImageThreshold(grayscaleImage, tempImagePath, 170);
        BufferedImage tempImage = loadImage(tempImagePath);

        int[] firstQuestCoords = findFirstQuest(tempImage);



        tempImage = cropImageByPixels(inputImage, tempImagePath, firstQuestCoords[0], firstQuestCoords[1], firstQuestCoords[2], firstQuestCoords[3]);
        setImageThreshold(tempImage, tempImagePath, 95);

        String result = recogniseText(tempImagePath);
        System.out.println(result);



        tempImage = cropImageByPixels(inputImage, tempImagePath, firstQuestCoords[4], firstQuestCoords[5], firstQuestCoords[6], firstQuestCoords[7]);
        setImageThreshold(tempImage, tempImagePath, 95);

        result = recogniseText(tempImagePath);
        System.out.println(result);



        tempImage = cropImageByPixels(inputImage, tempImagePath, firstQuestCoords[8], firstQuestCoords[9], firstQuestCoords[10], firstQuestCoords[11]);
        setImageThreshold(tempImage, tempImagePath, 95);

        result = recogniseText(tempImagePath);
        System.out.println(result);
    }

}