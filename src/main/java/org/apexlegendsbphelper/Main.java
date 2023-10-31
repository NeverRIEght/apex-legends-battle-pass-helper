package org.apexlegendsbphelper;
import static org.apexlegendsbphelper.FileUtil.*;
import static org.apexlegendsbphelper.ImageUtil.*;
import static org.apexlegendsbphelper.TextUtil.*;

import net.sourceforge.tess4j.TesseractException;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class Main {
    public static String inputImagePath = "/Users/michaelkomarov/Downloads/image_2023-10-24_19-32-46.png";
    public static String inputImageFolderPath;
    public static String tempFolderPath;
    public static String tempImagePath;
    public static String grayscaleImagePath;
    public static void main(String[] args) throws TesseractException, IOException {


        // Create and write path`s to directories

        inputImageFolderPath = new File(inputImagePath).getParentFile().toString();
        tempFolderPath = new File(inputImagePath).getParentFile().toString() + File.separator + "tmp";

        if (new File(tempFolderPath).exists()) {
            deleteDirectory(new File(tempFolderPath));
            new File(tempFolderPath).mkdirs();
            new File(tempFolderPath + File.separator + "tmpOR").mkdirs();
            new File(tempFolderPath + File.separator + "tmpBR").mkdirs();
            new File(tempFolderPath + File.separator + "tmpNBR").mkdirs();
            new File(tempFolderPath + File.separator + "tmpName").mkdirs();
            new File(tempFolderPath + File.separator + "tmpPr").mkdirs();
            new File(tempFolderPath + File.separator + "tmpRe").mkdirs();
        } else {
            new File(tempFolderPath).mkdirs();
            new File(tempFolderPath + File.separator + "tmpOR").mkdirs();
            new File(tempFolderPath + File.separator + "tmpBR").mkdirs();
            new File(tempFolderPath + File.separator + "tmpNBR").mkdirs();
            new File(tempFolderPath + File.separator + "tmpName").mkdirs();
            new File(tempFolderPath + File.separator + "tmpPr").mkdirs();
            new File(tempFolderPath + File.separator + "tmpRe").mkdirs();
        }



        grayscaleImagePath = tempFolderPath + File.separator + "input_grayscale.png";
        tempImagePath = tempFolderPath + File.separator + "input_blackwhite.png";

        // Create a recognition-essential version of input image

        BufferedImage inputImage = loadImage(inputImagePath);
        BufferedImage grayscaleImage = imageToGrayscale(inputImage, grayscaleImagePath);
        imageToBlackWhite(grayscaleImage, tempImagePath, 170);
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

        File tmpDirectory = new File(tempFolderPath);
        File[] tmpImages = tmpDirectory.listFiles();
        for (int i = 0; i < tmpImages.length; i++) {
            if (tmpImages[i].isFile() && (!tmpImages[i].getName().equals("input_grayscale.png") && (!tmpImages[i].getName().equals("input_blackwhite.png")))) {

                Quest currQuest = new Quest();


                BufferedImage buffImage = ImageIO.read(tmpImages[i]);

                int lastDotIndex = tmpImages[i].getName().lastIndexOf(".");
                String imgNumber = tmpImages[i].getName().substring(lastDotIndex - 1, lastDotIndex);
                String newImageName = tmpImages[i].getName().substring(0, lastDotIndex - 1) + imgNumber + "_tmpOR.png";

                BufferedImage currentImage = cropImageByPixels(buffImage, tempFolderPath + File.separator + "tmpOR" + File.separator + newImageName, 400, 0, 470, 64);

                imageToGrayscale(currentImage, tempFolderPath + File.separator + "tmpOR" + File.separator + newImageName);
                imageToBlackWhite(currentImage, tempFolderPath + File.separator + "tmpOR" + File.separator + newImageName, 120);
                currQuest.setHasBothGamemodes(imageIsOR(currentImage));

                newImageName = tmpImages[i].getName().substring(0, lastDotIndex - 1) + imgNumber + "_tmpBR.png";
                currentImage = cropImageByPixels(buffImage, tempFolderPath + File.separator + "tmpBR" + File.separator + newImageName, 5, 5, 15, 15);
                currQuest.setHasBR(checkForBROption(currentImage));

                newImageName = tmpImages[i].getName().substring(0, lastDotIndex - 1) + imgNumber + "_tmpNBR.png";
                currentImage = cropImageByPixels(buffImage, tempFolderPath + File.separator + "tmpNBR" + File.separator + newImageName, 470, 5, 480, 15);
                currQuest.setHasNBR(checkForNBROption(currentImage));



                newImageName = tmpImages[i].getName().substring(0, lastDotIndex - 1) + imgNumber + "_tmpName_BR.png";

                if(currQuest.isHasBothGamemodes()) {
                    // BR Part

                    newImageName = tmpImages[i].getName().substring(0, lastDotIndex - 1) + imgNumber + "_tmpName_BR.png";
                    currentImage = cropImageByPixels(buffImage, tempFolderPath + File.separator + "tmpName" + File.separator + newImageName, 30, 0, 385, 30);

                    currentImage = imageToGrayscale(currentImage, tempFolderPath + File.separator + "tmpName" + File.separator + newImageName);
                    currentImage = imageToBlackWhite(currentImage, tempFolderPath + File.separator + "tmpName" + File.separator + newImageName, 200);

                    currQuest.setName(recogniseText(tempFolderPath + File.separator + "tmpName" + File.separator + newImageName));

                    // NBR Part

                    newImageName = tmpImages[i].getName().substring(0, lastDotIndex - 1) + imgNumber + "_tmpName_NBR.png";
                    currentImage = cropImageByPixels(buffImage, tempFolderPath + File.separator + "tmpName" + File.separator + newImageName, 504, 0, 880, 30);

                    currentImage = imageToGrayscale(currentImage, tempFolderPath + File.separator + "tmpName" + File.separator + newImageName);
                    currentImage = imageToBlackWhite(currentImage, tempFolderPath + File.separator + "tmpName" + File.separator + newImageName, 180);

                    currQuest.setName(recogniseText(tempFolderPath + File.separator + "tmpName" + File.separator + newImageName));

                } else {
                    newImageName = tmpImages[i].getName().substring(0, lastDotIndex - 1) + imgNumber + "_tmpName.png";
                    if(currQuest.isHasBR()) {
                        currentImage = cropImageByPixels(buffImage, tempFolderPath + File.separator + "tmpName" + File.separator + newImageName, 30, 0, 700, 30);
                    } else {
                        currentImage = cropImageByPixels(buffImage, tempFolderPath + File.separator + "tmpName" + File.separator + newImageName, 0, 0, 700, 30);
                    }

                    currentImage = imageToGrayscale(currentImage, tempFolderPath + File.separator + "tmpName" + File.separator + newImageName);
                    currentImage = imageToBlackWhite(currentImage, tempFolderPath + File.separator + "tmpName" + File.separator + newImageName, 180);

                    currQuest.setName(recogniseText(tempFolderPath + File.separator + "tmpName" + File.separator + newImageName));
                }

                currQuest.setName(correctSentence(currQuest.getName(), System.getProperty("user.dir") + File.separator + "data" + File.separator + "tessvocabulary.txt"));

                System.out.println(String.format("Quest name: %s", currQuest.getName()));
                System.out.println(String.format("Has BR and NBR: %s", currQuest.isHasBothGamemodes()));
                System.out.println(String.format("Has BR: %s", currQuest.isHasBR()));
                System.out.println(String.format("Has NBR: %s", currQuest.isHasNBR()));
                System.out.println("---------------");

            }
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