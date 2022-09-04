package com.github.codeboy.piston4j.api;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CodeFile {
    private String name;
    private final String content;

    /**
     * creates a file without a name
     *
     * @param content the content of the file
     */
    public CodeFile(String content) {
        this.content = content;
    }

    /**
     * creates a file with name and content
     *
     * @param name    the name of the file
     * @param content the content of the file
     */
    public CodeFile(String name, String content) {
        this.name = name;
        this.content = content;
    }

    /**
     * creates a {@link CodeFile} from a real file
     */
    public static CodeFile fromFile(File file) throws IOException {
        return fromFile(Paths.get(file.getPath()));
    }

    /**
     * creates a {@link CodeFile} from a real file
     */
    public static CodeFile fromFile(Path path) throws IOException {
        return fromFile(path, Charset.defaultCharset());
    }

    /**
     * creates a {@link CodeFile} from a real file
     */
    public static CodeFile fromFile(Path path, Charset encoding) throws IOException {
        byte[] encoded = Files.readAllBytes(path);
        String content = new String(encoded, encoding);
        CodeFile codeFile = new CodeFile(path.getFileName().toString(), content);
        return codeFile;
    }

    /**
     * @return the name of the file - might be null
     */
    public String getName() {
        return name;
    }

    /**
     * @return the content of the file
     */
    public String getContent() {
        return content;
    }
}
