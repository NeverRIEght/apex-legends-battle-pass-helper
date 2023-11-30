package org.apexlegendsbphelper.Model;

import static org.apexlegendsbphelper.Model.MainUI.processWeekImages;
import net.sourceforge.tess4j.TesseractException;
import java.io.IOException;


public class Main {
    public static String inputFolderPath = "D:\\apex-tests\\image_2023-11-21_14-27-02.png";
    public static String tempFolderPath;
    public static String blackWhiteImagePath;
    public static String grayscaleImagePath;
    public static int imageTreshold = 175;
    public static int questsTresholdHigh = 150;
    public static int questsTresholdLow = 120;

    public static void main(String[] args) throws TesseractException, IOException {
//        Quest[] quests1 = processWeekImages("D:\\apex-tests\\testdata1.png", "D:\\apex-tests\\testdata2.png");
        Quest[] quests1 = processWeekImages("/Users/michaelkomarov/Downloads/image_2023-11-22_10-50-14.png", "/Users/michaelkomarov/Downloads/image_2023-11-22_10-50-14.png");

        for (Quest quest : quests1) {
            System.out.println(quest.getQuestNameBR());
        }
    }
}