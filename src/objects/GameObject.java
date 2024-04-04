package objects;

import main.Game;
import utilz.Constants;

import java.awt.*;
import java.awt.geom.Rectangle2D;

import static utilz.Constants.ObjectConstants.*;

public class GameObject {
    protected int x, y, objType;
    protected Rectangle2D.Float hitbox;
    protected boolean doAnimation, active = true;
    protected int aniTick, aniIndex;
    protected int xDrawOffset, yDrawOffset;

    public GameObject(int x, int y, int objType){
        this.x = x;
        this.y = y;
        this.objType = objType;
    }

    protected void updateAnimationTick(){
        aniTick++;
        if (aniTick >= Constants.ANI_SPEED){
            aniTick = 0;
            aniIndex++;
            if(aniIndex >= GetSpriteAmount(objType)){
                aniIndex = 0;
                if(objType == BARREL || objType == BOX){
                    doAnimation = false;
                    active = false;
                }
            }
        }
    }

    public void reset(){
        aniTick = 0;
        aniIndex = 0;
        active = true;
        doAnimation = objType != BARREL && objType != BOX;
    }

    protected void initHitbox(int width, int height) {
        hitbox = new Rectangle2D.Float(x, y, (int)(width * Game.SCALE), (int)(height * Game.SCALE));
    }
    public void DrawHitbox(Graphics g, int xlvlOffset) {     // for debugging
        g.setColor(Color.RED);
        g.drawRect((int) hitbox.x - xlvlOffset, (int) hitbox.y, (int) hitbox.width, (int) hitbox.height);
    }

    public int getObjType() {
        return objType;
    }
    public Rectangle2D.Float getHitbox() {
        return hitbox;
    }
    public boolean isActive() {
        return active;
    }
    public void setActive(boolean active) {
        this.active = active;
    }
    public int getxDrawOffset() {
        return xDrawOffset;
    }
    public int getyDrawOffset() {
        return yDrawOffset;
    }
    public int getAniIndex() {
        return aniIndex;
    }
}
