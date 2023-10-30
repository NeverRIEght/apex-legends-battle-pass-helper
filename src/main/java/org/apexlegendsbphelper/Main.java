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
    public static String tempImagePath;
    public static String grayscaleImagePath;
    public static void main(String[] args) throws TesseractException, IOException {


        // Create and write path`s to directories

        inputImageFolderPath = new File(inputImagePath).getParentFile().toString();
        tempFolderPath = new File(inputImagePath).getParentFile().toString() + File.separator + "tmp";
        if (new File(tempFolderPath).exists()) {
            File tmpDirectory = new File(tempFolderPath);
            File[] tmpImages = tmpDirectory.listFiles();

            for (int i = 0; i < tmpImages.length; i++) {
                if (tmpImages[i].isFile()) {
                    tmpImages[i].delete();
                }
            }
        } else {
            new File(tempFolderPath).mkdirs();
        }

        grayscaleImagePath = tempFolderPath + File.separator + "input_grayscale.png";
        tempImagePath = tempFolderPath + File.separator + "input_blackwhite.png";

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

        for (int i = 0; i < questsCount + 1; i++) {


            // Crop the main image and save it as tmp/tempimageN.png

            cropImageByPixels(inputImage, tempFolderPath + File.separator + "tempimage" + i + ".png",
                              firstQuestCoords[0], firstQuestCoords[1], firstQuestCoords[2], firstQuestCoords[3]);

            // Create an object, which will define current quest as an object

//            QuestUtil currentQuest = new QuestUtil(loadImage(tempFolderPath + File.separator + "tempimage" + i + ".png"),
//                    1, firstQuestCoords);

            firstQuestCoords[1] += heightIncrement;
            firstQuestCoords[3] += heightIncrement;
        }


        // Import and proceed every image in /tmp

//        File tmpDirectory = new File(tempFolderPath);
//        File[] tmpImages = tmpDirectory.listFiles();
//
//        if (tmpImages != null) {
//            for (int i = 0; i < tmpImages.length; i++) {
//                if (tmpImages[i].isFile() && (!tmpImages[i].getName().toLowerCase().endsWith("_grayscale.png")) && (!tmpImages[i].getName().toLowerCase().endsWith("_blackwhite.png"))) {
//                    try {
//                        BufferedImage buffImage = ImageIO.read(tmpImages[i]);
//
//                        BufferedImage orImage = cropImageByPixels(buffImage, tempFolderPath + File.separator + "tempimage" + i + "OR.png", 385, 0, 470, 65);
//
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        } else {
//            System.err.println("Директория не существует или не является директорией.");
//        }





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

//         Считать существующую БД, если она есть
//         Запросить картинку / несколько картинок у пользователя / узнать нужны ли они
//         Определение количества квестов на картинке
//         Определение точных границ первого квеста
//         Определение наличия OR. Если OR нету, то определить ещё BR/NBR. Если есть - тоже определить наличие BR/NBR
//         Определение отступа и создание массива нарезанных квестов для распознавания
//         Для каждого квеста: нарезать на составные части в зависимости от режимов и OR, распознать текст
//         Записать полученное по классам
//         Записать классы в БД
//         Сохранить файл БД
    }

}