package nsu.project.generators;

import javax.crypto.Cipher;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;

//TO-DO:
    //Add documentation
    //Test
public class SerialNumberGenerator {
    public static <T> String generate(Class<T> tClass) throws NoSuchAlgorithmException {
        List<String> fieldStrings = Arrays.stream(tClass.getFields())
                .map(f -> f.getName() + f.getType().getName())
                .toList();
        String toDigest = String.join("_", fieldStrings);
        return digester(toDigest);
    }
    private static String digester(String string) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        return Arrays.toString(md.digest(string.getBytes(StandardCharsets.UTF_8)));
    }
}
