package com.github.codeboy.piston4j.api;

public class File {
    private String name;
    private final String content;

    /**
     * creates a file without a name
     *
     * @param content the content of the file
     */
    public File(String content) {
        this.content = content;
    }

    /**
     * creates a file with name and content
     *
     * @param name    the name of the file
     * @param content the content of the file
     */
    public File(String name, String content) {
        this.name = name;
        this.content = content;
    }
}
