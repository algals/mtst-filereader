package lt.metasite.filereader.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class FilesUploadedServiceImpl implements FilesUploadedReader {

    private ExecutorService executorService;

    public FilesUploadedServiceImpl(@Qualifier(value = "front") ExecutorService executorService) {
        this.executorService = executorService;
    }

    @Override
    public Map<Character, List<String>>  readAllFromTmp() throws InterruptedException {
        Map<Character, List<String>> map = new HashMap<>();
        java.io.File dir = new java.io.File("tmp");
        java.io.File[] files = dir.listFiles((dir1, name) -> !name.contains("locked"));
        if(files.length>0){
            List<Callable<Map<Character, List<String>>>> callableTasks = new ArrayList<>();
            Arrays.stream(files).forEach(file ->
                    callableTasks.add(
                            () -> readFrom(file.toString()))

            );
            List<Future<Map<Character, List<String>>>> futures = executorService.invokeAll(callableTasks);
            futures.forEach(f -> {
                try {
                    map.putAll(f.get());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            });
        }
        return map;
    }

    @Override
    public Map<Character, List<String>> readFrom(String filepath) throws IOException {
        Map<Character, List<String>> map = new HashMap<>();
        java.io.File file = new java.io.File(filepath);
        File renamed =new File(file.toString()+".locked");
        file.renameTo(renamed);
        Scanner sc = new Scanner(renamed);
        while (sc.hasNext()) {
            String value = sc.next();
            Character key = value.charAt(0);
            List<String> list = new ArrayList<>();
            list.add(value);
            map.merge(key, list, (list1, list2) ->
                    Stream.of(list1, list2)
                            .flatMap(Collection::stream)
                            .collect(Collectors.toList()));
        }
        renamed.delete();
        return map;
    }
}
