package nsu.project.main_stream;

import nsu.project.filter_predicates.FilterPredicate;

import java.nio.file.Path;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.BaseStream;

public class JSONDataReaderStream<K> implements JSONDataStream<K> {
    private final Path storagePath;
    private final List<Predicate<K>> conditions = new ArrayList<>();
    private final HashMap<Long, Integer> DeserIdStatusMap = new HashMap<>();

    public JSONDataReaderStream(Path storagePath) {
        this.storagePath = storagePath;
    }

    //M1mur part
    public List<K> read() {
        return null;
    }

    @Override
    public List<K> filterCondition(FilterPredicate<K> filterPredicate) {
        return null;
    }
}
