package org.example.main;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.control.BotControl;
import org.example.control.Control;
import org.example.entity.Bubble;
import org.example.level.Level;
import org.example.collision.Barrier;
import org.example.entity.Monster;
import org.example.entity.Player;
import org.example.entity.Fruit;
import javax.swing.JPanel;

import java.awt.*;
import java.util.ArrayList;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor


public class GamePanel extends JPanel implements Runnable {

    private final int origSizeObj = 16;
    private final int scaleOrigSizeObj = 4;
    private final int totalSizeObj = origSizeObj * scaleOrigSizeObj;
    private final int widthSizePole = 12;
    private final int heightSizePole = 10;
    private final int sizeWidthPix = totalSizeObj * widthSizePole;
    private final int sizeHeightPix = totalSizeObj * heightSizePole;
    private int FPS = 75;
    Thread RunTime;

    private int currentLevel = 1;
    private boolean levelTransition = false;
    private long levelTransitionStartTime;
    private static final long LEVEL_TRANSITION_DELAY = 5000;

    private int score = 0;
    private boolean gameOver = false;

    public final ArrayList<Fruit> fruits = new ArrayList<>();
    public final ArrayList<Monster> monsters = new ArrayList<>();
    private final ArrayList<BotControl> botControls = new ArrayList<>();

    private Level level;

    private final Player player;

    private boolean returnToMenu = false;

    Control control = new Control();
    BotControl botControl = new BotControl();
    BotControl botControl1 = new BotControl();
    BotControl botControl2 = new BotControl();
    BotControl botControl3 = new BotControl();

    public GamePanel() {
        this.setPreferredSize(new Dimension(sizeWidthPix, sizeHeightPix));
        this.setBackground(Color.gray);
        this.setDoubleBuffered(true);

        this.addKeyListener(control);
        this.setFocusable(true);

        level = new Level(sizeWidthPix, sizeHeightPix, currentLevel);
        player = new Player(control, 20, 550, 48, 48, true, 3, FPS * 3);

        // Загружаем первый уровень
        loadLevel(currentLevel);
    }

    private void loadLevel(int levelNumber) {
        monsters.clear();
        botControls.clear();

        switch(levelNumber) {
            case 1:
                monsters.add(new Monster(botControl, 500, 550, 48, 48, true));
                monsters.add(new Monster(botControl1, 400, 400, 48, 48, true));
                monsters.add(new Monster(botControl2, 400, 400, 48, 48, true));
                monsters.add(new Monster(botControl3, 400, 400, 48, 48, true));
                break;
            case 2:
                monsters.add(new Monster(botControl, 500, 550, 48, 48, true));
                monsters.add(new Monster(botControl1, 400, 400, 48, 48, true));
                monsters.add(new Monster(botControl2, 400, 400, 48, 48, true));
                monsters.add(new Monster(botControl3, 400, 400, 48, 48, true));
                break;
            case 3:
                monsters.add(new Monster(botControl, 500, 550, 48, 48, true));
                monsters.add(new Monster(botControl1, 400, 400, 48, 48, true));
                monsters.add(new Monster(botControl2, 400, 400, 48, 48, true));
                monsters.add(new Monster(botControl3, 400, 400, 48, 48, true));
                break;
        }
        botControls.add(botControl);
        botControls.add(botControl1);
        botControls.add(botControl2);
        botControls.add(botControl3);


        player.setMonsters(monsters);
    }


    public void addFruit(Fruit fruit) {
        fruits.add(fruit);
    }

    private void update() {
        if (gameOver) {
            if (!returnToMenu) {
                returnToMenu = true;
            }
            return;
        }

        player.update(level.getBarriersOut(), control, this);
        if (player.getLives() <= 0) {
            gameOver = true;
            return;
        }
        updateFruits();
        updateMonsters();
        checkLevelTransition();
    }

    private void updateFruits() {
        for (int i = 0; i < fruits.size(); i++) {
            Fruit fruit = fruits.get(i);
            fruit.update(level.getBarriersOut());

            // Проверяем пересечение только если фрукт можно собрать
            if (fruit.isCollectable() && player.hitBox.intersects(fruit.hitBox)) {
                score += fruit.collect();
                fruits.remove(i);
                i--;
                continue;
            }

            if (!fruit.isAlive()) {
                fruits.remove(i);
                i--;
            }
        }
    }

    private void updateMonsters() {
        if (player.isAlive() && !player.getIsRespawning()) {
            for (int i = 0; i < monsters.size(); i++) {
                Monster m = monsters.get(i);
                m.update(level.getBarriersOut(), botControls.get(i), player, this);

                if (!m.isAlive()) {
                    score += 100;
                    monsters.remove(i);
                    botControls.remove(i);
                    i--;
                }
            }
            if (monsters.isEmpty() && !levelTransition) {
                levelTransition = true;
                levelTransitionStartTime = System.currentTimeMillis();
            }
        }
    }
    private void checkLevelTransition() {
        if (levelTransition) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - levelTransitionStartTime >= LEVEL_TRANSITION_DELAY) {
                currentLevel++;
                level = new Level(sizeWidthPix, sizeHeightPix, currentLevel);
                loadLevel(currentLevel);
                levelTransition = false;
                fruits.clear();
            }
        }
    }

    public void StartGame() {
        RunTime = new Thread(this);
        RunTime.start();
    }

    @Override
    public void run() {
        double Rt = 1000000000 / FPS;
        double Nrt = System.nanoTime() + Rt;

        while (RunTime != null) {
            update();
            repaint();
            try {
                double remTime = Nrt - System.nanoTime();
                remTime = remTime / 1000000;
                if (remTime < 0) {
                    remTime = 0;
                }
                Thread.sleep((long) remTime);
                Nrt += Rt;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void paintComponent(Graphics grp) {
        super.paintComponent(grp);
        for (Barrier barrier : level.getBarriersOut()) {
            barrier.draw(grp);
        }
        for (Monster monster : monsters) {
            monster.draw(grp);
        }
        for (Bubble bubble : player.getBubbles()) {
            bubble.draw(grp);
        }
        for (Fruit fruit : fruits) {
            fruit.draw(grp);
        }
        player.draw(grp);
        grp.setColor(Color.WHITE);
        grp.setFont(new Font("Arial", Font.BOLD, 16));
        grp.drawString("Score: " + score, 10, 40);
        grp.drawString("Level: " + currentLevel, 10, 60);
        if (levelTransition) {
            grp.setColor(new Color(255, 255, 255, 150));
            grp.fillRect(0, 0, getWidth(), getHeight());
            grp.setColor(Color.BLUE);
            grp.setFont(new Font("Arial", Font.BOLD, 50));
            String message = "Level " + (currentLevel + 1);
            grp.drawString(message, getWidth()/2 - 100, getHeight()/2);
        }
        if (gameOver) {
            grp.setColor(Color.RED);
            grp.setFont(new Font("Arial", Font.BOLD, 50));
            grp.drawString("Игра окончена", getWidth() / 2 - 150, getHeight() / 2);
        }
        grp.dispose();
    }
}
