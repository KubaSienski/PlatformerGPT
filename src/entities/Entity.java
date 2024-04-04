package entities;

import main.Game;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public abstract class Entity {
    protected float x, y;
    protected int width, height;
    protected Rectangle2D.Float hitbox;
    protected int aniTick, aniIndex;
    protected int state;
    protected float airSpeed = 0;
    protected boolean inAir = false;
    protected float walkSpeed;

    protected int maxHealth;
    protected int currentHealth;

    // Attack box
    protected Rectangle2D.Float attackBox;

    public Entity(float x, float y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    protected void initHitbox(int width, int height) {
        hitbox = new Rectangle2D.Float(x, y, (int)(width * Game.SCALE), (int)(height * Game.SCALE));
    }

    // funcs for testing
    protected void drawAttackBox(Graphics g, int lvlOffset) {
        g.setColor(Color.RED);
        g.drawRect((int) attackBox.x - lvlOffset, (int) attackBox.y, (int) attackBox.width, (int) attackBox.height);
    }
    public void DrawHitbox(Graphics g, int xlvlOffset) {     // for debugging
        g.setColor(Color.RED);
        g.drawRect((int) hitbox.x - xlvlOffset, (int) hitbox.y, (int) hitbox.width, (int) hitbox.height);
    }

    public Rectangle2D.Float getHitbox() {
        return hitbox;
    }
    public int getState() {
        return state;
    }
    public int getAniIndex() {
        return aniIndex;
    }
}
