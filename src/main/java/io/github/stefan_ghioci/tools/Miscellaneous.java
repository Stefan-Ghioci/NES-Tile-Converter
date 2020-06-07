package io.github.stefan_ghioci.tools;

import java.util.List;

public class Miscellaneous
{
    public static <T> T getRandomElement(List<T> list)
    {
        int randomIndex = (int) (Math.random() * list.size());
        return list.get(randomIndex);
    }
}
