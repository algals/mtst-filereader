package lt.metasite.filereader.factories;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CharacterHolder {

    private Map<FileRepo, List<Character>> map;

    public CharacterHolder(FileRepo fileRepo) {
        map = new HashMap();
        charFiller(fileRepo);
    }

    private void charFiller(FileRepo fileRepo) {
        List<Character> list = new ArrayList<>();
        char x = fileRepo.getFrom();
        while ((int) x <= fileRepo.getTo()) {
            list.add(x);
            x++;
        }
        map.put(fileRepo, list);
    }

    public Map<FileRepo, List<Character>> getMap() {
        return new HashMap<>(map);
    }
}
