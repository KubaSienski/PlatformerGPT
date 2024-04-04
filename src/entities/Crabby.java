package entities;

import main.Game;
import utilz.Constants;

import java.awt.geom.Rectangle2D;

public class Crabby extends Enemy{

    private int attackBoxOffsetX;

    public Crabby(float x, float y) {
        super(x, y, Constants.EnemyConstants.CRABBY_WIDTH, Constants.EnemyConstants.CRABBY_HEIGHT, Constants.EnemyConstants.CRABBY);
        initHitbox(22,19);
        initAttackBox();
    }

    private void initAttackBox() {
        attackBox = new Rectangle2D.Float(x, y, (int)(82 * Game.SCALE), (int)(19 * Game.SCALE));
        attackBoxOffsetX = (int)(30 * Game.SCALE);
    }

    public void update(int[][] lvlData, Player player){
        updateBehaviour(lvlData, player);
        updateAnimationTick();
        updateAttackBox();
    }

    private void updateAttackBox() {
        attackBox.x = hitbox.x - attackBoxOffsetX;
        attackBox.y = hitbox.y;
    }

    private void updateBehaviour(int[][] lvlData, Player player){
        if(firstUpdate)
            firstUpdateCheck(lvlData);
        if(inAir){
            updateInAir(lvlData);
        }else {
            switch (state){
                case Constants.EnemyConstants.IDLE -> newState(Constants.EnemyConstants.RUNNING);
                case Constants.EnemyConstants.RUNNING -> {
                    if(canSeePlayer(lvlData, player)) {
                        turnTowardsPlayer(player);
                        if (isPlayerCloseForAttack(player))
                            newState(Constants.EnemyConstants.ATTACK);
                    }
                    move(lvlData);
                }
                case Constants.EnemyConstants.ATTACK -> {
                    if(aniIndex == 0)
                        attackChecked = false;

                    if(aniIndex == 3 && !attackChecked)
                        checkEnemyHit(attackBox, player);
                }
            }
        }
    }

    public int flipX(){
        if(walkDir == Constants.Directions.RIGHT)
            return width;
        else return 0;
    }
    public int flipW(){
        if(walkDir == Constants.Directions.RIGHT)
            return -1;
        else return 1;
    }

}
