import java.nio.file.Files;
import java.nio.file.Paths;

public class Validator {
    public boolean isFileExists(String filePath) {
        return Files.exists(Paths.get(filePath));
    }

    public boolean isValidKey(int key, char[] alphabet) {
        return key >= 0 && key < alphabet.length;
    }
} 