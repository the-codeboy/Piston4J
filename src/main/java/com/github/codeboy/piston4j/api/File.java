package com.github.codeboy.piston4j.api;

public class File {
    private String name,content;

    public File(String content) {
        this.content = content;
    }

    public File(String name, String content) {
        this.name = name;
        this.content = content;
    }
}
