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


                int questHeight = determineQuestHeight(inputImage);
                System.out.println(questHeight);
                cropQuestsOnImage(inputImage);
            }
        }


    }

}