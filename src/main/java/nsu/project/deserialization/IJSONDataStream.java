package nsu.project.deserialization;

import java.lang.reflect.Field;
import java.util.List;

public interface IJSONDataStream {
    <K> List<K> read();

    void update();

    <K> boolean write(Class<K> kClass, K kObject);

    <K> boolean write(Class<K> kClass, List<K> kObjectList);

    <K> boolean write(Class<K> kClass, K... kObjects);

    <K> void and(Class<K> kClass, String fieldName, String fieldValue);

    <K> void or(Class<K> kClass, String fieldName, String fieldValue);

    <K> void not(Class<K> kClass, String fieldName, String fieldValue);

    <K> void findFirstBy(Class<K> kClass, String fieldName, String fieldValue);

    <K> void findAllBy(Class<K> kClass, String fieldName, String fieldValue);
}
