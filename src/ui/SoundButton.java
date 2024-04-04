package ui;

import utilz.Constants;
import utilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;

public class SoundButton extends PauseButton{

    private BufferedImage[][] soundImgs;
    private boolean mouseOver, mousePressed;
    private boolean muted;
    private int rowIndex, columnIndex;

    public SoundButton(int x, int y, int width, int height) {
        super(x, y, width, height);

        loadSoundImgs();
    }

    private void loadSoundImgs() {
        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.SOUND_BUTTONS);
        soundImgs = new BufferedImage[2][3];
        for(int i = 0; i < soundImgs.length; i++)
            for (int j = 0; j < soundImgs[i].length; j++)
                soundImgs[i][j] = temp.getSubimage(j * Constants.UI.PauseButtons.SOUND_SIZE_DEFAULT,
                        i * Constants.UI.PauseButtons.SOUND_SIZE_DEFAULT,
                        Constants.UI.PauseButtons.SOUND_SIZE_DEFAULT,
                        Constants.UI.PauseButtons.SOUND_SIZE_DEFAULT);
    }

    public void update(){
        if(muted)
            rowIndex = 1;
        else rowIndex = 0;

        columnIndex = 0;
        if(mouseOver)
            columnIndex = 1;
        if(mousePressed)
            columnIndex= 2;

    }

    public void resetBools(){
        mouseOver = false;
        mousePressed = false;
    }

    public void draw(Graphics g){
        g.drawImage(soundImgs[rowIndex][columnIndex], x, y, width, height, null);
    }

    public void setMouseOver(boolean mouseOver) {
        this.mouseOver = mouseOver;
    }

    public boolean isMousePressed() {
        return mousePressed;
    }

    public void setMousePressed(boolean mousePressed) {
        this.mousePressed = mousePressed;
    }

    public boolean isMuted() {
        return muted;
    }

    public void setMuted(boolean muted) {
        this.muted = muted;
    }
}
