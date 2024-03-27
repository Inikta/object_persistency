package nsu.project.main_stream;

import nsu.project.filter_predicates.FilterPredicate;
import org.json.JSONArray;
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
    private String kClassName = "Person";   //!!!!!!
    private JSONObject storageObject;
    private JSONObject oldJsonChunk;
    private JSONObject editedJsonChunk;
    private long chunkBeginLine = 0;
    private long chunkEndLine = 0;
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
                                 JSONObject storageObject,
                                 JSONObject oldJsonChunk,
                                 JSONObject editedJsonChunk,
                                 long chunkBeginLine,
                                 long chunkEndLine,
                                 long chunkBeginPosition,
                                 long chunkEndPosition,
                                 List<K> inputObjects) {
        this.storagePathString = storagePathString;
        this.filterExpression = filterExpression;
        this.kClassName = kClassName;
        this.storageObject = storageObject;
        this.oldJsonChunk = oldJsonChunk;
        this.editedJsonChunk = editedJsonChunk;
        this.chunkBeginLine = chunkBeginLine;
        this.chunkEndLine = chunkEndLine;
        this.chunkBeginPosition = chunkBeginPosition;
        this.chunkEndPosition = chunkEndPosition;
        this.inputObjects = inputObjects;
    }

    private JSONObject readStorageToJSONObject() throws IOException {
        String storageString = new String(Files.readAllBytes(Path.of(storagePathString)));
        return new JSONObject(storageString);
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

        storageObject = readStorageToJSONObject();

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

            String inputLine = "";
            List<String> storageClasses = new ArrayList<>();
            boolean kClassFound = false;
            String prevLine;

            while (true) {
                prevLine = inputLine;
                inputLine = in.readLine();


                if (inputLine == null) {
                    if (!stringBuilder.isEmpty()) {
                        stringBuilder.delete(stringBuilder.length() - 2, stringBuilder.length());
                        if (chunkBeginLine == 0) {
                            stringBuilder.delete(0, 40);
                        }
                    }
                    break;
                }

                if (storageClasses.isEmpty() & inputLine.contains("\"classNames\": [")
                        & (prevLine.contains("{")
                        || (prevLine.contains("") & (inputLine.contains("\"" + kClassName + "\""))))) {
                    if (inputLine.substring(0, inputLine.indexOf("],") + 1).strip().contains("]")) {
                        String str = inputLine.substring(0, inputLine.indexOf("],") + 1).replaceAll("\\p{Punct}", ":");
                        storageClasses = Arrays
                                .stream(str.split(":"))
                                .map(String::strip)
                                .filter(w -> !w.equals("") & !w.equals("classNames"))
                                .toList();
                    } else {
                        prevLine = inputLine;
                        inputLine = in.readLine();
                        chunkBeginLine++;
                        while (!inputLine.strip().equals("],")) {
                            if (inputLine.strip().matches("\"\\p{Upper}\\p{Alpha}*\",?")) {
                                String temp = inputLine.strip();
                                if (temp.contains(",")) {
                                    temp = temp.substring(0, temp.length() - 1);
                                }
                                storageClasses.add(temp);
                            }
                            prevLine = inputLine;
                            inputLine = in.readLine();
                        }

                        if (!storageClasses.isEmpty()) {
                            continue;
                        }
                    }
                }

                if (inputLine.strip().contains("\"" + kClassName + "\": [")
                        & (prevLine.strip().contains("{") || prevLine.strip().contains("\"storage\": [{") || inputLine.contains("\"storage\": [{"))
                        & !kClassFound) {
                    kClassFound = true;
                    chunkEndLine = chunkBeginLine;
                    stringBuilder.append("{").append(inputLine.strip());
                    continue;
                }

                if (kClassFound) {
                    String finalInputLine = inputLine;
                    boolean c1 = storageClasses.stream().anyMatch(w -> finalInputLine.contains(w + ": ["));
                    boolean c2 = prevLine.strip().contains("{");
                    if (c1 & c2) {
                        stringBuilder.deleteCharAt(stringBuilder.length() - 2);
                        break;
                    } else {
                        stringBuilder.append(inputLine.strip());
                        chunkEndLine++;
                    }
                }

                if (!kClassFound) {
                    ++chunkBeginLine;
                }
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
        return this;
    }

    private List<String> convertInputToJSONString(List<K> inputObjects) {
        return inputObjects.stream().map(Object::toString).toList();
    }

    public JSONDataWriterStream<K> filter(FilterPredicate<K> filterPredicate) {
        filterExpression = filterPredicate;
        JSONArray jsonArray = oldJsonChunk.getJSONArray(kClassName);

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject temp = jsonArray.getJSONObject(i);
        }

        return this;
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

    public long getChunkBeginLine() {
        return chunkBeginLine;
    }

    public long getChunkEndLine() {
        return chunkEndLine;
    }

    public List<K> getInputObjects() {
        return inputObjects;
    }
}
