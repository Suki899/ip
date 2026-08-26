# Suki

Suki is a personal assistant chatbot that helps you keep track of your tasks, built from the Duke project template for CS2103T.

## Setting up in Intellij

Prerequisites: JDK 25, update Intellij to the most recent version.

1. Open Intellij (if you are not in the welcome screen, click `File` > `Close Project` to close the existing project first)
1. Open the project into Intellij as follows:
   1. Click `Open`.
   1. Select the project directory, and click `OK`.
   1. If there are any further prompts, accept the defaults.
1. Configure the project to use **JDK 25** (not other versions) as explained in [here](https://www.jetbrains.com/help/idea/sdk.html#set-up-jdk).<br>
   In the same dialog, set the **Project language level** field to the `SDK default` option.
1. After that, locate the `src/main/java/suki/Suki.java` file, right-click it, and choose `Run Suki.main()` (if the code editor is showing compile errors, try restarting the IDE). If the setup is correct, you should see something like the below as the output:
   ```
    ____   _   _  _  __ ___ 
   / ___| | | | || |/ /|_ _|
   \___ \ | | | || ' /  | | 
    ___) || |_| || . \  | | 
   |____/  \___/ |_|\_\|___|
   ```

**Warning:** Keep the `src\main\java` folder as the root folder for Java files (i.e., don't rename those folders or move Java files to another folder outside of this folder path), as this is the default location some tools (e.g., Gradle) expect to find Java files.

## Building from the command line

The project has no build tool yet, so the steps below use `javac` and `jar`
directly. They are run from the project root.

Compile the application:

```
javac -d build/main src/main/java/suki/*.java
```

Run it:

```
java -cp build/main suki.Suki
```

### Running the tests

The tests need the JUnit 5 console launcher, which is not committed to the
repository. Download it once into `lib/`:

```
mkdir -p lib
curl -o lib/junit-console.jar \
  https://repo1.maven.org/maven2/org/junit/platform/junit-platform-console-standalone/6.1.3/junit-platform-console-standalone-6.1.3.jar
```

Then compile and run the suite:

```
javac -cp build/main:lib/junit-console.jar -d build/test src/test/java/suki/*.java
java -jar lib/junit-console.jar execute --class-path build/main:build/test --scan-class-path
```

### Building the JAR

```
jar --create --file build/suki.jar --main-class suki.Suki -C build/main .
java -jar build/suki.jar
```

The JAR is self-contained and can be run from any directory. Suki saves tasks
to `data/suki.txt` relative to the directory it is started in, creating the
folder if needed.
