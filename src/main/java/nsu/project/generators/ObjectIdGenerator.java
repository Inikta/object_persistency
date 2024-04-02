package nsu.project.generators;

//M1mur part
public class ObjectIdGenerator {
    public static <T> int generate(T object) {
        return object.hashCode();
    }
}
