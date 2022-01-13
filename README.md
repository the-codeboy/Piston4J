# Piston4J

A Java Wrapper for [Piston](https://github.com/engineer-man/piston)

## Download

[![](https://jitpack.io/v/the-codeboy/Piston4J.svg)](https://jitpack.io/#the-codeboy/Piston4J)

Please replace **VERSION** below with the version shown above!

**Maven**
```xml
<dependency>
	    <groupId>com.github.the-codeboy</groupId>
	    <artifactId>Piston4J</artifactId>
	    <version>VERSION</version>
	</dependency>
```
```xml
	<repositories>
		<repository>
		    <id>jitpack.io</id>
		    <url>https://jitpack.io</url>
		</repository>
	</repositories>
```

**Gradle**
```gradle
dependencies {
	        implementation 'com.github.the-codeboy:Piston4J:VERSION'
	}

repositories {
			maven { url 'https://jitpack.io' }
		}
```

## Examples

> Running code in one line. It is not recommended to use the api like this
```java
System.out.println(Piston.getDefaultApi().execute("python","print(\"Hello, World!\")").getOutput().getStdout());
```

> A simple Hello World using javascript

```java
import com.github.codeboy.piston4j.api.CodeFile;
import com.github.codeboy.piston4j.api.ExecutionResult;
import com.github.codeboy.piston4j.api.CodeFile;
import com.github.codeboy.piston4j.api.Piston;
import com.github.codeboy.piston4j.api.Runtime;

import java.util.Optional;

public class Example {
    public static void main(String[] args) {
        Piston api = Piston.getDefaultApi();//get the api at https://emkc.org/api/v2/piston
        Optional<Runtime> optionalRuntime = api.getRuntime("js");//get the javascript runtime
        if (optionalRuntime.isPresent()) {//check if the runtime exists
            Runtime runtime = optionalRuntime.get();
            CodeFile codeFile = new CodeFile("main.js", "console.log(\"Hello World!\")");//create the codeFile containing the javascript code
            ExecutionResult result = runtime.execute(codeFile);//execute the codeFile
            System.out.println(result.getOutput().getOutput());//print the result
        }
    }
}

```

> You can also execute the code without getting the runtime. However this is not recommended since it wont work unless you know the correct version of the runtime

```java
import com.github.codeboy.piston4j.api.CodeFile;
import com.github.codeboy.piston4j.api.ExecutionRequest;
import com.github.codeboy.piston4j.api.ExecutionResult;
import com.github.codeboy.piston4j.api.Piston;

public class Example2 {
    public static void main(String[] args) {
        Piston api = Piston.getDefaultApi();//get the api at https://emkc.org/api/v2/piston
        CodeFile codeFile = new CodeFile("main.js", "console.log(\"Hello World!\")");//create the codeFile containing the javascript code
        ExecutionRequest request = new ExecutionRequest("js", "16.3.0", codeFile);//create the request using the codeFile, a language and a version
        ExecutionResult result = api.execute(request);//execute the request
        System.out.println(result.getOutput().getOutput());//print the result
    }
}

```
