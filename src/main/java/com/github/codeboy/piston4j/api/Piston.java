package com.github.codeboy.piston4j.api;


import com.github.codeboy.piston4j.exceptions.PistonException;
import com.github.codeboy.piston4j.util.Util;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class Piston {
    private final static HashMap<String, Piston> instances = new HashMap<>();
    private static Piston piston;

    private final String url;
    private final int retryLimit = 10;
    private final int retryTime = 500;
    private List<Runtime> runtimes = new ArrayList<>();
    private boolean initialised = false;
    private Thread initialisationThread;
    private String apiKey = null;

    /**
     * private to prevent creation of multiple instances for the same api
     */
    private Piston(String url) {
        this.url = url;
        initRuntimes();
    }

    /**
     * it is not recommended to use this method
     * @param language the language the code is in
     * @param code the code
     * @return the result of the program or the compile output if the result is empty
     */
    public static String run(String language, String code) {
        ExecutionResult result = getDefaultApi().execute(language, code);
        ExecutionOutput out = result.getOutput();
        String output = out.getOutput();
        if (output != null && output.length() > 0) {
            return output;
        }
        ExecutionOutput compileOutput = result.getCompileOutput();
        if (compileOutput != null && (output = compileOutput.getOutput()) != null
                && output.length() > 0) {
            return output;
        }
        return "";
    }

    /**
     * @return the default instance {@link Piston}
     */
    public static Piston getDefaultApi() {
        return piston == null ? piston = new Piston("https://emkc.org/api/v2/piston") : piston;
    }

    /**
     * @param url the url of the api
     * @return an instance of {@link Piston} using the specified api
     */
    public static Piston getInstance(String url) {
        return instances.containsKey(url) ? instances.get(url) : new Piston(url);
    }

    private void initRuntimes() {
        initialisationThread = new Thread(() -> {
            try {
                URL url = new URL(getUrl() + "/runtimes");
                String json = Util.get(url);
                Gson gson = new Gson();
                Type listType = new TypeToken<List<Runtime>>() {
                }.getType();
                List<Runtime> runtimes = gson.fromJson(json, listType);
                this.runtimes = runtimes;
                for (Runtime runtime : runtimes) {
                    runtime.setPiston(this);
                }
                initialised = true;
                instances.put(getUrl(), this);
            } catch (MalformedURLException e) {
                throw new IllegalArgumentException("\"" + url + "\" is not a valid url");
            } catch (IOException e) {
                throw new PistonException(e);
            }
        });
        initialisationThread.start();
    }

    /**
     * @param language the language of the runtime
     * @return An optional runtime with the specified language
     */
    public Optional<Runtime> getRuntime(String language) {
        return Optional.ofNullable(getRuntimeUnsafe(language));
    }

    /**
     * You should not use this method. Instead use {@link Piston#getRuntime(String)}
     *
     * @param language the language of the runtime
     * @return The runtime with the specified language. This might be null if the api doesn´t support this language
     */
    public Runtime getRuntimeUnsafe(String language) {
        for (Runtime runtime : getRuntimes()) {
            if (runtime.hasAlias(language))
                return runtime;
        }
        return null;
    }

    /**
     * @return the url where this api is located
     */
    public String getUrl() {
        return url;
    }

    /**
     * If the runtimes haven´t been retrieved yet this method waits for that before it returns
     *
     * @return A list of all the runtimes supported
     */
    public List<Runtime> getRuntimes() {
        if (!initialised && initialisationThread != null) {
            try {
                initialisationThread.join();
            } catch (InterruptedException e) {
                throw new PistonException(e);
            }
        }
        return runtimes;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    /**
     * Executes the given code using an available version of the language supplied
     *
     * @param language the language the code is in
     * @param code     the code
     * @return the result
     */
    public ExecutionResult execute(String language, String code) {
        return execute(language, new CodeFile(code));
    }

    /**
     * Executes the given code using an available version of the language supplied
     *
     * @param language the language the code is in
     * @param code     the code. Must contain at least one CodeFile
     * @return the result
     */
    public ExecutionResult execute(String language, CodeFile... code) {
        Runtime runtime = getRuntime(language)
                .orElseThrow(() -> new PistonException("Language \"" + language + "\" not found"));
        return runtime.execute(code);
    }

    /**
     * Executes the given code using the version of the language supplied
     *
     * @param language the language the code is in
     * @param version  the version of the language
     * @param code     the code
     * @return the result
     */
    public ExecutionResult execute(String language, String version, String code) {
        ExecutionRequest request = new ExecutionRequest(language, version, new CodeFile(code));
        return execute(request);
    }

    /**
     * Executes code from an {@link ExecutionRequest}
     *
     * @param request the {@link ExecutionRequest}. This must contain at least one file
     * @return the result of the Execution
     */
    public ExecutionResult execute(ExecutionRequest request) {
        if (!request.isValid())
            throw new IllegalArgumentException("Request invalid");
        HttpURLConnection con;
        try {
            URL url = new URL(getUrl() + "/execute");
            int retries = -1;
            do {
                retries++;
                if (retries >= retryLimit) {
                    throw new PistonException("Reached retry limit (" + retryLimit + ")");
                }

                Thread.sleep((long) retries * retryTime);
                con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("POST");
                if (apiKey != null)
                    con.addRequestProperty("Authorization", apiKey);
                con.addRequestProperty("Content-Type", "application/" + "POST");
                con.setDoOutput(true);
                con.setRequestProperty("User-Agent", "Piston4J");

                String requestBody = new Gson().toJson(request);
                con.setRequestProperty("Content-Length", Integer.toString(requestBody.length()));
                con.getOutputStream().write(requestBody.getBytes(StandardCharsets.UTF_8));
            } while (con.getResponseCode() == 429);
        } catch (IOException | InterruptedException e) {
            throw new PistonException(e);
        }


        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
            String inputLine;
            StringBuilder stringBuilder = new StringBuilder();
            while ((inputLine = bufferedReader.readLine()) != null) {
                stringBuilder.append(inputLine);
            }
            String result = stringBuilder.toString();
            return new Gson().fromJson(result, ExecutionResult.class);
        } catch (IOException e) {
            InputStream errorStream = con.getErrorStream();
            if (errorStream != null) {
                String error = Util.readStream(errorStream);
                JsonObject object = new Gson().fromJson(error, JsonObject.class);
                String message = error;
                if (object.has("message"))
                    message = object.get("message").getAsString();
                throw new PistonException(message);
            }
            throw new PistonException(e.getMessage());
        }
    }
}
