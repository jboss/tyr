/*
 * Copyright 2019 Red Hat, Inc, and individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
