package org.example.control;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.entity.Player;
import java.util.Random;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor

public class BotControl extends Control {
    private float x, y;
    private int actionTimer;
    private int currentAction = 0;
    private boolean onGround;
    private boolean jumpReady = true;
    private boolean wKeyPressed = false;
    private Player target;
    private final Random random = new Random(System.nanoTime());


    public BotControl() {
        this.actionTimer = 10 + random.nextInt(50);
    }

    public void update() {
        actionTimer--;
        if (actionTimer <= 0) {
            currentAction = makeDecision();
            actionTimer = 30 + random.nextInt(70);
        }
        applyAction();
    }

    public int makeDecision() {
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

    public void applyAction() {
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