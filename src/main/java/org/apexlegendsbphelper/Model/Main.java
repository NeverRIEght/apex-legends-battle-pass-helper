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
    public static String inputFolderPath = "D:\\apex-tests\\image_2023-11-21_14-27-02.png";
    public static String tempFolderPath;
    public static String blackWhiteImagePath;
    public static String grayscaleImagePath;
    public static int imageTreshold = 175;
    public static int questsTresholdHigh = 150;
    public static int questsTresholdLow = 120;

    public static void main(String[] args) throws TesseractException, IOException {
        Quest[] quests1 = processImage(inputFolderPath);
        Quest[] quests2 = processImage(inputFolderPath);
        Quest[] quests3 = new Quest[16];

        for(int i = 0; i < 8; i++) {
            quests3[i] = quests1[i];
            quests3[i + 8] = quests2[i];
        }

        System.out.println(quests3.length);

        Quest[] uniqueQuests = removeQuestsDuplicates(quests3);
        for (int i = 0; i < uniqueQuests.length; i++) {
            System.out.println(uniqueQuests[i].toString());
        }

    }
}