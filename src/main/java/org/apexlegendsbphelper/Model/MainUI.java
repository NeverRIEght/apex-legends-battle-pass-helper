package org.apexlegendsbphelper.Model;

import net.sourceforge.tess4j.TesseractException;

import java.io.IOException;

import static org.apexlegendsbphelper.Model.FXUtil.pathFixerWindows;
import static org.apexlegendsbphelper.Model.FileUtil.sortDictionary;
import static org.apexlegendsbphelper.Model.ImageUtil.processImage;
import static org.apexlegendsbphelper.Model.ImageUtil.removeQuestsDuplicates;
import static org.apexlegendsbphelper.Model.StringUtil.stringsEqualsProbability;

public abstract class MainUI {
    public static Quest[] scanImages(String imagePath1, String imagePath2) throws TesseractException, IOException {
        imagePath1 = pathFixerWindows(imagePath1);
        imagePath2 = pathFixerWindows(imagePath2);


        if(!imagePath1.isEmpty() && !imagePath2.isEmpty()) {
            sortDictionary();
            Quest[] quests1 = processImage(imagePath1);
            Quest[] quests2 = processImage(imagePath2);

            Quest[] quests3 = new Quest[quests1.length + quests2.length];
            System.arraycopy(quests1, 0, quests3, 0, quests1.length);
            System.arraycopy(quests2, 0, quests3, quests1.length, quests2.length);

            for(int i = 0; i < quests3.length; i++) {
                for(int j = i + 1; j < quests3.length; j++) {
                    if(stringsEqualsProbability(quests3[i].getQuestNameBR(), quests3[j].getQuestNameBR()) >= 75) {
                        quests3[j].setQuestNameBR(null);
                    }
                }
            }

            int finalQuestsCount = 0;
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
                    finalIndex++;
                }
            }
            sortDictionary();
            return finalQuests;
        } else {
            return null;
        }
    }
}
