package nsu.project.serialize;

import nsu.project.deserialise.Building;
import nsu.project.deserialise.Person;
import nsu.project.generators.ObjectIdGenerator;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SerializerPrototype {
    private static String FILE_NAME = "src/main/java/nsu/project/deserialise/data1.json";
    private static Map<String, Boolean> visitedPeople = new HashMap<>();
    private static Map<String, Boolean> visitedBuildings = new HashMap<>();

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
            // Чтение содержимого файла JSON
            String json_data = new String(Files.readAllBytes(Paths.get(FILE_NAME)));

            JSONObject data;
            // Парсинг JSON-данных
            if (json_data.isBlank()) {
                data = new JSONObject("{\"Person\":[{}],\"Building\":[{}]}");
            } else {
                data = new JSONObject(json_data);
            }
            // Создание объектов Person и Building
            serialize(personList, buildings, data);

            // Вывод информации о людях и зданиях


        } catch (IOException e) {
            System.err.println("Failed to read file: " + e.getMessage());
        }
    }

    public static void serialize(List<Person> personList, List<Building> buildings, JSONObject data) throws IOException {
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

    public static void personSerialize(Person person, JSONObject data) throws IOException {
        JSONObject peopleObject = data.getJSONArray("Person").getJSONObject(0);

        String personKey = String.valueOf(ObjectIdGenerator.generate(person));

        JSONObject personJSON = new JSONObject();
        personJSON.put("name", person.getName());
        personJSON.put("surname", person.getSurname());

        if (person.getHome() != null) {
            int buildingKey = ObjectIdGenerator.generate(person.getHome());
            personJSON.remove("home");
            personJSON.put("home", buildingKey);
        } else {
            personJSON.remove("home");
        }

        for (String key : peopleObject.keySet()) {
            if (key.equals(personKey)) {
                if (visitedPeople.containsKey(personKey) || visitedPeople.get(personKey)) {
                    continue;
                }
                peopleObject.remove(key);
            }
            peopleObject.put(key, personJSON);
        }

        data.accumulate("Person", personJSON);
        visitedPeople.put(personKey, true);
    }

    public static void buildingSerialize(Building building, JSONObject data) throws IOException {
        JSONObject buildingsObject = data.getJSONArray("Buildings").getJSONObject(0);

        String buildingKey = String.valueOf(ObjectIdGenerator.generate(building));
        JSONObject buildingJSON = new JSONObject();
        buildingJSON.put("address", building.getAddress());

        for (String key : buildingsObject.keySet()) {
            if (key.equals(buildingKey)) {
                if (visitedBuildings.containsKey(buildingKey) || visitedBuildings.get(buildingKey)) {
                    continue;
                }
                buildingsObject.remove(key);
            }
            buildingsObject.put(key, buildingJSON);
        }

        if (building.getCitizens() != null) {
            buildingJSON.remove("citizens");
            List<Integer> personKeys = new ArrayList<>();
            for (Person citizen : building.getCitizens()) {
                int personKey = ObjectIdGenerator.generate(citizen);
                personKeys.add(personKey);
            }
            buildingJSON.put("citizens", personKeys);
        } else {
            buildingJSON.remove("home");
        }

        //data.remove("Building");
        data.accumulate("Building", buildingJSON);
        visitedBuildings.put(buildingKey, true);
    }
}
