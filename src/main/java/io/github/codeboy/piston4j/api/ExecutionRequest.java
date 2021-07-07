package io.github.codeboy.piston4j.api;

public class ExecutionRequest {
    private String language,version;
    private File[]files;
    private String stdin="";
    private String[]args={};
    private int compile_timeout=10000,
            run_timeout=3000,
            compile_memory_limit=-1,
            run_memory_limit=-1;

    public ExecutionRequest(String language, String version, File...files) {
        this.language = language;
        this.version = version;
        this.files = files;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getVersion() {
        return version;
    }

    public ExecutionRequest setVersion(String version) {
        this.version = version;
        return this;
    }

    public File[] getFiles() {
        return files;
    }

    public ExecutionRequest setFiles(File[] files) {
        this.files = files;
        return this;
    }

    public String getStdin() {
        return stdin;
    }

    public ExecutionRequest setStdin(String stdin) {
        this.stdin = stdin;
        return this;
    }

    public String[] getArgs() {
        return args;
    }

    public ExecutionRequest setArgs(String... args) {
        this.args = args;
        return this;
    }

    public int getCompile_timeout() {
        return compile_timeout;
    }

    public ExecutionRequest setCompile_timeout(int compile_timeout) {
        this.compile_timeout = compile_timeout;
        return this;
    }

    public int getRun_timeout() {
        return run_timeout;
    }

    public ExecutionRequest setRun_timeout(int run_timeout) {
        this.run_timeout = run_timeout;
        return this;
    }

    public int getCompile_memory_limit() {
        return compile_memory_limit;
    }

    public ExecutionRequest setCompile_memory_limit(int compile_memory_limit) {
        this.compile_memory_limit = compile_memory_limit;
        return this;
    }

    public int getRun_memory_limit() {
        return run_memory_limit;
    }

    public ExecutionRequest setRun_memory_limit(int run_memory_limit) {
        this.run_memory_limit = run_memory_limit;
        return this;
    }
}
