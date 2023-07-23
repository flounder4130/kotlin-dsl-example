# HTML DSL for documenting APIs

This repository is an example of using DSL-based approach to generate API reference 
documentation. It uses Kotlin HTML DSL for defining the content structure and Swagger parser
for transforming the API specification to object model.

You can use it to get acquainted with the general approach or the specific HTML DSL application.
If the output format works for your case, you can also use it to generate your API reference 
by simply replacing the specification file.

## Walkthrough

If you're new to Kotlin, 
then it might be useful to have a quick look at the project structure:

### Build script

The `build.gradle.kts` file is a script that configures the entire project and defines
the tasks to build, run, and test it.

In it, we add the required external dependencies (HTML DSL and Swagger parser in our case):

```Gradle
dependencies {
    testImplementation(kotlin("test"))
    implementation("io.swagger.parser.v3:swagger-parser:2.1.16")
    implementation("org.jetbrains.kotlinx:kotlinx-html-jvm:0.7.5")
}
```

### Specification file

The `./spec/space-open-api.json` file is an OpenAPI specification,
which we use to source the data for the generated documentation. The example is not tied to this specific file: you can use any specification that conforms 
to the OAS standard.

### Resources

The `output` folder contains the following resources:
* `nav.js` – JavaScript file for populating the `nav` element
* `scroll.js` – JavaScript file for smooth scrolling to anchors
* `styles.css` – styles for the output

### Entry point

`./src/main/kotlin/Main.kt` is the single Kotlin file that is used for programmatically building
the resulting HTML. It is also the entry point for our documentation builder.

There are two paths in it:
* `const val specPath = "./spec/space-open-api.json"` – variable pointing to the specification file
* `generate("./output/reference.html")` – parameter representing the output path

Everything that goes inside `generate() { ... }`
is the DSL forming the output. If you are familiar with HTML, you'll recognize most function names
because they are the same as HTML tag names. On the other hand, you can notice regular Kotlin code, 
which you can also use within the document structure definition. This property allows us
to extend markup with custom logic.

## How to run

If you are using IntelliJ IDEA, click the **Run** button to the left of the `main()` function.
Alternatively, see the documentation for your IDE or use the `gradle run` terminal command.

As the result, the `reference.html` file will appear in the `./output/` folder.

