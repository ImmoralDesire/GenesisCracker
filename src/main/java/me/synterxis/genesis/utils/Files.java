package me.synterxis.genesis.utils;

import java.io.*;

public class Files {

    public static int getLines(InputStream inputStream) throws IOException {
        InputStream is = new BufferedInputStream(inputStream);
        try {
            byte[] c = new byte[1024];
            int count = 0;
            int readChars = 0;
            boolean empty = true;
            while ((readChars = is.read(c)) != -1) {
                empty = false;
                for (int i = 0; i < readChars; ++i) {
                    if (c[i] == '\n') {
                        ++count;
                    }
                }
            }
            return (count == 0 && !empty) ? 1 : count + 1;
        } finally {
            is.close();
        }
    }
}
