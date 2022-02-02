package utils.argscheckers.commandchecker;

public class CommandChecker {

    public String parseCommand(String[] args, int idx) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = idx; i < args.length; i++) {
            stringBuilder.append(args[i]).append(";");
        }
        return stringBuilder.toString();
    }
}
