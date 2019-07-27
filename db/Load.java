package db;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by Anthony Nguyen on 3/3/2017.
 */

public class Load {
    private String ff;

    public Load(String filepath) throws IOException {
        this.ff = filepath;
    }

    public String[] openFile() throws IOException {
        FileReader read = new FileReader(ff);
        BufferedReader textReader = new BufferedReader(read);

        String[] textData = new String[readLines()];

        for (int i = 0; i < textData.length; i++) {
            textData[i] = textReader.readLine();
        }

        textReader.close();
        return textData;
    }

    int readLines() throws IOException {
        FileReader filetoRead = new FileReader(ff);
        BufferedReader bf = new BufferedReader(filetoRead);

        String aLine;
        int numberLines = 0;

        while ((aLine = bf.readLine()) != null) {
            numberLines++;
        }

        bf.close();
        return numberLines;
    }
}

