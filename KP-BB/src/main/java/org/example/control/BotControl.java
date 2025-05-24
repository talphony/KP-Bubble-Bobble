package org.example.control;

import org.example.entity.Player;
import java.util.Random;

public class BotControl extends Control {
    public float x, y;
    private int actionTimer;
    private int currentAction = 0;
    private boolean onGround;
    public boolean jumpReady = true; // Добавляем флаг готовности прыжка
    private boolean wKeyPressed = false; // Для эмуляции нажатия/отпускания
    private Player target;
    private final Random random = new Random(System.nanoTime());


    public BotControl() {
        this.actionTimer = 10 + random.nextInt(50);
    }

    public void setTarget(Player player) {
        this.target = player;
    }

//    public void setX(float x) {
//        this.x = x;
//    }
//    public void setY(float y) {
//        this.y = y;
//    }

    public void setOnGround(boolean onGround) {
        this.onGround = onGround;
    }

    public void update() {
        actionTimer--;
        if (actionTimer <= 0) {
            currentAction = makeDecision();
            actionTimer = 30 + random.nextInt(70);
        }
        applyAction();
    }

    private int makeDecision() {
        if (target == null) {
            return random.nextInt(4);
        }

        float distance = target.getX() - x;

        if (random.nextFloat() < 0.9f) {
            if (Math.abs(distance) > 100) {
                return distance > 0 ? 2 : 1;
            } else if (onGround && random.nextFloat() < 0.7f) {
                return 3; // Прыжок
            }
        }
        return random.nextInt(4);
    }

    private void applyAction() {
        // Обработка прыжка (аналогично игроку)
        if (currentAction == 3) {
            if (!wKeyPressed && jumpReady) {
                UP = true;
                jumpReady = false;
            }
            wKeyPressed = true;
        } else {
            wKeyPressed = false;
            UP = false;
            if (!wKeyPressed) {
                jumpReady = true;
            }
        }

        LP = (currentAction == 1);
        RP = (currentAction == 2);
    }
}