package utils.fileoperations;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import utils.quicksort.QuickSort;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@AllArgsConstructor
public class FileParser {

    private static final int ARRAY_LEN = 100;

    private final String fileSeparator;
    private final QuickSort quickSort;
    private final String pathToInputDirectory;
    private final String command;
    private final boolean isInt;
    private final boolean lowHigh;

    private int linesToSkip;

    public FileParser(String pathToDirectory, String cmd, boolean lowhigh, boolean isint) {
        this.pathToInputDirectory = pathToDirectory;
        this.command = cmd;
        this.lowHigh = lowhigh;
        this.isInt = isint;
        this.linesToSkip = 0;
        this.fileSeparator = System.getProperty("file.separator");
        this.quickSort = new QuickSort();
    }

    public boolean containsNumber(String string) {
        return string.matches(".*\\d+.*");
    }

    public List<String> openCurrentFiles() throws IOException {
        String[] sub_str = command.split(";");
        Path path = Paths.get(pathToInputDirectory);
        List<Path> paths = FileLister.listFiles(path);
        List<String> filesName = new LinkedList<String>();
        AtomicBoolean normalFile = new AtomicBoolean(false);

        paths.forEach(x -> {
            for (int i = 0; i < sub_str.length; i++) {
                if (sub_str[i].split("\\.").length < 2) {
                    log.error("WRONG FILE NAMES");
                    System.exit(0);
                }
                if (x.getFileName().toString().equals(sub_str[i])) {
                    filesName.add(sub_str[i]);
                    normalFile.getAndSet(true);
                }
            }
        });

        if (!normalFile.get()) {
            log.error("NO SUCH INPUT FILES");
            System.exit(0);
        }

        return filesName;
    }


    public void parse() throws IOException {
        List<String> filesName = openCurrentFiles();
        if (filesName == null) {
            log.error("NO SUCH FILES");
            return;
        }
        AtomicInteger file_idx = new AtomicInteger();

        filesName.forEach(x -> {
            boolean go_then = false;
            while (!go_then) {
                String tmp_file_name = "tmpfile" + file_idx + ".txt";
                String relativePath = "src\\main\\resources\\processFiles"
                        + fileSeparator + tmp_file_name;
                file_idx.getAndIncrement();
                File new_file = new File(relativePath);
                if (isInt) {
                    go_then = takeInf(x, new_file);
                } else {
                    go_then = takeInfString(x, new_file);
                }
            }
        });
    }

    public boolean takeInf(String fileName, File out_file) {
        boolean result_check = true;
        File in_file = new File(pathToInputDirectory, fileName);

        try {
            FileOutputStream fos = new FileOutputStream(out_file);
            PrintStream printStream = new PrintStream(fos);
            int[] tmp_arr = new int[ARRAY_LEN];
            int iter_counter = 0;
            try {
                FileReader fileReader = new FileReader(in_file);
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                String string = bufferedReader.readLine();
                for (int i = 0; i < linesToSkip; i++) {
                    string = bufferedReader.readLine();
                }
                while (string != null && result_check) {
                    String parse_symbol = " ";
                    String[] sub_str = string.split(parse_symbol);
                    for (int i = 0; i < sub_str.length; i++) {
                        if (!containsNumber(sub_str[i])) {
                            log.error("CHECK YOUR FILE OR YOUR PROGRAM KEY, YOU GOT NO MATCH " +
                                    "WITH RIGHT TYPE OF DATA");
                            System.exit(0);
                        }
                        int a = Integer.parseInt(sub_str[i]);
                        if (iter_counter != ARRAY_LEN) {
                            tmp_arr[iter_counter] = a;
                            iter_counter++;
                            linesToSkip++;
                        } else {
                            result_check = false;
                        }
                    }
                    string = bufferedReader.readLine();
                }

                quickSort.qSort(tmp_arr, 0, iter_counter - 1);
                int[] reverse_arr = new int[iter_counter];
                if (!lowHigh) {
                    for (int i = 0; i < iter_counter; i++) {
                        reverse_arr[i] = tmp_arr[iter_counter - 1 - i];
                    }
                    tmp_arr = reverse_arr;
                }
                for (int i = 0; i < iter_counter; i++) {
                    printStream.println(tmp_arr[i]);
                }
                if (!result_check) {
                    return false;
                }
                bufferedReader.close();
                fileReader.close();
            } catch (IOException e) {
                log.error("FILE READER OPENING ERROR", e);
            }
            fos.close();
        } catch (IOException e) {
            log.error("FILE OUTPUT STREAM GENERATING ERROR", e);
        }
        linesToSkip = 0;
        return true;
    }

    public boolean takeInfString(String fileName, File out_file) {
        boolean result_check = true;
        File in_file = new File(pathToInputDirectory, fileName);

        try {
            FileOutputStream fos = new FileOutputStream(out_file);
            PrintStream printStream = new PrintStream(fos);
            InFileString[] tmp_arr = new InFileString[ARRAY_LEN];
            for (int i = 0; i < ARRAY_LEN; i++) {
                tmp_arr[i] = new InFileString("");
            }
            int iter_counter = 0;
            try {
                FileReader fileReader = new FileReader(in_file);
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                String string = bufferedReader.readLine();
                for (int i = 0; i < linesToSkip; i++) {
                    string = bufferedReader.readLine();
                }
                while (string != null && result_check) {
                    String a_str = string;
                    if (iter_counter != ARRAY_LEN) {
                        tmp_arr[iter_counter].putString(a_str);
                        iter_counter++;
                        linesToSkip++;
                    } else {
                        result_check = false;
                    }
                    string = bufferedReader.readLine();
                }
                quickSort.qSortString(tmp_arr, 0, iter_counter - 1);
                InFileString[] reverse_arr = new InFileString[iter_counter];
                if (!lowHigh) {
                    for (int i = 0; i < iter_counter; i++) {
                        reverse_arr[i] = tmp_arr[iter_counter - 1 - i];
                    }
                    tmp_arr = reverse_arr;
                }
                for (int i = 0; i < iter_counter; i++) {
                    printStream.println(tmp_arr[i].getString());
                }
                if (!result_check) {
                    return false;
                }
                bufferedReader.close();
                fileReader.close();
            } catch (IOException e) {
                log.error("FILE READER OPENING ERROR", e);
            }
            fos.close();
        } catch (IOException e) {
            log.error("FILE OUTPUT STREAM GENERATING ERROR", e);
        }
        linesToSkip = 0;
        return true;
    }
}
