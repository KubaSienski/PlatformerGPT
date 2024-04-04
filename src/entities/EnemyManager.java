package entities;

import gamestates.Playing;
import levels.Level;
import utilz.Constants;
import utilz.LoadSave;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class EnemyManager {
    private final Playing playing;
    private BufferedImage[][] crabbyArr;
    private ArrayList<Crabby> crabbies = new ArrayList<>();

    public EnemyManager(Playing playing){
        this.playing = playing;
        loadEnemyImgs();
    }

    public void loadEnemies(Level level) {
        crabbies = level.getCrabs();
        System.out.println("size of crabs: " + crabbies.size());
    }

    public void update(int[][] lvlData, Player player){
        boolean isAnyActive = false;
        for(Crabby c : crabbies)
            if(c.isActive()) {
                c.update(lvlData, player);
                isAnyActive = true;
            }
        if(!isAnyActive)
            playing.setLvlCompleted(true);
    }

    public void draw(Graphics g, int lvlOffset){
        drawCrabs(g, lvlOffset);
    }

    private void drawCrabs(Graphics g, int lvlOffset) {
        for(Crabby c: crabbies) {
            if (c.isActive())
                g.drawImage(crabbyArr[c.getState()][c.getAniIndex()],
                        (int) (c.getHitbox().x - Constants.EnemyConstants.CRABBY_DRAWOFFSET_X) - lvlOffset + c.flipX(),
                        (int) (c.getHitbox().y - Constants.EnemyConstants.CRABBY_DRAWOFFSET_Y),
                        Constants.EnemyConstants.CRABBY_WIDTH * c.flipW(),
                        Constants.EnemyConstants.CRABBY_HEIGHT, null);
        }
    }

    public void checkEnemyHit(Rectangle2D.Float attackBox){
        for(Crabby c: crabbies){
            if(c.isActive())
                if(attackBox.intersects(c.getHitbox())){
                    c.hurt(10);
                    return;
            }
        }
    }

    private void loadEnemyImgs() {
        crabbyArr = new BufferedImage[5][9];
        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.CRABBY_SPRITE);
        for (int i = 0; i < crabbyArr.length; i++){
            for (int j = 0; j<crabbyArr[i].length; j++){
                crabbyArr[i][j] = temp.getSubimage(j * Constants.EnemyConstants.CRABBY_WIDTH_DEFAULT, i * Constants.EnemyConstants.CRABBY_HEIGHT_DEFAULT,
                        Constants.EnemyConstants.CRABBY_WIDTH_DEFAULT, Constants.EnemyConstants.CRABBY_HEIGHT_DEFAULT);
            }
        }
    }

    public void resetAllEnemies(){
        for(Crabby c: crabbies){
            c.resetEnemy();
        }
    }
}
