# Workaround for IntelliJ IDEA Issue IJPL-328

This is a workaround for the IntelliJ IDEA issue IJPL-328, which causes the IDE can't handle 1Password's SSH Agent.

## Build

You need to hava [mise-en-place](https://mise.jdx.dev/) installed.

```bash
mise build
```

## Usage

Add the following line to the `gateway.vmoptions` file, which is located at: `%LOCALAPPDATA%\Programs\IntelliJ IDEA Ultimate\plugins\gateway-plugin\resources\gateway.vmoptions`.

```properties
-javaagent:C:\Users\atty\src\github.com\atty303\fix-op-jetbrains\out\agent\assembly.dest\out.jar
```

Make sure to replace the path with the actual path to the `out.jar` file.
