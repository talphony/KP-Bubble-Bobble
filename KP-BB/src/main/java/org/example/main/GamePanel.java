package org.example.main;

import org.example.control.BotControl;
import org.example.control.BotControlForPlayer;
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
public class GamePanel extends JPanel implements Runnable {

    final int origSizeObj = 16;
    final int scaleOrigSizeObj = 4;
    public final int totalSizeObj = origSizeObj * scaleOrigSizeObj;
    final int widthSizePole = 12;
    final int heightSizePole = 10;
    final int sizeWidthPix = totalSizeObj * widthSizePole;
    final int sizeHeightPix = totalSizeObj * heightSizePole;
    int FPS = 75;
    Thread RunTime;


    private int score = 0;
    private boolean gameOver = false;

    public final ArrayList<Fruit> fruits = new ArrayList<>();
    public final ArrayList<Monster> monsters = new ArrayList<>();
    private final ArrayList<BotControl> botControls = new ArrayList<>();

    public final Level level;

    private final Player player;

    BotControlForPlayer botControlForPlayer = new BotControlForPlayer();
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

        level = new Level(sizeWidthPix, sizeHeightPix);


        player = new Player(control, 20, 550, 48, 48, true, 3, FPS * 3);

        monsters.add(new Monster(botControl, 500, 550, 48, 48, true));
        monsters.add(new Monster(botControl1, 400, 400, 48, 48, true));
        monsters.add(new Monster(botControl2, 400, 400, 48, 48, true));
        monsters.add(new Monster(botControl3, 400, 400, 48, 48, true));

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
        if (gameOver) return;

        player.update(level.getBarriersOut(), control, this);
        if (player.getLives() <= 0) {
            gameOver = true;
            return;
        }
        updateFruits();
        updateMonsters();
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
                m.update(level.getBarriersOut(), botControls.get(i), player, this); // Передаем GamePanel

                if (!m.isAlive()) {
                    score += 100;
                    monsters.remove(i);
                    botControls.remove(i);
                    i--;
                }
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
        if (gameOver) {
            grp.setColor(Color.RED);
            grp.setFont(new Font("Arial", Font.BOLD, 50));
            grp.drawString("Игра окончена", getWidth() / 2 - 150, getHeight() / 2);
        }
        grp.dispose();
    }
}
