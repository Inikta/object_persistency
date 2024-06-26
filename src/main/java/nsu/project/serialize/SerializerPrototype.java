package nsu.project.serialize;

import nsu.project.annotations.ToSerialize;
import nsu.project.deserialise.Building;
import nsu.project.deserialise.Person;
import nsu.project.generators.ObjectIdGenerator;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SerializerPrototype {
    private static String FILE_NAME = "data1.json";
    private static Map<String, Boolean> visitedPeople = new HashMap<>();
    private static Map<String, Boolean> visitedBuildings = new HashMap<>();
    private static Map<String, String> peopleHome = new HashMap<>();

    public static void main(String[] args) {
        List<Person> personList = new ArrayList<>();
        List<Building> buildings = new ArrayList<>();
        int personLimit = 9;
        int buildingLimit = 3;

        for (int i = 0; i < personLimit; i++) {
            personList.add(new Person("Person" + i, "Chelovekov" + i, i + 5));
        }

        for (int i = 0; i < buildingLimit; i++) {
            Building building = new Building("Virtual Street #" + i, personList.subList((i * 3) % (personLimit + 1), ((i + 1) * 3) % (personLimit + 1)));
            buildings.add(building);
            personList.subList((i * 3) % (personLimit + 1), ((i + 1) * 3) % (personLimit + 1)).forEach(p -> p.setHome(building));
        }

        try {
            String json_data = new String(Files.readAllBytes(Paths.get(FILE_NAME)));

            JSONObject data;

            if (json_data.isBlank()) {
                data = new JSONObject("{\"Person\":[{}],\"Building\":[{}]}");
            } else {
                data = new JSONObject(json_data);
            }

            serialize(personList, buildings, data);

        } catch (IOException e) {
            System.err.println("Failed to read file: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static void serialize(List<Person> personList, List<Building> buildings, JSONObject data) throws IOException, ClassNotFoundException {
        for (Person p : personList) {
            personSerialize(p, data);
        }

        for (Building b : buildings) {
            buildingSerialize(b, data);
        }

        File file = new File(FILE_NAME);
        FileWriter fileWriter = new FileWriter(file, false);
        fileWriter.write(data.toString());
        fileWriter.close();
    }

    public static void personSerialize(Person person, JSONObject data) throws ClassNotFoundException {
        if (Person.class.getAnnotation(ToSerialize.class) == null) {
            throw new ClassNotFoundException("Class " + Person.class.getName() + " is not annotated for serialization.");
        }
        JSONObject peopleObject = data.getJSONArray("Person").getJSONObject(0);

        String personKey = String.valueOf(ObjectIdGenerator.generatePerson(person));

        JSONObject personJSON = new JSONObject();
        personJSON.put("name", person.getName());
        personJSON.put("surname", person.getSurname());
        personJSON.put("age", person.getAge());

        if (person.getHome() != null) {
            if (peopleHome.containsKey(personKey)) {
                if (peopleHome.get(personKey).isBlank()) {
                    String buildingKey = String.valueOf(ObjectIdGenerator.generateBuilding(person.getHome()));
                    personJSON.remove("home");
                    personJSON.put("home", buildingKey);
                    peopleHome.put(personKey, buildingKey);
                } else {
                    personJSON.put("home", peopleHome.get(personKey));
                }
            } else {
                String buildingKey = String.valueOf(ObjectIdGenerator.generateBuilding(person.getHome()));
                personJSON.remove("home");
                personJSON.put("home", buildingKey);
                peopleHome.put(personKey, buildingKey);
            }
        } else {
            personJSON.remove("home");
        }

        JSONObject editedPeopleObject = new JSONObject();
        for (String key : peopleObject.keySet()) {
            if (key.equals(personKey)) {
                if (visitedPeople.containsKey(personKey)) {
                    continue;
                }
            }
            editedPeopleObject.put(personKey, personJSON);
        }

        editedPeopleObject.put(personKey, personJSON);
        for (String key : editedPeopleObject.keySet()) {
            peopleObject.put(key, editedPeopleObject.get(key));
        }

        data.remove("Person");
        data.put("Person", new ArrayList<Person>());
        data.accumulate("Person", peopleObject);

        visitedPeople.put(personKey, true);
    }

    public static void buildingSerialize(Building building, JSONObject data) throws ClassNotFoundException {
        if (Building.class.getAnnotation(ToSerialize.class) == null) {
            throw new ClassNotFoundException("Class " + Person.class.getName() + " is not annotated for serialization.");
        }
        JSONObject buildingsObject = data.getJSONArray("Buildings").getJSONObject(0);

        String buildingKey = String.valueOf(ObjectIdGenerator.generateBuilding(building));
        JSONObject buildingJSON = new JSONObject();
        buildingJSON.put("address", building.getAddress());

        if (building.getCitizens() != null) {
            buildingJSON.remove("citizens");
            List<String> personKeys = new ArrayList<>();
            for (String citizenKey : peopleHome.keySet()) {
                if (peopleHome.get(citizenKey).equals(buildingKey)) {
                    personKeys.add(citizenKey);
                }
            }
            buildingJSON.put("citizens", personKeys);
        } else {
            buildingJSON.remove("home");
        }

        JSONObject editedBuildingsObject = new JSONObject();
        for (String key : buildingsObject.keySet()) {
            if (key.equals(buildingKey)) {
                if (visitedBuildings.containsKey(buildingKey)) {
                    continue;
                }
            }
            editedBuildingsObject.put(buildingKey, buildingJSON);
        }

        editedBuildingsObject.put(buildingKey, buildingJSON);
        for (String key : editedBuildingsObject.keySet()) {
            buildingsObject.put(key, editedBuildingsObject.get(key));
        }

        data.remove("Buildings");
        data.put("Buildings", new ArrayList<Building>());
        data.accumulate("Buildings", buildingsObject);

        visitedBuildings.put(buildingKey, true);
    }
}
