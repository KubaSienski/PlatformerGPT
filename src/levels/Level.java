package levels;

import entities.Crabby;
import main.Game;
import objects.GameContainer;
import objects.Potion;
import objects.Spike;
import utilz.HelpMethods;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Level {

    private final BufferedImage img;
    private int[][] lvlData;
    private ArrayList<Crabby> crabs;
    private ArrayList<Potion> potions;
    private ArrayList<Spike> spikes;
    private ArrayList<GameContainer> containers;
    private int lvlTilesWide;
    private int maxTilesOffset;
    private int maxLvlOffsetX;

    public Level(BufferedImage img){
        this.img = img;
        createLvlData();
        crabs = HelpMethods.GetCrabs(img);
        potions = HelpMethods.GetPotions(img);
        containers = HelpMethods.GetContainers(img);
        spikes = HelpMethods.GetSpikes(img);
        calcLvlOffsets();
    }

    private void calcLvlOffsets() {
        lvlTilesWide = img.getWidth();
        maxTilesOffset = lvlTilesWide - Game.TILES_IN_WIDTH;
        maxLvlOffsetX = Game.TILES_SIZE * maxTilesOffset;
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
    public int getLvlTilesWide() {
        return lvlTilesWide;
    }
    public ArrayList<Crabby> getCrabs() {
        return crabs;
    }
    public ArrayList<Potion> getPotions() {
        return potions;
    }
    public ArrayList<GameContainer> getContainers() {
        return containers;
    }
    public ArrayList<Spike> getSpikes() {
        return spikes;
    }
}
