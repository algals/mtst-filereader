package lt.metasite.filereader.controllers;

import lt.metasite.filereader.service.FileRepoReader;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ResultController {

    private FileRepoReader fileRepoReader;

    public ResultController(FileRepoReader fileRepoReader) {
        this.fileRepoReader = fileRepoReader;
    }

    @GetMapping("/result")
    public void getResult() throws InterruptedException {
        fileRepoReader.countInAll();
    }

}
