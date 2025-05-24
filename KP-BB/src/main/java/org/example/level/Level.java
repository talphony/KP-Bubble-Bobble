package org.example.level;

import org.example.collision.Barrier;
import java.util.ArrayList;

public class Level {
    private final ArrayList<Barrier> barriers;

    public Level(int screenWidth, int screenHeight) {
        this.barriers = new ArrayList<>();
        generateLevel(screenWidth, screenHeight);
    }

    public ArrayList<Barrier> getBarriersOut() {
        return barriers;
    }

    private void generateLevel(int width, int height) {
        createBoundaries(width, height);
    }

    private void createBoundaries(int width, int height) {
        int thickness = 20;
        barriers.add(new Barrier(0, 0, thickness, height, false));
        barriers.add(new Barrier(width - thickness, 0, thickness, height, false));
        barriers.add(new Barrier(0, 0, width, thickness, false));
        barriers.add(new Barrier(0, height - thickness, width, thickness, false));

        barriers.add(new Barrier(20, 499, 64, 25, true));
        barriers.add(new Barrier(20, 378, 64, 25, true));
        barriers.add(new Barrier(20, 257, 64, 25, true));

        barriers.add(new Barrier(684, 499, 64, 25, true));
        barriers.add(new Barrier(684, 378, 64, 25, true));
        barriers.add(new Barrier(684, 257, 64, 25, true));

        barriers.add(new Barrier(158, 499, 452, 25, true));
        barriers.add(new Barrier(158, 378, 452, 25, true));
        barriers.add(new Barrier(158, 257, 452, 25, true));
        //(height-25 [y]) - max(595), min(20)
    }
}