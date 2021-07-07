package com.github.codeboy.piston4j.api;

public class ExecutionRequest {
    private String language, version;
    private CodeFile[] files;
    private String stdin = "";
    private String[] args = {};
    private int compile_timeout = 10000,
            run_timeout = 3000,
            compile_memory_limit = -1,
            run_memory_limit = -1;

    /**
     * @param language the language of the request
     * @param version  the version of the programming language
     * @param codeFiles    Files used. Must be at least one if this request should be executed by {@link Piston#execute(ExecutionRequest)}
     */
    public ExecutionRequest(String language, String version, CodeFile... codeFiles) {
        this.language = language;
        this.version = version;
        this.files = codeFiles;
    }

    /**
     * @return the language used in the request
     */
    public String getLanguage() {
        return language;
    }

    /**
     * @param language the language used in the request
     * @return this for chaining
     */
    public ExecutionRequest setLanguage(String language) {
        this.language = language;
        return this;
    }

    /**
     * @return the version of the language used in the request
     */
    public String getVersion() {
        return version;
    }

    /**
     * @param version the version of the language used in the request
     * @return this for chaining
     */
    public ExecutionRequest setVersion(String version) {
        this.version = version;
        return this;
    }

    /**
     * @return the files used in this request
     */
    public CodeFile[] getFiles() {
        return files;
    }

    /**
     * @param codeFiles the codeFiles used in this request. The first element is the file that gets executed
     * @return this for chaining
     */
    public ExecutionRequest setFiles(CodeFile... codeFiles) {
        this.files = codeFiles;
        return this;
    }

    /**
     * @return the stdin used in this request
     */
    public String getStdin() {
        return stdin;
    }

    /**
     * @param stdin the stdin used in this request
     * @return this for chaining
     */
    public ExecutionRequest setStdin(String stdin) {
        this.stdin = stdin;
        return this;
    }

    /**
     * @return the arguments used when running the code
     */
    public String[] getArgs() {
        return args;
    }

    /**
     * @param args the arguments used when running the code
     * @return this for chaining
     */
    public ExecutionRequest setArgs(String... args) {
        this.args = args;
        return this;
    }

    /**
     * @return The maximum time allowed for the compile stage to finish before bailing out in millisecond
     */
    public int getCompile_timeout() {
        return compile_timeout;
    }

    /**
     * @param compile_timeout The maximum time allowed for the compile stage to finish before bailing out in millisecond
     * @return this for chaining
     */
    public ExecutionRequest setCompile_timeout(int compile_timeout) {
        this.compile_timeout = compile_timeout;
        return this;
    }

    /**
     * @return The maximum time allowed for the run stage to finish before bailing out in milliseconds
     */
    public int getRun_timeout() {
        return run_timeout;
    }

    /**
     * @param run_timeout The maximum time allowed for the run stage to finish before bailing out in milliseconds
     * @return this for chaining
     */
    public ExecutionRequest setRun_timeout(int run_timeout) {
        this.run_timeout = run_timeout;
        return this;
    }

    /**
     * @return The maximum amount of memory the compile stage is allowed to use in bytes
     */
    public int getCompile_memory_limit() {
        return compile_memory_limit;
    }

    /**
     * @param compile_memory_limit TThe maximum amount of memory the compile stage is allowed to use in bytes
     * @return this for chaining
     */
    public ExecutionRequest setCompile_memory_limit(int compile_memory_limit) {
        this.compile_memory_limit = compile_memory_limit;
        return this;
    }

    /**
     * @return The maximum amount of memory the run stage is allowed to use in bytes
     */
    public int getRun_memory_limit() {
        return run_memory_limit;
    }

    /**
     * @param run_memory_limit The maximum amount of memory the run stage is allowed to use in bytes
     * @return this for chaining
     */
    public ExecutionRequest setRun_memory_limit(int run_memory_limit) {
        this.run_memory_limit = run_memory_limit;
        return this;
    }

    /**
     * @return if this request can be sent
     */
    public boolean isValid() {
        return files.length > 0;
    }

}
