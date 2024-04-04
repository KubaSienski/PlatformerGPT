package ui;

import gamestates.GameState;
import gamestates.Playing;
import main.Game;
import utilz.Constants;
import utilz.LoadSave;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;


public class LevelCompletedOverlay {
    private final Playing playing;
    private UrmButton menu, next;
    private BufferedImage img;

    private int bgX, bgY, bgW, bgH;

    public LevelCompletedOverlay(Playing playing){
        this.playing = playing;
        initImg();
        initButtons();
    }

    private void initButtons() {
        int menuX = (int) (330 * Game.SCALE);
        int nextX = (int) (445 * Game.SCALE);
        int y = (int) (195 * Game.SCALE);
        next = new UrmButton(nextX, y,
                Constants.UI.URMButtons.URM_SIZE, Constants.UI.URMButtons.URM_SIZE,
                0);
        menu = new UrmButton(menuX, y,
                Constants.UI.URMButtons.URM_SIZE, Constants.UI.URMButtons.URM_SIZE,
                2);
    }

    private void initImg() {
        img  = LoadSave.GetSpriteAtlas(LoadSave.COMPLETED_SPRITE);
        bgW = (int)(img.getWidth() * Game.SCALE);
        bgH = (int)(img.getHeight() * Game.SCALE);
        bgX = (Game.GAME_WIDTH - bgW) / 2;
        bgY = (int) (72 * Game.SCALE);
    }

    public void update(){
        next.update();
        menu.update();
    }

    public void draw(Graphics g){
        g.drawImage(img, bgX, bgY, bgW, bgH, null);
        next.draw(g);
        menu.draw(g);
    }

    private boolean isIn(UrmButton b, MouseEvent e){
        return b.getBounds().contains(e.getX(), e.getY());
    }

    public void mouseMoved(MouseEvent e){
        next.setMouseOver(false);
        menu.setMouseOver(false);

        if(isIn(next, e))
            next.setMouseOver(true);
        if(isIn(menu, e))
            menu.setMouseOver(true);
    }

    public void mouseReleased(MouseEvent e){
        if(isIn(next, e))
            if(next.isMousePressed())
                playing.loadNextLevel();
        if(isIn(menu, e))
            if(menu.isMousePressed()) {
                playing.resetAll();
                GameState.state = GameState.MENU;
            }

        menu.resetBools();
        next.resetBools();
    }

    public void mousePressed(MouseEvent e){
        if(isIn(next, e))
            next.setMousePressed(true);
        if(isIn(menu, e))
            menu.setMousePressed(true);
    }
}
