package objects;

import main.Game;

import static utilz.Constants.ObjectConstants.*;

public class GameContainer extends GameObject {
    public GameContainer(int x, int y, int objType) {
        super(x, y, objType);
        createHitbox();
    }

    private void createHitbox() {
        if(objType == BOX){
            initHitbox(25,18);
            xDrawOffset = (int) (Game.SCALE * 7);
            yDrawOffset = (int) (Game.SCALE * 12);
        }else {
            initHitbox(23, 25);
            xDrawOffset = (int) (Game.SCALE * 8);
            yDrawOffset = (int) (Game.SCALE * 5);
        }

        hitbox.y += yDrawOffset + (int)(Game.SCALE * 2);
        hitbox.x += (float) xDrawOffset /2;
    }
    public void update(){
        if(doAnimation)
            updateAnimationTick();
    }
}
