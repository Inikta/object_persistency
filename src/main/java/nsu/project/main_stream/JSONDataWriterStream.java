package nsu.project.main_stream;

import nsu.project.filter_predicates.FilterPredicate;
import org.json.JSONException;
import org.json.JSONObject;

import javax.naming.InvalidNameException;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class JSONDataWriterStream<K> implements JSONDataStream<K> {
    private final String storagePathString;
    private FilterPredicate<K> filterExpression;
    private final HashMap<Long, Integer> SerIdStatusMap = new HashMap<>();
    private String kClassName;
    private JSONObject oldJsonChunk;
    private JSONObject editedJsonChunk;
    private long chunkBeginPosition = 0;
    private long chunkEndPosition = 0;
    private List<K> inputObjects;

    public JSONDataWriterStream(String storagePathString) throws IOException, InvalidNameException {
        this.storagePathString = storagePathString;
        prepareForFilterAndWrite();
    }

    private JSONDataWriterStream(String storagePathString,
                                 FilterPredicate<K> filterExpression,
                                 String kClassName,
                                 JSONObject oldJsonChunk,
                                 long chunkBeginPosition,
                                 long chunkEndPosition,
                                 List<K> inputObjects) {
        this.storagePathString = storagePathString;
        this.filterExpression = filterExpression;
        this.kClassName = kClassName;
        this.oldJsonChunk = oldJsonChunk;
        this.chunkBeginPosition = chunkBeginPosition;
        this.chunkEndPosition = chunkEndPosition;
        this.inputObjects = inputObjects;
    }

    private void prepareForFilterAndWrite() throws IOException, InvalidNameException {
        Path storagePath = Path.of(storagePathString);
        File storage = new File(storagePathString);
        if (!storagePath.getFileName().toString().matches("[\\w\\u0020]{0,255}.JSON")) {
            throw new InvalidNameException("Exception: invalid name of storage file.");
        }

        if (!storage.exists() || storage.isDirectory()) {
            Files.createFile(storagePath);
        }

        String readResult = readJSONChunk();
        try {
            oldJsonChunk = new JSONObject(readResult);
        } catch (JSONException e) {
            oldJsonChunk = new JSONObject();
        }
    }

    private String readJSONChunk() {
        StringBuilder stringBuilder = new StringBuilder();
        File file = new File(storagePathString);

        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream(file)))) {

            String inputLine;
            List<String> storageClasses = new ArrayList<>();
            boolean kClassFound = false;
            String prevLine = "";

            while (true) {
                inputLine = in.readLine();
                chunkEndPosition++;

                if (inputLine == null) {
                    if (!stringBuilder.isEmpty()) {
                        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
                        stringBuilder.deleteCharAt(stringBuilder.length() - 2);
                    }
                    break;
                }

                if (storageClasses.isEmpty() & inputLine.contains("\"classNames\": [") & prevLine.contains("{")) {
                    if (inputLine.strip().substring(16).contains("],")) {
                        String str = inputLine.replaceAll("\\p{Punct}", ":");
                        storageClasses = Arrays.stream(str.split(":")).map(String::strip).toList();
                    } else {
                        inputLine = in.readLine();
                        while (!inputLine.strip().equals("[,")) {
                            if (inputLine.strip().matches("\"\\p{Upper}\\p{Alpha}*\",?")) {
                                String temp = inputLine.strip();
                                if (temp.contains(",")) {
                                    temp = temp.substring(0, temp.length() - 1);
                                }
                                storageClasses.add(temp);
                            }
                            inputLine = in.readLine();
                        }
                    }
                    if (!storageClasses.isEmpty()) {
                        continue;
                    }
                    return "{\"Person\": []}";
                }

                if (inputLine.contains(kClassName + "\": [") & (prevLine.contains("},") || prevLine.contains("\"storage\" : ["))& !kClassFound) {
                    kClassFound = true;
                    stringBuilder.append(inputLine);
                    prevLine = inputLine;
                    continue;
                }

                if (kClassFound) {
                    String finalInputLine = inputLine;
                    if (storageClasses.stream().anyMatch(w -> finalInputLine.contains(w + " : ["))) {
                        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
                        stringBuilder.insert(0, '{');
                        stringBuilder.insert(stringBuilder.length(), '}');
                        break;
                    }
                }

                if (!kClassFound) {
                    ++chunkBeginPosition;
                }
                prevLine = inputLine;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return stringBuilder.toString();
    }
    public JSONDataWriterStream<K> put(K kObject) {
        return put(List.of(kObject));
    }

    public JSONDataWriterStream<K> put(K[] kObjects) {
        return put(List.of(kObjects));
    }

    public JSONDataWriterStream<K> put(List<K> kObjectList) {
        inputObjects = kObjectList;
        kClassName = kObjectList.getClass().getName();
        return new JSONDataWriterStream<>(
                this.storagePathString,
                this.filterExpression,
                this.kClassName,
                this.oldJsonChunk,
                this.chunkBeginPosition,
                this.chunkEndPosition,
                this.inputObjects);
    }

    public JSONDataWriterStream<K> filter(FilterPredicate<K> filterPredicate) {
        filterExpression = filterPredicate;



        return new JSONDataWriterStream<>(
                this.storagePathString,
                this.filterExpression,
                this.kClassName,
                this.oldJsonChunk,
                this.chunkBeginPosition,
                this.chunkEndPosition,
                this.inputObjects);
    }

    public void write() {

    }

    public String getStoragePathString() {
        return storagePathString;
    }

    public FilterPredicate<K> getFilterExpression() {
        return filterExpression;
    }

    public HashMap<Long, Integer> getSerIdStatusMap() {
        return SerIdStatusMap;
    }

    public String getkClassName() {
        return kClassName;
    }

    public JSONObject getOldJsonChunk() {
        return oldJsonChunk;
    }

    public JSONObject getEditedJsonChunk() {
        return editedJsonChunk;
    }

    public long getChunkBeginPosition() {
        return chunkBeginPosition;
    }

    public long getChunkEndPosition() {
        return chunkEndPosition;
    }

    public List<K> getInputObjects() {
        return inputObjects;
    }
}
