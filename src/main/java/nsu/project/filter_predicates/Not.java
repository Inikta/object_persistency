package nsu.project.filter_predicates;

import java.util.ArrayList;
import java.util.List;

public class Not<T> implements FilterPredicate<T> {

    private List<FilterPredicate<T>> filterPredicates = new ArrayList<>();

    public Not(FilterPredicate<T> filterPredicate) {
        this.filterPredicates.add(filterPredicate);
    }

    @Override
    public boolean evaluate() {
        return !filterPredicates.get(0).evaluate();
    }

    public List<FilterPredicate<T>> getFilterPredicates() {
        return filterPredicates;
    }

    public void setFilterPredicates(List<FilterPredicate<T>> filterPredicates) {
        this.filterPredicates = filterPredicates;
    }
}

