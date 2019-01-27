package lt.metasite.filereader.service;

import lt.metasite.filereader.Dto.CalculationResult;
import lt.metasite.filereader.factories.CharacterHolderFactory;
import lt.metasite.filereader.factories.FileRepo;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

@Service
public class FilesRepositoryServiceImpl implements FileRepoReader, FileRepoSaver {

    private CharacterHolderFactory factory;
    private SimpMessagingTemplate template;
    private FilesUploadedReader filesUploadedReader;
    private ExecutorService executorService;

    public FilesRepositoryServiceImpl(CharacterHolderFactory factory, SimpMessagingTemplate template, FilesUploadedReader filesUploadedReader, @Qualifier("repo") ExecutorService executorService) {
        this.factory = factory;
        this.template = template;
        this.filesUploadedReader = filesUploadedReader;
        this.executorService = executorService;
    }

    @Override
    public Map<Enum, Map<String, Integer>> countInAll() throws InterruptedException {
        List<Callable<Map<Enum, Map<String, Integer>>>> callableTasks = new ArrayList<>();
        Map<Enum, Map<String, Integer>> bigMap = new HashMap<>();
        Arrays.stream(FileRepo.values()).forEach(x -> {
            callableTasks.add(()-> readFrom(x));
        });
        List<Future<Map<Enum, Map<String, Integer>>>> futures = executorService.invokeAll(callableTasks);
        futures.forEach(f -> {
            try {
                bigMap.putAll(f.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        });
        template.convertAndSend("/topic/messages", new CalculationResult(bigMap));
        return bigMap;
    }

    @Override
    public Map<Enum, Map<String, Integer>> readFrom(Enum fileName) throws FileNotFoundException {
        Map<Enum, Map<String, Integer>> bigMap = new HashMap<>();
        Map<String, Integer> map = new HashMap<>();
        java.io.File file = new java.io.File("repo/"+fileName.toString());
        if(file.exists()){
            Scanner sc = new Scanner(file);
            while (sc.hasNext()) {
                String key = sc.next();
                map.compute(key, (k, v) -> v ==null ? 1 : ++v);
            }
            bigMap.put(fileName, map);
        }
        return bigMap;
    }

    @Override
    public void writeToAll() throws IOException, InterruptedException {
        Map<Character, List<String>> listMap = filesUploadedReader.readAllFromTmp();
        writeAll(listMap);
        countInAll();
    }

    @Override
    public void writeToAll(Map<Character, List<String>> listMap) throws IOException {
        writeAll(listMap);
    }

    private void writeAll(Map<Character, List<String>> listMap){
        listMap.entrySet().stream().forEach(x -> {
            Enum fileName = factory.getFileName(x.getKey());
            if(fileName!=null){
                executorService.execute(() -> {
                    try {
                        writeTo(fileName, x.getValue());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

            }
        });
    }

    @Override
    public void writeTo(Enum fileName, List<String> words) throws IOException {
        File file = new File("repo/"+fileName.toString());
        file.getParentFile().mkdirs();
        BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
        words.stream().forEach(x -> {
                    try {
                        writer.append(' ').append(x);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        );
        writer.close();
    }
}
