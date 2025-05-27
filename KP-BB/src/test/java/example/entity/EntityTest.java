package example.entity;

import org.example.collision.Barrier;
import org.example.control.Control;
import org.example.entity.Entity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class EntityTest {

    private Control control;
    private BufferedImage left1;
    private BufferedImage right1;
    private Player player;
    private ArrayList<Barrier> barriers;

    @BeforeEach
    void setUp() {
        // ручной мок Control
        control = new Control() {
            public boolean LP;
            public boolean RP;
            public boolean UP;
        };

        left1 = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);
        right1 = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);

        player = new Player(100, 100, 32, 32, true);
        player.setLeft1(left1);
        player.setRight1(right1);
        player.setCurrentImage(right1);

        barriers = new ArrayList<>();
    }

    @Test // горизонтальная коллизия
    void testHorizontalCollisionLeft() {
        Barrier barrier = new Barrier(120, 100, 32, 32, false);
        barriers.add(barrier);

        player.setX(110);
        player.updateHitBox();

        player.horizontalCollision(barriers);

        assertEquals(88, player.getX()); // 120 - 32 = 88
        assertEquals(new Rectangle(88, 100, 32, 32), player.getHitBox());
    }
    @Test
    void testHorizontalCollisionRight() {
        Barrier barrier = new Barrier(80, 100, 32, 32, false); // Барьер слева от игрока
        barriers.add(barrier);

        player.setX(100); // игрок движется вправо и сталкивается с барьером
        player.updateHitBox();

        player.horizontalCollision(barriers);

        assertEquals(80 + 32, player.getX());
        assertEquals(new Rectangle(112, 100, 32, 32), player.getHitBox());
    }


    @Test //запрыгивание на платформу
    void testVerticalCollisionJumpPlatform() {
        // нет столкновений
        Barrier barrier = new Barrier(200, 200, 50, 20, false);
        barriers.add(barrier);

        player.setY(100);
        player.setVerticalSpeed(2);
        player.setOnGround(false);
        player.updateHitBox();

        float initialSpeed = player.getVerticalSpeed();
        int initialY = player.getY();
        boolean initialGroundState = player.isOnGround();

        player.verticalCollision(barriers);

        assertEquals(initialY, player.getY()); //позиция Y не должна измениться
        assertEquals(initialSpeed, player.getVerticalSpeed()); //cкорость не должна измениться
        assertEquals(initialGroundState, player.isOnGround()); //cостояние onGround не должно измениться
    }



    //verticalMovement
    @Test
    void testVerticalMovementJumpOnGround() {
        player.setOnGround(true);
        control.UP = true;

        player.verticalMovement(control);

        assertEquals(player.getJumpForce(), player.getVerticalSpeed(), 0.5f); //должна быть применена сила прыжка
        assertFalse(player.isOnGround()); // игрок больше не должен быть на земле после прыжка
    }

    //horizontalMovement
    @Test // влево
    void testHorizontalMovementPressLeft() {
        // Подготовка
        control.LP = true;
        int initialX = player.getX();

        player.horizontalMovement(control);

        assertEquals(initialX - player.getMoveSpeed(), player.getX());
    }

    @Test // вправо
    void testHorizontalMovementPressRight() {
        control.RP = true;
        int initialX = player.getX();

        player.horizontalMovement(control);

        assertEquals(initialX + player.getMoveSpeed(), player.getX());
    }

    @Test // если клавиши не нажаты
    void testHorizontalMovementNonPress() {
        control.LP = false;
        control.RP = false;
        int initialX = player.getX();

        player.horizontalMovement(control);

        assertEquals(initialX, player.getX());
    }

    @Test   // если нажаты обе клавиши
    void testHorizontalMovementPressAll() {
        control.LP = true;
        control.RP = true;
        int initialX = player.getX();

        player.horizontalMovement(control);

        assertEquals(initialX, player.getX());
    }

    @Test
    void testUpdateDirectionDirectionRight() {
        control.RP = true;
        player.updateDirection(control);

        assertEquals("right", player.getDirection());
        assertEquals(right1, player.getCurrentImage());
    }

    @Test
    void testUpdateDirectionDirectionLeft() {
        control.LP = true;
        player.updateDirection(control);

        assertEquals("left", player.getDirection());
        assertEquals(left1, player.getCurrentImage());
    }

    @Test //должна быть применена гравитация
    void testVerticalMovementApplyGrav() {
        player.setOnGround(false);
        control.UP = false;
        float initialSpeed = player.getVerticalSpeed();

        player.verticalMovement(control);

        assertEquals(initialSpeed + player.getGravity(), player.getVerticalSpeed(), 0.01f);
    }

    @Test //проверка отсутствия гравитации на земле
    void testVerticalMovementOnGroundJump() {
        player.setOnGround(true);
        control.UP = false;
        float initialSpeed = player.getVerticalSpeed();

        player.verticalMovement(control);

        assertEquals(initialSpeed, player.getVerticalSpeed(), 0.01f);
    }

    static class Player extends Entity {
        public Player(int x, int y, int width, int height, boolean isAlive) {
            super(x, y, width, height, isAlive);
        }
    }
}