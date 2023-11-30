package org.apexlegendsbphelper.Model;

import net.sourceforge.tess4j.TesseractException;

import java.io.IOException;

import static org.apexlegendsbphelper.Model.DictionaryUtil.sortDictionary;
import static org.apexlegendsbphelper.Model.ImageUtil.processImage;
import static org.apexlegendsbphelper.Model.StringUtil.*;

public abstract class MainUI {
    public static Quest[] processWeekImages(String imagePath1, String imagePath2) throws TesseractException, IOException {
        imagePath1 = pathFixer(imagePath1);
        imagePath2 = pathFixer(imagePath2);



        if(!imagePath1.isEmpty() && !imagePath2.isEmpty()) {
            sortDictionary();
            Quest[] quests1 = processImage(imagePath1);
            Quest[] quests2 = processImage(imagePath2);

            for(int i = 0; i < quests1.length; i++) {
                boolean isBRHasNumber = checkForNumber(quests1[i].getQuestNameBR());
                boolean isNBRHasNumber = checkForNumber(quests1[i].getQuestNameNBR());

                //if one of the quests in quests2 has the same name as quest1 and has a number in its name,
                //than we replace quest1 name with quest2 name

                if((!isBRHasNumber || !isNBRHasNumber) && quests1[i] != null) {
                    for(int j = 0; j < quests2.length; j++) {
                        if(quests2[j] != null) {
                            if(stringsEqualsProbability(quests1[i].getQuestNameBR(), quests2[j].getQuestNameBR()) >= 75) {
                                if(checkForNumber(quests2[j].getQuestNameBR())) {
                                    quests1[i].setQuestNameBR(quests2[j].getQuestNameBR());
                                }
                                if(checkForNumber(quests2[j].getQuestNameNBR())) {
                                    quests1[i].setQuestNameNBR(quests2[j].getQuestNameNBR());
                                }
                            }
                        }
                    }
                }
            }

            Quest[] quests3 = new Quest[quests1.length + quests2.length];
            System.arraycopy(quests1, 0, quests3, 0, quests1.length);
            System.arraycopy(quests2, 0, quests3, quests1.length, quests2.length);

            for(int i = 0; i < quests3.length; i++) {
                for(int j = i + 1; j < quests3.length; j++) {
                    if(stringsEqualsProbability(quests3[i].getQuestNameBR(), quests3[j].getQuestNameBR()) >= 75) {
                        quests3[i].setQuestNameBR(null);
                    }
                }
            }

            int finalQuestsCount = 0;
            for (Quest quest : quests3) {
                if (quest.getQuestNameBR() != null) {
                    finalQuestsCount++;
                }
            }

            Quest[] finalQuests = new Quest[finalQuestsCount];
            int finalIndex = 0;

            for (Quest quest : quests3) {
                if (quest.getQuestNameBR() != null) {
                    finalQuests[finalIndex] = quest;
                    finalIndex++;
                }
            }

            for (Quest quest : finalQuests) {
                quest.setQuestNameBR(removeCommaFromNumber(quest.getQuestNameBR()));
                quest.setQuestNameNBR(removeCommaFromNumber(quest.getQuestNameNBR()));

                quest.setQuestNameBR(replaceOWithZero(quest.getQuestNameBR()));
                quest.setQuestNameNBR(replaceOWithZero(quest.getQuestNameNBR()));
            }

            sortDictionary();
            return finalQuests;
        } else {
            return null;
        }
    }
}
