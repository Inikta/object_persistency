package nsu.project.deserialise;

import nsu.project.annotations.ToSerialize;

import java.util.List;

@ToSerialize
public class Building {
    private String address;
    private List<Person> citizens;

    public Building(String address, List<Person> citizens) {
        this.address = address;
        this.citizens = citizens;
    }


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<Person> getCitizens() {
        return citizens;
    }

    public void setCitizens(List<Person> citizens) {
        this.citizens = citizens;
    }

    @Override
    public String toString() {
        return "Building{" +
                "address='" + address + '\'' +
                ", citizens=" + (citizens != null ? citizens.toString() : null) +
                '}';
    }
}
