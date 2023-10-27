package org.apexlegendsbphelper;

import java.awt.image.BufferedImage;

public class QuestUtil {
    public BufferedImage questImage;
    public int questOnImageNumber;
    public int[] questCoords = new int[4];

    QuestUtil(BufferedImage questImage, int questOnImageNumber, int[] questCoords) {
        this.questImage = questImage;
        this.questOnImageNumber = questOnImageNumber;
        this.questCoords = questCoords;
    }


}
