package nsu.project.serialize;

import nsu.project.deserialise.Building;
import nsu.project.deserialise.Person;
import nsu.project.generators.ObjectIdGenerator;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SerializerPrototype {
    public static void main(String[] args) {
        try {
            // Чтение содержимого файла JSON
            String json_data = new String(Files.readAllBytes(Paths.get("data.json")));

            // Парсинг JSON-данных
            JSONObject data = new JSONObject(json_data);

            // Создание объектов Person и Building
            personSerialize(data);
            buildingSerialize(data);

            // Вывод информации о людях и зданиях
            try (FileWriter writer = new FileWriter("output.txt")) {
                writer.write("People:\n");
                for (Person person : peopleMap.values()) {
                    writer.write(person.toString() + "\n");
                }

                writer.write("\nBuildings:\n");
                for (Building building : buildingsMap.values()) {
                    writer.write(building.toString() + "\n");
                }
            }

        } catch (IOException e) {
            System.err.println("Failed to read file: " + e.getMessage());
        }
    }

    public static void personSerialize(List<Person> list) {
        JSONArray peopleJSONObject = new JSONArray(list);
        for (int i = 0; i < peopleJSONObject.length(); i++) {
            JSONObject person = peopleJSONObject.getJSONObject(i);
            int id = ObjectIdGenerator.generate(list.get(i));
            JSONObject personInfo = peopleObject.getJSONObject(personKey);
            String personName = personInfo.getString("name");
            String personSurname = personInfo.getString("surname");
            int personAge = personInfo.getInt("age");
            String personHomeId = personInfo.optString("home", null); // используем optString для получения значения или null
            Building homeBuilding = null;

            if (personHomeId != null) {
                JSONObject buildingInfo = buildingsObject.optJSONObject(personHomeId);
                if (buildingInfo != null) {
                    String buildingAddress = buildingInfo.getString("address");
                    // здесь также можно получить список жителей здания и передать его в конструктор Building
                    homeBuilding = new Building(buildingAddress, null);
                }
            }

            visitedPeople.put(personKey, true);
            Person person = new Person(personName, personSurname, personAge, homeBuilding);
            peopleMap.put(personKey, person);
        }
    }


    public static void buildingSerialize(List<Building> list) {
        JSONObject buildingsObject = data.getJSONArray("Buildings").getJSONObject(0);
        for (String buildingKey : buildingsObject.keySet()) {
            if (buildingsMap.containsKey(buildingKey) || visitedBuildings.containsKey(buildingKey)) {
                continue;
            }

            JSONObject buildingInfo = buildingsObject.getJSONObject(buildingKey);
            String buildingAddress = buildingInfo.getString("address");
            List<Person> buildingResidents = new ArrayList<>();
            JSONArray citizensIds = buildingInfo.getJSONArray("citizens");
            for (int i = 0; i < citizensIds.length(); i++) {
                String citizenId = citizensIds.getString(i);
                Person resident = peopleMap.get(citizenId);
                if (resident != null) {
                    buildingResidents.add(resident);
                }
            }

            visitedBuildings.put(buildingKey, true);
            Building building = buildingResidents.isEmpty() ? new Building(buildingAddress, null) : new Building(buildingAddress, buildingResidents);
            buildingsMap.put(buildingKey, building);
        }
    }
}
