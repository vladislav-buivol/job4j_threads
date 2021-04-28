package ru.job4j.pool;

import org.junit.Test;

import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class ParallelIndexSearchTest {

    @Test
    public void testSearch() {
        int size = 100000;
        String[] arr = new String[size];
        int expectedIndex = 34526;
        int expectedIndex2 = size - 1;
        int expectedIndex3 = 0;
        for (int i = 0; i < (size - 1); i++) {
            arr[i] = UUID.randomUUID().toString();
        }
        ParallelIndexSearch<String> search = new ParallelIndexSearch<>(arr, arr[expectedIndex]);
        Integer result = search.find();
        Integer result2 = search.find(null);
        Integer result3 = search.find(arr[0]);
        assertThat(expectedIndex, is(result));
        assertThat(expectedIndex2, is(result2));
        assertThat(expectedIndex3, is(result3));
    }

    @Test
    public void testSearch2() {
        int size = 100000;
        String[] arr = new String[size];
        int expectedIndex = 0;
        for (int i = 0; i < (size - 1); i++) {
            arr[i] = String.valueOf(1);
        }
        ParallelIndexSearch<String> search = new ParallelIndexSearch<>(arr, arr[expectedIndex]);
        Integer result = search.find();
        Integer result3 = search.find(arr[0]);
        assertThat(expectedIndex, is(result));
        assertThat(expectedIndex, is(result3));
    }

}