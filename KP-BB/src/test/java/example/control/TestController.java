package example.control;

import org.example.entity.PlayerController;

public class  TestController implements PlayerController {
    public boolean UP, LP, RP, shot;

    @Override
    public boolean isUP() { return UP; }
    @Override
    public boolean isLP() { return LP; }
    @Override
    public boolean isRP() { return RP; }
    @Override
    public boolean isShot() { return shot; }

    @Override
    public void setX(float x) {}
    @Override
    public void setY(float y) {}
    @Override
    public void setOnGround(boolean onGround) {}
    @Override
    public void update() {}
}