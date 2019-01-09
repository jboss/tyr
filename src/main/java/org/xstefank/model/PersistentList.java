package org.xstefank.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PersistentList {

    private List<String> list = new ArrayList<>();
    private File file;

    public PersistentList(File file) {
        this.file = file;
        if (file.exists()) {
            loadFileContent(file);
        } else {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new IllegalStateException("Cannot create new file with name " + file.getName(), e);
            }
        }
    }

    public PersistentList(String dirName, String fileName) {
        this(new File(dirName, fileName));
    }

    public void addUser(String username) {
        if (list.contains(username)) {
            return;
        }
        list.add(username);
        try (FileWriter fw = new FileWriter(file, true)) {
            fw.write(username + "\n");
        } catch (IOException e) {
            throw new IllegalStateException("Cannot write user to file", e);
        }
    }

    public boolean hasUsername(String  username) {
        return list.contains(username);
    }

    private void loadFileContent(File file) {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.startsWith("#")) {
                    list.add(line);
                }
            }
        } catch (IOException e) {
            throw new IllegalStateException("Cannot read file " + file.getPath(), e);
        }
    }
}
