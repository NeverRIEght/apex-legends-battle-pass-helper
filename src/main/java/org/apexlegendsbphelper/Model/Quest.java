package org.apexlegendsbphelper.Model;

import java.util.Objects;
import java.nio.charset.StandardCharsets;

import static org.apexlegendsbphelper.Model.FileUtil.addQuestToDictionary;

public class Quest {
    private String questNameBR;
    private String questNameNBR;
    private boolean isCompleted;
    private byte questReward;
    Quest(String questNameBR, String questNameNBR, String isCompleted, String questReward) {
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

    private String processQuestName(String questName) {
        questName = questName.trim();

        questName = questName.replace(".", ",");

        String[] questNameWords = questName.split(" ");

        int numberIndex = -1;
        for(int i = 0; i < questNameWords.length; i++) {
            String tempWord = questNameWords[i];
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
            questNameWords[numberIndex] = "$";
            String dictionaryQuestName = "";
            for(String word : questNameWords) {
                dictionaryQuestName += word + " ";
            }
            dictionaryQuestName = dictionaryQuestName.trim();

            addQuestToDictionary(dictionaryQuestName);
            return questName;
        } else {
            System.out.println("Quest name is not valid: " + questName);
        }
        return null;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Quest quest = (Quest) o;
        return isCompleted == quest.isCompleted &&
                questReward == quest.questReward &&
                Objects.equals(questNameBR, quest.questNameBR) &&
                Objects.equals(questNameNBR, quest.questNameNBR);
    }

    @Override
    public int hashCode() {
        return Objects.hash(questNameBR, questNameNBR, isCompleted, questReward);
    }

    @Override
    public String toString() {
        return "Quest name (BR): " + questNameBR + ";\nQuest name (NBR): " + questNameNBR + ";\nCompleted? " + isCompleted + ";\nReward: " + questReward;
    }
}
