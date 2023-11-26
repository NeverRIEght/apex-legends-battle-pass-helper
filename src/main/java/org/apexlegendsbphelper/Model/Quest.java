package org.apexlegendsbphelper.Model;

import java.io.IOException;
import java.util.Objects;
import java.nio.charset.StandardCharsets;

import static org.apexlegendsbphelper.Model.FileUtil.addQuestToDictionary;
import static org.apexlegendsbphelper.Model.FileUtil.searchInDictionary;
import static org.apexlegendsbphelper.Model.StringUtil.replaceNumberWithDollar;

public class Quest {
    private String questNameBR;
    private String questNameNBR;
    private boolean isCompleted;
    private byte questReward;
    Quest(String questNameBR, String questNameNBR, String isCompleted, String questReward) throws IOException {
        if (questNameBR != null && questNameNBR != null) {
            if(!questNameBR.isEmpty()) {
                this.questNameBR = processQuestName(questNameBR);
            }
            if(!questNameNBR.isEmpty()) {
                this.questNameNBR = processQuestName(questNameNBR);
            }
        }

        if (isCompleted != null && isCompleted.trim().equals("Completed")) {
            this.isCompleted = true;
        } else if (isCompleted != null && !isCompleted.trim().equals("Completed")) {
            this.isCompleted = false;
        }

        if(questReward != null && !questReward.isEmpty()) {
            this.questReward = (byte) Integer.parseInt(questReward.trim());
        }
    }

    private String processQuestName(String questName) throws IOException {
        questName = questName.trim();
        questName = questName.replace(".", ",");

        String dictionaryQuestName = replaceNumberWithDollar(questName);
        if(dictionaryQuestName == null) {
            return questName;
        } else {
            addQuestToDictionary(dictionaryQuestName);

        }


        String[] questNameSplitted = questName.split(" ");
//        System.out.println("Ищем строку: " + questName);
//        String dictionaryOccur = searchInDictionary(dictionaryQuestName);
//        if(dictionaryOccur != null) {
//            questName = dictionaryOccur.replace("$", questNameSplitted[Integer.parseInt(returnArray[1])]);
//            questNameSplitted = questName.split(" ");
//        }

        if(returnArray[0].equals("true")) {

            questName = "";
            for(String word : questNameSplitted) {
                questName += word + " ";
            }
            questName = questName.trim();
        }

        return questName;
    }



    public String getQuestNameBR() {
        return questNameBR;
    }

    public void setQuestNameBR(String questNameBR) {
        this.questNameBR = questNameBR;
    }

    public String getQuestNameNBR() {
        return questNameNBR;
    }

    public void setQuestNameNBR(String questNameNBR) {
        this.questNameNBR = questNameNBR;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public byte getQuestReward() {
        return questReward;
    }

    public void setQuestReward(byte questReward) {
        this.questReward = questReward;
    }

    @Override
    public String toString() {
        return "Quest name (BR): " + questNameBR + ";\nQuest name (NBR): " + questNameNBR + ";\nCompleted? " + isCompleted + ";\nReward: " + questReward;
    }
}
