package ui;

import utilz.Constants;
import utilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;


public class VolumeButton extends PauseButton{

    private BufferedImage[] imgs;
    private BufferedImage slider;
    private int index = 0;
    private boolean mouseOver, mousePressed;
    private int buttonX,minX,maxX;

    public VolumeButton(int x, int y, int width, int height) {
        super(x + width/2, y, Constants.UI.VolumeButtons.VOLUME_WIDTH, height);
        bounds.x-= Constants.UI.VolumeButtons.VOLUME_WIDTH /2;
        buttonX = x + width/2;
        this.x = x;
        this.width = width;
        minX = x+ Constants.UI.VolumeButtons.VOLUME_WIDTH /2;
        maxX = x + width- Constants.UI.VolumeButtons.VOLUME_WIDTH /2;
        loadImgs();
    }

    private void loadImgs() {
        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.VOLUME_BUTTONS);
        imgs = new BufferedImage[3];
        for(int i = 0; i < imgs.length; i++)
            imgs[i] = temp.getSubimage(i * Constants.UI.VolumeButtons.VOLUME_WIDTH_DEFAULT,
                    0,
                    Constants.UI.VolumeButtons.VOLUME_WIDTH_DEFAULT,
                    Constants.UI.VolumeButtons.VOLUME_HEIGHT_DEFAULT);

        slider = temp.getSubimage(3 * Constants.UI.VolumeButtons.VOLUME_WIDTH_DEFAULT,
                0,
                Constants.UI.VolumeButtons.SLIDER_WIDTH_DEFAULT,
                Constants.UI.VolumeButtons.VOLUME_HEIGHT_DEFAULT);
    }

    public void update(){
        index = 0;
        if(mouseOver)
            index = 1;
        if(mousePressed)
            index = 2;
    }

    public void draw(Graphics g){
        g.drawImage(slider, x,y,width,height, null);
        g.drawImage(imgs[index],
                buttonX - Constants.UI.VolumeButtons.VOLUME_WIDTH /2,
                y,
                Constants.UI.VolumeButtons.VOLUME_WIDTH,
                height,
                null);
    }

    public void changeX(int x){
        if(x<minX)
            buttonX = minX;
        else buttonX = Math.min(x, maxX);

        bounds.x = buttonX - Constants.UI.VolumeButtons.VOLUME_WIDTH /2;
    }

    public void resetBools(){
        mouseOver = false;
        mousePressed = false;
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
}
