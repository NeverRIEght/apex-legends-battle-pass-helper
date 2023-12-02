package org.apexlegendsbphelper.Model;

import static org.apexlegendsbphelper.Model.FileUtil.recreateTempDirectories;
import static org.apexlegendsbphelper.Model.MainUI.processWeekImages;
import net.sourceforge.tess4j.TesseractException;
import java.io.IOException;


public class Main {
    public static void main(String[] args) throws TesseractException, IOException {
        recreateTempDirectories();
        Quest[] quests1 = processWeekImages("D:\\apex-tests\\testdata1.png", "D:\\apex-tests\\testdata2.png");
//        Quest[] quests1 = processWeekImages("/Users/michaelkomarov/Downloads/image_2023-11-22_10-50-14.png", "/Users/michaelkomarov/Downloads/image_2023-11-22_10-50-32.png");

        for (Quest quest : quests1) {
            System.out.println(quest.getQuestNameBR());
        }
    }
}