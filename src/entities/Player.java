package entities;

import main.Game;
import utilz.Constants;
import utilz.HelpMethods;
import utilz.LoadSave;

import java.awt.*;
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

    public Player(float x, float y, int width, int height) {
        super(x, y, width, height);
        loadAnimations();
        initHitbox(x, y, (int)(20 * Game.SCALE), (int)(27 * Game.SCALE));
    }

    public void update() {
        updateAnimationTick();
        updatePos();
        setAnimation();
    }

    public void render(Graphics g, int lvlOffset) {
        g.drawImage(animations[playerAction][aniIndex], (int) (hitbox.x - xDrawOffset) - lvlOffset, (int) (hitbox.y - yDrawOffset), width, height, null);
        //DrawHitbox(g);
    }

    private void updateAnimationTick() {
        aniTick++;
        if (aniTick >= aniSpeed) {
            aniTick = 0;
            aniIndex++;
            if (aniIndex >= Constants.PlayerConstants.GetSpriteAmount(playerAction)) {
                aniIndex = 0;
                attacking = false;
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

        if (attacking) playerAction = Constants.PlayerConstants.ATTACK_1;

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

        if (right)
            xSpeed += playerSpeed;
        if (left)
            xSpeed -= playerSpeed;

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

    // animating player
    private void loadAnimations() {
        BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.PLAYER_ATLAS);

        animations = new BufferedImage[9][6];
        for (int i = 0; i < animations.length; i++) {
            for (int j = 0; j < animations[i].length; j++) {
                animations[i][j] = img.getSubimage(j * 64, i * 40, 64, 40);
            }
        }
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
}
