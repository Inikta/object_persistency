package nsu.project.main_stream;

import java.nio.file.Path;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.BaseStream;

public class JSONDataWriterStream<K> {
    private final Path storagePath;
    private final List<Predicate<K>> conditions = new ArrayList<>();
    private final HashMap<Long, Integer> SerIdStatusMap = new HashMap<>();

    public JSONDataWriterStream(Path storagePath) {
        this.storagePath = storagePath;
    }

    public void update() {
    }

    public boolean write(Class<K> kClass, K kObject) {
        return write(kClass, List.of(kObject));
    }

    public boolean write(Class<K> kClass, K[] kObjects) {
        return write(kClass, List.of(kObjects));
    }

    public boolean write(Class<K> kClass, List<K> kObjectList) {
        return true;
    }

}
