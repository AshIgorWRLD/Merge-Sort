package utils.fileoperations;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InFileString {

    private String string;
    private Integer number;

    public InFileString(String string) {
        this.string = string;
        this.number = string.length();
    }

    public void putString(String str) {
        this.string = str;
        this.number = str.length();
    }
}
