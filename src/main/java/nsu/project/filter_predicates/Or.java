package nsu.project.filter_predicates;

public class Or<T> implements FilterPredicate<T> {

    private FilterPredicate<T> filterPredicate1;
    private FilterPredicate<T> filterPredicate2;
    private FilterOperations operation;
    private FilterPredicate<T> result;

    public Or(FilterPredicate<T> filterPredicate1, FilterPredicate<T> filterPredicate2, FilterOperations operation, FilterPredicate<T> result) {
        this.filterPredicate1 = filterPredicate1;
        this.filterPredicate2 = filterPredicate2;
        this.operation = operation;
        this.result = result;
    }

    @Override
    public boolean evaluate() {
        switch (operation) {
            case EQUALS -> {
                if (filterPredicate1.evaluate() || filterPredicate2.evaluate() == result.evaluate()) {
                    return true;
                }
            }
            case NOT_EQUAL -> {
                if (filterPredicate1.evaluate() || filterPredicate2.evaluate() != result.evaluate()) {
                    return true;
                }
            }
        }
        return false;
    }

    public FilterPredicate<T> getFilterPredicate1() {
        return filterPredicate1;
    }

    public void setFilterPredicate1(FilterPredicate<T> filterPredicate1) {
        this.filterPredicate1 = filterPredicate1;
    }

    public FilterPredicate<T> getFilterPredicate2() {
        return filterPredicate2;
    }

    public void setFilterPredicate2(FilterPredicate<T> filterPredicate2) {
        this.filterPredicate2 = filterPredicate2;
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

