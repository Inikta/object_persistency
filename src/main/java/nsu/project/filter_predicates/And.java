package nsu.project.filter_predicates;

import java.util.ArrayList;
import java.util.List;

public class And<T> implements FilterPredicate<T> {
    private List<FilterPredicate<T>> filterPredicates = new ArrayList<>();
    private FilterOperations operation;
    private FilterPredicate<T> result;

    public And(FilterPredicate<T> filterPredicate1, FilterPredicate<T> filterPredicate2, FilterOperations operation, FilterPredicate<T> result) {
        this.filterPredicates.add(filterPredicate1);
        this.filterPredicates.add(filterPredicate2);
        this.operation = operation;
        this.result = result;
    }

    @Override
    public boolean evaluate() {
        switch (operation) {
            case EQUALS -> {
                if (filterPredicates.get(0).evaluate() & filterPredicates.get(1).evaluate() == result.evaluate()) {
                    return true;
                }
            }
            case NOT_EQUAL -> {
                if (filterPredicates.get(0).evaluate() & filterPredicates.get(1).evaluate() != result.evaluate()) {
                    return true;
                }
            }
        }
        return false;
    }

    public List<FilterPredicate<T>> getFilterPredicates() {
        return filterPredicates;
    }

    public void setFilterPredicates(List<FilterPredicate<T>> filterPredicates) {
        this.filterPredicates = filterPredicates;
    }

    public FilterOperations getOperation() {
        return operation;
    }

    public void setOperation(FilterOperations operation) {
        this.operation = operation;
    }

    public FilterPredicate<T> getResult() {
        return result;
    }

    public void setResult(FilterPredicate<T> result) {
        this.result = result;
    }
}
