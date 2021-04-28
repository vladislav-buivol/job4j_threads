package ru.job4j.pool;

public class MergeSort {

    public static int[] sort(int[] array) {
        return sort(array, 0, array.length - 1);
    }

    private static int[] sort(int[] array, int from, int to) {
        // при следующем условии, массив из одного элемента
        // делить нечего, возвращаем элемент
        if (from == to) {
            return new int[]{array[from]};
        }
        // попали сюда, значит в массиве более одного элемента
        // находим середину
        int mid = (from + to) / 2;
        // объединяем отсортированные части
        return merge(
                // сортируем левую часть
                sort(array, from, mid),
                // сортируем правую часть
                sort(array, mid + 1, to)
        );
    }

    // Метод объединения двух отсортированных массивов
    public static int[] merge(int[] left, int[] right) {
        int li = 0;
        int ri = 0;
        int resI = 0;
        int[] result = new int[left.length + right.length];
        while (resI != result.length) {
            if (li == left.length) {
                result[resI++] = right[ri++];
            } else if (ri == right.length) {
                result[resI++] = left[li++];
            } else if (left[li] < right[ri]) {
                result[resI++] = left[li++];
            } else {
                result[resI++] = right[ri++];
            }
        }
        return result;
    }

    public static void main(String[] args) {
        int[] arr = new int[]{4, 4};
        int[] arr2 = new int[]{2, 7};
        int[] arr3 = new int[]{5, 4, 7, 1};
        int[] arr4 = new int[]{1,2,3};
        int[] arr5 = new int[]{1};
        for (int i : MergeSort.merge(arr4, arr5)) {
            //    for (int i : MergeSort.sort(arr3)) {
            System.out.println(i);
        }
    }

}