package lt.metasite.filereader.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface FileRepoSaver {
    void writeToAll() throws IOException, InterruptedException;

    void writeToAll(Map<Character, List<String>> listMap) throws IOException;

    void writeTo(Enum fileName, List<String> words) throws IOException;
}
