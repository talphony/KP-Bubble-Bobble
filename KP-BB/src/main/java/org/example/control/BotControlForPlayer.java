package org.example.control;

import org.example.entity.Fruit;
import org.example.entity.Monster;
import org.example.entity.Player;
import org.example.entity.PlayerController;
import org.example.main.GamePanel;

import java.util.ArrayList;
import java.util.Random;

public class BotControlForPlayer implements PlayerController {
    public boolean UP, LP, RP, shot;
    public float x, y;
    private int actionTimer;
    private int currentAction = 0;
    private boolean onGround;
    public boolean jumpReady = true;
    private boolean wKeyPressed = false;
    private boolean shiftKeyPressed = false;
    private boolean shotReady = true;
    private Player player;
    private GamePanel gamePanel;
    private final Random random = new Random(System.nanoTime());

    public BotControlForPlayer() {
        this.actionTimer = 10 + random.nextInt(50);
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setGamePanel(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    @Override
    public void setOnGround(boolean onGround) {
        this.onGround = onGround;
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
    public void update() {
        if (player == null || !player.isAlive() || gamePanel == null) return;

        actionTimer--;
        if (actionTimer <= 0) {
            currentAction = makeDecision();
            actionTimer = 30 + random.nextInt(70);
        }
        applyAction();
    }

    private int makeDecision() {
        if (player == null) {
            return random.nextInt(5);
        }

        Monster nearestMonster = findNearestMonster();
        Fruit nearestFruit = findNearestFruit();

        float distToMonster = nearestMonster != null ? distanceTo(player.x, player.y, nearestMonster.x, nearestMonster.y) : Float.MAX_VALUE;
        float distToFruit = nearestFruit != null ? distanceTo(player.x, player.y, nearestFruit.x, nearestFruit.y) : Float.MAX_VALUE;

        if (distToMonster < distToFruit && nearestMonster != null && distToMonster < 300) {
            if (distToMonster < 200 && shotReady) {
                return 4;
            } else if (Math.abs(distToMonster) > 50) {
                return distToMonster > 0 ? 2 : 1;
            } else if (onGround && random.nextFloat() < 0.3f) {
                return 3;
            }
        } else if (nearestFruit != null && distToFruit < 300) {
            if (Math.abs(distToFruit) > 50) {
                return distToFruit > 0 ? 2 : 1;
            } else if (onGround && random.nextFloat() < 0.3f) {
                return 3;
            }
        }

        if (onGround && random.nextFloat() < 0.5f) {
            return random.nextInt(3);
        } else if (onGround && random.nextFloat() < 0.2f) {
            return 3;
        }
        return 0;
    }

    private void applyAction() {
        UP = false;
        LP = false;
        RP = false;
        shot = false;

        switch (currentAction) {
            case 1:
                LP = true;
                break;
            case 2:
                RP = true;
                break;
            case 3:
                if (!wKeyPressed && jumpReady) {
                    UP = true;
                    jumpReady = false;
                }
                wKeyPressed = true;
                break;
            case 4:
                if (!shiftKeyPressed && shotReady) {
                    shot = true;
                    shotReady = false;
                }
                shiftKeyPressed = true;
                break;
        }

        if (currentAction != 3) {
            wKeyPressed = false;
        }
        if (currentAction != 4) {
            shiftKeyPressed = false;
        }

        if (!wKeyPressed) {
            UP = false;
            jumpReady = true;
        }
        if (!shiftKeyPressed) {
            shot = false;
            shotReady = true;
        }
    }

    private Monster findNearestMonster() {
        ArrayList<Monster> monsters = gamePanel.monsters;
        Monster nearest = null;
        float minDist = Float.MAX_VALUE;
        for (Monster monster : monsters) {
            if (!monster.isAlive()) continue;
            float dist = distanceTo(player.x, player.y, monster.x, monster.y);
            if (dist < minDist) {
                minDist = dist;
                nearest = monster;
            }
        }
        return nearest;
    }

    private Fruit findNearestFruit() {
        ArrayList<Fruit> fruits = gamePanel.fruits;
        Fruit nearest = null;
        float minDist = Float.MAX_VALUE;
        for (Fruit fruit : fruits) {
            if (!fruit.isAlive() || !fruit.isCollectable()) continue;
            float dist = distanceTo(player.x, player.y, fruit.x, fruit.y);
            if (dist < minDist) {
                minDist = dist;
                nearest = fruit;
            }
        }
        return nearest;
    }

    private float distanceTo(int x1, int y1, int x2, int y2) {
        return (float) Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
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
}