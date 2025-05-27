package example.control;

import org.example.control.BotControl;
import org.example.entity.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BotControlTest {
    private BotControl botControl;
    private Player target;
    private TestController testController;

    @BeforeEach
    void setUp() {
        testController = new TestController();
        target = new Player(
                testController, // тестовый контроллер
                200,           // x
                100,           // y
                32,           // width
                32,           // height
                true,          // isAlive
                3,            // lives
                60            // respawnTime
        );
        botControl = new BotControl();
        botControl.setTarget(target);
        botControl.setOnGround(true);
    }

    @Test
    void testDecision() {
        botControl.setX(50); //слева от цели
        int decision = botControl.makeDecision();
        assertEquals(2, decision); //движение вправо
    }

    @Test
    void testApplyJump() {
        botControl.setCurrentAction(3); // прыжок
        botControl.applyAction();
        assertTrue(botControl.isUP());
    }

    @Test
    void testRandomAction() {
        botControl.setTarget(null);
        int decision = botControl.makeDecision();
        assertTrue(decision >= 0 && decision <= 3); // случайное действие 0-3
    }

    @Test
    void testTimerReset() {
        botControl.setActionTimer(1); // таймер на сброс
        botControl.update();
        assertTrue(botControl.getActionTimer() > 1); // таймер
    }

}