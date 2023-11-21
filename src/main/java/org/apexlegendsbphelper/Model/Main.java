package org.apexlegendsbphelper.Model;

import static org.apexlegendsbphelper.Model.FileUtil.*;
import static org.apexlegendsbphelper.Model.ImageUtil.*;

import net.sourceforge.tess4j.TesseractException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Objects;


public class Main {
    public static String inputFolderPath = "D:\\apex-tests";
    public static String tempFolderPath;
    public static String tempImagePath;
    public static String blackWhiteImagePath;
    public static String grayscaleImagePath;
    public static int imageTreshold = 175;
    public static int questsTresholdHigh = 150;
    public static int questsTresholdLow = 120;

    public static void main(String[] args) throws TesseractException, IOException {


        tempFolderPath = new File(inputFolderPath) + File.separator + "tmp";
        tempImagePath = tempFolderPath + File.separator + "input.png";
        grayscaleImagePath = tempFolderPath + File.separator + "input_grayscale.png";
        blackWhiteImagePath = tempFolderPath + File.separator + "input_blackwhite.png";


        recreateDirectories(tempFolderPath);


        File mainDirectory = new File(inputFolderPath);
        File[] weekImages = mainDirectory.listFiles();
        for (int imgIndex = 0; imgIndex < Objects.requireNonNull(weekImages).length; imgIndex++) {
            if (weekImages[imgIndex].isFile() && weekImages[imgIndex].getName().endsWith(".png")) {

                BufferedImage inputImage = ImageIO.read(weekImages[imgIndex]);
                BufferedImage grayscaleImage = imageToGrayscale(inputImage, grayscaleImagePath);
                imageToBlackWhite(grayscaleImage, blackWhiteImagePath, imageTreshold);

                if (cropQuestsOnImage(inputImage)) {
                    System.out.println("Quests cropped successfully");
                } else {
                    System.out.println("Quests cropped with an error");
                }

                File questsDirectory = new File(tempFolderPath + File.separator + "tmpQuests");
                File[] questImages = questsDirectory.listFiles();
                for (int questIndex = 0; questIndex < Objects.requireNonNull(questImages).length; questIndex++) {
                    if (questImages[questIndex].isFile() && questImages[questIndex].getName().endsWith(".png")) {

                        BufferedImage questImage = ImageIO.read(questImages[questIndex]);

                        System.out.println(questImages[questIndex].getName());
                        int questType = determineQuestType(questImage);

                        int currentTreshold = questsTresholdHigh;

                        switch (questType) {
                            case 1 -> {
                                int[] firstNBRPixelCoords = searchFirstColoredPixel(questImage, -16758925, 0, 0);
                                int[] lastBRPixelCoords = searchLastColoredPixel(questImage, -12544866);
                                int[] lastNBRPixelCoords = searchLastColoredPixel(questImage, -16758925);

                                lastBRPixelCoords[0] += 2;
                                lastBRPixelCoords[1] += 2;
                                lastNBRPixelCoords[0] += 2;
                                lastNBRPixelCoords[1] += 2;
                                firstNBRPixelCoords[0] -= 2;

                                // Cut: Name BR

                                BufferedImage questBRPart = cropImageByPixels(questImage, tempFolderPath + File.separator + "tmpQuestsBR" + File.separator + "quest" + (questIndex + 1) + ".png", lastBRPixelCoords[0], 0, firstNBRPixelCoords[0], lastBRPixelCoords[1]);
                                questBRPart = cropImageByPixels(questBRPart, tempFolderPath + File.separator + "tmpQuestsBR" + File.separator + "quest" + (questIndex + 1) + ".png", 0, 0, questBRPart.getWidth() - questBRPart.getWidth() / 5, questBRPart.getHeight());

                                // Cut: Name NBR

                                BufferedImage questNBRPart = cropImageByPixels(questImage, tempFolderPath + File.separator + "tmpQuestsNBR" + File.separator + "quest" + (questIndex + 1) + ".png", lastNBRPixelCoords[0], 0, questImage.getWidth(), lastNBRPixelCoords[1]);
                                questNBRPart = cropImageByPixels(questNBRPart, tempFolderPath + File.separator + "tmpQuestsNBR" + File.separator + "quest" + (questIndex + 1) + ".png", 0, 0, questNBRPart.getWidth() - questNBRPart.getWidth() / 5, questNBRPart.getHeight());

                                // Cut: Status BR

                                BufferedImage questBRProgressPart = cropImageByPixels(questImage, tempFolderPath + File.separator + "tmpQuestsBRProgress" + File.separator + "quest" + (questIndex + 1) + ".png", 0, (questImage.getHeight() - questImage.getHeight() / 10 * 6), firstNBRPixelCoords[0], questImage.getHeight());
                                questBRProgressPart = cropImageByPixels(questBRProgressPart, tempFolderPath + File.separator + "tmpQuestsBRProgress" + File.separator + "quest" + (questIndex + 1) + ".png", questBRPart.getHeight() / 5 * 4, 0, questBRPart.getWidth() - questBRPart.getWidth() / 5, questBRPart.getHeight());

                                // Cut: Status NBR

                                BufferedImage questNBRProgressPart = cropImageByPixels(questImage, tempFolderPath + File.separator + "tmpQuestsNBRProgress" + File.separator + "quest" + (questIndex + 1) + ".png", questImage.getWidth() / 100 * 53, (questImage.getHeight() - questImage.getHeight() / 10 * 6), questImage.getWidth(), questImage.getHeight());
                                questNBRProgressPart = cropImageByPixels(questNBRProgressPart, tempFolderPath + File.separator + "tmpQuestsNBRProgress" + File.separator + "quest" + (questIndex + 1) + ".png", questNBRProgressPart.getWidth() / 100 * 1, 0, questBRPart.getWidth() - questBRPart.getWidth() / 5, questBRPart.getHeight());

                                // Cut: Reward

                                BufferedImage questRewardPart = cropImageByPixels(questImage, tempFolderPath + File.separator + "tmpQuestsReward" + File.separator + "quest" + (questIndex + 1) + ".png", questImage.getWidth() - questImage.getWidth() / 100 * 12, questImage.getHeight() / 10, questImage.getWidth() - questImage.getWidth() / 100 * 7, questImage.getHeight() - questImage.getHeight() / 10);

                                // Determine Treshold
                                // Recognise: BR Progress

                                imageToBlackWhite(questBRProgressPart, tempFolderPath + File.separator + "tmpQuestsBRProgress" + File.separator + "quest" + (questIndex + 1) + "BW.png", currentTreshold);
                                String questBRProgress = recogniseText(tempFolderPath + File.separator + "tmpQuestsBRProgress" + File.separator + "quest" + (questIndex + 1) + "BW.png", false);
                                if(questBRProgress.trim().isEmpty()) {
                                    currentTreshold = questsTresholdLow;
                                    imageToBlackWhite(questBRProgressPart, tempFolderPath + File.separator + "tmpQuestsBRProgress" + File.separator + "quest" + (questIndex + 1) + "BW.png", currentTreshold);
                                    questBRProgress = recogniseText(tempFolderPath + File.separator + "tmpQuestsBRProgress" + File.separator + "quest" + (questIndex + 1) + "BW.png", false);
                                }

                                // Recognise: NBR Progress

                                imageToBlackWhite(questNBRProgressPart, tempFolderPath + File.separator + "tmpQuestsNBRProgress" + File.separator + "quest" + (questIndex + 1) + "BW.png", currentTreshold);
                                String questNBRProgress = recogniseText(tempFolderPath + File.separator + "tmpQuestsNBRProgress" + File.separator + "quest" + (questIndex + 1) + "BW.png", false);

                                // Recognise: BR Name

                                imageToBlackWhite(questBRPart, tempFolderPath + File.separator + "tmpQuestsBR" + File.separator + "quest" + (questIndex + 1) + ".png", currentTreshold);
                                String questNameBR = recogniseText(tempFolderPath + File.separator + "tmpQuestsBR" + File.separator + "quest" + (questIndex + 1) + ".png", false);

                                // Recognise: NBR Name

                                imageToBlackWhite(questNBRPart, tempFolderPath + File.separator + "tmpQuestsNBR" + File.separator + "quest" + (questIndex + 1) + ".png", currentTreshold);
                                String questNameNBR = recogniseText(tempFolderPath + File.separator + "tmpQuestsNBR" + File.separator + "quest" + (questIndex + 1) + ".png", false);

                                // Recognise: Reward

                                imageToBlackWhite(questRewardPart, tempFolderPath + File.separator + "tmpQuestsReward" + File.separator + "quest" + (questIndex + 1) + ".png", currentTreshold);
                                String questReward = recogniseText(tempFolderPath + File.separator + "tmpQuestsReward" + File.separator + "quest" + (questIndex + 1) + ".png", false);

                                // Output

                                System.out.println("QNameBR: " + questNameBR.trim());
                                System.out.println("QNameNBR: " + questNameNBR.trim());
                                System.out.println("QBRProgress: " + questBRProgress.trim());
                                System.out.println("QNBRProgress: " + questNBRProgress.trim());
                                System.out.println("QReward: " + questReward.trim());
                            }
                            case 2 -> {
                                int[] firstBRPixelCoords = searchFirstColoredPixel(questImage, -12544866, 0, 0);
                                int[] lastBRPixelCoords = searchLastColoredPixel(questImage, -12544866);

                                lastBRPixelCoords[0] += 2;
                                lastBRPixelCoords[1] += 2;

                                // Cut: Name BR

                                BufferedImage questNamePart = cropImageByPixels(questImage, tempFolderPath + File.separator + "tmpQuestsBR" + File.separator + "quest" + (questIndex + 1) + ".png", lastBRPixelCoords[0], firstBRPixelCoords[1], questImage.getWidth() - questImage.getWidth() / 100 * 12, lastBRPixelCoords[1]);

                                // Cut: Status BR

                                BufferedImage questBRProgressPart = cropImageByPixels(questImage, tempFolderPath + File.separator + "tmpQuestsBRProgress" + File.separator + "quest" + (questIndex + 1) + ".png", 0, (questImage.getHeight() - questImage.getHeight() / 10 * 5) / 5 * 4, questImage.getWidth(), questImage.getHeight() / 10 * 8);
                                questBRProgressPart = cropImageByPixels(questBRProgressPart, tempFolderPath + File.separator + "tmpQuestsBRProgress" + File.separator + "quest" + (questIndex + 1) + ".png", questBRProgressPart.getWidth() / 100 * 2, 0, questBRProgressPart.getWidth() - questBRProgressPart.getWidth() / 5, questBRProgressPart.getHeight());

                                // Cut: Reward

                                BufferedImage questRewardPart = cropImageByPixels(questImage, tempFolderPath + File.separator + "tmpQuestsReward" + File.separator + "quest" + (questIndex + 1) + ".png", questImage.getWidth() - questImage.getWidth() / 100 * 12, questImage.getHeight() / 10, questImage.getWidth() - questImage.getWidth() / 100 * 7, questImage.getHeight() - questImage.getHeight() / 10);

                                // Determine Treshold
                                // Recognise: BR Progress

                                imageToBlackWhite(questBRProgressPart, tempFolderPath + File.separator + "tmpQuestsBRProgress" + File.separator + "quest" + (questIndex + 1) + "BW.png", currentTreshold);
                                String questBRProgress = recogniseText(tempFolderPath + File.separator + "tmpQuestsBRProgress" + File.separator + "quest" + (questIndex + 1) + "BW.png", false);
                                if(questBRProgress.trim().isEmpty()) {
                                    currentTreshold = questsTresholdLow;
                                    imageToBlackWhite(questBRProgressPart, tempFolderPath + File.separator + "tmpQuestsBRProgress" + File.separator + "quest" + (questIndex + 1) + "BW.png", currentTreshold);
                                    questBRProgress = recogniseText(tempFolderPath + File.separator + "tmpQuestsBRProgress" + File.separator + "quest" + (questIndex + 1) + "BW.png", false);
                                }

                                // Recognise: BR Name

                                imageToBlackWhite(questNamePart, tempFolderPath + File.separator + "tmpQuestsBR" + File.separator + "quest" + (questIndex + 1) + ".png", questsTresholdHigh);
                                String questNameBR = recogniseText(tempFolderPath + File.separator + "tmpQuestsBR" + File.separator + "quest" + (questIndex + 1) + ".png", false);

                                // Recognise: Reward

                                imageToBlackWhite(questRewardPart, tempFolderPath + File.separator + "tmpQuestsReward" + File.separator + "quest" + (questIndex + 1) + ".png", questsTresholdHigh);
                                String questReward = recogniseText(tempFolderPath + File.separator + "tmpQuestsReward" + File.separator + "quest" + (questIndex + 1) + ".png", false);


                                System.out.println("QNameBR: " + questNameBR.trim());
                                System.out.println("QBRProgress: " + questBRProgress.trim());
                                System.out.println("QReward: " + questReward.trim());
                            }
                            case 3 -> {

                                // Cut: Name

                                BufferedImage questNamePart = cropImageByPixels(questImage, tempFolderPath + File.separator + "tmpQuestsReg" + File.separator + "quest" + (questIndex + 1) + ".png", 0, 0, questImage.getWidth() - questImage.getWidth() / 100 * 12, questImage.getHeight() - questImage.getHeight() / 2);

                                // Cut: Status
//
                                BufferedImage questBRProgressPart = cropImageByPixels(questImage, tempFolderPath + File.separator + "tmpQuestsBRProgress" + File.separator + "quest" + (questIndex + 1) + ".png", 0, (questImage.getHeight() - questImage.getHeight() / 10 * 5) / 5 * 4, questImage.getWidth(), questImage.getHeight() / 10 * 8);
                                questBRProgressPart = cropImageByPixels(questBRProgressPart, tempFolderPath + File.separator + "tmpQuestsBRProgress" + File.separator + "quest" + (questIndex + 1) + ".png", questBRProgressPart.getWidth() / 100 * 2, 0, questBRProgressPart.getWidth() - questBRProgressPart.getWidth() / 5, questBRProgressPart.getHeight());

                                // Cut: Reward

                                BufferedImage questRewardPart = cropImageByPixels(questImage, tempFolderPath + File.separator + "tmpQuestsReward" + File.separator + "quest" + (questIndex + 1) + ".png", questImage.getWidth() - questImage.getWidth() / 100 * 12, questImage.getHeight() / 10, questImage.getWidth() - questImage.getWidth() / 100 * 7, questImage.getHeight() - questImage.getHeight() / 10);

                                // Determine Treshold
                                // Recognise: Progress

                                imageToBlackWhite(questBRProgressPart, tempFolderPath + File.separator + "tmpQuestsBRProgress" + File.separator + "quest" + (questIndex + 1) + "BW.png", currentTreshold);
                                String questProgress = recogniseText(tempFolderPath + File.separator + "tmpQuestsBRProgress" + File.separator + "quest" + (questIndex + 1) + "BW.png", false);
                                if(questProgress.trim().isEmpty()) {
                                    currentTreshold = questsTresholdLow;
                                    imageToBlackWhite(questBRProgressPart, tempFolderPath + File.separator + "tmpQuestsBRProgress" + File.separator + "quest" + (questIndex + 1) + "BW.png", currentTreshold);
                                    questProgress = recogniseText(tempFolderPath + File.separator + "tmpQuestsBRProgress" + File.separator + "quest" + (questIndex + 1) + "BW.png", false);
                                }

                                // Recognise: Name

                                imageToBlackWhite(questNamePart, tempFolderPath + File.separator + "tmpQuestsReg" + File.separator + "quest" + (questIndex + 1) + ".png", currentTreshold);
                                String questNameReg = recogniseText(tempFolderPath + File.separator + "tmpQuestsReg" + File.separator + "quest" + (questIndex + 1) + ".png", false);

                                // Recognise: Reward

                                imageToBlackWhite(questRewardPart, tempFolderPath + File.separator + "tmpQuestsReward" + File.separator + "quest" + (questIndex + 1) + ".png", currentTreshold);
                                String questReward = recogniseText(tempFolderPath + File.separator + "tmpQuestsReward" + File.separator + "quest" + (questIndex + 1) + ".png", false);


                                System.out.println("QNameReg: " + questNameReg.trim());
                                System.out.println("QProgress: " + questProgress.trim());
                                System.out.println("QReward: " + questReward.trim());
                            }
                            default -> System.out.println("Recognition aborted, invalid quest type");
                        }
                    }
                }
            }
        }


    }

}