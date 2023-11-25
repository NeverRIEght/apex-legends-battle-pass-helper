package org.apexlegendsbphelper.Model;

import static org.apexlegendsbphelper.Model.FileUtil.*;
import static org.apexlegendsbphelper.Model.ImageUtil.*;
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
        Quest[] quests1 = processImage("D:\\apex-tests\\testdata1.png");
        Quest[] quests2 = processImage("D:\\apex-tests\\testdata2.png");

        Quest[] quests3 = new Quest[quests1.length + quests2.length];
        System.arraycopy(quests1, 0, quests3, 0, quests1.length);
        System.arraycopy(quests2, 0, quests3, quests1.length, quests2.length);

        int finalQuestsCount = 0;
        for(int i = 0; i < quests3.length; i++) {
            for(int j = i + 1; j < quests3.length; j++) {
                if(stringsEqualsProbability(quests3[i].getQuestNameBR(), quests3[j].getQuestNameBR()) >= 70) {
//                    System.out.println("--------------------");
//                    System.out.println("First quest: " + quests3[i].getQuestNameBR());
//                    System.out.println("Second quest: " + quests3[j].getQuestNameBR());
//                    System.out.println("Probability: " + stringsEqualsProbability(quests3[i].getQuestNameBR(), quests3[j].getQuestNameBR()));
                    quests3[i].setQuestNameBR(null);
                }
            }
        }
        for(int i = 0; i < quests3.length; i++) {
            if(quests3[i].getQuestNameBR() != null) {
                finalQuestsCount++;
            }
        }

        Quest[] finalQuests = new Quest[finalQuestsCount];
        int finalIndex = 0;
        for(int i = 0; i < quests3.length; i++) {
            if(quests3[i].getQuestNameBR() != null) {
                finalQuests[finalIndex] = quests3[i];
                System.out.println(finalQuests[finalIndex].getQuestNameBR());
                System.out.println("------------------------------------");
                finalIndex++;
            }
        }
    }
}