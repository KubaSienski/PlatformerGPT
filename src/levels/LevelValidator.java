package levels;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class LevelValidator {

    public static boolean isLevelPassable(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        Color[][] pixels = new Color[width][height];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                pixels[x][y] = new Color(image.getRGB(x, y));
            }
        }

        for (int y = 0; y < height; y++) {
            int spikeCount = 0;
            for (int x = 0; x < width; x++) {
                if (pixels[x][y].equals(new Color(11, 10, 4))) {
                    spikeCount++;
                    if (spikeCount > 3) {
                        return false;
                    }
                } else {
                    spikeCount = 0;
                }
            }
        }

        for (int x = 0; x < width; x++) {
            int floorHeight = 0;
            for (int y = 0; y < height; y++) {
                if (pixels[x][y].equals(new Color(39, 10, 10))) {
                    floorHeight++;
                    if (floorHeight > 3) {
                        return false;
                    }
                    if (floorHeight == 1 && y == height - 4) {
                        if (x == width - 1 || x == 0 || !pixels[x + 1][y - 1].equals(new Color(39, 10, 10)) && !pixels[x - 1][y - 1].equals(new Color(39, 10, 10))) {
                            return false;
                        }
                    }
                } else {
                    floorHeight = 0;
                }
            }
        }

        return true;
    }
}
