package objects;

import gamestates.Playing;
import levels.Level;
import utilz.LoadSave;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static utilz.Constants.ObjectConstants.*;

public class ObjectManager {
    private Playing playing;
    private BufferedImage[][] potionImgs, containerImgs;
    private BufferedImage spikeImg;
    private ArrayList<Potion> potions;
    private ArrayList<GameContainer> containers;
    private ArrayList<Spike> spikes;

    public ObjectManager(Playing playing){
        this.playing = playing;
        loadImgs();
    }

    public void checkObjectTouched(Rectangle2D.Float hitBox){
        for (Potion p : potions)
            if(p.isActive()){
                if(hitBox.intersects(p.getHitbox())) {
                    p.setActive(false);
                    applyEffectToPlayer(p);
                }
            }
        for (Spike s : spikes)
            if (hitBox.intersects(s.getHitbox()))
                playing.getPlayer().kill();
    }
    public void applyEffectToPlayer(Potion p){
        if (p.getObjType() == RED_POTION)
            playing.getPlayer().changeHealth(RED_POTION_VALUE);
        else
            playing.getPlayer().changePower(BLUE_POTION_VALUE);
    }
    public void checkObjectHit(Rectangle2D.Float attackBox){
        for (GameContainer gc : containers)
            if (gc.isActive() && !gc.doAnimation){
                if(gc.getHitbox().intersects(attackBox)){
                    gc.setDoAnimation(true);
                    int type = 0;
                    float yOffset = gc.getHitbox().height/2;
                    if(gc.getObjType() == BARREL) {
                        type = 1;
                        yOffset = 0;
                    }
                    potions.add(new Potion((int) (gc.getHitbox().x + gc.getHitbox().width/2),
                            (int) (gc.getHitbox().y - yOffset),
                            type));
                    return;
                }
            }
    }

    public void loadObjects(Level newLevel) {
        potions = new ArrayList<>(newLevel.getPotions());
        containers = new ArrayList<>(newLevel.getContainers());
        spikes = new ArrayList<>(newLevel.getSpikes());
    }

    private void loadImgs() {
        BufferedImage potionSprites = LoadSave.GetSpriteAtlas(LoadSave.POTION_ATLAS);
        potionImgs = new BufferedImage[2][7];

        for (int i = 0; i< potionImgs.length; i++)
            for (int j = 0; j<potionImgs[i].length; j++)
                potionImgs[i][j] = potionSprites.getSubimage(12*j, 16*i, 12, 16);

        BufferedImage containerSprites = LoadSave.GetSpriteAtlas(LoadSave.CONTAINER_ATLAS);
        containerImgs = new BufferedImage[2][8];

        for (int i = 0; i< containerImgs.length; i++)
            for (int j = 0; j<containerImgs[i].length; j++)
                containerImgs[i][j] = containerSprites.getSubimage(40*j, 30*i, 40, 30);

        spikeImg = LoadSave.GetSpriteAtlas(LoadSave.TRAP_ATLAS);
    }

    public void update(){
        for(Potion p : potions)
            if(p.isActive())
                p.update();
        for (GameContainer gc : containers)
            if (gc.isActive())
                gc.update();
    }
    public void draw(Graphics g, int xLvlOffset){
        drawPotions(g, xLvlOffset);
        drawContainers(g, xLvlOffset);
        drawTraps(g, xLvlOffset);
    }

    private void drawTraps(Graphics g, int xLvlOffset) {
        for(Spike spike : spikes)
            g.drawImage(
                    spikeImg,
                    (int) (spike.getHitbox().x - xLvlOffset),
                    (int) (spike.getHitbox().y - spike.getyDrawOffset()),
                    SPIKE_WIDTH,
                    SPIKE_HEIGHT,
                    null
            );
    }

    private void drawContainers(Graphics g, int xLvlOffset) {
        for (GameContainer gc : containers) {
            if (gc.isActive()) {
                int type = 0;
                if (gc.getObjType() == BARREL)
                    type = 1;
                g.drawImage(
                        containerImgs[type][gc.getAniIndex()],
                        (int) (gc.getHitbox().x - gc.getxDrawOffset() - xLvlOffset),
                        (int) (gc.getHitbox().y - gc.getyDrawOffset()),
                        CONTAINER_WIDTH,
                        CONTAINER_HEIGHT,
                        null
                );
            }
        }


    }

    private void drawPotions(Graphics g, int xLvlOffset) {
        for(Potion p : potions)
            if(p.isActive()) {
                int type = 0;
                if(p.getObjType() == RED_POTION)
                    type = 1;
                g.drawImage(potionImgs[type][p.getAniIndex()],
                        (int) (p.getHitbox().x - p.getxDrawOffset() - xLvlOffset),
                        (int) (p.getHitbox().y - p.getyDrawOffset()),
                        POTION_WIDTH,
                        POTION_HEIGHT,
                        null);
            }
    }

    public void resetAllObjects() {
        loadObjects(playing.getLevelManager().GetCurrentLevel());

        for(Potion p : potions)
            p.reset();
        for(GameContainer gc : containers)
            gc.reset();
    }
}
