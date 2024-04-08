package levels;

import java.awt.*;
import java.awt.image.BufferedImage;

public class LevelGenerator {
    public static BufferedImage generateImage(int width, int enemyCount, int potionCount, int energyPotionCount, int barrelCount, int boxCount, int spikeCount) {
        int height = 14;

        final Color background = new Color(11, 90, 60);
        final Color floor = new Color(39, 10, 10);
        final Color crabby = new Color(11, 0, 10);
        final Color red_potion = new Color(11, 10, 0);
        final Color blue_potion = new Color(11, 10, 1);
        final Color barrel = new Color(11, 10, 2);
        final Color box = new Color(11, 10, 3);
        final Color spikes = new Color(11, 10, 4);

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();

        // Inicjalizuj tablicę pixeli
        Color[][] pixels = new Color[width][height];

        // Wypełnij tło
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                pixels[x][y] = background;
            }
        }

        // Rozstaw podłogę
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                pixels[x][height - 1] = floor;
                if (y <= height - 2 && y >= height - 4) {
                    int rnd = (int) (Math.random() * 20);
                    if (rnd == 0) pixels[x][y] = floor;
                }
            }
        }

        //  traps
        for (int i = 0; i < spikeCount; i++) {
            int x = (int) (Math.random() * width);
            int y = height - 1;
            if (pixels[x][y].equals(floor) || pixels[x][y].equals(background)) {
                pixels[x][y] = spikes;
            }
        }

        // enemies
        for (int i = 0; i < enemyCount; i++) {
            int x = (int) (Math.random() * width);
            int y = (int) (Math.random() * (height - 2));
            if (!pixels[x][height - 1].equals(spikes))
                pixels[x][y] = crabby;
            else i--;
        }

        // objects
        for (int i = 0; i < potionCount; i++) {
            int x = (int) (Math.random() * width);
            int y = (int) (Math.random() * (height - 2));
            if (pixels[x][y].equals(background) && pixels[x][y + 1].equals(floor)) {
                pixels[x][y] = red_potion;
            } else i--;
        }
        for (int i = 0; i < energyPotionCount; i++) {
            int x = (int) (Math.random() * width);
            int y = height - 2;
            if (pixels[x][y].equals(background) && pixels[x][y + 1].equals(floor)) {
                pixels[x][y] = blue_potion;
            } else i--;
        }
        for (int i = 0; i < barrelCount; i++) {
            int x = (int) (Math.random() * width);
            int y = height - 2;
            if (pixels[x][y].equals(background) && pixels[x][y + 1].equals(floor)) {
                pixels[x][y] = barrel;
            } else i--;
        }
        for (int i = 0; i < boxCount; i++) {
            int x = (int) (Math.random() * width);
            int y = height - 2;
            if (pixels[x][y].equals(background) && pixels[x][y + 1].equals(floor)) {
                pixels[x][y] = box;
            } else i--;
        }

        // Renderuj obrazek
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                g.setColor(pixels[x][y]);
                g.fillRect(x, y, 1, 1);
            }
        }

        // Zapisz obrazek do pliku
        /*try {
            File output = new File("res/lvls/generated_level.png");
            ImageIO.write(image, "png", output);
            System.out.println("Obrazek został wygenerowany pomyślnie.");
        } catch (IOException e) {
            System.out.println("Wystąpił błąd podczas zapisywania obrazka: " + e.getMessage());
        }*/
        return image;
    }

}