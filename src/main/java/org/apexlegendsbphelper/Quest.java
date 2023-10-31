package org.apexlegendsbphelper;

public class Quest {
    private int id;
    private String name;
    private short type;
    private boolean hasBR = false;
    private boolean hasNBR = false;
    private boolean hasBothGamemodes = false;
    private int BRProgress = 0;
    private int BRTarget = 0;
    private int NBRProgress = 0;
    private int NBRTarget = 0;
    private boolean isCompleted = false;
    private Legend associatedLegend = null;
    private Weapon associatedWeapon = null;
    private boolean isSocialActivity = false;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public short getType() {
        return type;
    }

    public void setType(short type) {
        this.type = type;
    }

    public boolean isHasBR() {
        return hasBR;
    }

    public void setHasBR(boolean hasBR) {
        this.hasBR = hasBR;
    }

    public boolean isHasNBR() {
        return hasNBR;
    }

    public void setHasNBR(boolean hasNBR) {
        this.hasNBR = hasNBR;
    }

    public boolean isHasBothGamemodes() {
        return hasBothGamemodes;
    }

    public void setHasBothGamemodes(boolean hasBothGamemodes) {
        this.hasBothGamemodes = hasBothGamemodes;
    }

    public int getBRProgress() {
        return BRProgress;
    }

    public void setBRProgress(int BRProgress) {
        this.BRProgress = BRProgress;
    }

    public int getBRTarget() {
        return BRTarget;
    }

    public void setBRTarget(int BRTarget) {
        this.BRTarget = BRTarget;
    }

    public int getNBRProgress() {
        return NBRProgress;
    }

    public void setNBRProgress(int NBRProgress) {
        this.NBRProgress = NBRProgress;
    }

    public int getNBRTarget() {
        return NBRTarget;
    }

    public void setNBRTarget(int NBRTarget) {
        this.NBRTarget = NBRTarget;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public Legend getAssociatedLegend() {
        return associatedLegend;
    }

    public void setAssociatedLegend(Legend associatedLegend) {
        this.associatedLegend = associatedLegend;
    }

    public Weapon getAssociatedWeapon() {
        return associatedWeapon;
    }

    public void setAssociatedWeapon(Weapon associatedWeapon) {
        this.associatedWeapon = associatedWeapon;
    }

    public boolean isSocialActivity() {
        return isSocialActivity;
    }

    public void setSocialActivity(boolean socialActivity) {
        isSocialActivity = socialActivity;
    }
}
