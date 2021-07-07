package io.github.codeboy.piston4j.api;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import io.github.codeboy.piston4j.util.Util;
import io.github.codeboy.piston4j.exceptions.PistonException;

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
    private static final Piston piston = new Piston("https://emkc.org/api/v2/piston");

    private final String url;
    private List<Runtime> runtimes = new ArrayList<>();
    private boolean initialised=false;
    private Thread initialisationThread;
    private int retryLimit=10;
    private int retryTime=1000;

    private Piston(String url) {
        this.url = url;
        initRuntimes();
    }

    /**
     * @return the default instance {@link Piston}
     */
    public static Piston getDefaultApi() {
        return piston;
    }

    public static Piston getInstance(String url) {
        return instances.containsKey(url) ? instances.get(url) : new Piston(url);
    }

    private void initRuntimes() {
        initialisationThread=new Thread(() -> {
            try {
                URL url = new URL(getUrl() + "/runtimes");
                String json = Util.get(url);
                Gson gson = new Gson();
                Type listType = new TypeToken<List<Runtime>>() {
                }.getType();
                List<Runtime> runtimes = gson.fromJson(json, listType);
                this.runtimes=runtimes;
                for (Runtime runtime : runtimes) {
                    runtime.setPiston(this);
                }
                initialised=true;
                instances.put(getUrl(), this);
            } catch (MalformedURLException e) {
                throw new IllegalArgumentException("\""+url+"\" is not a valid url");
            }
            catch (IOException e){
                e.printStackTrace();
            }
            System.out.println("initialised runtimes");
        });
        initialisationThread.start();
    }

    public Optional<Runtime> getRuntime(String language) {
        return Optional.ofNullable(getRuntimeUnsafe(language));
    }

    public Runtime getRuntimeUnsafe(String language) {
        for (Runtime runtime : getRuntimes()) {
            if (runtime.hasAlias(language))
                return runtime;
        }
        return null;
    }

    public String getUrl() {
        return url;
    }

    public List<Runtime> getRuntimes() {
        if(!initialised&&initialisationThread!=null) {
            try {
                initialisationThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return runtimes;
    }

    public ExecutionResult execute(ExecutionRequest request) {
        HttpURLConnection con;
        try {
            URL url = new URL(getUrl() + "/execute");
            int retries=-1;
            do {
                retries++;
                if(retries>=retryLimit){
                    throw new PistonException("Reached retry limit ("+retryLimit+")");
                }else if(retries>0){
//                    System.err.println("Request failed. Retrying in "+retries*retryTime/1000+" seconds");
                }
                Thread.sleep((long) retries *retryTime);
                con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("POST");
                con.addRequestProperty("Content-Type", "application/" + "POST");
                con.setDoOutput(true);

                String requestBody = new Gson().toJson(request);
//            System.out.println(requestBody);
                con.setRequestProperty("Content-Length", Integer.toString(requestBody.length()));
                con.getOutputStream().write(requestBody.getBytes(StandardCharsets.UTF_8));
            } while (con.getResponseCode()==429);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return new ExecutionResult();
        }


        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
            String inputLine;
            StringBuilder stringBuilder = new StringBuilder();
            while ((inputLine = bufferedReader.readLine()) != null) {
                stringBuilder.append(inputLine);
            }
            String result = stringBuilder.toString();
            ExecutionResult executionResult = new Gson().fromJson(result, ExecutionResult.class);
            return executionResult;
        }catch (IOException e){
            InputStream errorStream=con.getErrorStream();
            if(errorStream!=null){
                String error=Util.readStream(errorStream);
                JsonObject object=new Gson().fromJson(error,JsonObject.class);
                String message=error;
                if(object.has("message"))
                    message=object.get("message").getAsString();
                throw new PistonException(message);
            }
        }
        return new ExecutionResult();
    }
}
