package nsu.project.main_stream;

import nsu.project.filter_predicates.FilterPredicate;

import java.util.List;
import java.util.stream.BaseStream;

public interface JSONDataStream<K> {
    List<K> filterCondition(FilterPredicate<K> filterPredicate);
}
