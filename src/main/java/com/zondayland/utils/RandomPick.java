package com.zondayland.utils;

import java.util.*;

public class RandomPick <T> {

    public Map<T, Double> pool;

    public static <T> RandomPick<T> of(Map<T, Double> pool) {
        return new RandomPick<T>(pool);
    }

    public RandomPick(Map<T, Double> pool) {
        this.pool = pool;
    }

    public T getNext() {
        Random rand = new Random();
        rand.setSeed(System.currentTimeMillis());

        List<T> shuffledKeys = new ArrayList<T>(this.pool.keySet());
        Collections.shuffle(shuffledKeys);

        int idx = 0;
        while (true) {
            T key = shuffledKeys.get(idx++ % shuffledKeys.size());
            if (rand.nextDouble() < this.pool.get(key)) {
                return key;
            }
        }
    }

}
