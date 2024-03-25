package nsu.project.filter_predicates;

import com.sun.jdi.InvalidTypeException;
import org.json.JSONException;
import org.json.JSONObject;

public class ValuePredicate<T, K> implements FilterPredicate<T> {
    private String fieldName;
    private String fieldType;
    private String value;
    private FilterOperations operation;
    private JSONObject jsonObject;

    public ValuePredicate(String fieldName,
                          String fieldType,
                          FilterOperations operation,
                          String value,
                          JSONObject object) {
        this.fieldName = fieldName;
        this.fieldType = fieldType;
        this.operation = operation;
        this.value = value;
        this.jsonObject = object;
    }

    @Override
    public boolean evaluate() {

        switch (fieldType) {
            case "Integer" -> {
                try {
                    return intPred(jsonObject.getInt(fieldName));
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }

            case "Long" -> {
                try {
                    return longPred(jsonObject.getLong(fieldName));
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }

            case "Float" -> {
                try {
                    return floatPred(jsonObject.getFloat(fieldName));
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }

            case "Double" -> {
                try {
                    return doublePred(jsonObject.getDouble(fieldName));
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }

            case "String" -> {
                try {
                    return stringPred(jsonObject.getString(fieldName));
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }

            default -> {
                try {
                    throw new InvalidTypeException();
                } catch (InvalidTypeException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private boolean intPred(int toCompare) {
        int v = Integer.parseInt(value);
        switch (operation) {

            case EQUALS -> {
                return toCompare == v;
            }
            case NOT_EQUAL -> {
                return toCompare != v;
            }
            case LESS -> {
                return toCompare < v;
            }
            case GREATER -> {
                return toCompare > v;
            }
            case LESS_OR_EQUAL -> {
                return toCompare <= v;
            }
            case GREATER_OR_EQUAL -> {
                return toCompare >= v;
            }
            default -> {
                return false;
            }
        }
    }

    private boolean longPred(long toCompare) {
        long v = Long.parseLong(value);
        switch (operation) {

            case EQUALS -> {
                return toCompare == v;
            }
            case NOT_EQUAL -> {
                return toCompare != v;
            }
            case LESS -> {
                return toCompare < v;
            }
            case GREATER -> {
                return toCompare > v;
            }
            case LESS_OR_EQUAL -> {
                return toCompare <= v;
            }
            case GREATER_OR_EQUAL -> {
                return toCompare >= v;
            }
            default -> {
                return false;
            }
        }
    }

    private boolean floatPred(float toCompare) {
        float v = Float.parseFloat(value);
        switch (operation) {

            case EQUALS -> {
                return toCompare == v;
            }
            case NOT_EQUAL -> {
                return toCompare != v;
            }
            case LESS -> {
                return toCompare < v;
            }
            case GREATER -> {
                return toCompare > v;
            }
            case LESS_OR_EQUAL -> {
                return toCompare <= v;
            }
            case GREATER_OR_EQUAL -> {
                return toCompare >= v;
            }
            default -> {
                return false;
            }
        }
    }

    private boolean doublePred(double toCompare) {
        double v = Double.parseDouble(value);
        switch (operation) {

            case EQUALS -> {
                return toCompare == v;
            }
            case NOT_EQUAL -> {
                return toCompare != v;
            }
            case LESS -> {
                return toCompare < v;
            }
            case GREATER -> {
                return toCompare > v;
            }
            case LESS_OR_EQUAL -> {
                return toCompare <= v;
            }
            case GREATER_OR_EQUAL -> {
                return toCompare >= v;
            }
            default -> {
                return false;
            }
        }
    }

    private boolean stringPred(String toCompare) {
        switch (operation) {

            case EQUALS -> {
                return toCompare.equals(value);
            }
            case NOT_EQUAL -> {
                return !toCompare.equals(value);
            }
            case CONTAINS -> {
                return toCompare.contains(value);
            }
            default -> {
                return false;
            }
        }
    }
}
