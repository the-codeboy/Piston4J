package com.github.codeboy.piston4j.api;

public class ExecutionResult {
    private String language, version;
    private ExecutionOutput run;

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

    @Override
    public String toString() {
        return "ExecutionResult{" +
                "language='" + language + '\'' +
                ", version='" + version + '\'' +
                ", run=" + run +
                '}';
    }
    //endregion

    public static class ExecutionOutput {
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
}
