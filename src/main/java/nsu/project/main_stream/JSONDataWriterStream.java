package nsu.project.main_stream;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import nsu.project.filter_predicates.FilterPredicate;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

public class JSONDataWriterStream<K> implements JSONDataStream<K> {
    private final Path storagePath;
    private FilterPredicate<K> filterExpression;
    private final HashMap<Long, Integer> SerIdStatusMap = new HashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public JSONDataWriterStream(Path storagePath) {
        this.storagePath = storagePath;
    }

    public void write(Class<K> kClass, K kObject) {
        write(kClass, List.of(kObject));
    }

    public void write(Class<K> kClass, K[] kObjects) {
        write(kClass, List.of(kObjects));
    }

    public void write(Class<K> kClass, List<K> kObjectList) {

    }

    private String objectToJsonString(K object, int objectId) throws JsonProcessingException {
        String json = objectMapper.writeValueAsString(object);
        json = objectId + ":" + json;
        return json;
    }
    private void jsonPlaceFinder(Set<K> objects) throws IOException {
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(String.valueOf(storagePath)));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        String className = objects.getClass().getName();
        HashMap<Integer, K> hashToObjectMap = new HashMap<>();
        objects.forEach(o -> hashToObjectMap.put(o.hashCode(), o));

        boolean isKClass = false;
        boolean isBegin = false;
        boolean isEnd = false;
        boolean isPlace = false;
        for (String line; (line = reader.readLine()) != null;) {
            if (line.contains(className)) {
                isKClass = true;
                continue;
            }
            if (isKClass & line.contains("["))
            {
                isBegin = true;
                continue;
            }
            if (isKClass & isBegin )
            if (hashToObjectMap.containsKey(Integer.parseInt(line))) {

            } else if (line.contains()) {
                if (isKClass) {

                }
            }
        }

    }

    @Override
    public List<K> filterCondition(FilterPredicate<K> filterPredicate) {
        filterExpression = filterPredicate;

        return null;
    }
}
