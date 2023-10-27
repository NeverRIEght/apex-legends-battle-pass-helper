package org.apexlegendsbphelper;
import static org.apexlegendsbphelper.ImageUtil.*;

import net.sourceforge.tess4j.TesseractException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;



public class Main {
    public static String inputImagePath = "C:\\Users\\LFKom\\Downloads\\image_2023-10-24_19-32-46.png";
    public static String inputImageFolderPath;
    public static String tempFolderPath;
    public static String grayscaleImagePath;
    public static String tempImagePath;
    public static void main(String[] args) throws TesseractException, IOException {


        // Create and write path`s to directories

        inputImageFolderPath = new File(inputImagePath).getParentFile().toString();
        tempFolderPath = new File(inputImagePath).getParentFile().toString() + File.separator + "tmp";

        if (!new File(tempFolderPath).exists()) {
            if (!new File(tempFolderPath).mkdirs()) {
                System.out.println("Failed to create tmp directory");
            }
        }

        grayscaleImagePath = tempFolderPath + "input_grayscale.png";
        tempImagePath = tempFolderPath + "tempimage.png";


        // Create a recognition-essential version of input image

        BufferedImage inputImage = loadImage(inputImagePath);
        BufferedImage grayscaleImage = imageToGrayscale(inputImage, grayscaleImagePath);
        setImageThreshold(grayscaleImage, tempImagePath, 170);
        BufferedImage tempImage = loadImage(tempImagePath);


        // Calculate the first quest borders

        int[] firstQuestCoords = findFirstQuest(tempImage);


        // Calculate the total amount of quest on the image

        int remainingHeight = inputImage.getHeight() - firstQuestCoords[1];
        int questsCount = remainingHeight / ((firstQuestCoords[3] - firstQuestCoords[1]) + 6);


        // Crop quests from the main image and save it as tmp/tempimageN.png

        int heightIncrement = (firstQuestCoords[3] - firstQuestCoords[1]) + 6;

        int i = 0;
        while (i < questsCount + 1) {
            cropImageByPixels(inputImage, inputImageFolderPath + "tempimage" + i + ".png",
                              firstQuestCoords[0], firstQuestCoords[1], firstQuestCoords[2], firstQuestCoords[3]);
            firstQuestCoords[1] += heightIncrement;
            firstQuestCoords[3] += heightIncrement;
            i++;
        }















//        tempImage = cropImageByPixels(inputImage, tempImagePath, firstQuestCoords[0], firstQuestCoords[1], firstQuestCoords[2], firstQuestCoords[3]);
//        setImageThreshold(tempImage, tempImagePath, 95);
//
//        String result = recogniseText(tempImagePath);
//        System.out.println(result);
//
//
//
//        tempImage = cropImageByPixels(inputImage, tempImagePath, firstQuestCoords[4], firstQuestCoords[5], firstQuestCoords[6], firstQuestCoords[7]);
//        setImageThreshold(tempImage, tempImagePath, 95);
//
//        result = recogniseText(tempImagePath);
//        System.out.println(result);
//
//
//
//        tempImage = cropImageByPixels(inputImage, tempImagePath, firstQuestCoords[8], firstQuestCoords[9], firstQuestCoords[10], firstQuestCoords[11]);
//        setImageThreshold(tempImage, tempImagePath, 95);
//
//        result = recogniseText(tempImagePath);
//        System.out.println(result);

        // Считать существующую БД, если она есть
        // Запросить картинку / несколько картинок у пользователя / узнать нужны ли они
        // Определение количества квестов на картинке
        // Определение точных границ первого квеста
        // Определение наличия OR. Если OR нету, то определить ещё BR/NBR. Если есть - тоже определить наличие BR/NBR
        // Определение отступа и создание массива нарезанных квестов для распознавания
        // Для каждого квеста: нарезать на составные части в зависимости от режимов и OR, распознать текст
        // Записать полученное по классам
        // Записать классы в БД
        // Сохранить файл БД
    }

}