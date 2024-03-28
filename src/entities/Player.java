package entities;

import gamestates.Playing;
import main.Game;
import utilz.Constants;
import utilz.HelpMethods;
import utilz.LoadSave;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class Player extends Entity {
    private BufferedImage[][] animations;
    private int aniTick = 0, aniIndex = 0, aniSpeed = 20;       //player animation parameters
    private int playerAction = Constants.PlayerConstants.IDLE;
    private boolean moving = false, attacking = false;
    private boolean left, up, down, right, jump;
    private float playerSpeed = Game.SCALE;
    private int[][] lvlData;
    private float xDrawOffset = 21 * Game.SCALE, yDrawOffset = 4 * Game.SCALE;

    //  jumping / gravity
    private float airSpeed = 0f;
    private float gravity = 0.04f * Game.SCALE;
    private float jumpSpeed = -2.25f * Game.SCALE;
    private float fallSpeedAfterColision = 0.5f * Game.SCALE;
    private boolean inAir = false;

    // status bar ui
    private BufferedImage statusBarImg;

    private int statusBarWidth = (int) (192 * Game.SCALE);
    private int statusBarHeight = (int) (58 * Game.SCALE);
    private int statusBarX = (int) (10 * Game.SCALE);
    private int statusBarY = (int) (10 * Game.SCALE);

    private int healthBarWidth = (int) (150 * Game.SCALE);
    private int healthBarHeight = (int) (4 * Game.SCALE);
    private int healthBarX = (int) (34 * Game.SCALE);
    private int healthBarY = (int) (14 * Game.SCALE);

    private int maxHealth = 100;
    private int currentHealth = maxHealth;
    private int healthWidth = healthBarWidth;

    // Attack box
    private Rectangle2D.Float attackBox;

    private int flipX = 0;
    private int flipW = 1;

    private boolean attackChecked;
    private Playing playing;

    public Player(float x, float y, int width, int height, Playing playing) {
        super(x, y, width, height);
        this.playing = playing;
        loadAnimations();
        initHitbox(x, y, (int)(20 * Game.SCALE), (int)(27 * Game.SCALE));
        initAttackBox();
    }

    private void initAttackBox() {
        attackBox = new Rectangle2D.Float(x,y,(int)(20 * Game.SCALE), (int)(20 * Game.SCALE));
    }

    public void update() {
        updateHealthBar();

        if(currentHealth <= 0) {
            playing.setGameOver(true);
            return;
        }

        updateAttackBox();
        updateAnimationTick();
        updatePos();
        if(attacking)
            chceckAttack();
        
        setAnimation();
    }

    private void chceckAttack() {
        if(attackChecked || aniIndex != 1)
            return;
        attackChecked = true;
        playing.checkEnemyHit(attackBox);
    }

    private void updateAttackBox() {
        if(right){
            attackBox.x = hitbox.x + hitbox.width + (int)(Game.SCALE * 10);
        } else if (left) {
            attackBox.x = hitbox.x - hitbox.width - (int)(Game.SCALE * 10);
        }
        attackBox.y = hitbox.y + (int)(10 * Game.SCALE);
    }

    private void updateHealthBar() {
        healthWidth = (int)((currentHealth / (float)(maxHealth)) * healthBarWidth);
    }

    public void render(Graphics g, int lvlOffset) {
        g.drawImage(animations[playerAction][aniIndex],
                (int) (hitbox.x - xDrawOffset) - lvlOffset + flipX,
                (int) (hitbox.y - yDrawOffset),
                width * flipW, height, null);
        //drawAttackBox(g, lvlOffset);
        drawUI(g);
    }

    private void drawAttackBox(Graphics g, int lvlOffset) {
        g.setColor(Color.RED);
        g.drawRect((int) attackBox.x - lvlOffset, (int) attackBox.y, (int) attackBox.width, (int) attackBox.height);
    }

    private void drawUI(Graphics g) {
        g.drawImage(statusBarImg, statusBarX, statusBarY,statusBarWidth, statusBarHeight, null);
        g.setColor(Color.RED);
        g.fillRect(healthBarX + statusBarX, healthBarY + statusBarY, healthWidth, healthBarHeight);
    }

    private void updateAnimationTick() {
        aniTick++;
        if (aniTick >= aniSpeed) {
            aniTick = 0;
            aniIndex++;
            if (aniIndex >= Constants.PlayerConstants.GetSpriteAmount(playerAction)) {
                aniIndex = 0;
                attacking = false;
                attackChecked = false;
            }
        }
    }

    private void setAnimation() {
        int startAni = playerAction;

        if (moving) playerAction = Constants.PlayerConstants.RUNNING;
        else playerAction = Constants.PlayerConstants.IDLE;

        if (inAir) {
            if (airSpeed < 0)
                playerAction = Constants.PlayerConstants.JUMP;
            else
                playerAction = Constants.PlayerConstants.FALLING;
        }

        if (attacking) {
            playerAction = Constants.PlayerConstants.ATTACK;
            if(startAni != Constants.PlayerConstants.ATTACK){
                aniIndex = 1;
                aniTick = 0;
                return;
            }
        }

        if (startAni != playerAction) {
            aniIndex = 0;
            aniTick = 0;
        }
    }

    private void updatePos() {
        moving = false;

        if (jump) {
            jump();
        }

        if(!inAir)
            if((!left && !right) || (right && left))
                return;

        float xSpeed = 0;

        if (right) {
            xSpeed += playerSpeed;
            flipX = 0;
            flipW = 1;
        }
        if (left) {
            xSpeed -= playerSpeed;
            flipX = width;
            flipW = -1;
        }

        if (!HelpMethods.IsEntityOnFloor(hitbox, lvlData)) {
            inAir = true;
        }

        if (inAir) {
            if (HelpMethods.CanMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, lvlData)) {
                hitbox.y += airSpeed;
                airSpeed += gravity;
                updateXPos(xSpeed);
            } else {
                hitbox.y = HelpMethods.GetEntityYPosUnderRoofOrAboveFloor(hitbox, airSpeed);
                if (airSpeed > 0) {
                    resetInAir();
                } else airSpeed = fallSpeedAfterColision;
                updateXPos(xSpeed);
            }
        } else
            updateXPos(xSpeed);

        moving = true;
    }

    private void jump() {
        if (inAir) return;
        inAir = true;
        airSpeed = jumpSpeed;
    }

    private void resetInAir() {
        inAir = false;
        airSpeed = 0;
    }

    private void updateXPos(float xSpeed) {
        if (HelpMethods.CanMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, lvlData)) {
            hitbox.x += xSpeed;
        } else {
            hitbox.x = HelpMethods.GetEntityXPosNextToWall(hitbox, xSpeed);
        }
    }

    public void changeHealth(int value){
        currentHealth += value;

        if(currentHealth <= 0){
            currentHealth = 0;
            //gameOver();
        } else if(currentHealth > maxHealth)
            currentHealth = maxHealth;
    }

    // animating player
    private void loadAnimations() {
        BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.PLAYER_ATLAS);

        animations = new BufferedImage[7][8];
        for (int i = 0; i < animations.length; i++) {
            for (int j = 0; j < animations[i].length; j++) {
                animations[i][j] = img.getSubimage(j * 64, i * 40, 64, 40);
            }
        }

        statusBarImg = LoadSave.GetSpriteAtlas(LoadSave.STATUS_BAR);
    }

    public void loadLvlData(int[][] lvlData) {
        this.lvlData = lvlData;
        if (!HelpMethods.IsEntityOnFloor(hitbox, lvlData))
            inAir = true;
    }

    public void setAttacking(boolean attacking) {
        this.attacking = attacking;
    }

    public boolean isLeft() {
        return left;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public boolean isUp() {
        return up;
    }

    public void setUp(boolean up) {
        this.up = up;
    }

    public boolean isDown() {
        return down;
    }

    public void setDown(boolean down) {
        this.down = down;
    }

    public boolean isRight() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public void resetDirBooleans() {
        left = false;
        up = false;
        right = false;
        down = false;
    }

    public void setJump(boolean jump) {
        this.jump = jump;
    }

    public void resetAll() {
        resetDirBooleans();
        inAir = false;
        attacking = false;
        moving = false;
        playerAction = Constants.PlayerConstants.IDLE;
        currentHealth = maxHealth;

        hitbox.x = x;
        hitbox.y = y;

        if (!HelpMethods.IsEntityOnFloor(hitbox, lvlData)) {
            inAir = true;
        }
    }
}
