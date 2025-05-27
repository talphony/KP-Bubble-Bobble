package example.collision;


import org.example.collision.calc;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
class calcTest {
    @Test
    void sum() {
        calc c = new calc();
        int sum = c.sum(6,5);
        Assertions.assertEquals(11,sum);
    }
}
