package org.example.entity;

import org.example.collision.Barrier;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class Fruit extends Entity {
    private static final int POINTS = 300;
    private boolean collected = false;
    private int spawnTimer = 113; // 1.5 секунды при 75 FPS
    private boolean isCollectable = false;

    public Fruit(int x, int y) {
        super(x, y, 30, 30, true);
        fruitImage();
    }

    private void fruitImage() {
        try {
            BufferedImage original = ImageIO.read(
                    Objects.requireNonNull(getClass().getResourceAsStream("/dengi.png")));
            currentImage = scaleImage(original, width, height);
        } catch (IOException e) {
            createPlaceholderImage();
            e.printStackTrace();
        }


    }
    private void createPlaceholderImage() {
        currentImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = currentImage.createGraphics();
        g2d.setColor(Color.YELLOW);
        g2d.fillOval(0, 0, width, height);
        g2d.dispose();
    }
    private BufferedImage scaleImage(BufferedImage original, int width, int height) {
        BufferedImage scaled = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = scaled.createGraphics();
        g2.drawImage(original, 0, 0, width, height, null);
        g2.dispose();
        return scaled;
    }


    public void update(ArrayList<Barrier> barriers) {
        if (!collected) {
            if (spawnTimer > 0) {
                spawnTimer--;
                if (spawnTimer <= 0) {
                    isCollectable = true;
                }
            }
            gravity();
            updateHitBox();
            verticalCollision(barriers);
            horizontalCollision(barriers);
        }
    }

    public int collect() {
        if (spawnTimer > 0 || collected) {
            return 0; // Нельзя собрать, если не прошло 1.5 секунд или уже собран
        }
        collected = true;
        isAlive = false;
        return POINTS; // Начисляем очки, если фрукт доступен
    }

    public void draw(Graphics g) {
        if (!collected && currentImage != null) {
            g.drawImage(currentImage, x, y, null);
        }
    }
    public boolean isCollectable() {
        return isCollectable;
    }
}