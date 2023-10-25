package org.apexlegendsbphelper;
import static org.apexlegendsbphelper.ImageUtil.*;

import net.sourceforge.tess4j.ITessAPI;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.File;
import javax.imageio.*;



public class Main {
    public static String folderPath;
    public static String imagePath = "/Users/michaelkomarov/Downloads/image_2023-10-24_19-32-46.png";
    public static String grayscaleImagePath;
    public static String tempImagePath;
    public static void main(String[] args) throws TesseractException, IOException {

        int lastStop = imagePath.lastIndexOf('.');
        if (lastStop >= 0) {
            grayscaleImagePath = imagePath.substring(0, lastStop) + "_grayscale.png";
        }

        int lastSlash = imagePath.lastIndexOf('/');
        if (lastSlash >= 0) {
            folderPath = imagePath.substring(0, lastSlash) + "/";
            tempImagePath = folderPath + "tempimage.png";
        }

        BufferedImage inputImage = loadImage(imagePath);
        BufferedImage grayscaleImage = imageToGrayscale(inputImage, grayscaleImagePath);
        setImageThreshold(grayscaleImage, tempImagePath, 170);
        BufferedImage tempImage = loadImage(tempImagePath);

        int[] firstQuestCoords = findFirstQuest(tempImage);



        cropImageByPixels(inputImage, tempImagePath, firstQuestCoords[0], firstQuestCoords[1], firstQuestCoords[2], firstQuestCoords[3]);
        tempImage = loadImage(tempImagePath);
        setImageThreshold(tempImage, tempImagePath, 95);

        String result = recogniseText(tempImagePath);
        System.out.println(result);



        cropImageByPixels(inputImage, tempImagePath, firstQuestCoords[4], firstQuestCoords[5], firstQuestCoords[6], firstQuestCoords[7]);
        tempImage = loadImage(tempImagePath);
        setImageThreshold(tempImage, tempImagePath, 95);

        result = recogniseText(tempImagePath);
        System.out.println(result);



        cropImageByPixels(inputImage, tempImagePath, firstQuestCoords[8], firstQuestCoords[9], firstQuestCoords[10], firstQuestCoords[11]);
        tempImage = loadImage(tempImagePath);
        setImageThreshold(tempImage, tempImagePath, 95);

        result = recogniseText(tempImagePath);
        System.out.println(result);
    }

}