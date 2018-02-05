package dentiq.api.util;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileUtil {
	
		
	public static void saveFile(String uploadDir, String fileNameToBeSaved, byte[] contents) throws Exception {
		Path path = Paths.get(uploadDir + fileNameToBeSaved);
        Files.write(path, contents);
	}

}
