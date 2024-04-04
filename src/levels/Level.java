package levels;

import entities.Crabby;
import main.Game;
import utilz.HelpMethods;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Level {

    private final BufferedImage img;
    private int[][] lvlData;
    private ArrayList<Crabby> crabs;

    private int lvlTilesWide;
    private int maxTilesOffset;
    private int maxLvlOffsetX;

    public Level(BufferedImage img){
        this.img = img;
        createLvlData();
        createEnemies();
        calcLvlOffsets();
    }

    private void calcLvlOffsets() {
        lvlTilesWide = img.getWidth();
        maxTilesOffset = lvlTilesWide - Game.TILES_IN_WIDTH;
        maxLvlOffsetX = Game.TILES_SIZE * maxTilesOffset;
    }

    private void createEnemies() {
        crabs = HelpMethods.GetCrabs(img);
    }

    private void createLvlData() {
        lvlData = HelpMethods.GetLevelData(img);
    }

    public int getSpriteIndex(int x, int y){
        return lvlData[y][x];
    }

    public int[][] getLvlData(){ return lvlData; }

    public int getMaxLvlOffsetX(){
        return maxLvlOffsetX;
    }
    public ArrayList<Crabby> getCrabs() {
        return crabs;
    }
}
