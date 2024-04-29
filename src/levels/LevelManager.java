package levels;

import main.Game;
import utilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;

public class LevelManager {

    private final Game game;
    private BufferedImage[] levelSprite;
    private Level currentLevel;

    private Long start, end;
    private int time;

    private int[] parameters;

    private int deathCount;

    LevelParameterDecisionTree decisionTree;

    public LevelManager(Game game) {
        this.game = game;
        importOutsideSprites();
        decisionTree = new LevelParameterDecisionTree();
        try {
            decisionTree.train();
        } catch (Exception e) {
            e.printStackTrace();
        }
        deathCount = 0;
        BufferedImage img = LevelGenerator.generateImage(
                Game.TILES_IN_WIDTH,
                1,
                0,
                0,
                0,
                0,
                0
        );
        parameters = new int[]{Game.TILES_IN_WIDTH,1,0,0,0,0,0};
        currentLevel = new Level(img);
        start = System.currentTimeMillis();
    }

    public void loadNextLevel() {
        end = System.currentTimeMillis();
        time = (int) ((end - start)/parameters[0]);
        try {
            parameters = decisionTree.decideLevelParameters(deathCount, time, parameters);
        } catch (Exception e) {
            e.printStackTrace();
        }
        BufferedImage img = LevelGenerator.generateImage(
                parameters[0],
                parameters[1],
                parameters[2],
                parameters[3],
                parameters[4],
                parameters[5],
                parameters[6]
        );
        deathCount = 0;
        currentLevel = new Level(img);
        start = System.currentTimeMillis();
        game.getPlaying().getEnemyManager().loadEnemies(currentLevel);
        game.getPlaying().getPlayer().loadLvlData(currentLevel.getLvlData());
        game.getPlaying().setMaxLvlOffsetX(currentLevel.getMaxLvlOffsetX());
        game.getPlaying().getObjectManager().loadObjects(currentLevel);
    }

    private void importOutsideSprites() {
        BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.LEVEL_ATLAS);
        levelSprite = new BufferedImage[48];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 12; j++) {
                int index = i * 12 + j;
                levelSprite[index] = img.getSubimage(j * 32, i * 32, 32, 32);
            }
        }
    }

    public void draw(Graphics g, int lvlOffset) {
        for (int i = 0; i < Game.TILES_IN_HEIGHT; i++) {
            for (int j = 0; j < currentLevel.getLvlTilesWide(); j++) {
                int index = currentLevel.getSpriteIndex(j, i);
                g.drawImage(levelSprite[index], j * Game.TILES_SIZE - lvlOffset,
                        i * Game.TILES_SIZE,
                        Game.TILES_SIZE,
                        Game.TILES_SIZE,
                        null);
            }
        }
    }

    public void update() {
    }

    public Level GetCurrentLevel() {
        return currentLevel;
    }

    public void increaseDeathCount() {
        deathCount++;
    }

    public int getDeathCount() {
        return deathCount;
    }
}
