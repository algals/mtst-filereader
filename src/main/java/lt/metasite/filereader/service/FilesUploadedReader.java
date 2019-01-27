package lt.metasite.filereader.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface FilesUploadedReader {
    Map<Character, List<String>> readAllFromTmp() throws InterruptedException;

    Map<Character, List<String>> readFrom(String filepath) throws IOException;
}
