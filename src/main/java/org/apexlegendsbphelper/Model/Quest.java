package org.apexlegendsbphelper.Model;

import java.util.Objects;

import static org.apexlegendsbphelper.Model.FileUtil.addQuestToDictionary;

public class Quest {
    private String questNameBR;
    private String questNameNBR;
    private boolean isCompleted;
    private byte questReward;
    Quest(String questNameBR, String questNameNBR, String isCompleted, String questReward) {
        if (questNameBR != null && questNameNBR != null) {
            if(!questNameBR.isEmpty()) {
                this.questNameBR = questNameBR.trim();
                this.questNameBR = this.questNameBR.replace(".", ",");

            }
            if(!questNameNBR.isEmpty()) {
                this.questNameNBR = questNameNBR.trim();
                //this.questNameNBR = this.questNameNBR.replace(",", ".");
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
        addQuestToDictionary(this.questNameBR);
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
