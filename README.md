[![](https://jitpack.io/v/the-codeboy/Piston4J.svg)](https://jitpack.io/#the-codeboy/Piston4J)
# Piston4J

A Java Wrapper for [Piston](https://github.com/engineer-man/piston)

## Usage

Use ``Piston.getDefaultApi()`` to get a Piston object. Using this you can get the available Runtimes using ``Piston#getRuntimes()`` or you can get specific Runtimes: ``Piston#getRuntime(name)``

Once you have a Runtime you can execute Code by calling ``Runtime#execute()`` and passing at least one File (note that this is not the class from java.io).

## Examples

> A simple Hello World using javascript

```java
import com.github.codeboy.piston4j.api.ExecutionResult;
import com.github.codeboy.piston4j.api.File;
import com.github.codeboy.piston4j.api.Piston;
import com.github.codeboy.piston4j.api.Runtime;

import java.util.Optional;

public class Example {
    public static void main(String[] args) {
        Piston api = Piston.getDefaultApi();
        Optional<Runtime> optionalRuntime = api.getRuntime("js");
        if (optionalRuntime.isPresent()) {
            Runtime runtime = optionalRuntime.get();
            File file = new File("main.js", "console.log(\"Hello World!\")");
            ExecutionResult result = runtime.execute(file);
            System.out.println(result.getOutput());
        }
    }
}

```

> You can also execute the code without getting the runtime. However this is not recommended since it wont work unless you know the correct version of the runtime

```java
import com.github.codeboy.piston4j.api.ExecutionRequest;
import com.github.codeboy.piston4j.api.ExecutionResult;
import com.github.codeboy.piston4j.api.File;
import com.github.codeboy.piston4j.api.Piston;

public class Example {
    public static void main(String[] args) {
        Piston api = Piston.getDefaultApi();
        File file = new File("main.js", "console.log(\"Hello World!\")");
        ExecutionRequest request=new ExecutionRequest("js","16.3.0",file);
        ExecutionResult result = api.execute(request);
        System.out.println(result.getOutput().getOutput());
    }
}

```
