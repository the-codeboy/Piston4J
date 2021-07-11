package com.github.codeboy.piston4j.api;

public class ExecutionOutput {
    private String stdout, stderr, output;
    private int code;
    private String signal;

    public String getStdout() {
        return stdout;
    }

    public String getStderr() {
        return stderr;
    }

    public String getOutput() {
        return output;
    }

    public int getCode() {
        return code;
    }

    public String getSignal() {
        return signal;
    }

    @Override
    public String toString() {
        return "ExecutionOutput{" +
                "stdout='" + stdout + '\'' +
                ", stderr='" + stderr + '\'' +
                ", output='" + output + '\'' +
                ", code=" + code +
                ", signal='" + signal + '\'' +
                '}';
    }
}
