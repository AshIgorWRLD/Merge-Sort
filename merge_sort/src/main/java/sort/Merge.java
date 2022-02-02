package sort;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import utils.fileoperations.FileLister;
import utils.fileoperations.FileOpener;
import utils.fileoperations.FileOpenerString;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@AllArgsConstructor
public class Merge {

    private final String fileSeparator = System.getProperty("file.separator");
    private final String pathToOutDirectory;
    private final String pathToTmpDirectory;
    private final String outFileName;
    private final boolean lowHigh;
    private final boolean isInt;

    public void sort() throws IOException {
        if (isInt) {
            mergeSort();
        } else {
            mergeSortString();
        }
    }

    public void mergeSort() throws IOException {
        Path path = Paths.get(pathToTmpDirectory);
        List<Path> paths = FileLister.listFiles(path);
        AtomicInteger filesCounter = new AtomicInteger();
        paths.forEach(x -> filesCounter.getAndIncrement());
        Vector<FileOpener> fileVector = new Vector<>();

        paths.forEach(x -> {
            try {
                fillInf(x, fileVector);
            } catch (IOException e) {
                log.error("DATA ERROR", e);
            }
        });
        String relativePath = pathToOutDirectory
                + fileSeparator + outFileName;
        File result_file = new File(relativePath);
        FileOutputStream fos = new FileOutputStream(result_file);
        PrintStream printStream = new PrintStream(fos);
        while (!fileVector.isEmpty()) {
            sorting(fileVector, printStream);
        }
    }

    public void mergeSortString() throws IOException {
        Path path = Paths.get(pathToTmpDirectory);
        List<Path> paths = FileLister.listFiles(path);
        AtomicInteger filesCounter = new AtomicInteger();
        paths.forEach(x -> filesCounter.getAndIncrement());
        Vector<FileOpenerString> fileVector = new Vector<>();

        paths.forEach(x -> {
            try {
                fillInfString(x, fileVector);
            } catch (IOException e) {
                log.error("DATA ERROR", e);
            }
        });
        String relativePath = pathToOutDirectory
                + fileSeparator + outFileName;
        File result_file = new File(relativePath);
        FileOutputStream fos = new FileOutputStream(result_file);
        PrintStream printStream = new PrintStream(fos);
        while (!fileVector.isEmpty()) {
            sortingString(fileVector, printStream);
        }
    }

    public void fillInf(Path path,
                        Vector<FileOpener> fileVector) throws IOException {
        FileOpener new_fo = new FileOpener();
        new_fo.setFile(new File(pathToTmpDirectory, path.getFileName().toString()));
        fileVector.add(new_fo);
    }

    public void fillInfString(Path path,
                              Vector<FileOpenerString> fileVector) throws IOException {
        FileOpenerString new_fo = new FileOpenerString();
        new_fo.setFile(new File(pathToTmpDirectory, path.getFileName().toString()));
        fileVector.add(new_fo);
    }

    public void sorting(Vector<FileOpener> fileVector,
                        PrintStream printStream) throws IOException {
        boolean[] isEmpty = new boolean[fileVector.size()];
        for (int i = 0; i < fileVector.size(); i++) {
            isEmpty[i] = false;
        }
        AtomicInteger minimum = new AtomicInteger();
        AtomicBoolean firstTry = new AtomicBoolean(true);
        AtomicInteger idx = new AtomicInteger();

        fileVector.forEach(x -> {
            Integer new_number = x.getTopNumber();
            if (new_number == null) {
                isEmpty[fileVector.indexOf(x)] = true;
            } else {
                if (firstTry.get()) {
                    minimum.getAndSet(x.getTopNumber());
                    firstTry.getAndSet(false);
                    idx.getAndSet(fileVector.indexOf(x));
                } else {
                    if (lowHigh) {
                        if (x.getTopNumber() < minimum.get()) {
                            minimum.getAndSet(x.getTopNumber());
                            idx.getAndSet(fileVector.indexOf(x));
                        }
                    } else {
                        if (x.getTopNumber() > minimum.get()) {
                            minimum.getAndSet(x.getTopNumber());
                            idx.getAndSet(fileVector.indexOf(x));
                        }
                    }
                }
            }
        });

        fileVector.get(idx.get()).readNextNumber();

        for (int i = 0; i < fileVector.size(); i++) {
            if (isEmpty[i]) {
                fileVector.get(i).close();
                fileVector.remove(i);
            }
        }
        if (!firstTry.get()) {
            printStream.println(minimum.get());
        }
    }


    public void sortingString(Vector<FileOpenerString> fileVector,
                              PrintStream printStream) throws IOException {
        boolean[] isEmpty = new boolean[fileVector.size()];
        for (int i = 0; i < fileVector.size(); i++) {
            isEmpty[i] = false;
        }
        AtomicInteger minimum = new AtomicInteger();
        AtomicReference<String> minStr = new AtomicReference<String>();
        AtomicBoolean firstTry = new AtomicBoolean(true);
        AtomicInteger idx = new AtomicInteger();

        fileVector.forEach(x -> {
            String new_str = x.getTopString();
            if (new_str == null) {
                isEmpty[fileVector.indexOf(x)] = true;
            } else {
                if (firstTry.get()) {
                    minimum.getAndSet(x.getTopNumber());
                    minStr.getAndSet(x.getTopString());
                    firstTry.getAndSet(false);
                    idx.getAndSet(fileVector.indexOf(x));
                } else {
                    if (lowHigh) {
                        if (x.getTopNumber() < minimum.get()) {
                            minimum.getAndSet(x.getTopNumber());
                            minStr.getAndSet(x.getTopString());
                            idx.getAndSet(fileVector.indexOf(x));
                        }
                    } else {
                        if (x.getTopNumber() > minimum.get()) {
                            minimum.getAndSet(x.getTopNumber());
                            minStr.getAndSet(x.getTopString());
                            idx.getAndSet(fileVector.indexOf(x));
                        }
                    }
                }
            }
        });
        fileVector.get(idx.get()).readNextNumber();

        for (int i = 0; i < fileVector.size(); i++) {
            if (isEmpty[i]) {
                fileVector.get(i).close();
                fileVector.remove(i);
            }
        }
        if (!firstTry.get()) {
            printStream.println(minStr.get());
        }
    }
}
