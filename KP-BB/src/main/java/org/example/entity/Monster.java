package org.example.entity;

import org.example.collision.Barrier;
import org.example.control.BotControl;
import org.example.main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class Monster extends Entity {
    private boolean isTrapped = false;
    private int trappedTimer = 0;
    private boolean escapedFromBubble = false;

    public Monster(BotControl botControl, int x, int y, int width, int height, boolean isAlive) {
        super(x, y, width, height, isAlive);
        playerImage();
        currentImage = right1;
    }

    public void kill() {
        isAlive = false;
    }

    public boolean getIsTrapped() {
        return isTrapped;
    }

    public void setIsTrapped(boolean isTrapped) {
        this.isTrapped = isTrapped;
        if (!isTrapped) {
            escapedFromBubble = true; // Монстр сбежал из пузыря
        }
    }
    public boolean hasEscapedFromBubble() {
        return escapedFromBubble;
    }

    public void update(ArrayList<Barrier> barriers, BotControl botControl, Player player,  GamePanel gamePanel) {
        if (isTrapped) {
            trappedTimer++;
            return; // Ничего не делаем, пока монстр в пузыре
        }
        if (!isAlive) {
            gamePanel.addFruit(new Fruit(x, y));
            return;
        }
        botControl.x = x;
        botControl.y = y;
        botControl.setOnGround(onGround);
        botControl.setTarget(player);
        killPlayer(player);
        botControl.update();

        horizontalMovement(botControl);
        updateDirection(botControl);
        updateHitBox();
        horizontalCollision(barriers);


        verticalMovement(botControl);
        updateDirection(botControl);
        updateHitBox();
        verticalCollision(barriers);
    }

    public void draw(Graphics gr) {
        if (isTrapped) {
//            gr.setColor(new Color(127, 255, 237));
//            gr.fillOval(x-5, y-5, width+10, height+10);
        } else if (isAlive) {
//            gr.setColor(new Color(255, 0, 0));
//            gr.fillRect(hitBox.x, hitBox.y, hitBox.width, hitBox.height);
            gr.drawImage(currentImage, x, y, null);
        }
    }

    private void playerImage() {
        try {
            left1 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/NagievLeft.png")));
            right1 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/NagievRight.png")));
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

    private void killPlayer(Player player) {
        if (player.hitBox.intersects(this.hitBox)) {
            player.isAlive = false;
            player.respawn();
        }
    }
}