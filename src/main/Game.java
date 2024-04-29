package main;

import gamestates.GameState;
import gamestates.Menu;
import gamestates.Playing;

import java.awt.*;

public class Game implements Runnable {

    private final GameWin gameWin;
    private final GamePanel gamePanel;
    private Thread gameThread;
    private final int FPS_SET = 120;
    private final int UPS_SET = 200;

    private Playing playing;
    private Menu menu;

    public static final int TILES_DEFAULT_SIZE = 32;
    public static final float SCALE = 1.5f;
    public static final int TILES_IN_WIDTH = 26;
    public static final int TILES_IN_HEIGHT = 14;
    public static final int TILES_SIZE = (int) (TILES_DEFAULT_SIZE * SCALE);
    public static final int GAME_WIDTH = TILES_SIZE * TILES_IN_WIDTH;
    public static final int GAME_HEIGHT = TILES_SIZE * TILES_IN_HEIGHT;

    public Game() {
        initClasses();
        gamePanel = new GamePanel(this);
        gameWin = new GameWin(gamePanel);
        gamePanel.setFocusable(true);
        gamePanel.requestFocus();
        gamePanel.requestFocusInWindow();
        startGameLoop();
    }

    private void initClasses() {
        menu = new Menu(this);
        playing = new Playing(this);
    }

    private void startGameLoop() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void update() {
        switch (GameState.state) {
            case PLAYING -> {
                playing.update();
            }
            case MENU -> menu.update();
            case OPTIONS -> {
                GameState.state = GameState.MENU;
                menu.update();
                //System.exit(0);
            }
            case QUIT -> System.exit(0);
            default -> {
            }
        }
    }

    public void render(Graphics g) {
        switch (GameState.state) {
            case PLAYING -> {
                playing.draw(g);
            }
            case MENU -> menu.draw(g);
            default -> {
            }
        }
    }

    @Override
    public void run() {
        double timePerFrame = 1000000000.0 / FPS_SET;
        double timePerUpdate = 1000000000.0 / UPS_SET;

        long previousTime = System.nanoTime();

        double deltaU = 0;
        double deltaF = 0;

        //game loop
        while (true) {
            // repainting every frame
            long currentTime = System.nanoTime();

            deltaU += (currentTime - previousTime) / timePerUpdate;
            deltaF += (currentTime - previousTime) / timePerFrame;
            previousTime = currentTime;

            if (deltaU >= 1) {
                update();
                deltaU--;
            }
            if (deltaF >= 1) {
                gamePanel.repaint();
                deltaF--;
            }

        }
    }

    public void windowFocusLost() {
        if(GameState.state == GameState.PLAYING)
            playing.windowFocusLost();
    }
    public Menu getMenu(){return menu;}
    public Playing getPlaying(){return playing;}
}
