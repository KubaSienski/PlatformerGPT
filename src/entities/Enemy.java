package entities;

import main.Game;
import utilz.Constants;
import utilz.HelpMethods;

import java.awt.geom.Rectangle2D;

public abstract class Enemy extends Entity{
    protected int enemyType;
    protected boolean firstUpdate = true;
    protected float walkDir = Constants.Directions.LEFT;
    protected int tileY;
    protected float attackDistance = Game.TILES_SIZE;
    protected boolean active = true;
    protected boolean attackChecked;

    public Enemy(float x, float y, int width, int height, int enemyType) {
        super(x, y, width, height);
        this.enemyType = enemyType;
        this.walkSpeed = 0.35f * Game.SCALE;
        maxHealth = Constants.EnemyConstants.GetMaxHealth(enemyType);
        currentHealth = maxHealth;
    }

    protected void firstUpdateCheck(int[][] lvlData){
        if(!HelpMethods.IsEntityOnFloor(hitbox, lvlData))
            inAir = true;
        firstUpdate = false;
    }

    protected void turnTowardsPlayer(Player player){
        if(player.hitbox.x > hitbox.x)
            walkDir = Constants.Directions.RIGHT;
        else
            walkDir = Constants.Directions.LEFT;
    }

    protected boolean canSeePlayer(int[][] lvlData, Player player){
        int playerTileY = (int) (player.getHitbox().y / Game.TILES_SIZE);
        if(playerTileY == tileY)
            if(isPlayerInRange(player))
                return HelpMethods.IsSightClear(lvlData, hitbox, player.hitbox, tileY);
        return false;
    }

    protected boolean isPlayerInRange(Player player) {
        int absVal = (int) Math.abs(player.hitbox.x - hitbox.x);
        return absVal <= attackDistance * 5;
    }

    protected boolean isPlayerCloseForAttack(Player player) {
        int absVal = (int) Math.abs(player.hitbox.x - hitbox.x);
        return absVal <= attackDistance;
    }

    protected void updateInAir(int[][] lvlData){
        if(HelpMethods.CanMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, lvlData)){
            hitbox.y += airSpeed;
            airSpeed += Constants.GRAVITY;
        }else {
            inAir = false;
            hitbox.y = HelpMethods.GetEntityYPosUnderRoofOrAboveFloor(hitbox, airSpeed);
            tileY = (int) (hitbox.y / Game.TILES_SIZE);
        }
    }

    protected void move(int[][] lvlData){
        float xSpeed;
        if(walkDir == Constants.Directions.LEFT)
            xSpeed = -walkSpeed;
        else
            xSpeed = walkSpeed;

        if(HelpMethods.CanMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, lvlData))
            if(HelpMethods.IsFloor(hitbox, xSpeed, lvlData)){
                hitbox.x += xSpeed;
                return;
            }
        changeWalkDir();
    }

    protected void newState(int enemyState){
        this.state = enemyState;
        aniTick = 0;
        aniIndex = 0;
    }

    public void hurt(int amount){
        currentHealth -= amount;
        if(currentHealth <= 0)
            newState(Constants.EnemyConstants.DEAD);
        else
            newState(Constants.EnemyConstants.HIT);
    }

    protected void checkEnemyHit(Rectangle2D.Float attackBox, Player player) {
        if(attackBox.intersects(player.hitbox))
            player.changeHealth(-Constants.EnemyConstants.GetEnemyDmg(enemyType));
        attackChecked = true;
    }

    protected void updateAnimationTick(){
        aniTick++;
        if (aniTick >= Constants.ANI_SPEED){
            aniTick = 0;
            aniIndex++;
            if(aniIndex >= Constants.EnemyConstants.GetSpriteAmount(enemyType, state)){
                aniIndex = 0;

                switch (state){
                    case Constants.EnemyConstants.ATTACK, Constants.EnemyConstants.HIT
                            -> state = Constants.EnemyConstants.IDLE;
                    case Constants.EnemyConstants.DEAD -> active = false;
                }
            }
        }
    }

    protected void changeWalkDir() {
        if(walkDir == Constants.Directions.LEFT)
            walkDir = Constants.Directions.RIGHT;
        else
            walkDir = Constants.Directions.LEFT;
    }

    public void resetEnemy(){
        hitbox.x = x;
        hitbox.y = y;
        firstUpdate = true;
        currentHealth = maxHealth;
        newState(Constants.EnemyConstants.IDLE);
        active = true;
        airSpeed = 0;
    }

    public boolean isActive() {
        return active;
    }
}
