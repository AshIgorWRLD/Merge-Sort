import lombok.extern.slf4j.Slf4j;
import sort.Merge;
import utils.argscheckers.commandchecker.CommandChecker;
import utils.argscheckers.keychecker.KeyChecker;
import utils.cleaner.Cleaner;
import utils.fileoperations.FileParser;

import java.io.IOException;

@Slf4j
public class Main {

    private static final String INPUT_FILES_PATH = "inFiles";
    private static final String PROCESS_FILES_PATH = "src\\main\\resources\\processFiles";
    private static final String OUTPUT_FILES_PATH = "outFiles";

    public static void main(String[] args) throws IOException {

        int[] idxs = {0, 1};
        boolean[] params = {true, true, false}; // first - lowHigh, second - isInt

        KeyChecker keyChecker = new KeyChecker();

        if (args.length < 2) {
            log.error("WRONG ARGUMENTS");
            return;
        }

        String strcheck1 = args[0];
        String strcheck2 = args[1];
        keyChecker.check(strcheck1, params, idxs);
        keyChecker.check(strcheck2, params, idxs);

        if (!params[2]) {
            log.error("PLEASE CHOOSE KEY '-s' OR '-i'");
            return;
        }
        if (args.length < 3) {
            log.error("TOO FEW ARGUMENTS");
            return;
        }
        if (args[idxs[0]].split("\\.").length < 2) {
            log.error("WRONG FILE NAMES");
            return;
        }
        Cleaner cleaner = new Cleaner(PROCESS_FILES_PATH);
        cleaner.clean();
        CommandChecker commandChecker = new CommandChecker();
        FileParser fileParser = new FileParser(INPUT_FILES_PATH,
                commandChecker.parseCommand(args, idxs[1]), params[0], params[1]);
        Merge mgClass = new Merge(OUTPUT_FILES_PATH, PROCESS_FILES_PATH, args[idxs[0]],
                params[0], params[1]);
        fileParser.parse();
        mgClass.sort();
        log.info("YOUR RESULT IS IN 'outFiles' DIRECTORY");
    }
}
