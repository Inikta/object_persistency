package nsu.project.annotations;

public @interface ToSerialize {
    long serialVersionID() default 0L;
}
