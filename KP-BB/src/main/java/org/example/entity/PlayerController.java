package org.example.entity;

public interface PlayerController {
    boolean isUP();
    boolean isLP();
    boolean isRP();
    boolean isShot();
    void setX(float x);
    void setY(float y);
    void setOnGround(boolean onGround);
    void update();
}