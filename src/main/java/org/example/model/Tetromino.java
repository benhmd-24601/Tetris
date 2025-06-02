package org.example.model;

import org.example.config.ConfigManager;

import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Represents a Tetromino with type, current rotation and position.
 * The static SHAPES map holds the 4 rotation‐matrices for each ShapeType.
 */
public class Tetromino {
    private static final Map<ShapeType, int[][][]> SHAPES = Map.of(
            ShapeType.I, new int[][][]{
                    // Rotation 0
                    {{1, 1, 1, 1}},
                    // Rotation 1
                    {{1}, {1}, {1}, {1}},
                    // Rotation 2
                    {{1, 1, 1, 1}},
                    // Rotation 3
                    {{1}, {1}, {1}, {1}}
            },
            ShapeType.O, new int[][][]{
                    {{1, 1}, {1, 1}}, {{1, 1}, {1, 1}}, {{1, 1}, {1, 1}}, {{1, 1}, {1, 1}}
            },
            ShapeType.T, new int[][][]{
                    // up
                    {{0, 1, 0},
                            {1, 1, 1},
                            {0, 0, 0}},
                    // right
                    {{0, 1, 0},
                            {0, 1, 1},
                            {0, 1, 0}},
                    // down
                    {{0, 0, 0},
                            {1, 1, 1},
                            {0, 1, 0}},
                    // left
                    {{0, 1, 0},
                            {1, 1, 0},
                            {0, 1, 0}}
            },
            ShapeType.S, new int[][][]{
                    // horizontal
                    {{0, 1, 1},
                            {1, 1, 0},
                            {0, 0, 0}},
                    // vertical
                    {{0, 1, 0},
                            {0, 1, 1},
                            {0, 0, 1}},
                    // horizontal
                    {{0, 1, 1},
                            {1, 1, 0},
                            {0, 0, 0}},
                    // vertical
                    {{0, 1, 0},
                            {0, 1, 1},
                            {0, 0, 1}}
            },
            ShapeType.Z, new int[][][]{
                    // horizontal
                    {{1, 1, 0},
                            {0, 1, 1},
                            {0, 0, 0}},
                    // vertical
                    {{0, 0, 1},
                            {0, 1, 1},
                            {0, 1, 0}},
                    // horizontal
                    {{1, 1, 0},
                            {0, 1, 1},
                            {0, 0, 0}},
                    // vertical
                    {{0, 0, 1},
                            {0, 1, 1},
                            {0, 1, 0}}
            },
            ShapeType.J, new int[][][]{
                    // up
                    {{1, 0, 0},
                            {1, 1, 1},
                            {0, 0, 0}},
                    // right
                    {{0, 1, 1},
                            {0, 1, 0},
                            {0, 1, 0}},
                    // down
                    {{0, 0, 0},
                            {1, 1, 1},
                            {0, 0, 1}},
                    // left
                    {{0, 1, 0},
                            {0, 1, 0},
                            {1, 1, 0}}
            },
            ShapeType.L, new int[][][]{
                    // up
                    {{0, 0, 1},
                            {1, 1, 1},
                            {0, 0, 0}},
                    // right
                    {{0, 1, 0},
                            {0, 1, 0},
                            {0, 1, 1}},
                    // down
                    {{0, 0, 0},
                            {1, 1, 1},
                            {1, 0, 0}},
                    // left
                    {{1, 1, 0},
                            {0, 1, 0},
                            {0, 1, 0}}
            }
    );

    private final ShapeType type;
    private final Position position;
    private int rotation;

    public Tetromino(ShapeType type, Position start) {
        this.type = type;
        this.position = start;
        this.rotation = 0;
    }

    public ShapeType getType() {
        return type;
    }

    public Position getPosition() {
        return position;
    }

    /**
     * @return the current rotation matrix for this tetromino
     */
    public int[][] getMatrix() {
        return SHAPES.get(type)[rotation];
    }

    // movement
    public void moveDown() {
        position.setY(position.getY() + 1);
    }

    public void moveUp() {
        position.setY(position.getY() - 1);
    }

    public void moveLeft() {
        position.setX(position.getX() - 1);
    }

    public void moveRight() {
        position.setX(position.getX() + 1);
    }

    /**
     * rotate clockwise (one step among 0..3)
     */
    public void rotate() {
        rotation = (rotation + 1) % 4;
    }

    /**
     * Factory: spawn a random tetromino (from types listed in config),
     * centered at top of the board.
     */
    public static Tetromino spawnRandom(int boardCols) {
        var cfg = ConfigManager.getInstance().config();
        List<ShapeType> types = cfg.getTetrominoTypes().stream()
                .map(ShapeType::valueOf)
                .toList();
        ShapeType t = types.get(new Random().nextInt(types.size()));
        int startX = boardCols / 2 - 2;
        return new Tetromino(t, new Position(startX, 0));
    }
}