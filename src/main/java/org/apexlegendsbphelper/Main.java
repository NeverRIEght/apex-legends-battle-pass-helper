package org.apexlegendsbphelper;

import static org.apexlegendsbphelper.FileUtil.*;
import static org.apexlegendsbphelper.ImageUtil.*;

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
    public static int questsTreshold = 150;

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

                                BufferedImage questBRPart = cropImageByPixels(questImage, tempFolderPath + File.separator + "tmpQuestsBR" + File.separator + "quest" + (questIndex + 1) + ".png", lastBRPixelCoords[0], 0, firstNBRPixelCoords[0], lastBRPixelCoords[1]);
                                questBRPart = cropImageByPixels(questBRPart, tempFolderPath + File.separator + "tmpQuestsBR" + File.separator + "quest" + (questIndex + 1) + ".png", 0, 0, questBRPart.getWidth() - questBRPart.getWidth() / 5, questBRPart.getHeight());
                                imageToBlackWhite(questBRPart, tempFolderPath + File.separator + "tmpQuestsBR" + File.separator + "quest" + (questIndex + 1) + ".png", questsTreshold);
                                String questNameBR = recogniseText(tempFolderPath + File.separator + "tmpQuestsBR" + File.separator + "quest" + (questIndex + 1) + ".png", false);
                                System.out.println("QNameBR: " + questNameBR);

                                BufferedImage questNBRPart = cropImageByPixels(questImage, tempFolderPath + File.separator + "tmpQuestsNBR" + File.separator + "quest" + (questIndex + 1) + ".png", lastNBRPixelCoords[0], 0, questImage.getWidth(), lastNBRPixelCoords[1]);
                                questNBRPart = cropImageByPixels(questNBRPart, tempFolderPath + File.separator + "tmpQuestsNBR" + File.separator + "quest" + (questIndex + 1) + ".png", 0, 0, questNBRPart.getWidth() - questNBRPart.getWidth() / 5, questNBRPart.getHeight());
                                imageToBlackWhite(questNBRPart, tempFolderPath + File.separator + "tmpQuestsNBR" + File.separator + "quest" + (questIndex + 1) + ".png", questsTreshold);
                                String questNameNBR = recogniseText(tempFolderPath + File.separator + "tmpQuestsNBR" + File.separator + "quest" + (questIndex + 1) + ".png", false);
                                System.out.println("QNameNBR: " + questNameNBR);
                            }
                            case 2 -> {
                                int[] firstBRPixelCoords = searchFirstColoredPixel(questImage, -12544866, 0, 0);
                                int[] lastBRPixelCoords = searchLastColoredPixel(questImage, -12544866);

                                lastBRPixelCoords[0] += 2;
                                lastBRPixelCoords[1] += 2;

                                BufferedImage questNamePart = cropImageByPixels(questImage, tempFolderPath + File.separator + "tmpQuestsBR" + File.separator + "quest" + (questIndex + 1) + ".png", lastBRPixelCoords[0], firstBRPixelCoords[1], questImage.getWidth() - questImage.getWidth() / 100 * 12, lastBRPixelCoords[1]);
                                imageToBlackWhite(questNamePart, tempFolderPath + File.separator + "tmpQuestsBR" + File.separator + "quest" + (questIndex + 1) + ".png", questsTreshold);
                                String questNameBR = recogniseText(tempFolderPath + File.separator + "tmpQuestsBR" + File.separator + "quest" + (questIndex + 1) + ".png", false);
                                System.out.println("QNameBR: " + questNameBR);
                            }
                            case 3 -> {
                                BufferedImage questNamePart = cropImageByPixels(questImage, tempFolderPath + File.separator + "tmpQuestsReg" + File.separator + "quest" + (questIndex + 1) + ".png", 0, 0, questImage.getWidth() - questImage.getWidth() / 100 * 12, questImage.getHeight() - questImage.getHeight() / 2);
                                imageToBlackWhite(questNamePart, tempFolderPath + File.separator + "tmpQuestsReg" + File.separator + "quest" + (questIndex + 1) + ".png", questsTreshold);
                                String questNameReg = recogniseText(tempFolderPath + File.separator + "tmpQuestsReg" + File.separator + "quest" + (questIndex + 1) + ".png", false);
                                System.out.println("QNameReg: " + questNameReg);
                            }
                            default -> System.out.println("Error Occurred");
                        }

                        BufferedImage questRewardPart = cropImageByPixels(questImage, tempFolderPath + File.separator + "tmpQuestsReward" + File.separator + "quest" + (questIndex + 1) + ".png", questImage.getWidth() - questImage.getWidth() / 100 * 12, questImage.getHeight() / 10, questImage.getWidth() - questImage.getWidth() / 100 * 7, questImage.getHeight() - questImage.getHeight() / 10);
                        imageToBlackWhite(questRewardPart, tempFolderPath + File.separator + "tmpQuestsReward" + File.separator + "quest" + (questIndex + 1) + ".png", questsTreshold);
                        String questReward = recogniseText(tempFolderPath + File.separator + "tmpQuestsReward" + File.separator + "quest" + (questIndex + 1) + ".png", false);
                        System.out.println("QReward: " + questReward);
                    }
                }
            }
        }


    }

}