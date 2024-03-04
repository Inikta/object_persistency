package nsu.project.deserialization;

import java.util.List;

public class JSONDataStream implements IJSONDataStream {
    @Override
    public <K> List<K> read() {
        return null;
    }

    @Override
    public void update() {

    }

    @Override
    public <K> boolean write(Class<K> kClass, K kObject) {
        return false;
    }

    @Override
    public <K> boolean write(Class<K> kClass, List<K> kObjectList) {
        return false;
    }

    @Override
    public <K> boolean write(Class<K> kClass, K... kObjects) {
        return false;
    }

    @Override
    public <K> void and(Class<K> kClass, String fieldName, String fieldValue) {

    }

    @Override
    public <K> void or(Class<K> kClass, String fieldName, String fieldValue) {

    }

    @Override
    public <K> void not(Class<K> kClass, String fieldName, String fieldValue) {

    }

    @Override
    public <K> void findFirstBy(Class<K> kClass, String fieldName, String fieldValue) {

    }

    @Override
    public <K> void findAllBy(Class<K> kClass, String fieldName, String fieldValue) {

    }
}
