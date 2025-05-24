package org.example.entity;

import org.example.main.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.Objects;

public class Bubble {
    public int x, y;
    public int width = 60, height = 60;
    public Rectangle hitBox;
    public boolean isActive = true;
    private final int speed = 3;
    private int direction; // Не final для отскока
    private Monster trappedMonster = null;
    private float floatOffset = 0;
    private int lifeTime = 70; // Увеличено до ~3 секунд для пустого пузыря
    private BufferedImage bubbleImage;
    private BufferedImage bubbleWithMonsterImage;
    private int distanceTraveled = 0;
    private final int maxDistance = 200;
    private boolean isFloatingUp = false;
    private boolean isPopped = false;
    private int popAnimationTimer = 0;
    private final int popAnimationDuration = 15;
    private BufferedImage popAnimationImage;
    private float floatSpeed = 1.5f;
    private int trapTimer = 0;
    private static final int KILL_WINDOW_START = 150;
    private static final int STOP_Y = 50;
    private static final int KILL_WINDOW_END = 525;
    private static final int MAP_WIDTH = 768; // Границы карты
    private static final int MAP_HEIGHT = 640;

    public Bubble(int startX, int startY, int direction) {
        this.x = startX;
        this.y = startY;
        this.direction = direction;
        this.hitBox = new Rectangle(x, y, width, height);
        loadImages();
    }

    private void loadImages() {
        try {
            bubbleImage = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/polovnik.png")));
            bubbleWithMonsterImage = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/deadNagiev.png")));
            popAnimationImage = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Boom.png")));

            bubbleImage = scaleImage(bubbleImage, width, height);
            bubbleWithMonsterImage = scaleImage(bubbleWithMonsterImage, width, height);
            popAnimationImage = scaleImage(popAnimationImage, width, height);
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

    public void update(GamePanel gamePanel) {
        if (handlePopAnimation()) {
            return;
        }

        if (trappedMonster == null) {
            updateEmptyBubble();
        } else {
            updateBubbleWithMonster();
        }
        if (trappedMonster != null && !trappedMonster.isAlive()) {
            gamePanel.addFruit(new Fruit(x, y));
        }

        updateHitBox();
        checkLifeTime();
    }

    private boolean handlePopAnimation() {
        if (!isPopped) {
            return false;
        }
        popAnimationTimer++;
        if (popAnimationTimer >= popAnimationDuration) {
            isActive = false;
            if (trappedMonster != null) {
                trappedMonster.setIsTrapped(false);
                trappedMonster.y += 5;
            }
        }
        return true;
    }

    private void updateEmptyBubble() {
        if (!isFloatingUp) {
            // Горизонтальное движение с волнообразной траекторией
            x += speed * direction;
            distanceTraveled += Math.abs(speed);
            floatOffset += 0.1f;
            y += (int)(Math.sin(floatOffset) * 0.7);

            // Ограничение по горизонтали
            if (x < 0) {
                x = 0;
                direction = 1; // Отскок вправо
            } else if (x + width > MAP_WIDTH) {
                x = MAP_WIDTH - width;
                direction = -1; // Отскок влево
            }

            // Ограничение по вертикали
            if (y < STOP_Y) {
                y = STOP_Y;
            } else if (y + height > MAP_HEIGHT) {
                y = MAP_HEIGHT - height;
            }

            if (distanceTraveled >= maxDistance) {
                isFloatingUp = true;
            }
        } else {
            // Медленное всплытие пустого пузыря
            if (y > STOP_Y) {
                y -= 1;
            }
            if (y <= STOP_Y) {
                y = STOP_Y;
            }
        }
    }

    private void updateBubbleWithMonster() {
        // Всплытие пузыря с монстром
        if (y > STOP_Y) {
            y -= floatSpeed;
        }
        if (y <= STOP_Y) {
            y = STOP_Y;
            floatSpeed = 0;
        }
        // Ограничение по горизонтали
        if (x < 0) {
            x = 0;
        } else if (x + width > MAP_WIDTH) {
            x = MAP_WIDTH - width;
        }
        // Ограничение по нижней границе
        if (y + height > MAP_HEIGHT) {
            y = MAP_HEIGHT - height;
        }

        trappedMonster.x = x;
        trappedMonster.y = y;

        // Увеличение таймера поимки
        trapTimer++;
    }

    private void updateHitBox() {
        hitBox.setLocation(x, y);
    }

    private void checkLifeTime() {
        lifeTime--;
        if (lifeTime <= 0) {
            pop();
        }
    }

    public void draw(Graphics g) {
        if (isPopped) {
            if (popAnimationTimer < popAnimationDuration && popAnimationImage != null) {
                g.drawImage(popAnimationImage, x, y, null);
            }
            return;
        }

        if (trappedMonster == null) {
            if (bubbleImage != null) {
                g.drawImage(bubbleImage, x, y, null);
            }
        } else {
            if (bubbleWithMonsterImage != null) {
                g.drawImage(bubbleWithMonsterImage, x, y, null);
            }
        }
    }

    public boolean trapMonster(Monster monster) {
        if (trappedMonster == null && hitBox.intersects(monster.hitBox)) {
            trappedMonster = monster;
            monster.setIsTrapped(true);
            width = 50;
            height = 50;
            bubbleWithMonsterImage = scaleImage(bubbleWithMonsterImage, width, height);
            hitBox.setSize(width, height);
            isFloatingUp = true;
            lifeTime = 525; // 7 секунд
            trapTimer = 0;
            return true;
        }
        return false;
    }

    public boolean canBeKilled() {
        return trappedMonster != null && trapTimer >= KILL_WINDOW_START && trapTimer <= KILL_WINDOW_END;
    }

    public void pop() {
        if (!isPopped) {
            isPopped = true;
            if (trappedMonster != null) {
                trappedMonster.setIsTrapped(false);
                trappedMonster.y += 5;
            }
        }
    }

    public boolean isPopped() {
        return isPopped;
    }

    public Monster getTrappedMonster() {
        return trappedMonster;
    }

    public boolean containsMonster() {
        return trappedMonster != null;
    }

    public void increaseFloatSpeed(float amount) {
        this.floatSpeed += amount;
    }
}