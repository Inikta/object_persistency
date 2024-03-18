package nsu.project.filter_predicates;

public class Not<T> implements FilterPredicate<T> {

    private FilterPredicate<T> filterPredicate;

    public Not(FilterPredicate<T> filterPredicate) {
        this.filterPredicate = filterPredicate;
    }

    @Override
    public boolean evaluate() {
        return !filterPredicate.evaluate();
    }

    public FilterPredicate<T> getFilterPredicate() {
        return filterPredicate;
    }

    public void setFilterPredicate(FilterPredicate<T> filterPredicate) {
        this.filterPredicate = filterPredicate;
    }
}

