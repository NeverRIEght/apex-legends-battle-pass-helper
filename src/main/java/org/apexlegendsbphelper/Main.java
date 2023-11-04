package org.apexlegendsbphelper;
import static org.apexlegendsbphelper.FileUtil.*;
import static org.apexlegendsbphelper.ImageUtil.*;

import net.sourceforge.tess4j.TesseractException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class Main {
    public static String inputFolderPath = "D:\\apex-tests";
    public static String inputImageFolderPath;
    public static String tempFolderPath;
    public static String tempImagePath;
    public static String grayscaleImagePath;
    public static void main(String[] args) throws TesseractException, IOException {


        // Create and write path`s to directories

        inputImageFolderPath = new File(inputFolderPath).toString();
        tempFolderPath = new File(inputImageFolderPath).toString() + File.separator + "tmp";
        grayscaleImagePath = tempFolderPath + File.separator + "input_grayscale.png";
        tempImagePath = tempFolderPath + File.separator + "input_blackwhite.png";

        recreateDirectories(tempFolderPath);


        File mainDirectory = new File(inputFolderPath);
        File[] weekImages = mainDirectory.listFiles();
        for (int imgIndex = 0; imgIndex < weekImages.length; imgIndex++) {
            if (weekImages[imgIndex].isFile() && weekImages[imgIndex].getName().endsWith(".png")) {


                // Create a recognition-essential version of input image

                BufferedImage inputImage = ImageIO.read(weekImages[imgIndex]);
                BufferedImage grayscaleImage = imageToGrayscale(inputImage, grayscaleImagePath);
                imageToBlackWhite(grayscaleImage, tempImagePath, 170);
                BufferedImage tempImage = loadImage(tempImagePath);

                getTextCoords(tempImagePath);

                // Calculate the first quest borders

//                int[] firstQuestCoords = findFirstQuest(tempImage);
//
//
//                // Calculate the total amount of quest on the image
//
//                int remainingHeight = inputImage.getHeight() - firstQuestCoords[1];
//                int questsCount = remainingHeight / ((firstQuestCoords[3] - firstQuestCoords[1]) + 6);
//
//
//                // Crop quests from the main image and save it as tmp/tempimageN.png
//
//                int heightIncrement = (firstQuestCoords[3] - firstQuestCoords[1]) + 6;
//
//                for (int i = 0; i < questsCount; i++) {
//
//
//                    // Crop the main image and save it as tmp/tempimageN.png
//
//                    cropImageByPixels(inputImage, tempFolderPath + File.separator
//                                    + "tempimage" + i + ".png",
//                                      firstQuestCoords[0], firstQuestCoords[1],
//                                      firstQuestCoords[2], firstQuestCoords[3]);
//
//                    firstQuestCoords[1] += heightIncrement;
//                    firstQuestCoords[3] += heightIncrement;
//                }
//
//
//                // Import and proceed every image in /tmp
//
//                File tmpDirectory = new File(tempFolderPath);
//                File[] tmpImages = tmpDirectory.listFiles();
//                for (int i = 0; i < tmpImages.length; i++) {
//                    if (tmpImages[i].isFile() && (!tmpImages[i].getName().equals("input_grayscale.png") && (!tmpImages[i].getName().equals("input_blackwhite.png")))) {
//
//                        Quest currQuest = new Quest();
//
//
//                        BufferedImage buffImage = ImageIO.read(tmpImages[i]);
//
//                        int lastDotIndex = tmpImages[i].getName().lastIndexOf(".");
//                        String imgNumber = tmpImages[i].getName().substring(lastDotIndex - 1, lastDotIndex);
//                        String newImageName = tmpImages[i].getName().substring(0, lastDotIndex - 1) + imgNumber + "_tmpOR.png";
//
//                        BufferedImage currentImage = cropImageByPixels(buffImage, tempFolderPath + File.separator + "tmpOR" + File.separator + newImageName, 400, 0, 470, 64);
//
//                        imageToGrayscale(currentImage, tempFolderPath + File.separator + "tmpOR" + File.separator + newImageName);
//                        imageToBlackWhite(currentImage, tempFolderPath + File.separator + "tmpOR" + File.separator + newImageName, 120);
//                        currQuest.setHasBothGamemodes(imageIsOR(currentImage));
//
//                        newImageName = tmpImages[i].getName().substring(0, lastDotIndex - 1) + imgNumber + "_tmpBR.png";
//                        currentImage = cropImageByPixels(buffImage, tempFolderPath + File.separator + "tmpBR" + File.separator + newImageName, 5, 5, 15, 15);
//                        currQuest.setHasBR(checkForBROption(currentImage));
//
//                        newImageName = tmpImages[i].getName().substring(0, lastDotIndex - 1) + imgNumber + "_tmpNBR.png";
//                        currentImage = cropImageByPixels(buffImage, tempFolderPath + File.separator + "tmpNBR" + File.separator + newImageName, 470, 5, 480, 15);
//                        currQuest.setHasNBR(checkForNBROption(currentImage));
//
//
//                        // Recognise quest name
//
//                        newImageName = tmpImages[i].getName().substring(0, lastDotIndex - 1) + imgNumber + "_tmpName_BR.png";
//
//                        if(currQuest.isHasBothGamemodes()) {
//                            // BR Part
//
//                            newImageName = tmpImages[i].getName().substring(0, lastDotIndex - 1) + imgNumber + "_tmpName_BR.png";
//                            currentImage = cropImageByPixels(buffImage, tempFolderPath + File.separator + "tmpName" + File.separator + newImageName, 30, 0, 385, 30);
//
//                            currentImage = imageToGrayscale(currentImage, tempFolderPath + File.separator + "tmpName" + File.separator + newImageName);
//                            currentImage = imageToBlackWhite(currentImage, tempFolderPath + File.separator + "tmpName" + File.separator + newImageName, 200);
//
//                            currQuest.setName(recogniseText(tempFolderPath + File.separator + "tmpName" + File.separator + newImageName, false));
//
//                            // NBR Part
//
//                            newImageName = tmpImages[i].getName().substring(0, lastDotIndex - 1) + imgNumber + "_tmpName_NBR.png";
//                            currentImage = cropImageByPixels(buffImage, tempFolderPath + File.separator + "tmpName" + File.separator + newImageName, 504, 0, 880, 30);
//
//                            currentImage = imageToGrayscale(currentImage, tempFolderPath + File.separator + "tmpName" + File.separator + newImageName);
//                            currentImage = imageToBlackWhite(currentImage, tempFolderPath + File.separator + "tmpName" + File.separator + newImageName, 180);
//
//                            currQuest.setName(recogniseText(tempFolderPath + File.separator + "tmpName" + File.separator + newImageName, false));
//
//                        } else {
//                            newImageName = tmpImages[i].getName().substring(0, lastDotIndex - 1) + imgNumber + "_tmpName.png";
//                            if(currQuest.isHasBR()) {
//                                currentImage = cropImageByPixels(buffImage, tempFolderPath + File.separator + "tmpName" + File.separator + newImageName, 30, 0, 700, 30);
//                            } else {
//                                currentImage = cropImageByPixels(buffImage, tempFolderPath + File.separator + "tmpName" + File.separator + newImageName, 0, 0, 700, 30);
//                            }
//
//                            currentImage = imageToGrayscale(currentImage, tempFolderPath + File.separator + "tmpName" + File.separator + newImageName);
//                            currentImage = imageToBlackWhite(currentImage, tempFolderPath + File.separator + "tmpName" + File.separator + newImageName, 180);
//
//                            currQuest.setName(recogniseText(tempFolderPath + File.separator + "tmpName" + File.separator + newImageName, false));
//                        }
//
//
//                        // Recognise quest progress
//
//                        if(currQuest.isHasBothGamemodes()) {
//                            // BR Part
//
//                            newImageName = tmpImages[i].getName().substring(0, lastDotIndex - 1) + imgNumber + "_tmpPr_BR.png";
//                            currentImage = cropImageByPixels(buffImage, tempFolderPath + File.separator + "tmpPr" + File.separator + newImageName, 24, 34, 400, 60);
//
//                            currentImage = imageToGrayscale(currentImage, tempFolderPath + File.separator + "tmpPr" + File.separator + newImageName);
//                            currentImage = imageToBlackWhite(currentImage, tempFolderPath + File.separator + "tmpPr" + File.separator + newImageName, 180);
//
//                            if(recogniseText(tempFolderPath + File.separator + "tmpPr" + File.separator + newImageName, false).trim() == "Completed") {
//                                currQuest.setCompleted(true);
//                            }
//
//
//                            // NBR Part
//
//                            newImageName = tmpImages[i].getName().substring(0, lastDotIndex - 1) + imgNumber + "_tmpPr_BR.png";
//                            currentImage = cropImageByPixels(buffImage, tempFolderPath + File.separator + "tmpPr" + File.separator + newImageName, 490, 34, 700, 60);
//
//                            currentImage = imageToGrayscale(currentImage, tempFolderPath + File.separator + "tmpPr" + File.separator + newImageName);
//                            currentImage = imageToBlackWhite(currentImage, tempFolderPath + File.separator + "tmpPr" + File.separator + newImageName, 180);
//
//                            System.out.println("\"" + recogniseText(tempFolderPath + File.separator + "tmpPr" + File.separator + newImageName, false) + "\"");
//                            if(recogniseText(tempFolderPath + File.separator + "tmpPr" + File.separator + newImageName, false).trim() == "Completed") {
//                                currQuest.setCompleted(true);
//                            }
//                        } else {
//                            newImageName = tmpImages[i].getName().substring(0, lastDotIndex - 1) + imgNumber + "_tmpPr_BR.png";
//                            currentImage = cropImageByPixels(buffImage, tempFolderPath + File.separator + "tmpPr" + File.separator + newImageName, 24, 34, 400, 60);
//
//                            currentImage = imageToGrayscale(currentImage, tempFolderPath + File.separator + "tmpPr" + File.separator + newImageName);
//                            currentImage = imageToBlackWhite(currentImage, tempFolderPath + File.separator + "tmpPr" + File.separator + newImageName, 180);
//
//                            System.out.println("\"" + recogniseText(tempFolderPath + File.separator + "tmpPr" + File.separator + newImageName, false) + "\"");
//                            if(recogniseText(tempFolderPath + File.separator + "tmpPr" + File.separator + newImageName, false).trim() == "Completed") {
//                                currQuest.setCompleted(true);
//                            }
//                        }
//
//
//
//
//                        // Recognise quest reward
//
//                        newImageName = tmpImages[i].getName().substring(0, lastDotIndex - 1) + imgNumber + "_tmpRe_BR.png";
//                        currentImage = cropImageByPixels(buffImage, tempFolderPath + File.separator + "tmpRe" + File.separator + newImageName, buffImage.getWidth() - 100, 10, buffImage.getWidth() - 60, 50);
//
//                        currentImage = imageToGrayscale(currentImage, tempFolderPath + File.separator + "tmpRe" + File.separator + newImageName);
//                        currentImage = imageToBlackWhite(currentImage, tempFolderPath + File.separator + "tmpRe" + File.separator + newImageName, 170);
//
//                        //System.out.println("\"" + recogniseText(tempFolderPath + File.separator + "tmpRe" + File.separator + newImageName, true).trim() + "\"");
//
//
//                        // Print Quest
//
//                        //currQuest.setName(correctSentence(currQuest.getName(), System.getProperty("user.dir") + File.separator + "data" + File.separator + "tessvocabulary.txt"));
//
//                        System.out.println(String.format("Quest name: %s", currQuest.getName()));
//                        System.out.println(String.format("Has BR and NBR: %s", currQuest.isHasBothGamemodes()));
//                        System.out.println(String.format("Has BR: %s", currQuest.isHasBR()));
//                        System.out.println(String.format("Has NBR: %s", currQuest.isHasNBR()));
//                        System.out.println(String.format("Completed: %b", currQuest.isCompleted()));
//                        System.out.println("---------------");
//                    }
//                }
            }
        }






    }

}