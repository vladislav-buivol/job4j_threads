package ru.job4j.cache;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class CacheTest {

    @Test
    public void testAdd() {
        Cache cache = new Cache();
        Base base1 = new Base(1, 0);
        Base base2 = new Base(2, 0);
        Base base3 = new Base(3, 0);
        Base base4 = new Base(3, 0);
        cache.add(base1);
        cache.add(base2);
        cache.add(base2);
        cache.add(base2);
        cache.add(base3);
        cache.add(base4);
        assertThat(cache.size(), is(3));
    }

    @Test
    public void testDelete() {
        List<Base> bases = new ArrayList<>();
        Cache cache = new Cache();
        int nrOfElements = 5;
        for (int i = 0; i < nrOfElements; i++) {
            Base base = new Base(i, 0);
            cache.add(base);
            bases.add(base);
        }
        Base base = new Base(nrOfElements + 1, 0);
        cache.delete(bases.get(0));
        cache.delete(bases.get(0));
        cache.delete(bases.get(2));
        cache.delete(bases.get(nrOfElements - 1));
        cache.delete(base);
        assertThat(cache.size(), is(2));
    }

    @Test
    public void testUpdate() {
        Cache cache = new Cache();
        Base base1 = new Base(1, 1);
        Base base2 = new Base(2, 1);
        Base base3 = new Base(3, 1);
        Base base4 = new Base(4, 1);
        cache.add(base1);
        cache.add(base2);
        cache.add(base3);
        cache.add(base4);
        base1.setName("asd");
        assertThat(true, is(cache.update(base1)));
        assertThat(true, is(cache.update(base3)));
        assertThat(true, is(cache.update(base4)));
        assertThat(cache.get(base1.getId()).getName(), is("asd"));
        assertThat(cache.get(base3.getId()).getName(), nullValue());
        assertThat(cache.get(base4.getId()).getVersion(), is(2));
    }


    @Test(expected = OptimisticException.class)
    public void testOptimisticException() {
        Cache cache = new Cache();
        Base base1 = new Base(1, 0);
        cache.add(base1);
        assertThat(true, is(cache.update(base1)));
        assertThat(true, is(cache.update(base1)));
    }

}