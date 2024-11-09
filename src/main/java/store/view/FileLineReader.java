package store.view;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileLineReader {

    private final ClassLoader classLoader;

    public FileLineReader() {
        this.classLoader = getClass().getClassLoader();
    }

    public List<String> readLine(String fileName) {
        List<String> fileLines = new ArrayList<>();
        try (BufferedReader bufferedReader = getBufferedReader(fileName)) {

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                fileLines.add(line);
            }

            return fileLines;
        } catch (IOException e) {
            throw new RuntimeException("파일 읽기에 실패했습니다.");
        }
    }

    private BufferedReader getBufferedReader(String fileName) throws IOException {
        InputStream inputStream = classLoader.getResourceAsStream(fileName);
        if (inputStream == null) {
            throw new FileNotFoundException(fileName);
        }

        InputStreamReader reader = new InputStreamReader(inputStream);
        return new BufferedReader(reader);

    }

}
