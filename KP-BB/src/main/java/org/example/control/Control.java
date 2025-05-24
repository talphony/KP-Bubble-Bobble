package org.example.control;

import org.example.entity.PlayerController;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Control implements KeyListener, PlayerController {
    public boolean UP, LP, RP;
    public boolean shot = false;

    private boolean wKeyPressed = false;
    private boolean jumpReady = true;
    private boolean shiftKeyPressed = false;
    private boolean shotReady = true;
    private boolean canShoot = true;

    // Поля для совместимости с BotControlForPlayer
    private float x, y;
    private boolean onGround;

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        int i = e.getKeyCode();

        if (i == KeyEvent.VK_SHIFT && canShoot) {
            if (!shiftKeyPressed && shotReady) {
                shot = true;
                shotReady = false;
                shiftKeyPressed = true;
            }
        }

        if (i == KeyEvent.VK_SPACE) {
            if (!wKeyPressed && jumpReady) {
                UP = true;
                jumpReady = false;
            }
            wKeyPressed = true;
        }

        if (i == KeyEvent.VK_A) {
            LP = true;
        }
        if (i == KeyEvent.VK_D) {
            RP = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int i = e.getKeyCode();

        if (i == KeyEvent.VK_SHIFT) {
            shiftKeyPressed = false;
            shot = false;
            if (!shiftKeyPressed) {
                shotReady = true;
            }
        }

        if (i == KeyEvent.VK_SPACE) {
            wKeyPressed = false;
            UP = false;
            if (!wKeyPressed) {
                jumpReady = true;
            }
        }

        if (i == KeyEvent.VK_A) {
            LP = false;
        }
        if (i == KeyEvent.VK_D) {
            RP = false;
        }
    }

    public void setShootingEnabled(boolean enabled) {
        this.canShoot = enabled;
        if (!enabled) {
            this.shot = false;
            this.shiftKeyPressed = false;
            this.shotReady = true;
        }
    }

    @Override
    public boolean isUP() {
        return UP;
    }

    @Override
    public boolean isLP() {
        return LP;
    }

    @Override
    public boolean isRP() {
        return RP;
    }

    @Override
    public boolean isShot() {
        return shot;
    }

    @Override
    public void setX(float x) {
        this.x = x;
    }

    @Override
    public void setY(float y) {
        this.y = y;
    }

    @Override
    public void setOnGround(boolean onGround) {
        this.onGround = onGround;
    }

    @Override
    public void update() {
        // Для ручного управления обновление не требуется, так как действия обновляются через keyPressed/keyReleased
    }
}