package nsu.project.deserelise;
import java.io.*;
import java.nio.file.AccessDeniedException;
import java.nio.file.Files;
import java.util.*;

public class JSONDataStream<Person, Building> {
    private String filePath;

    public JSONDataStream(String filePath) {
        this.filePath = filePath;
    }

    public DeserializedData read() throws FileNotFoundException, AccessDeniedException, NotSerializableException, ClassNotFoundException, IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new FileNotFoundException("File not found: " + filePath);
        }
        if (!file.canRead()) {
            throw new AccessDeniedException("Access denied to file: " + filePath);
        }

        String content = new String(Files.readAllBytes(file.toPath()));
        Map<String, Object> data = parseJSON(content);

        Map<Object, Boolean> visited = new HashMap<>();
        for (String key : data.keySet()) {
            visited.put(key, false);
        }

        for (Map.Entry<String, Object> entry : data.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (!visited.get(key)) {
                visited.put(key, true);
/*                if (value instanceof Person || value instanceof Building) {
                    if (value.getClass().getAnnotation(Ignore.class) != null) {
                        throw new NotSerializableException("Class " + value.getClass().getSimpleName() + " has @Ignore annotation");
                    }
                    if (value == null) {
                        throw new NullPointerException("Field value is null");
                    }
                }*/
                if (value != null && data.containsValue(value)) {
                    visited.put(value, true);
                }
            }
        }

        return new DeserializedData(data);
    }

    private Map<String, Object> parseJSON(String json) {
        Map<String, Object> map = new HashMap<>();
        String[] tokens = tokenize(json);

        int i = 0;
        while (i < tokens.length) {
            String key = tokens[i].substring(1, tokens[i].length() - 1);
            i += 2; // Skip the colon

            if (tokens[i].equals("{")) {
                // Object
                i++;
                Map<String, Object> value = parseJSON(json.substring(i));
                map.put(key, value);
                i += value.size() * 2; // Move to the next key-value pair
            } else if (tokens[i].equals("[")) {
                // Array
                i++;
                List<Object> value = parseJSONArray(json.substring(i));
                map.put(key, value);
                i += value.size() * 2; // Move to the next key-value pair
            } else {
                // Primitive value
                map.put(key, parsePrimitive(tokens[i]));
            }

            i++; // Move to the next key
        }

        return map;
    }

    private List<Object> parseJSONArray(String json) {
        List<Object> list = new ArrayList<>();
        String[] tokens = tokenize(json);

        int i = 0;
        while (i < tokens.length) {
            if (tokens[i].equals("{")) {
                // Object
                i++;
                Map<String, Object> value = parseJSON(json.substring(i));
                list.add(value);
                i += value.size() * 2; // Move to the next value
            } else if (tokens[i].equals("[")) {
                // Array
                i++;
                List<Object> value = parseJSONArray(json.substring(i));
                list.add(value);
                i += value.size() * 2; // Move to the next value
            } else {
                // Primitive value
                list.add(parsePrimitive(tokens[i]));
            }

            i++; // Move to the next value
        }

        return list;
    }

    private Object parsePrimitive(String token) {
        if (token.equals("null")) {
            return null;
        } else if (token.equals("true")) {
            return true;
        } else if (token.equals("false")) {
            return false;
        } else if (token.startsWith("\"") && token.endsWith("\"")) {
            return token.substring(1, token.length() - 1);
        } else {
            try {
                return Integer.parseInt(token);
            } catch (NumberFormatException e) {
                try {
                    return Double.parseDouble(token);
                } catch (NumberFormatException ex) {
                    throw new IllegalArgumentException("Unsupported primitive value: " + token);
                }
            }
        }
    }

    private String[] tokenize(String json) {
        List<String> tokens = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        boolean inString = false;

        for (char c : json.toCharArray()) {
            if (c == '{' || c == '}' || c == '[' || c == ']' || c == ':' || c == ',') {
                if (inString) {
                    sb.append(c);
                } else {
                    if (sb.length() > 0) {
                        tokens.add(sb.toString());
                        sb.setLength(0);
                    }
                    tokens.add(Character.toString(c));
                }
            } else if (c == '"') {
                inString = !inString;
                sb.append(c);
            } else if (!Character.isWhitespace(c)) {
                sb.append(c);
            }
        }

        if (sb.length() > 0) {
            tokens.add(sb.toString());
        }

        return tokens.toArray(new String[0]);
    }

    public static void main(String[] args) {
        String filePath = "data.json";
        JSONDataStream stream = new JSONDataStream(filePath);

        try {
            DeserializedData data = stream.read();
            System.out.println("Deserialized data: " + data);
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + e.getMessage());
        } catch (AccessDeniedException e) {
            System.err.println("Access denied: " + e.getMessage());
        } catch (NotSerializableException e) {
            System.err.println("Not serializable: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.err.println("Class not found: " + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
