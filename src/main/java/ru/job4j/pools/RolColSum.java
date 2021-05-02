package ru.job4j.pools;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class RolColSum {
    public static class Sums {
        private int rowSum;
        private int colSum;

        public Sums(int rowSum, int colSum) {
            this.rowSum = rowSum;
            this.colSum = colSum;
        }

        public int getRowSum() {
            return rowSum;
        }

        public int getColSum() {
            return colSum;
        }

        @Override
        public String toString() {
            return "Sums{" +
                    "rowSum=" + rowSum +
                    ", colSum=" + colSum +
                    '}';
        }
    }

    public static Sums[] sum(int[][] matrix) {
        int rows = matrix.length;
        int columns = matrix[0].length;
        int maxMatrixSize = Math.max(rows, columns);
        Sums[] sums = new Sums[Math.max(rows, columns)];
        for (int i = 0; i < maxMatrixSize; i++) {
            Integer[] rowAndColumnSums = getRowAndColumnSizeForI(i, matrix, rows, columns);
            sums[i] = new Sums(rowAndColumnSums[0], rowAndColumnSums[1]);
        }
        return sums;
    }

    public static Sums[] asyncSum(int[][] matrix) throws ExecutionException, InterruptedException {
        int rows = matrix.length;
        int columns = matrix[0].length;
        int maxMatrixSize = Math.max(rows, columns);
        Sums[] sums = new Sums[Math.max(rows, columns)];
        Map<Integer, CompletableFuture<Integer[]>> futures = new HashMap<>();
        for (int i = 0; i < maxMatrixSize; i++) {
            int finalI = i;
            futures.put(i, CompletableFuture.supplyAsync(() -> getRowAndColumnSizeForI(finalI, matrix, rows, columns)));
        }
        for (Integer key : futures.keySet()) {
            Integer[] data = futures.get(key).get();
            sums[key] = new Sums(data[0], data[1]);
        }
        return sums;
    }

    /**
     * @param i- row and column number
     * @return Integer[], where 2 elements. 1 element row i sum, 2 element column i sum. If sum for i cannot be calculated then -1
     */
    private static Integer[] getRowAndColumnSizeForI(int i, int[][] matrix, int totalRowsNr, int totalColumnsNr) {
        int rowSum = -1;
        int columnSum = -1;
        if (i < totalRowsNr) {
            rowSum = calculateRowSum(matrix[i], totalColumnsNr);
        }
        if (i < totalColumnsNr) {
            columnSum = calculateColumnSum(matrix, i);
        }
        return new Integer[]{rowSum, columnSum};
    }

    private static Integer calculateColumnSum(int[][] matrix, int column) {
        int columnSum = 0;
        for (int[] ints : matrix) {
            columnSum += ints[column];
        }
        return columnSum;
    }

    private static Integer calculateRowSum(int[] matrix, int totalColumnsNr) {
        int rowSum = 0;
        for (int column = 0; column < totalColumnsNr; column++) {
            rowSum += matrix[column];
        }
        return rowSum;
    }
}