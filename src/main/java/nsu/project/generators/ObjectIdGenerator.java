package nsu.project.generators;

import java.util.Random;

//M1mur part
public class ObjectIdGenerator {
    private static Random random = new Random(123456789);
    public static <T> int generate(T object) {
        return random.nextInt(1, Integer.MAX_VALUE);
    }
}