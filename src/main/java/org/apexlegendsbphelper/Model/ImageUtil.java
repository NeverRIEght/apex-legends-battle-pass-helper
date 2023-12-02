package org.apexlegendsbphelper.Model;

import net.sourceforge.tess4j.ITessAPI;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.File;
import java.util.Objects;
import javax.imageio.*;

import static org.apexlegendsbphelper.Model.FileUtil.loadNewFile;
import static org.apexlegendsbphelper.Model.FileUtil.recreateTempDirectories;
import static org.apexlegendsbphelper.Model.Main.*;

public abstract class ImageUtil {
    private static final String tesseractDatapath;
    private static final String tesseractSetLanguage = "eng+eng_old";
    private static final String tesseractSetDigits = "digits";
    static int tesseractSetPageSegMode = 1;
    static int tesseractSetOcrEngineMode = 1;

    static {
        StringBuilder datapathBuilder = new StringBuilder();
        datapathBuilder.append(System.getProperty("user.dir"));
        datapathBuilder.append(File.separator);
        datapathBuilder.append("lib");
        datapathBuilder.append(File.separator);
        datapathBuilder.append("tesseract");
        datapathBuilder.append(File.separator);
        datapathBuilder.append("share");
        datapathBuilder.append(File.separator);
        datapathBuilder.append("tessdata");
        tesseractDatapath = datapathBuilder.toString();
    }

    public static BufferedImage imageToGrayscale(BufferedImage image, String pathToGrayscale) throws IOException {
        BufferedImage result;

        int width = image.getWidth();
        int height = image.getHeight();

        BufferedImage grayscaleImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
        Graphics g = grayscaleImage.getGraphics();
        g.drawImage(image, 0, 0, null);


        ImageIO.write(grayscaleImage, "png", new File(pathToGrayscale));
        result = grayscaleImage;

        return result;
    }

    public static BufferedImage imageToBlackWhite(BufferedImage image, String outputPath, int threshold) throws IOException {
        BufferedImage outputImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        int width = image.getWidth();
        int height = image.getHeight();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color color = new Color(image.getRGB(x, y));
                int grayscale = (int) (0.299 * color.getRed() + 0.587 * color.getGreen() + 0.114 * color.getBlue());

                if (grayscale > threshold) {
                    outputImage.setRGB(x, y, Color.WHITE.getRGB());
                } else {
                    outputImage.setRGB(x, y, Color.BLACK.getRGB());
                }
            }
        }
        ImageIO.write(outputImage, "png", new File(outputPath));
        return outputImage;
    }

    public static BufferedImage cropImageByPixels(BufferedImage image, String outputImagePath, int startX, int startY, int endX, int endY) throws IOException {
        int width = endX - startX;
        int height = endY - startY;

        BufferedImage croppedImage = new BufferedImage(width, height, image.getType());

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int sourceX = x + startX;
                int sourceY = y + startY;
                int rgb = image.getRGB(sourceX, sourceY);
                croppedImage.setRGB(x, y, rgb);
            }
        }
        ImageIO.write(croppedImage, "png", new File(outputImagePath));
        return croppedImage;
    }

    public static String recogniseText(String pathToImage, boolean isDigitsOnly) throws TesseractException {
        Tesseract tesseract = new Tesseract();
        tesseract.setDatapath(tesseractDatapath);

        if (isDigitsOnly) {
            tesseract.setLanguage(tesseractSetDigits);
        } else {
            tesseract.setLanguage(tesseractSetLanguage);
        }

        tesseract.setPageSegMode(tesseractSetPageSegMode);
        tesseract.setOcrEngineMode(tesseractSetOcrEngineMode);
        tesseract.setPageSegMode(ITessAPI.TessPageSegMode.PSM_AUTO);

        return tesseract.doOCR(loadNewFile(pathToImage));
    }

    public static int[] searchFirstColoredPixel(BufferedImage image, int colorCode, int startX, int startY) {
        int[] coords = new int[]{-1, -1};
        for (int x = startX; x < image.getWidth(); x++) {
            for (int y = startY; y < image.getHeight(); y++) {

                int color = image.getRGB(x, y);
                if (color == colorCode) {
                    coords[0] = x;
                    coords[1] = y;
                    return coords;
                }
            }
        }
        return coords;
    }

    public static int[] searchLastColoredPixel(BufferedImage image, int colorCode) {
        int[] coords = new int[]{-1, -1};
        for (int x = image.getWidth() - 1; x > 0; x--) {
            for (int y = image.getHeight() - 1; y > 0; y--) {

                int color = image.getRGB(x, y);
                if (color == colorCode) {
                    coords[0] = x;
                    coords[1] = y;
                    return coords;
                }
            }
        }
        return coords;
    }

    public static int determineQuestHeight(BufferedImage image) {
        int[] coords1 = searchFirstColoredPixel(image, -12544866, 0, 0);
        int[] coords2 = searchFirstColoredPixel(image, -12544866, coords1[0], coords1[1] + 50);

        return coords2[1] - coords1[1];
    }

    public static boolean cropQuestsOnImage(BufferedImage image) throws IOException {

        int questHeight = determineQuestHeight(image);

        int[] firstBRPixel = searchFirstColoredPixel(image, -12544866, 0, 0);

        image = cropImageByPixels(image, blackWhiteImagePath, firstBRPixel[0], 0, image.getWidth(), image.getHeight());
        BufferedImage grayscaleImage = imageToGrayscale(image, grayscaleImagePath);
        BufferedImage blackWhiteImage = imageToBlackWhite(grayscaleImage, blackWhiteImagePath, 175);

        int[] firstQuestCoords = new int[]{-1, -1, -1, -1};

        boolean foundFirstQuestH = false;

        for (int y = firstBRPixel[1] - 2; y > 0; y--) {

            int color = blackWhiteImage.getRGB(firstBRPixel[0], y); // -16777216 = black; -1 = white
            int prevColor = blackWhiteImage.getRGB(firstBRPixel[0], y + 1);

            if (color == -1 && prevColor == -16777216 && !foundFirstQuestH) {
                int leftCount = firstBRPixel[0];
                int leftPositive = 0;
                int rightCount = blackWhiteImage.getWidth() - firstBRPixel[0];
                int rightPositive = 0;

                for (int i = leftCount; i > 0; i--) {
                    int outerColor = blackWhiteImage.getRGB(firstBRPixel[0] - i, y); // -16777216 = black; -1 = white
                    int interColor = blackWhiteImage.getRGB(firstBRPixel[0] - i, y + 1);

                    if (outerColor == -1 && interColor == -16777216) {
                        leftPositive++;
                    }
                }

                for (int i = firstBRPixel[0]; i < blackWhiteImage.getWidth(); i++) {
                    int outerColor = blackWhiteImage.getRGB(i, y); // -16777216 = black; -1 = white
                    int interColor = blackWhiteImage.getRGB(i, y + 1);

                    if (outerColor == -1 && interColor == -16777216) {
                        rightPositive++;
                    }
                }

                double percentage = (double) (leftPositive + rightPositive) / (leftCount + rightCount);
                if (percentage > 0.4) {
                    firstQuestCoords[1] = y + 1;
                    firstQuestCoords[3] = firstQuestCoords[1] + questHeight;
                    foundFirstQuestH = true;
                }
            }
        }

        int startQuestsCounter = firstQuestCoords[3] / questHeight;
        int questsCounter = startQuestsCounter;

        for (int i = firstQuestCoords[3]; i > questHeight; i -= questHeight) {
            cropImageByPixels(image, tempFolderPath + File.separator + "tmpQuests" + File.separator + "quest" + questsCounter + ".png", 0, i - questHeight, image.getWidth(), i);
            questsCounter--;
        }

        questsCounter = startQuestsCounter;

        for (int i = firstQuestCoords[3]; i <= image.getHeight(); i += questHeight) {
            cropImageByPixels(image, tempFolderPath + File.separator + "tmpQuests" + File.separator + "quest" + questsCounter + ".png", 0, i - questHeight, image.getWidth(), i);
            questsCounter++;
        }

        return questsCounter == 9;
    }

    public static int determineQuestType(BufferedImage questImage) {
//        return:
//        1 = BR and NBR
//        2 = BR only
//        3 = regular

        int[] lastBRPixelCoords = searchLastColoredPixel(questImage, -12544866);
        int[] lastNBRPixelCoords = searchLastColoredPixel(questImage, -16758925);

        if (lastBRPixelCoords[0] != -1 && lastNBRPixelCoords[0] != -1) {
            return 1;
        } else if (lastBRPixelCoords[0] != -1) {
            return 2;
        } else {
            return 3;
        }
    }

    public static Quest[] processImage(String pathToImage) throws TesseractException, IOException {

        tempFolderPath = System.getProperty("user.dir") + File.separator + "tmp";
        grayscaleImagePath = tempFolderPath + File.separator + "input_grayscale.png";
        blackWhiteImagePath = tempFolderPath + File.separator + "input_blackwhite.png";


        recreateTempDirectories(tempFolderPath);

        BufferedImage inputImage = ImageIO.read(loadNewFile(pathToImage));
        BufferedImage grayscaleImage = imageToGrayscale(inputImage, grayscaleImagePath);
        imageToBlackWhite(grayscaleImage, blackWhiteImagePath, imageTreshold);

        if (cropQuestsOnImage(inputImage)) {
            System.out.println("Quests cropped successfully");
        } else {
            System.out.println("Quests cropped with an error");
        }

        Quest[] questsOnImage = new Quest[8];

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

                        StringBuilder outputPathBuilder = new StringBuilder();
                        outputPathBuilder.append(tempFolderPath);
                        outputPathBuilder.append(File.separator);
                        outputPathBuilder.append("tmpQuestsBR");
                        outputPathBuilder.append(File.separator);
                        outputPathBuilder.append("quest");
                        outputPathBuilder.append(questIndex + 1);
                        outputPathBuilder.append(".png");

                        BufferedImage questBRPart = cropImageByPixels(questImage, outputPathBuilder.toString(), lastBRPixelCoords[0], 0, firstNBRPixelCoords[0], lastBRPixelCoords[1]);
                        questBRPart = cropImageByPixels(questBRPart, outputPathBuilder.toString(), 0, 0, questBRPart.getWidth() - questBRPart.getWidth() / 5, questBRPart.getHeight());

                        // Cut: Name NBR

                        outputPathBuilder = new StringBuilder();
                        outputPathBuilder.append(tempFolderPath);
                        outputPathBuilder.append(File.separator);
                        outputPathBuilder.append("tmpQuestsNBR");
                        outputPathBuilder.append(File.separator);
                        outputPathBuilder.append("quest");
                        outputPathBuilder.append(questIndex + 1);
                        outputPathBuilder.append(".png");

                        BufferedImage questNBRPart = cropImageByPixels(questImage, outputPathBuilder.toString(), lastNBRPixelCoords[0], 0, questImage.getWidth(), lastNBRPixelCoords[1]);
                        questNBRPart = cropImageByPixels(questNBRPart, outputPathBuilder.toString(), 0, 0, questNBRPart.getWidth() - questNBRPart.getWidth() / 5, questNBRPart.getHeight());

                        // Cut: Status BR

                        outputPathBuilder = new StringBuilder();
                        outputPathBuilder.append(tempFolderPath);
                        outputPathBuilder.append(File.separator);
                        outputPathBuilder.append("tmpQuestsBRProgress");
                        outputPathBuilder.append(File.separator);
                        outputPathBuilder.append("quest");
                        outputPathBuilder.append(questIndex + 1);
                        outputPathBuilder.append(".png");

                        BufferedImage questBRProgressPart = cropImageByPixels(questImage, outputPathBuilder.toString(), 0, (questImage.getHeight() - questImage.getHeight() / 10 * 6), firstNBRPixelCoords[0], questImage.getHeight());
                        questBRProgressPart = cropImageByPixels(questBRProgressPart, outputPathBuilder.toString(), questBRPart.getHeight() / 5 * 4, 0, questBRPart.getWidth() - questBRPart.getWidth() / 5, questBRPart.getHeight());

                        // Cut: Status NBR

                        outputPathBuilder = new StringBuilder();
                        outputPathBuilder.append(tempFolderPath);
                        outputPathBuilder.append(File.separator);
                        outputPathBuilder.append("tmpQuestsNBRProgress");
                        outputPathBuilder.append(File.separator);
                        outputPathBuilder.append("quest");
                        outputPathBuilder.append(questIndex + 1);
                        outputPathBuilder.append(".png");

                        BufferedImage questNBRProgressPart = cropImageByPixels(questImage, outputPathBuilder.toString(), questImage.getWidth() / 100 * 53, (questImage.getHeight() - questImage.getHeight() / 10 * 6), questImage.getWidth(), questImage.getHeight());
                        questNBRProgressPart = cropImageByPixels(questNBRProgressPart, outputPathBuilder.toString(), questNBRProgressPart.getWidth() / 100, 0, questBRPart.getWidth() - questBRPart.getWidth() / 5, questBRPart.getHeight());

                        // Cut: Reward

                        outputPathBuilder = new StringBuilder();
                        outputPathBuilder.append(tempFolderPath);
                        outputPathBuilder.append(File.separator);
                        outputPathBuilder.append("tmpQuestsReward");
                        outputPathBuilder.append(File.separator);
                        outputPathBuilder.append("quest");
                        outputPathBuilder.append(questIndex + 1);
                        outputPathBuilder.append(".png");

                        BufferedImage questRewardPart = cropImageByPixels(questImage, outputPathBuilder.toString(), questImage.getWidth() - questImage.getWidth() / 100 * 12, questImage.getHeight() / 10, questImage.getWidth() - questImage.getWidth() / 100 * 7, questImage.getHeight() - questImage.getHeight() / 10);

                        // Determine Treshold
                        // Recognise: BR Progress

                        outputPathBuilder = new StringBuilder();
                        outputPathBuilder.append(tempFolderPath);
                        outputPathBuilder.append(File.separator);
                        outputPathBuilder.append("tmpQuestsBRProgress");
                        outputPathBuilder.append(File.separator);
                        outputPathBuilder.append("quest");
                        outputPathBuilder.append(questIndex + 1);
                        outputPathBuilder.append("BW.png");

                        imageToBlackWhite(questBRProgressPart, outputPathBuilder.toString(), currentTreshold);
                        String questBRProgress = recogniseText(outputPathBuilder.toString(), false);
                        if (questBRProgress.trim().isEmpty()) {
                            currentTreshold = questsTresholdLow;
                            imageToBlackWhite(questBRProgressPart, outputPathBuilder.toString(), currentTreshold);
                            questBRProgress = recogniseText(outputPathBuilder.toString(), false);
                        }

                        // Recognise: NBR Progress

                        outputPathBuilder = new StringBuilder();
                        outputPathBuilder.append(tempFolderPath);
                        outputPathBuilder.append(File.separator);
                        outputPathBuilder.append("tmpQuestsNBRProgress");
                        outputPathBuilder.append(File.separator);
                        outputPathBuilder.append("quest");
                        outputPathBuilder.append(questIndex + 1);
                        outputPathBuilder.append("BW.png");

                        imageToBlackWhite(questNBRProgressPart, outputPathBuilder.toString(), currentTreshold);
                        String questNBRProgress = recogniseText(outputPathBuilder.toString(), false);

                        // Recognise: BR Name

                        outputPathBuilder = new StringBuilder();
                        outputPathBuilder.append(tempFolderPath);
                        outputPathBuilder.append(File.separator);
                        outputPathBuilder.append("tmpQuestsBR");
                        outputPathBuilder.append(File.separator);
                        outputPathBuilder.append("quest");
                        outputPathBuilder.append(questIndex + 1);
                        outputPathBuilder.append(".png");

                        imageToBlackWhite(questBRPart, outputPathBuilder.toString(), currentTreshold);
                        String questNameBR = recogniseText(outputPathBuilder.toString(), false);

                        // Recognise: NBR Name

                        outputPathBuilder = new StringBuilder();
                        outputPathBuilder.append(tempFolderPath);
                        outputPathBuilder.append(File.separator);
                        outputPathBuilder.append("tmpQuestsNBR");
                        outputPathBuilder.append(File.separator);
                        outputPathBuilder.append("quest");
                        outputPathBuilder.append(questIndex + 1);
                        outputPathBuilder.append(".png");

                        imageToBlackWhite(questNBRPart, outputPathBuilder.toString(), currentTreshold);
                        String questNameNBR = recogniseText(outputPathBuilder.toString(), false);

                        // Recognise: Reward

                        outputPathBuilder = new StringBuilder();
                        outputPathBuilder.append(tempFolderPath);
                        outputPathBuilder.append(File.separator);
                        outputPathBuilder.append("tmpQuestsReward");
                        outputPathBuilder.append(File.separator);
                        outputPathBuilder.append("quest");
                        outputPathBuilder.append(questIndex + 1);
                        outputPathBuilder.append(".png");

                        imageToBlackWhite(questRewardPart, outputPathBuilder.toString(), currentTreshold);
                        String questReward = recogniseText(outputPathBuilder.toString(), false);

                        // Output

//                        System.out.println("QNameBR: " + questNameBR.trim());
//                        System.out.println("QNameNBR: " + questNameNBR.trim());
//                        System.out.println("QBRProgress: " + questBRProgress.trim());
//                        System.out.println("QNBRProgress: " + questNBRProgress.trim());
//                        System.out.println("QReward: " + questReward.trim());

                        Quest quest = new Quest(questNameBR, questNameNBR, questBRProgress, questReward);
                        questsOnImage[questIndex] = quest;
                    }
                    case 2 -> {
                        int[] firstBRPixelCoords = searchFirstColoredPixel(questImage, -12544866, 0, 0);
                        int[] lastBRPixelCoords = searchLastColoredPixel(questImage, -12544866);

                        lastBRPixelCoords[0] += 2;
                        lastBRPixelCoords[1] += 2;

                        // Cut: Name BR

                        StringBuilder outputPathBuilder = new StringBuilder();
                        outputPathBuilder.append(tempFolderPath);
                        outputPathBuilder.append(File.separator);
                        outputPathBuilder.append("tmpQuestsBR");
                        outputPathBuilder.append(File.separator);
                        outputPathBuilder.append("quest");
                        outputPathBuilder.append(questIndex + 1);
                        outputPathBuilder.append(".png");

                        BufferedImage questNamePart = cropImageByPixels(questImage, outputPathBuilder.toString(), lastBRPixelCoords[0], firstBRPixelCoords[1], questImage.getWidth() - questImage.getWidth() / 100 * 12, lastBRPixelCoords[1]);

                        // Cut: Status BR

                        outputPathBuilder = new StringBuilder();
                        outputPathBuilder.append(tempFolderPath);
                        outputPathBuilder.append(File.separator);
                        outputPathBuilder.append("tmpQuestsBRProgress");
                        outputPathBuilder.append(File.separator);
                        outputPathBuilder.append("quest");
                        outputPathBuilder.append(questIndex + 1);
                        outputPathBuilder.append(".png");

                        BufferedImage questBRProgressPart = cropImageByPixels(questImage, outputPathBuilder.toString(), 0, (questImage.getHeight() - questImage.getHeight() / 10 * 5) / 5 * 4, questImage.getWidth(), questImage.getHeight() / 10 * 8);
                        questBRProgressPart = cropImageByPixels(questBRProgressPart, outputPathBuilder.toString(), questBRProgressPart.getWidth() / 100 * 2, 0, questBRProgressPart.getWidth() - questBRProgressPart.getWidth() / 5, questBRProgressPart.getHeight());

                        // Cut: Reward

                        outputPathBuilder = new StringBuilder();
                        outputPathBuilder.append(tempFolderPath);
                        outputPathBuilder.append(File.separator);
                        outputPathBuilder.append("tmpQuestsReward");
                        outputPathBuilder.append(File.separator);
                        outputPathBuilder.append("quest");
                        outputPathBuilder.append(questIndex + 1);
                        outputPathBuilder.append(".png");

                        BufferedImage questRewardPart = cropImageByPixels(questImage, outputPathBuilder.toString(), questImage.getWidth() - questImage.getWidth() / 100 * 12, questImage.getHeight() / 10, questImage.getWidth() - questImage.getWidth() / 100 * 7, questImage.getHeight() - questImage.getHeight() / 10);

                        // Determine Treshold
                        // Recognise: BR Progress

                        outputPathBuilder = new StringBuilder();
                        outputPathBuilder.append(tempFolderPath);
                        outputPathBuilder.append(File.separator);
                        outputPathBuilder.append("tmpQuestsBRProgress");
                        outputPathBuilder.append(File.separator);
                        outputPathBuilder.append("quest");
                        outputPathBuilder.append(questIndex + 1);
                        outputPathBuilder.append("BW.png");

                        imageToBlackWhite(questBRProgressPart, outputPathBuilder.toString(), currentTreshold);
                        String questBRProgress = recogniseText(outputPathBuilder.toString(), false);
                        if (questBRProgress.trim().isEmpty()) {
                            currentTreshold = questsTresholdLow;
                            imageToBlackWhite(questBRProgressPart, outputPathBuilder.toString(), currentTreshold);
                            questBRProgress = recogniseText(outputPathBuilder.toString(), false);
                        }

                        // Recognise: BR Name

                        outputPathBuilder = new StringBuilder();
                        outputPathBuilder.append(tempFolderPath);
                        outputPathBuilder.append(File.separator);
                        outputPathBuilder.append("tmpQuestsBR");
                        outputPathBuilder.append(File.separator);
                        outputPathBuilder.append("quest");
                        outputPathBuilder.append(questIndex + 1);
                        outputPathBuilder.append(".png");

                        imageToBlackWhite(questNamePart, outputPathBuilder.toString(), questsTresholdHigh);
                        String questNameBR = recogniseText(outputPathBuilder.toString(), false);

                        // Recognise: Reward

                        outputPathBuilder = new StringBuilder();
                        outputPathBuilder.append(tempFolderPath);
                        outputPathBuilder.append(File.separator);
                        outputPathBuilder.append("tmpQuestsReward");
                        outputPathBuilder.append(File.separator);
                        outputPathBuilder.append("quest");
                        outputPathBuilder.append(questIndex + 1);
                        outputPathBuilder.append(".png");

                        imageToBlackWhite(questRewardPart, outputPathBuilder.toString(), questsTresholdHigh);
                        String questReward = recogniseText(outputPathBuilder.toString(), false);


//                        System.out.println("QNameBR: " + questNameBR.trim());
//                        System.out.println("QBRProgress: " + questBRProgress.trim());
//                        System.out.println("QReward: " + questReward.trim());

                        Quest quest = new Quest(questNameBR, "", questBRProgress, questReward);
                        questsOnImage[questIndex] = quest;
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
                        if (questProgress.trim().isEmpty()) {
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


//                        System.out.println("QNameReg: " + questNameReg.trim());
//                        System.out.println("QProgress: " + questProgress.trim());
//                        System.out.println("QReward: " + questReward.trim());

                        Quest quest = new Quest(questNameReg, "", questProgress, questReward);
                        questsOnImage[questIndex] = quest;
                    }
                    default -> System.out.println("Recognition aborted, invalid quest type");
                }
            }
        }
        return questsOnImage;
    }
}