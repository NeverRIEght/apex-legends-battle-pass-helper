package org.apexlegendsbphelper;
import static org.apexlegendsbphelper.ImageUtil.*;

import net.sourceforge.tess4j.TesseractException;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;



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

        int i = 0;
        while(i < firstQuestCoords.length) {
            System.out.println(firstQuestCoords[i]);
            i++;
        }

        try {
            i = 0;
            while (i < 1000) {
                tempImage = cropImageByPixels(inputImage, folderPath + "tempimage" + i + ".png", firstQuestCoords[0], firstQuestCoords[1], firstQuestCoords[2], firstQuestCoords[3]);
//                firstQuestCoords[0]++;
//                firstQuestCoords[1]++;
//                firstQuestCoords[2]++;
//                firstQuestCoords[3]++;
                i++;
            }
        } catch (Exception ex) {

        }
        //tempImage = cropImageByPixels(inputImage, tempImagePath, firstQuestCoords[0], firstQuestCoords[1], firstQuestCoords[2], firstQuestCoords[3]);















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