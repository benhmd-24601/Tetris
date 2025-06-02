package org.example.config;

import java.util.List;

public class GameConfig {
    private int rows;
    private int cols;
    private int speed;
    private boolean allowRotation;

    // فیلد window حذف شد:
    // private WindowConfig window;

    private List<String> tetrominoTypes;

    // فقط لیست رزولوشن‌ها باقی می‌ماند:
    private List<Resolution> resolutions;

    // getter/setter برای rows:
    public int getRows() {
        return rows;
    }
    public void setRows(int rows) {
        this.rows = rows;
    }

    // getter/setter برای cols:
    public int getCols() {
        return cols;
    }
    public void setCols(int cols) {
        this.cols = cols;
    }

    // getter/setter برای speed:
    public int getSpeed() {
        return speed;
    }
    public void setSpeed(int speed) {
        this.speed = speed;
    }

    // getter/setter برای allowRotation:
    public boolean isAllowRotation() {
        return allowRotation;
    }
    public void setAllowRotation(boolean allowRotation) {
        this.allowRotation = allowRotation;
    }

    // getter/setter برای tetrominoTypes:
    public List<String> getTetrominoTypes() {
        return tetrominoTypes;
    }
    public void setTetrominoTypes(List<String> tetrominoTypes) {
        this.tetrominoTypes = tetrominoTypes;
    }

    // getter/setter برای resolutions:
    public List<Resolution> getResolutions() {
        return resolutions;
    }
    public void setResolutions(List<Resolution> resolutions) {
        this.resolutions = resolutions;
    }
}
