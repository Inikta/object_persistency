package nsu.project.filter_predicates;

import com.sun.jdi.InvalidTypeException;

import java.lang.reflect.Field;
import java.util.NoSuchElementException;

public class ValuePredicate<T, K> implements FilterPredicate<T> {
    private String fieldName;
    private String fieldType;
    private String value;
    private FilterOperations operation;
    private T object;

    public ValuePredicate(String fieldName,
                          String fieldType,
                          FilterOperations operation,
                          String value,
                          T object) {
        this.fieldName = fieldName;
        this.fieldType = fieldType;
        this.operation = operation;
        this.value = value;
        this.object = object;
    }

    @Override
    public boolean evaluate() {
        Field field;
        try {
            field = object.getClass().getField(fieldName);
        } catch (NoSuchFieldException e) {
            throw new NoSuchElementException();
        }

        field.setAccessible(true);
        String typeName = field.getType().getName();

        switch (typeName) {
            case "Integer" -> {
                try {
                    return intPred((Integer) field.get(object));
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }

            case "Long" -> {
                try {
                    return longPred((Long) field.get(object));
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }

            case "Short" -> {
                try {
                    return shortPred((Short) field.get(object));
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
            case "Float" -> {
                try {
                    return floatPred((Float) field.get(object));
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }

            case "Double" -> {
                try {
                    return doublePred((Double) field.get(object));
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }

            case "String" -> {
                try {
                    return stringPred((String) field.get(object));
                } catch (IllegalAccessException e) {
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

    private boolean shortPred(short toCompare) {
        short v = Short.parseShort(value);
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
