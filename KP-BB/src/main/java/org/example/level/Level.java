package org.example.level;

import org.example.collision.Barrier;
import java.util.ArrayList;

public class Level {
    private final ArrayList<Barrier> barriers;
    private int currentLevel;

    public Level(int screenWidth, int screenHeight, int level) {
        this.barriers = new ArrayList<>();
        this.currentLevel = level;
        generateLevel(screenWidth, screenHeight);
    }

    public ArrayList<Barrier> getBarriersOut() {
        return barriers;
    }

    private void generateLevel(int width, int height) {
        createBoundaries(width, height);
        createLevelSpecificBarriers();
    }

    private void createBoundaries(int width, int height) {
        int thickness = 20;
        barriers.add(new Barrier(0, 0, thickness, height, false));
        barriers.add(new Barrier(width - thickness, 0, thickness, height, false));
        barriers.add(new Barrier(0, 0, width, thickness, false));
        barriers.add(new Barrier(0, height - thickness, width, thickness, false));
    }

    private void createLevelSpecificBarriers() {
        switch(currentLevel) {
            case 1:
                barriers.add(new Barrier(20, 499, 64, 25, true));
                barriers.add(new Barrier(20, 378, 64, 25, true));
                barriers.add(new Barrier(20, 257, 64, 25, true));
                barriers.add(new Barrier(684, 499, 64, 25, true));
                barriers.add(new Barrier(684, 378, 64, 25, true));
                barriers.add(new Barrier(684, 257, 64, 25, true));
                barriers.add(new Barrier(158, 499, 452, 25, true));
                barriers.add(new Barrier(158, 378, 452, 25, true));
                barriers.add(new Barrier(158, 257, 452, 25, true));
                break;
            case 2:
                barriers.add(new Barrier(80, 500, 162, 25, true));
                barriers.add(new Barrier(302, 500, 162, 25, true));
                barriers.add(new Barrier(524, 500, 162, 25, true));
                barriers.add(new Barrier(20, 350, 300, 25, true));
                barriers.add(new Barrier(448, 350, 300, 25, true));
                barriers.add(new Barrier(250, 200, 270, 25, true));
                break;
            case 3:
                barriers.add(new Barrier(80, 499, 162, 25, true));
                barriers.add(new Barrier(302, 499, 162, 25, true));
                barriers.add(new Barrier(524, 499, 162, 25, true));
                barriers.add(new Barrier(141, 378, 486, 25, true));
                barriers.add(new Barrier(207, 257, 153, 25, true));
                barriers.add(new Barrier(420, 257, 153, 25, true));
                barriers.add(new Barrier(283, 136, 227, 25, true));
                break;

            //(height-25 [y]) - max(595), min(20)
            //ширина 768
            //высота 640
            // 728
        }
    }
}