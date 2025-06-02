package org.example.model;

import org.example.config.ConfigManager;

public class Board {
    private final int rows;
    private final int cols;
    private final boolean[][] grid;

    public Board() {
        var cfg = ConfigManager.getInstance().config();
        this.rows = cfg.getRows();
        this.cols = cfg.getCols();
        this.grid = new boolean[rows][cols];
    }

    public int getRows() { return rows; }
    public int getCols() { return cols; }
    public boolean[][] getGrid() { return grid; }

    public boolean isOccupied(int x, int y) {
        if (x<0||y<0||x>=cols||y>=rows) return true;
        return grid[y][x];
    }

    public void place(Tetromino t) {
        int[][] shape = t.getMatrix();
        var p = t.getPosition();
        for (int r=0; r<shape.length; r++) {
            for (int c=0; c<shape[r].length; c++) {
                if (shape[r][c]==1) {
                    grid[p.getY()+r][p.getX()+c] = true;
                }
            }
        }
    }

    /**
     * Clears full lines and returns how many were removed.
     */
    public int clearFullLines() {
        int cleared=0;
        for (int y=rows-1; y>=0; y--) {
            boolean full=true;
            for (int x=0; x<cols; x++){
                if (!grid[y][x]) { full=false; break; }
            }
            if (full) {
                cleared++;
                System.arraycopy(grid,0,grid,1, y);
                grid[0]=new boolean[cols];
                y++; // recheck this row
            }
        }
        return cleared;
    }
}
