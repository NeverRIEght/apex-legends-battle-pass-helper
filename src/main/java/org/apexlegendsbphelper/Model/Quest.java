package org.apexlegendsbphelper.Model;

import java.io.IOException;
import java.util.Objects;
import java.nio.charset.StandardCharsets;

import static org.apexlegendsbphelper.Model.FileUtil.addQuestToDictionary;
import static org.apexlegendsbphelper.Model.FileUtil.searchInDictionary;

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

        String[] returnArray = replaceNumberWithDollar(questName);
        String dictionaryQuestName = returnArray[2];

        String[] questNameSplitted = questName.split(" ");
        System.out.println("Ищем строку: " + questName);
        String dictionaryOccur = searchInDictionary(dictionaryQuestName);
        if(dictionaryOccur != null) {
            questName = dictionaryOccur.replace("$", questNameSplitted[Integer.parseInt(returnArray[1])]);
            questNameSplitted = questName.split(" ");
        }

        if(returnArray[0].equals("true")) {
            addQuestToDictionary(dictionaryQuestName);

            for(int i = 0; i < questNameSplitted.length; i++) {
                if(i != Integer.parseInt(returnArray[1])) {
                    for(int j = 0; j < questNameSplitted[i].length(); j++) {
                        if(Character.isDigit(questNameSplitted[i].charAt(j)) && questNameSplitted[i].charAt(j) == '0') {
                            questNameSplitted[j] = "O";
                        }
                    }
                }
            }

            questName = "";
            for(String word : questNameSplitted) {
                questName += word + " ";
            }
            questName = questName.trim();
        }

        return questName;
    }

    private String[] replaceNumberWithDollar(String inputString) {
        inputString = inputString.trim();
        String ouputString = inputString;
        String[] outputArray = new String[3];

        String[] ouputStringSplitted = ouputString.split(" ");

        int numberIndex = -1;
        for(int i = 0; i < ouputStringSplitted.length; i++) {
            String tempWord = ouputStringSplitted[i];
            tempWord = tempWord.replace(",", "");
            tempWord = tempWord.replace(".", "");
            tempWord = tempWord.replace("o", "0");
            tempWord = tempWord.replace("O", "0");
            try {
                Integer.parseInt(tempWord);
            } catch (NumberFormatException e) {
                continue;
            }
            numberIndex = i;
            break;
        }

        if(numberIndex != -1) {
            outputArray[0] = "true";
            outputArray[1] = numberIndex + "";
            ouputStringSplitted[numberIndex] = "$";
            ouputString = "";
            for(String word : ouputStringSplitted) {
                ouputString += word + " ";
            }
            ouputString = ouputString.trim();
        } else {
            outputArray[0] = "false";
            outputArray[1] = "";
        }
        outputArray[2] = ouputString;
        return outputArray;
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
