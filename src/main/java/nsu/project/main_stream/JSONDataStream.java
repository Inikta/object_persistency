package nsu.project.main_stream;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Predicate;

public class JSONDataStream <K> {
    private final Path storagePath;
    private final List<Predicate<K>> conditions = new ArrayList<>();
    private final HashMap<Long, Integer> SerIdStatusMap = new HashMap<>();
    private final HashMap<Long, Integer> DeserIdStatusMap = new HashMap<>();

    public JSONDataStream(Path storagePath) {
        this.storagePath = storagePath;
    }
    
    public List<K> read() {
        return null;
    }

    public void update() {

    }

    public boolean write(Class<K> kClass, Object kObject) {
        return false;
    }
    
    public boolean write(Class<K> kClass, List<K> kObjectList) {
        return false;
    }
    
    public boolean write(Class<K> kClass, Object[] kObjects) {
        return false;
    }

    public void and(Class<K> kClass, String fieldName, String fieldValue) {

    }

    public void or(Class<K> kClass, String fieldName, String fieldValue) {

    }
    
    public void not(Class<K> kClass, String fieldName, String fieldValue) {

    }
    
    public void findFirstBy(Class<K> kClass, String fieldName, String fieldValue) {

    }

    public void findAllBy(Class<K> kClass, String fieldName, String fieldValue) {

    }
}
