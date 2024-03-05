package nsu.project.data;

import java.util.List;

public interface IDeserializedData <T> {
    public List<T> unwrap();
}
