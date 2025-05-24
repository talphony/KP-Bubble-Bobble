package org.example.collision;

import java.awt.*;

public class Barrier {
    public int x, y, width, height;
    public Rectangle hitBox;
    public boolean canJump;

    public Barrier(int x, int y, int width, int height, boolean canJump) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        hitBox = new Rectangle(x, y, width, height);
        this.canJump =  canJump;
    }
    public void draw(Graphics g2) {
        if (canJump) {
            g2.setColor(new Color(255, 30, 82));
        } else {
            g2.setColor(new Color(207, 207, 207));
        }
        g2.fillRect(x, y, width, height);
    }
}
