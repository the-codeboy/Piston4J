package com.github.codeboy.piston4j.api;

public class ExecutionResult {
    private String language, version;
    private ExecutionOutput run,compile;

    //region getter
    public String getLanguage() {
        return language;
    }

    public String getVersion() {
        return version;
    }

    public ExecutionOutput getOutput() {
        return run;
    }

    public ExecutionOutput getCompileOutput() {
        return compile;
    }

    @Override
    public String toString() {
        return "ExecutionResult{" +
                "language='" + language + '\'' +
                ", version='" + version + '\'' +
                (compile!=null?", compile='" + compile + '\'':"")+
                ", run=" + run +
                '}';
    }
    //endregion

}
