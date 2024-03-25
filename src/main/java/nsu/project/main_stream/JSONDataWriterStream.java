package nsu.project.main_stream;

import nsu.project.filter_predicates.FilterPredicate;
import org.json.JSONObject;

import java.io.*;
import java.util.*;

public class JSONDataWriterStream<K> implements JSONDataStream<K> {
    private final String storagePath;
    private FilterPredicate<K> filterExpression;
    private final HashMap<Long, Integer> SerIdStatusMap = new HashMap<>();
    private String kClassName;
    private JSONObject jsonChunk;

    public JSONDataWriterStream(String storagePath) {
        this.storagePath = storagePath;
        prepareForFilterAndWrite();
    }

    private JSONDataWriterStream(String storagePath,
                                 FilterPredicate<K> filterExpression,
                                 String kClassName,
                                 JSONObject jsonChunk) {
        this.storagePath = storagePath;
        this.filterExpression = filterExpression;
        this.kClassName = kClassName;
        this.jsonChunk = jsonChunk;
    }

    public void write(K kObject) {
        write(List.of(kObject));
    }

    public void write(K[] kObjects) {
        write(List.of(kObjects));
    }

    public void write(List<K> kObjectList) {


        kClassName = kObjectList.getClass().getName();
    }

    private void prepareForFilterAndWrite() {
        String readResult = readJSON();
        jsonChunk = new JSONObject(readResult);
    }

    private String readJSON() {
        StringBuilder stringBuilder = new StringBuilder();
        File file = new File(storagePath);

        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream(file)))) {

            String inputLine;
            List<String> storageClasses = new ArrayList<>();
            boolean kClassFound = false;
            String prevLine = "";
            while (true) {
                inputLine = in.readLine();

                if (inputLine == null) {
                    stringBuilder.deleteCharAt(stringBuilder.length() - 1);
                    stringBuilder.deleteCharAt(stringBuilder.length() - 2);
                    break;
                }

                if (storageClasses.isEmpty() & inputLine.contains("classNames : [") & prevLine.contains("{")) {
                    String str = inputLine.replaceAll("\\p{Punct}", " ");
                    storageClasses = Arrays.stream(str.split("\\s")).toList();
                }


                if (inputLine.contains(kClassName + ": [") & prevLine.contains("],") & !kClassFound) {
                    kClassFound = true;
                    stringBuilder.append(inputLine);
                    prevLine = inputLine;
                    continue;
                }

                if (kClassFound) {
                    String finalInputLine = inputLine;
                    if (storageClasses.stream()
                            .anyMatch(w -> (
                                    w.equals(finalInputLine.substring(0, w.length()).strip())
                                            & !w.equals(kClassName))) & prevLine.strip().equals("],")) {
                        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
                        stringBuilder.insert(0, '{');
                        stringBuilder.insert(stringBuilder.length(), '}');
                        break;
                    }
                }

                prevLine = inputLine;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return stringBuilder.toString();
    }

    public JSONDataWriterStream<K> filter(FilterPredicate<K> filterPredicate) {
        filterExpression = filterPredicate;

        jsonChunk.keys()

        return new JSONDataWriterStream<>(this.storagePath, this.filterExpression, this.kClassName, this.jsonChunk);
    }


}
