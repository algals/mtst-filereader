package lt.metasite.filereader.service;

import java.io.FileNotFoundException;
import java.util.Map;

public interface FileRepoReader {
    Map<Enum, Map<String, Integer>> countInAll() throws InterruptedException;

    Map<Enum, Map<String, Integer>> readFrom(Enum fileName) throws FileNotFoundException;
}
