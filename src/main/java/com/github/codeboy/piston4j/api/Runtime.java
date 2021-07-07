package com.github.codeboy.piston4j.api;

import java.util.Arrays;

public class Runtime {
    private String language;
    private String version;
    private String[]aliases;
    private Piston piston;

    public boolean hasAlias(String alias){
        return language.equalsIgnoreCase(alias)|| Arrays.stream(aliases).anyMatch(alias::equalsIgnoreCase);
    }

    protected void setPiston(Piston piston) {
        this.piston = piston;
    }

    public ExecutionResult execute(File... files){
        ExecutionRequest request=new ExecutionRequest(language,version,files);
        return piston.execute(request);
    }

    @Override
    public String toString() {
        return "Runtime{" +
                "language='" + language + '\'' +
                ", version='" + version + '\'' +
                ", aliases=" + Arrays.toString(aliases) +
                '}';
    }

    public String getLanguage() {
        return language;
    }

    public String getVersion() {
        return version;
    }

    public String[] getAliases() {
        return aliases;
    }

    public Piston getPiston() {
        return piston;
    }
}