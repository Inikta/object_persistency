package nsu.project.serialization;

public @interface ToSerialize {
    long serialVersionID() default 0L;
}
