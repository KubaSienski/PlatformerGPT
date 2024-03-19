package levels;

import main.Game;
import utilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;

import static main.Game.TILES_SIZE;

public class LevelManager {
    Game game;
    private BufferedImage[] levelSprite;
    private BufferedImage levelFloor;
    private Level levelOne;

    public LevelManager(Game game){
        this.game = game;
        importOutsideSprites();
        levelOne = new Level(LoadSave.GetLevelData());
    }

    private void importOutsideSprites() {
        BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.LEVEL_ATLAS);
        //levelFloor = LoadSave.GetSpriteAtlas(LoadSave.LEVEL_FLOOR);
        levelSprite = new BufferedImage[48];
        for(int i = 0; i<4; i++){
            for(int j = 0; j<12; j++){
                int index = i * 12 + j;
                levelSprite[index] = img.getSubimage(j*32, i*32, 32,32);
            }
        }
    }

    public void draw(Graphics g, int lvlOffset){
        for(int i = 0; i < Game.TILES_IN_HEIGHT; i++){
            for(int j = 0; j<levelOne.getLvlData()[0].length; j++){
                int index = levelOne.getSpriteIndex(j,i);
                g.drawImage(levelSprite[index], j * TILES_SIZE - lvlOffset,i * TILES_SIZE,TILES_SIZE,TILES_SIZE, null);
            }
        }
    }

    public void update(){
    }

    public Level GetCurrentLevel(){
        return levelOne;
    }
}
