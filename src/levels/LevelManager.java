package levels;

import main.Game;
import utilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class LevelManager {

    private final Game game;
    private BufferedImage[] levelSprite;
    private ArrayList<Level> levels;
    private int lvlIndex = 0;
    private Level currentLevel;

    public LevelManager(Game game){
        this.game = game;
        importOutsideSprites();
        levels = new ArrayList<>();
        buildAllLevels();
        currentLevel = levels.get(0);
    }

    public void loadNextLevel(){
        lvlIndex++;
        if(lvlIndex >= levels.size()) {
            BufferedImage img = LevelGenerator.generateImage(
                    50,
                    4,
                    0,
                    0,
                    2,
                    1,
                    10
            );
            currentLevel = new Level(img);
        }else
            currentLevel = levels.get(lvlIndex);
        game.getPlaying().getEnemyManager().loadEnemies(currentLevel);
        game.getPlaying().getPlayer().loadLvlData(currentLevel.getLvlData());
        game.getPlaying().setMaxLvlOffsetX(currentLevel.getMaxLvlOffsetX());
        game.getPlaying().getObjectManager().loadObjects(currentLevel);
    }

    private void buildAllLevels() {
        BufferedImage[] allLevels = LoadSave.GetAllLevels();
        for (BufferedImage img : allLevels)
            levels.add(new Level(img));
    }

    private void importOutsideSprites() {
        BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.LEVEL_ATLAS);
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
            for(int j = 0; j<currentLevel.getLvlTilesWide(); j++){
                int index = currentLevel.getSpriteIndex(j,i);
                g.drawImage(levelSprite[index], j * Game.TILES_SIZE - lvlOffset,
                        i * Game.TILES_SIZE,
                        Game.TILES_SIZE,
                        Game.TILES_SIZE,
                        null);
            }
        }
    }

    public void update(){
    }

    public Level GetCurrentLevel(){
        return currentLevel;
    }
}
