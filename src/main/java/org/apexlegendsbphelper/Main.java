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
    public static String tempFolderPath;
    public static String tempImagePath;
    public static String blackWhiteImagePath;
    public static String grayscaleImagePath;

    public static void main(String[] args) throws TesseractException, IOException {


        // Create and write path`s to directories

        tempFolderPath = new File(inputFolderPath).toString() + File.separator + "tmp";
        tempImagePath = tempFolderPath + File.separator + "input.png";
        grayscaleImagePath = tempFolderPath + File.separator + "input_grayscale.png";
        blackWhiteImagePath = tempFolderPath + File.separator + "input_blackwhite.png";


        recreateDirectories(tempFolderPath);


        File mainDirectory = new File(inputFolderPath);
        File[] weekImages = mainDirectory.listFiles();
        for (int imgIndex = 0; imgIndex < weekImages.length; imgIndex++) {
            if (weekImages[imgIndex].isFile() && weekImages[imgIndex].getName().endsWith(".png")) {


                // Create a recognition-essential version of input image

                BufferedImage inputImage = ImageIO.read(weekImages[imgIndex]);
                BufferedImage grayscaleImage = imageToGrayscale(inputImage, grayscaleImagePath);
                imageToBlackWhite(grayscaleImage, blackWhiteImagePath, 175);
                BufferedImage tempImage = loadImage(blackWhiteImagePath);

                if (cropQuestsOnImage(inputImage)) {
                    System.out.println("Quests cropped successfully");
                } else {
                    System.out.println("Quests cropped with an error");
                }

                File questsDirectory = new File(tempFolderPath + File.separator + "tmpQuests");
                File[] questImages = questsDirectory.listFiles();
                for (int questIndex = 0; questIndex < questImages.length; questIndex++) {
                    if (questImages[questIndex].isFile() && questImages[questIndex].getName().endsWith(".png")) {

                        BufferedImage questImage = ImageIO.read(questImages[questIndex]);

                        System.out.println(questImages[questIndex].getName());
                        int questType = determineQuestType(questImage);
                        BufferedImage questBlackWhite = imageToBlackWhite(questImage, tempFolderPath + File.separator + "tmpQuestsBlackWhite" + File.separator + "quest" + (questIndex + 1) + ".png", 150);

                        switch (questType) {
                            case 1 -> {
                                int[] firstBRPixelCoords = new int[2];
                                int[] firstNBRPixelCoords = new int[2];
                                firstBRPixelCoords = searchFirstColoredPixel(questImage, -12544866, 0, 0);
                                firstNBRPixelCoords = searchFirstColoredPixel(questImage, -16758925, 0, 0);

                                int[] lastBRPixelCoords = new int[2];
                                int[] lastNBRPixelCoords = new int[2];
                                lastBRPixelCoords = searchLastColoredPixel(questImage, -12544866);
                                lastNBRPixelCoords = searchLastColoredPixel(questImage, -16758925);

                                lastBRPixelCoords[0] += 2;
                                lastBRPixelCoords[1] += 2;
                                lastNBRPixelCoords[0] += 2;
                                lastNBRPixelCoords[1] += 2;
                                firstNBRPixelCoords[0] -= 2;

                                BufferedImage questBRPart = cropImageByPixels(questImage, tempFolderPath + File.separator + "tmpQuestsBR" + File.separator + "quest" + (questIndex + 1) + ".png", lastBRPixelCoords[0], 0, firstNBRPixelCoords[0], lastBRPixelCoords[1]);
                                questBRPart = cropImageByPixels(questBRPart, tempFolderPath + File.separator + "tmpQuestsBR" + File.separator + "quest" + (questIndex + 1) + ".png", 0, 0, questBRPart.getWidth() - questBRPart.getWidth() / 5, questBRPart.getHeight());
                                imageToBlackWhite(questBRPart, tempFolderPath + File.separator + "tmpQuestsBR" + File.separator + "quest" + (questIndex + 1) + ".png", 150);
                                String questNameBR = recogniseText(tempFolderPath + File.separator + "tmpQuestsBR" + File.separator + "quest" + (questIndex + 1) + ".png", false);
                                System.out.println("QName: " + questNameBR);
                            }
                            case 2 -> {

                            }
                            case 3 -> {

                            }
                            default -> {

                            }
                        }
                    }
                }

//                BufferedImage im1 = loadImage("D:\\apex-tests\\tmp\\tmpQuests\\quest2.png");
//                System.out.println(recognisePixelColor(im1, 622, 9));
            }
        }


    }

}