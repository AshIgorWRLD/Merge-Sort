package utils.cleaner;

import lombok.AllArgsConstructor;
import utils.fileoperations.FileLister;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@AllArgsConstructor
public class Cleaner {

    private final String pathToTmpDirectory;

    public void clean() throws IOException {
        Path path = Paths.get(pathToTmpDirectory);
        List<Path> paths = FileLister.listFiles(path);
        paths.forEach(x -> {
            File file = new File(pathToTmpDirectory, x.getFileName().toString());
            File newFile = new File(file.getAbsolutePath());
            newFile.delete();
        });
    }
}
