package nsu.project.generators;

import nsu.project.deserialise.Building;
import nsu.project.deserialise.Person;

import java.util.Objects;
import java.util.Random;


public class ObjectIdGenerator {
    private static Random random = new Random(123456789);

    public static int generatePerson(Person person) {
        return Objects.hash(person.getName(), person.getSurname(), person.getAge(), person.getHome());
    }

    public static int generateBuilding(Building building) {
        return Objects.hash(building.getAddress());
    }
}