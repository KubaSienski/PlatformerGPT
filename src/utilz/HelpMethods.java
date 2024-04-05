package utilz;

import entities.Crabby;
import main.Game;
import objects.GameContainer;
import objects.Potion;
import objects.Spike;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static utilz.Constants.ObjectConstants.*;

public class HelpMethods {

    public static boolean CanMoveHere(float x, float y, float width, float height, int[][] lvlData) {
        if (!IsSolid(x, y, lvlData))
            if (!IsSolid(x + width, y + height, lvlData))
                if (!IsSolid(x + width, y, lvlData))
                    return !IsSolid(x, y + height, lvlData);
        return false;
    }

    private static boolean IsSolid(float x, float y, int[][] lvlData) {
        int maxWidth = lvlData[0].length * Game.TILES_SIZE;
        if (x < 0 || x >= maxWidth)
            return true;
        if (y < 0 || y >= Game.GAME_HEIGHT)
            return true;

        float xIndex = x / Game.TILES_SIZE;
        float yIndex = y / Game.TILES_SIZE;

        return IsTileSolid((int) xIndex, (int) yIndex, lvlData);
    }

    public static boolean IsTileSolid(int xTile, int yTile, int[][] lvlData){
        int value = lvlData[yTile][xTile];
        return value != 11;
    }

    public static float GetEntityXPosNextToWall(Rectangle2D.Float hitbox, float xSpeed){
        int currentTile = (int)(hitbox.x/Game.TILES_SIZE);
        if(xSpeed>0){
            // right
            int tileXPos = currentTile*Game.TILES_SIZE;
            int xOffset = (int)(Game.TILES_SIZE - hitbox.width);
            return tileXPos + xOffset - 1;
        }
        else{
            // left
            return currentTile * Game.TILES_SIZE;
        }
    }
    public static float GetEntityYPosUnderRoofOrAboveFloor(Rectangle2D.Float hitbox, float airSpeed){
        int currentTile = (int)(hitbox.y/Game.TILES_SIZE);
        if(airSpeed>0){
            // falling
            int tileYPos = currentTile*Game.TILES_SIZE;
            int yOffset = (int)(Game.TILES_SIZE - hitbox.height);
            return tileYPos + yOffset - 1;
        }
        else{
            // jumping
            return currentTile * Game.TILES_SIZE;
        }
    }

    public static boolean IsEntityOnFloor(Rectangle2D.Float hitbox, int[][] lvlData){
        // check the pixel below bottom left and bottom right
        if(!IsSolid(hitbox.x, hitbox.y + hitbox.height +1, lvlData))
            return IsSolid(hitbox.x + hitbox.width, hitbox.y + hitbox.height + 1, lvlData);
        return true;
    }

    public static boolean IsFloor(Rectangle2D.Float hitbox, float xSpeed, int[][] lvlData){
        if(xSpeed > 0)
            return IsSolid(hitbox.x + hitbox.width + xSpeed, hitbox.y + hitbox.height + 1, lvlData);
        else
            return IsSolid(hitbox.x + xSpeed, hitbox.y + hitbox.height + 1, lvlData);
    }

    public static boolean IsAllTilesWalkable(int xStart, int xEnd, int y, int[][] lvlData){
        for(int i =0; i < xEnd - xStart; i++) {
            if (IsTileSolid(xStart + i, y, lvlData))
                return false;
            if (!IsTileSolid(xStart + i, y + 1, lvlData))
                return false;
        }
        return true;
    }

    public static boolean IsSightClear(int[][] lvlData, Rectangle2D.Float firstHitbox,
                                       Rectangle2D.Float secondHitbox, int yTile){
        int firstXTile = (int) (firstHitbox.x / Game.TILES_SIZE);
        int secondXTile = (int) (secondHitbox.x / Game.TILES_SIZE);

        if(firstXTile > secondXTile)
            return IsAllTilesWalkable(secondXTile, firstXTile, yTile, lvlData);
        else
            return IsAllTilesWalkable(firstXTile, secondXTile, yTile, lvlData);
    }

    public static int[][] GetLevelData(BufferedImage img){

        int[][] lvlData = new int[img.getHeight()][img.getWidth()];

        for(int i = 0; i< img.getHeight();i++){
            for (int j = 0; j< img.getWidth();j++){
                Color color = new Color(img.getRGB(j,i));
                int value = color.getRed();
                if(value>=48) value = 0;
                lvlData[i][j] = value;
            }
        }
        return lvlData;
    }

    public static ArrayList<Crabby> GetCrabs(BufferedImage img){
        ArrayList<Crabby> list = new ArrayList<>();

        for(int i = 0; i< img.getHeight();i++){
            for (int j = 0; j< img.getWidth();j++){
                Color color = new Color(img.getRGB(j,i));
                int value = color.getGreen();
                if(value== Constants.EnemyConstants.CRABBY)
                    list.add(new Crabby(j* Game.TILES_SIZE, i* Game.TILES_SIZE));
            }
        }
        return list;
    }

    public static ArrayList<Potion> GetPotions(BufferedImage img){
        ArrayList<Potion> list = new ArrayList<>();

        for(int i = 0; i< img.getHeight();i++){
            for (int j = 0; j< img.getWidth();j++){
                Color color = new Color(img.getRGB(j,i));
                int value = color.getBlue();
                if(value== BLUE_POTION || value == RED_POTION)
                    list.add(new Potion(j* Game.TILES_SIZE, i * Game.TILES_SIZE, value));
            }
        }
        return list;
    }

    public static ArrayList<GameContainer> GetContainers(BufferedImage img){
        ArrayList<GameContainer> list = new ArrayList<>();

        for(int i = 0; i< img.getHeight();i++){
            for (int j = 0; j< img.getWidth();j++){
                Color color = new Color(img.getRGB(j,i));
                int value = color.getBlue();
                if(value== BOX || value == BARREL)
                    list.add(new GameContainer(j* Game.TILES_SIZE, i * Game.TILES_SIZE, value));
            }
        }
        return list;
    }

    public static ArrayList<Spike> GetSpikes(BufferedImage img) {
        ArrayList<Spike> list = new ArrayList<>();

        for(int i = 0; i< img.getHeight();i++){
            for (int j = 0; j< img.getWidth();j++){
                Color color = new Color(img.getRGB(j,i));
                int value = color.getBlue();
                if(value == SPIKE)
                    list.add(new Spike(j* Game.TILES_SIZE, i * Game.TILES_SIZE, SPIKE));
            }
        }
        return list;
    }
}
