package org.apexlegendsbphelper.Model;

import net.sourceforge.tess4j.ITessAPI;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.File;
import javax.imageio.*;

import static java.util.Objects.requireNonNull;
import static org.apexlegendsbphelper.App.*;
import static org.apexlegendsbphelper.Model.FileUtil.copyToWeekImages;
import static org.apexlegendsbphelper.Model.FileUtil.loadNewFile;

public abstract class ImageUtil {
    private static final String tesseractDatapath = System.getProperty("user.dir")
            + File.separator
            + "lib"
            + File.separator
            + "tesseract"
            + File.separator
            + "share"
            + File.separator
            + "tessdata";
    private static final String tesseractSetLanguage = "eng+eng_old";
    private static final String tesseractSetDigits = "digits";
    static int tesseractSetPageSegMode = 1;
    static int tesseractSetOcrEngineMode = 1;

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

    public static BufferedImage cropImageByPixels(BufferedImage inputImage, String outputImagePath, int startX, int startY, int endX, int endY) throws IOException {
        int width = endX - startX;
        int height = endY - startY;

        BufferedImage croppedImage = new BufferedImage(width, height, inputImage.getType());

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int sourceX = x + startX;
                int sourceY = y + startY;
                int rgb = inputImage.getRGB(sourceX, sourceY);
                croppedImage.setRGB(x, y, rgb);
            }
        }
        ImageIO.write(croppedImage, "png", new File(outputImagePath));
        return croppedImage;
    }

    public static String recogniseText(String pathToImage, boolean isDigitsOnly) throws TesseractException {
        if(loadNewFile(pathToImage) == null) {
            System.out.println("recogniseText: File not found");
            return null;
        }

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


        String result = tesseract.doOCR(loadNewFile(pathToImage));

        if(result == null) {
            System.out.println("recogniseText: result is null");
            return "";
        } else {
            return result.trim();
        }
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

    public static BufferedImage[] cropQuestsOnImage(BufferedImage inputImage) throws IOException {

        int questHeight = determineQuestHeight(inputImage);

        int[] firstBRPixel = searchFirstColoredPixel(inputImage, -12544866, 0, 0);

        inputImage = cropImageByPixels(inputImage, blackWhiteImagePath, firstBRPixel[0], 0, inputImage.getWidth(), inputImage.getHeight());
        BufferedImage grayscaleImage = imageToGrayscale(inputImage, grayscaleImagePath);
        BufferedImage blackWhiteImage = imageToBlackWhite(grayscaleImage, blackWhiteImagePath, 175);

        int[] firstQuestCoords = {-1, -1, -1, -1};

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

        BufferedImage[] questImages = new BufferedImage[8];

        for (int i = firstQuestCoords[3]; i > questHeight; i -= questHeight) {
            StringBuilder outputPathBuilder = new StringBuilder();
            outputPathBuilder.append(tempFolderPath);
            outputPathBuilder.append(File.separator);
            outputPathBuilder.append("tmpQuests");
            outputPathBuilder.append(File.separator);
            outputPathBuilder.append("quest");
            outputPathBuilder.append(questsCounter);
            outputPathBuilder.append(".png");

            questImages[questsCounter - 1] = cropImageByPixels(inputImage, outputPathBuilder.toString(), 0, i - questHeight, inputImage.getWidth(), i);
            questsCounter--;
        }

        questsCounter = startQuestsCounter;

        for (int i = firstQuestCoords[3]; i <= inputImage.getHeight(); i += questHeight) {
            StringBuilder outputPathBuilder = new StringBuilder();
            outputPathBuilder.append(tempFolderPath);
            outputPathBuilder.append(File.separator);
            outputPathBuilder.append("tmpQuests");
            outputPathBuilder.append(File.separator);
            outputPathBuilder.append("quest");
            outputPathBuilder.append(questsCounter);
            outputPathBuilder.append(".png");

            questImages[questsCounter - 1] = cropImageByPixels(inputImage, outputPathBuilder.toString(), 0, i - questHeight, inputImage.getWidth(), i);
            questsCounter++;
        }

        return questImages;
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

        BufferedImage inputImage = ImageIO.read(requireNonNull(loadNewFile(pathToImage)));
        BufferedImage grayscaleImage = imageToGrayscale(inputImage, grayscaleImagePath);
        imageToBlackWhite(grayscaleImage, blackWhiteImagePath, imageTreshold);

        BufferedImage[] questsImages = cropQuestsOnImage(inputImage);

        if (questsImages.length != 8) {
            System.out.println("Quests cropped with an error");
            return null;
        }

        File questsDirectory = new File(tempFolderPath + File.separator + "tmpQuests");
        File[] questsImagesFiles = requireNonNull(questsDirectory.listFiles());

        if(questsImagesFiles.length != questsImages.length) {
            System.out.println("Quests images loaded with an error");
            return null;
        }

        Quest[] questsOnImage = new Quest[8];

        for (int questIndex = 0; questIndex < questsImages.length; questIndex++) {
            BufferedImage questImage = questsImages[questIndex];
            System.out.println(questsImagesFiles[questIndex].getName());

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
                    if (questBRProgress.isEmpty()) {
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
                    copyToWeekImages(questsImagesFiles[questIndex].getAbsolutePath(), 1, questIndex + 1);
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
                    if (questBRProgress.isEmpty()) {
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
                    copyToWeekImages(questsImagesFiles[questIndex].getAbsolutePath(), 1, questIndex + 1);
                }
                case 3 -> {

                    // Cut: Name

                    StringBuilder outputPathBuilder = new StringBuilder();
                    outputPathBuilder.append(tempFolderPath);
                    outputPathBuilder.append(File.separator);
                    outputPathBuilder.append("tmpQuestsReg");
                    outputPathBuilder.append(File.separator);
                    outputPathBuilder.append("quest");
                    outputPathBuilder.append(questIndex + 1);
                    outputPathBuilder.append(".png");

                    BufferedImage questNamePart = cropImageByPixels(questImage, outputPathBuilder.toString(), 0, 0, questImage.getWidth() - questImage.getWidth() / 100 * 12, questImage.getHeight() - questImage.getHeight() / 2);

                    // Cut: Status

                    outputPathBuilder = new StringBuilder();
                    outputPathBuilder.append(tempFolderPath);
                    outputPathBuilder.append(File.separator);
                    outputPathBuilder.append("tmpQuestsBRProgress");
                    outputPathBuilder.append(File.separator);
                    outputPathBuilder.append("quest");
                    outputPathBuilder.append(questIndex + 1);
                    outputPathBuilder.append(".png");
//
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
                    // Recognise: Progress

                    outputPathBuilder = new StringBuilder();
                    outputPathBuilder.append(tempFolderPath);
                    outputPathBuilder.append(File.separator);
                    outputPathBuilder.append("tmpQuestsBRProgress");
                    outputPathBuilder.append(File.separator);
                    outputPathBuilder.append("quest");
                    outputPathBuilder.append(questIndex + 1);
                    outputPathBuilder.append("BW.png");

                    imageToBlackWhite(questBRProgressPart, outputPathBuilder.toString(), currentTreshold);
                    String questProgress = recogniseText(outputPathBuilder.toString(), false);
                    if (questProgress.isEmpty()) {
                        currentTreshold = questsTresholdLow;
                        imageToBlackWhite(questBRProgressPart, outputPathBuilder.toString(), currentTreshold);
                        questProgress = recogniseText(outputPathBuilder.toString(), false);
                    }

                    // Recognise: Name

                    outputPathBuilder = new StringBuilder();
                    outputPathBuilder.append(tempFolderPath);
                    outputPathBuilder.append(File.separator);
                    outputPathBuilder.append("tmpQuestsReg");
                    outputPathBuilder.append(File.separator);
                    outputPathBuilder.append("quest");
                    outputPathBuilder.append(questIndex + 1);
                    outputPathBuilder.append(".png");

                    imageToBlackWhite(questNamePart, outputPathBuilder.toString(), currentTreshold);
                    String questNameReg = recogniseText(outputPathBuilder.toString(), false);

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


//                        System.out.println("QNameReg: " + questNameReg.trim());
//                        System.out.println("QProgress: " + questProgress.trim());
//                        System.out.println("QReward: " + questReward.trim());

                    Quest quest = new Quest(questNameReg, "", questProgress, questReward);
                    questsOnImage[questIndex] = quest;
                    copyToWeekImages(questsImagesFiles[questIndex].getAbsolutePath(), 1, questIndex + 1);
                }
                default -> System.out.println("Recognition aborted, invalid quest type");
            }
        }

        for(Quest quest : questsOnImage) {
            if(quest == null) {
                System.out.println("ProcessImage: some quests are null");
                return new Quest[0];
            }
        }
        return questsOnImage;
    }
}