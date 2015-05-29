# JJSBridge
Java-JavaScript bridge, to be nice

Basically, I wanted a way that I could run JS scripts in my program like in any webbrowser. This means:
 - No `importClass` or `importPackage` stuff. I want the script sandboxed, so you aren't able to access classes unless the program writer explicitly allows it. Also, no package names.
 - A suite of simple and robustly mirrors of what you can do in a webbrowser. For example, there should be a port of `XMLHttpRequest` accessible, compliant (as much as possible) to the web standards.
 - Give fields & methods a permission system (like `Object.defineProperty`). You should be able to have a read-only field, add a getter or setter, or make unaccessible properties.
 - Bridged classes' methods (Java methods called in JS) should be full objects, so I can set custom properties if I want to.
 - A simple way of controlling script execution, maybe multithreaded.

## Getting started

1. Compile your own jar or get a release, and add it to your classpath.
2. Mark your methods, fields, and classes with annotations (more on that later)
3. Create a JSBridge, and export some classes/objects into it.
4. Load in some JS
5. Run it
6. There is no step 6!

## Annotating stuff

Annotations are used to describe how everything should be accessible to a JS script. Methods and Fields not annotated, by default, are invisible, and unannotated classes may have suboptimal behavior.
