package com.nebeek.newsstand.test;

import java.util.ArrayList;
import java.util.List;

public class AppMath {
    public int add(int x, int y) {
        return x + y;
    }

    public List<Integer> getPrimeFactors(int n) {
        List<Integer> result = new ArrayList<>();

        for (int i = 2; i <= n; i++) {
            if (n % i == 0) {
                boolean isPrime = true;
                for (Integer factor : result) {
                    if (i % factor == 0) {
                        isPrime = false;
                    }
                }
                if (isPrime) {
                    result.add(i);
                }
            }
        }
        return result;
    }
}
