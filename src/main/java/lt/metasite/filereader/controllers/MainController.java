package lt.metasite.filereader.controllers;

import lt.metasite.filereader.service.FileRepoReader;
import lt.metasite.filereader.service.FileRepoSaver;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.concurrent.ExecutorService;

@Controller
public class MainController {

    private SimpMessagingTemplate template;
    private FileRepoSaver fileRepoSaver;
    private ExecutorService executorService;

    public MainController(SimpMessagingTemplate template, FileRepoSaver fileRepoSaver, @Qualifier("front") ExecutorService executorService) {
        this.template = template;
        this.fileRepoSaver = fileRepoSaver;
        this.executorService = executorService;
    }

    @GetMapping("/")
    public String startPage() {
        return "start";
    }

    @PostMapping("/upload")
    public String singleFileUpload(@RequestParam("file") MultipartFile file
                                   ) {
        try {
            byte[] bytes = file.getBytes();
            String fileName = file.getOriginalFilename()+"_"+Calendar.getInstance().getTimeInMillis();
            File directory = new File("tmp");
            if(!directory.exists()){
                directory.mkdir();
            }
            Path path = Paths.get(directory.toString()+"/"+fileName);

            Files.write(path, bytes);
            executorService.execute(() -> {
                try {
                    fileRepoSaver.writeToAll();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "redirect:/";
    }

}
