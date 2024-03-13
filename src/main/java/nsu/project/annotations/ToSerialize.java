package nsu.project.annotations;

public @interface ToSerialize {
    long serialVersionID() default 0L;
    long objectId() default 0L;
}
