package io.github.stefan_ghioci.tools;

import java.math.BigInteger;
import java.util.List;

public class Miscellaneous
{
    public static <T> T getRandomElement(List<T> list)
    {
        int randomIndex = (int) (Math.random() * list.size());
        return list.get(randomIndex);
    }

    public static int gcd(int a, int b)
    {
        return BigInteger.valueOf(a).gcd(BigInteger.valueOf(b)).intValue();
    }

}
