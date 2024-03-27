package tests;

import nsu.project.main_stream.JSONDataWriterStream;

import javax.naming.InvalidNameException;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, InvalidNameException {
        JSONDataWriterStream<Person> jsonDataWriterStream = new JSONDataWriterStream<>("../test2.JSON");
        System.out.println(jsonDataWriterStream.getOldJsonChunk());
    }
}
