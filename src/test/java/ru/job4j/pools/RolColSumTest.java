package ru.job4j.pools;

import org.junit.Test;

import java.util.concurrent.ExecutionException;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static ru.job4j.pools.RolColSum.asyncSum;
import static ru.job4j.pools.RolColSum.sum;

public class RolColSumTest {
    private final int[][] rectangularMatrixMoreColumns = new int[][]{
            new int[]{1, 2, 3, 3},
            new int[]{4, 5, 6, 3},
            new int[]{7, 8, 9, 3},
    };
    private final int[][] rectangularMatrixMoreRows = new int[][]{
            new int[]{1, 2},
            new int[]{4, 5},
            new int[]{7, 8},
    };

    private final int[][] squareMatrix = new int[][]{
            new int[]{1, 2, 3},
            new int[]{4, 5, 6},
            new int[]{7, 8, 9},
    };

    private final int[][] hugeMatrix = createHugeSquareMatrix(20000);

    @Test
    public void rectangularMatrixMoreRowsTest() {
        RolColSum.Sums[] sums = sum(rectangularMatrixMoreRows);
        int[] expectedRowsSums = new int[]{3, 9, 15};
        int[] expectedColumnSums = new int[]{12, 15, -1};
        for (int i = 0; i < sums.length; i++) {
            assertThat(sums[i].getRowSum(), is(expectedRowsSums[i]));
            assertThat(sums[i].getColSum(), is(expectedColumnSums[i]));
        }
    }

    @Test
    public void rectangularMatrixMoreColumnsTest() {
        RolColSum.Sums[] sums = sum(rectangularMatrixMoreColumns);
        int[] expectedRowsSums = new int[]{9, 18, 27, -1};
        int[] expectedColumnSums = new int[]{12, 15, 18, 9};
        for (int i = 0; i < sums.length; i++) {
            assertThat(sums[i].getRowSum(), is(expectedRowsSums[i]));
            assertThat(sums[i].getColSum(), is(expectedColumnSums[i]));
        }
    }

    @Test
    public void squareMatrix() {
        RolColSum.Sums[] sums = sum(squareMatrix);
        int[] expectedRowsSums = new int[]{6, 15, 24};
        int[] expectedColumnSums = new int[]{12, 15, 18};
        for (int i = 0; i < sums.length; i++) {
            assertThat(sums[i].getRowSum(), is(expectedRowsSums[i]));
            assertThat(sums[i].getColSum(), is(expectedColumnSums[i]));
        }
    }


    @Test
    public void rectangularMatrixMoreRowsTestAsync() throws ExecutionException, InterruptedException {
        RolColSum.Sums[] sums = sum(rectangularMatrixMoreRows);
        RolColSum.Sums[] sumsAsync = asyncSum(rectangularMatrixMoreRows);
        int[] expectedRowsSums = new int[]{3, 9, 15};
        int[] expectedColumnSums = new int[]{12, 15, -1};
        for (int i = 0; i < sums.length; i++) {
            assertThat(sumsAsync[i].getRowSum(), is(expectedRowsSums[i]));
            assertThat(sumsAsync[i].getColSum(), is(expectedColumnSums[i]));
        }
    }

    @Test
    public void rectangularMatrixMoreColumnsTestAsync() throws ExecutionException, InterruptedException {
        RolColSum.Sums[] sums = sum(rectangularMatrixMoreColumns);
        RolColSum.Sums[] sumsAsync = asyncSum(rectangularMatrixMoreColumns);
        int[] expectedRowsSums = new int[]{9, 18, 27, -1};
        int[] expectedColumnSums = new int[]{12, 15, 18, 9};
        for (int i = 0; i < sums.length; i++) {
            assertThat(sumsAsync[i].getRowSum(), is(expectedRowsSums[i]));
            assertThat(sumsAsync[i].getColSum(), is(expectedColumnSums[i]));
        }
    }

    @Test
    public void squareMatrixAsync() throws ExecutionException, InterruptedException {
        RolColSum.Sums[] sums = sum(squareMatrix);
        RolColSum.Sums[] sumsAsync = asyncSum(squareMatrix);
        int[] expectedRowsSums = new int[]{6, 15, 24};
        int[] expectedColumnSums = new int[]{12, 15, 18};
        for (int i = 0; i < sums.length; i++) {
            assertThat(sumsAsync[i].getRowSum(), is(expectedRowsSums[i]));
            assertThat(sumsAsync[i].getColSum(), is(expectedColumnSums[i]));
        }
    }

    @Test
    public void hugeMatrixAsync() throws ExecutionException, InterruptedException {
        RolColSum.Sums[] sumsAsync = asyncSum(hugeMatrix);
        for (RolColSum.Sums sums : sumsAsync) {
            assertThat(sums.getRowSum(), is(hugeMatrix.length));
            assertThat(sums.getColSum(), is(hugeMatrix.length));
        }
    }

    @Test
    public void hugeMatrix() {
        RolColSum.Sums[] sums = sum(hugeMatrix);
        for (RolColSum.Sums sum : sums) {
            assertThat(sum.getRowSum(), is(hugeMatrix.length));
            assertThat(sum.getColSum(), is(hugeMatrix.length));
        }
    }

    /**
     * @param size - matrix size
     * @return matrix where all values are 1
     */
    private int[][] createHugeSquareMatrix(int size) {
        int value = 1;
        int[][] matrix = new int[size][size];
        for (int i = 0; i < size; i++) {
            int[] rows = new int[size];
            for (int j = 0; j < size; j++) {
                rows[j] = value;
            }
            matrix[i] = rows;
        }
        return matrix;
    }

}