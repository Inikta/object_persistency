package nsu.project.main_stream;

import java.util.stream.BaseStream;

public interface JSONDataStream<K> extends BaseStream<K, JSONDataStream<K>> {
    public default void or(Class<K> kClass, String fieldName, String fieldValue) {

    }

    public default void not(Class<K> kClass, String fieldName, String fieldValue) {

    }

    public default void findFirstBy(Class<K> kClass, String fieldName, String fieldValue) {

    }

    public default void findAllBy(Class<K> kClass, String fieldName, String fieldValue) {

    }
}
