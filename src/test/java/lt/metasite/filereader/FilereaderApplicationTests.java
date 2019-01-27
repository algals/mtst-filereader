package lt.metasite.filereader;

import lt.metasite.filereader.factories.CharacterHolderFactory;
import lt.metasite.filereader.factories.FileRepo;
import lt.metasite.filereader.service.FileRepoReader;
import lt.metasite.filereader.service.FileRepoSaver;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FilereaderApplicationTests {

	@Autowired
	CharacterHolderFactory factory;

	@Autowired
	FileRepoReader fileRepoReader;

	@Autowired
	FileRepoSaver fileRepoSaver;

	@Test
	public void contextLoads() throws IOException, InterruptedException {
		fileRepoSaver.writeToAll();
	}

	@Test
	public void readFromTest() throws FileNotFoundException {
		Map<Enum, Map<String, Integer>> map = fileRepoReader.readFrom(FileRepo.From_A_to_G);
	}

	@Test
	public void readAllFromTest() throws FileNotFoundException, InterruptedException {
		Map<Enum, Map<String, Integer>> map = fileRepoReader.countInAll();
	}

	@Test
	public void readFromTmpTest() throws FileNotFoundException, InterruptedException {
//		Map<Character, List<String>> map = filesConsumer.;
	}
}

