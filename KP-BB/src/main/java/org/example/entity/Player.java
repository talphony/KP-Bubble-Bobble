package org.example.entity;

import org.example.collision.Barrier;
import org.example.control.Control;
import org.example.main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import static java.util.Objects.*;

public class Player extends Entity {
    private final PlayerController controller;
    private int lives;
    private final int respawnTime;
    private boolean isRespawning = false;
    private int respawnTimer = 0;
    private ArrayList<Monster> monsters = new ArrayList<>();
    private final ArrayList<Bubble> bubbles = new ArrayList<>();
    private int bubbleCooldown = 0;
    private final int bubbleCooldownTime = 75;

    public boolean getIsRespawning() {
        return isRespawning;
    }

    public Player(PlayerController controller, int x, int y, int width, int height, boolean isAlive, int lives, int respawnTime) {
        super(x, y, width, height, isAlive);
        this.controller = controller;
        this.lives = lives;
        this.respawnTime = respawnTime;
        playerImage();
        currentImage = right1;
    }

    public void setMonsters(ArrayList<Monster> monsters) {
        this.monsters = monsters;
    }


    public int getLives() {
        return lives;
    }

    public void respawn() {
        if (lives > 0) {
            lives--;
            isAlive = true;
            isRespawning = true;
            respawnTimer = respawnTime;
            this.x = 20;
            this.y = 550;
            updateHitBox();
        }
    }

    public void update(ArrayList<Barrier> barriers, Control control, GamePanel gamePanel) {
        control.setShootingEnabled(!isRespawning);

        horizontalMovement(control);
        updateDirection(control);
        updateHitBox();
        horizontalCollision(barriers);

        verticalMovement(control);
        updateDirection(control);
        updateHitBox();
        verticalCollision(barriers);

        for (int i = 0; i < bubbles.size(); i++) {
            Bubble bubble = bubbles.get(i);
            bubble.update(gamePanel); // Передаем gamePanel в update

            if (!bubble.isActive) {
                bubbles.remove(i);
                i--;
            }
        }

        if (isRespawning) {
            respawnTimer--;
            if (respawnTimer <= 0) {
                isRespawning = false;
            }
        }
        if (bubbleCooldown > 0) {
            bubbleCooldown--;
        }

        if (control.shot && bubbleCooldown == 0) {
            shootBubble();
            bubbleCooldown = bubbleCooldownTime;
        }

        for (int i = 0; i < bubbles.size(); i++) {
            Bubble bubble = bubbles.get(i);
            bubble.update(gamePanel);

            // Проверка столкновений пузырей с монстрами
            for (Monster monster : monsters) {
                if (monster.isAlive && !monster.getIsTrapped()) {
                    bubble.trapMonster(monster);
                }
            }

            // Проверка столкновения игрока с пузырем, содержащим монстра
            if (bubble.containsMonster() && !bubble.isPopped() && hitBox.intersects(bubble.hitBox) && bubble.canBeKilled()) {
                Monster trappedMonster = bubble.getTrappedMonster();
                if (trappedMonster != null) {
                    trappedMonster.isAlive = false; // Убиваем монстра
                    bubble.pop(); // Лопаем пузырь
                }
            }

            if (!bubble.isActive) {
                bubbles.remove(i);
                i--;
            }

        }
    }


    private void shootBubble() {
        int bubbleDirection = this.direction.equals("right") ? 1 : -1;
        int bubbleX = bubbleDirection > 0 ? x + width : x - 30;
        int bubbleY = y + height/2 - 15;
        bubbles.add(new Bubble(bubbleX, bubbleY, bubbleDirection));
    }

    public void draw(Graphics gr) {
        if (isRespawning) {
            if (respawnTimer % 10 < 5) {
                gr.drawImage(currentImage, x, y, null);
            }
            return;
        }
        for (Bubble bubble : bubbles) {
            bubble.draw(gr);
        }
        gr.drawImage(currentImage, x, y, null);
        gr.setColor(Color.WHITE);
        gr.drawString("Lives: " + lives, 10, 20);
    }

    private void playerImage() {
        try {
            left1 = ImageIO.read(requireNonNull(getClass().getResourceAsStream("/ChefLeft.png")));
            right1 = ImageIO.read(requireNonNull(getClass().getResourceAsStream("/ChefRight.png")));
            left1 = scaleImage(left1, width, height);
            right1 = scaleImage(right1, width, height);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private BufferedImage scaleImage(BufferedImage original, int width, int height) {
        BufferedImage scaled = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = scaled.createGraphics();
        g2.drawImage(original, 0, 0, width, height, null);
        g2.dispose();
        return scaled;
    }

    public ArrayList<Bubble> getBubbles() {
        return bubbles;
    }
}