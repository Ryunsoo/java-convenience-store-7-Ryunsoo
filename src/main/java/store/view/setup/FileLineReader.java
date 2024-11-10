package store.view.setup;

import store.exception.StoreSetUpFailException;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

public class FileLineReader {

    public List<String> readLines(String fileName) {
        try (Stream<String> lines = Files.lines(getFilePath(fileName))) {
            return lines.toList();
        } catch (IOException | URISyntaxException e) {
            throw new StoreSetUpFailException("파일 읽기에 실패했습니다.");
        }
    }

    private Path getFilePath(String fileName) throws URISyntaxException {
        final URL fileUrl = ClassLoader.getSystemResource(fileName);
        if (fileUrl == null) {
            throw new StoreSetUpFailException("파일을 읽을 수 없습니다.");
        }
        URI fileUrlURI = fileUrl.toURI();
        return Path.of(fileUrlURI);
    }

}
