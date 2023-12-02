package org.apexlegendsbphelper.Model;

import java.io.IOException;

public class Quest {
    private String questNameBR;
    private String questNameNBR;
    private boolean isCompleted;
    private byte questReward;
    Quest(String questNameBR, String questNameNBR, String isCompleted, String questReward) {
        if (questNameBR != null && questNameNBR != null) {
            if(!questNameBR.isEmpty()) {
                questNameBR = questNameBR.trim();
                questNameBR = questNameBR.replace(".", ",");
                this.questNameBR = questNameBR;
            }
            if(!questNameNBR.isEmpty()) {
                questNameNBR = questNameNBR.trim();
                questNameNBR = questNameNBR.replace(".", ",");
                this.questNameNBR = questNameNBR;
            }
        }

        if (isCompleted != null && isCompleted.trim().equals("Completed")) {
            this.isCompleted = true;
        } else if (isCompleted != null) {
            this.isCompleted = false;
        }

        if(questReward != null && !questReward.isEmpty()) {
            this.questReward = (byte) Integer.parseInt(questReward.trim());
        }
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
