package nsu.project.deserialise;

import nsu.project.annotations.ToSerialize;
import org.json.JSONPropertyIgnore;

@ToSerialize
public class Person {
    private String name;
    private String surname;
    private Integer age;
    private Building home;

    public Person(String name, String surname, Integer age, Building home) {
        this.name = name;
        this.surname = surname;
        this.age = age;
        this.home = home;
    }

    public Person(String name, String surname, Integer age) {
        this.name = name;
        this.surname = surname;
        this.age = age;
    }

    // Геттеры и сеттеры для полей
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    //@JSONPropertyIgnore
    public Building getHome() {
        return home;
    }

    public void setHome(Building home) {
        this.home = home;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Person{name='").append(name).append('\'');
        sb.append(", surname='").append(surname).append('\'');
        sb.append(", age=").append(age);
        sb.append(", home=").append(home != null ? home.toString() : "null");
        sb.append('}');
        return sb.toString();
    }

}