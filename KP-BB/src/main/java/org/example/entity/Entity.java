package org.example.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.collision.Barrier;
import org.example.control.Control;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class Entity {
    protected int x, y;
    protected int width, height;
    protected int moveSpeed = 3;
    protected float verticalSpeed = 0;
    protected float gravity = 0.5f;
    protected float jumpForce  = -12f;
    public boolean onGround = false;
    protected boolean isAlive;
    protected BufferedImage left1, right1;
    protected String direction = "right";
    protected BufferedImage currentImage;
    public Rectangle hitBox;

    protected Entity(int x, int y, int width, int height, boolean isAlive) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.isAlive =  isAlive;
        this.height = height;
        this.hitBox = new Rectangle(x, y, width, height);
    }


    public boolean isAlive() {
        return isAlive;
    }

    public void horizontalCollision(ArrayList<Barrier> barriers) {
        if (barriers == null) return;

        for (Barrier barrier : barriers) {
            if (barrier != null && barrier.getHitBox() != null && hitBox.intersects(barrier.getHitBox())) {
                if (x + width / 2 < barrier.getX() + barrier.getWidth() / 2) {
                    x = barrier.getX() - width;
                } else {
                    x = barrier.getX() + barrier.getWidth();
                }
                updateHitBox();
            }
        }
    }

    public void verticalCollision(ArrayList<Barrier> barriers) {
        onGround = false;
        for (Barrier barrier : barriers) {
            if (hitBox.intersects(barrier.hitBox)) {
                if (barrier.canJump) { //кенджамп
                    if (y > barrier.getY()) {
                        y = barrier.getY() - height;
                        verticalSpeed = 0;
                        onGround = true;
                    } else if (y < barrier.getY()) {
                        y = barrier.getY() - height;
                        verticalSpeed = 0;
                        onGround = true;
                    }
                } else {
                    if (y < barrier.getY()) { // непроходимые платформы
                        y = barrier.getY() - height; // верх
                        verticalSpeed = 0;
                        onGround = true;
                    } else {
                        y = barrier.getY() + barrier.getHeight(); // низ
                        verticalSpeed = 0;
                    }
                }
                updateHitBox();
            }
        }
    }

    public void updateHitBox() {
        hitBox.setLocation(x, y);
    }

    protected void gravity() {
        if (!onGround) {
            verticalSpeed += gravity;
            y += verticalSpeed;
        }
    }
    public void horizontalMovement(Control control) {
        if (control.LP) x -= moveSpeed;
        if (control.RP) x += moveSpeed;
    }
    public void verticalMovement(Control control) {
        if (control.UP && onGround) {
            verticalSpeed = jumpForce;
            onGround = false;
            control.UP = false;
        }
        gravity();
    }
    public void updateDirection(Control control) {
        if (control.LP) {
            direction = "left";
            currentImage = left1;
        } else if (control.RP) {
            direction = "right";
            currentImage = right1;
        }
    }
}