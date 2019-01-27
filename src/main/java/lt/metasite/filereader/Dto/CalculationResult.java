package lt.metasite.filereader.Dto;

import java.util.Map;

public class CalculationResult {

    Map<Enum, Map<String, Integer>> result;

    public CalculationResult(Map<Enum, Map<String, Integer>> bigMap) {
        this.result = bigMap;
    }

    public Map<Enum, Map<String, Integer>> getResult() {
        return result;
    }

    public void setBigMap(Map<Enum, Map<String, Integer>> bigMap) {
        this.result = bigMap;
    }
}
