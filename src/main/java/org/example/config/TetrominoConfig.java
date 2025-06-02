package org.example.config;

public class TetrominoConfig {
    private String name;
    private boolean enabled;
    private int[][][] matrices; // matrices[0]..matrices[3]

    // ← احتیاج به یک ctor خالی (Jackson)
    public TetrominoConfig() { }

    /**
     * سازنده برای وقتی فقط یک ماتریس اولیه دارید (مثل قبل)
     * و می‌خواهید خودکار چرخش بسازد.
     */
    public TetrominoConfig(String name, boolean enabled, int[][] firstMatrix) {
        this.name = name;
        this.enabled = enabled;
        buildRotations(firstMatrix);
    }

    /**
     * سازندهٔ جدید که مستقیماً چهار ماتریس (۴ جهت) را از بیرون می‌گیرد.
     * فرض می‌کنیم caller قبل از این‌که این ctor را صدا بزند، از کاربر
     * چهار ماتریس ۴×۴ جداگانه گرفته و آن را در int[4][4][4] ریخته است.
     */
    public TetrominoConfig(String name, boolean enabled, int[][][] allMatrices) {
        this.name = name;
        this.enabled = enabled;
        // مستقیماً بارگذاری چهار ماتریس:
        this.matrices = new int[4][][];
        for (int i = 0; i < 4; i++) {
            // تک تک سطرها را عمیق کپی می‌کنیم تا ارجاع به آرایهٔ خارجی نباشد:
            int n = allMatrices[i].length;
            this.matrices[i] = new int[n][n];
            for (int r = 0; r < n; r++) {
                System.arraycopy(allMatrices[i][r], 0, this.matrices[i][r], 0, n);
            }
        }
    }

    private void buildRotations(int[][] firstMatrix) {
        int n = firstMatrix.length;
        this.matrices = new int[4][n][n];
        // ماتریس اولیه:
        for (int i = 0; i < n; i++) {
            System.arraycopy(firstMatrix[i], 0, matrices[0][i], 0, n);
        }
        // تولید خودکار جهت‌های بعدی با چرخش ساعتگرد
        for (int i = 1; i < 4; i++) {
            matrices[i] = rotateClockwise(matrices[i - 1]);
        }
    }

    private int[][] rotateClockwise(int[][] mat) {
        int n = mat.length;
        int[][] out = new int[n][n];
        for (int r = 0; r < n; r++) {
            for (int c = 0; c < n; c++) {
                out[c][n - 1 - r] = mat[r][c];
            }
        }
        return out;
    }

    // ===== گتر/سترهای Jackson-دوستانه =====
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }

    public int[][][] getMatrices() { return matrices; }
    public void setMatrices(int[][][] matrices) { this.matrices = matrices; }

    public int[][] getRotationMatrix(int index) {
        return matrices[index % 4];
    }
}
