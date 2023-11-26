package org.apexlegendsbphelper.Model;

import static org.apexlegendsbphelper.Model.DictionaryUtil.sortDictionary;
import static org.apexlegendsbphelper.Model.FileUtil.*;
import static org.apexlegendsbphelper.Model.ImageUtil.*;
import static org.apexlegendsbphelper.Model.MainUI.processWeekImages;
import static org.apexlegendsbphelper.Model.StringUtil.*;

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
        Quest[] quests1 = processWeekImages("D:\\apex-tests\\testdata1.png", "D:\\apex-tests\\testdata2.png");

        for(int i = 0; i < quests1.length; i++) {
            System.out.println(quests1[i].getQuestNameBR());
        }
    }
}