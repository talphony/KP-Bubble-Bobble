package org.example.entity;

import org.example.collision.Barrier;
import org.example.control.Control;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Entity {
    public int x, y;
    protected int width, height;
    protected int moveSpeed = 3;
    protected float verticalSpeed = 0;
    protected float gravity = 0.5f;
    protected float jumpForce  = -12f;
    protected boolean onGround = false;
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

    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public  int getWidth() {
        return width;
    }
    public  int getHeight() {
        return height;
    }
    public boolean isAlive() {
        return isAlive;
    }

    protected void horizontalCollision(ArrayList<Barrier> barriers) {
        for (Barrier barrier : barriers) {
            if (hitBox.intersects(barrier.hitBox)) {
                if (x + width / 2 < barrier.x + barrier.width / 2) {
                    x = barrier.x - width;
                } else {
                    x = barrier.x + barrier.width;
                }
                updateHitBox();
            }
        }
    }

    protected void verticalCollision(ArrayList<Barrier> barriers) {
        onGround = false;
        for (Barrier barrier : barriers) {
            if (hitBox.intersects(barrier.hitBox)) {
                if (barrier.canJump) { //кенджамп
                    if (y > barrier.y) {
                        y = barrier.y - height;
                        verticalSpeed = 0;
                        onGround = true;
                    } else if (y < barrier.y) {
                        y = barrier.y - height;
                        verticalSpeed = 0;
                        onGround = true;
                    }
                } else {
                    if (y < barrier.y) { // непроходимые платформы
                        y = barrier.y - height; // верх
                        verticalSpeed = 0;
                        onGround = true;
                    } else {
                        y = barrier.y + barrier.height; // низ
                        verticalSpeed = 0;
                    }
                }
                updateHitBox();
            }
        }
    }

    protected void updateHitBox() {
        hitBox.setLocation(x, y);
    }

    protected void gravity() {
        if (!onGround) {
            verticalSpeed += gravity;
            y += verticalSpeed;
        }
    }
    protected void horizontalMovement(Control control) {
        if (control.LP) x -= moveSpeed;
        if (control.RP) x += moveSpeed;
    }
    protected void verticalMovement(Control control) {
        if (control.UP && onGround) {
            verticalSpeed = jumpForce;
            onGround = false;
            control.UP = false;
        }
        gravity();
    }
    protected void updateDirection(Control control) {
        if (control.LP) {
            direction = "left";
            currentImage = left1;
        } else if (control.RP) {
            direction = "right";
            currentImage = right1;
        }
    }

}