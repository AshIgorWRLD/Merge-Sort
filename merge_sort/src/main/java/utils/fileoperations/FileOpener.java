package utils.fileoperations;

import lombok.NoArgsConstructor;

import java.io.*;

@NoArgsConstructor
public class FileOpener {

    private Integer number = null;
    private BufferedReader bufferedReader;

    public void setFile(File file) throws IOException {
        FileReader fileReader = new FileReader(file);
        this.bufferedReader = new BufferedReader(fileReader);
        String str = this.bufferedReader.readLine();
        if (str != null) {
            this.number = Integer.parseInt(str);
        }
    }

    public Integer getTopNumber() {
        return number;
    }

    public void readNextNumber() throws IOException {
        String new_str = this.bufferedReader.readLine();
        if (new_str == null) {
            this.number = null;
            return;
        }
        this.number = Integer.parseInt(new_str);
    }

    public void close() throws IOException {
        bufferedReader.close();
    }
}
