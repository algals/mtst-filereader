package lt.metasite.filereader.factories;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CharacterHolderFactory {

    private Map<FileRepo, List<Character>> globalMap;

    public CharacterHolderFactory() {
        globalMap = new HashMap<>();
        Arrays.stream(FileRepo.values()).forEach(x -> globalMap.putAll(new CharacterHolder(x).getMap()));
    }

    public Enum getFileName(Character firstLetter) {
        return globalMap.entrySet().stream().filter(f -> f.getValue().contains(firstLetter)).map(k -> k.getKey()).findFirst().orElse(null);
    }
}
