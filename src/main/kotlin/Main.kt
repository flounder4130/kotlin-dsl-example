import io.swagger.parser.OpenAPIParser
import io.swagger.v3.oas.models.*
import kotlinx.html.*
import kotlinx.html.stream.createHTML
import kotlin.io.path.*

const val specPath = "./spec/space-open-api.json"
val spec: OpenAPI = OpenAPIParser().readLocation(specPath, null, null).openAPI

fun main() = generate("./output/reference.html") {
    head {
        link { rel = "preconnect"; href = "https://fonts.googleapis.com" }
        link { rel = "preconnect"; href = "https://fonts.gstatic.com" }
        link { rel = "stylesheet"; href = "https://fonts.googleapis.com/css2?family=Roboto:wght@400;500;700;900&display=swap" }
        link { rel = "stylesheet"; href = "styles.css" }
    }

    body {
        nav {
            h1 { +"${spec.info.title} ${spec.info.version} API" }
            /* the rest is populated through JS */
        }

        div {
            id = "main"
            spec.paths.forEach { path ->
                path.methods().forEach { method ->
                    val methodInfo = method.value
                    h1(classes = "method-summary") {
                        id = methodInfo.summary.toId()
                        +methodInfo.summary
                    }
                    p(classes = "path") {
                        span(classes = "method-label") { +method.key }
                        +path.key
                    }
                    methodInfo.description?.let { p(classes = "method-description") { +it } }
                    parametersTable(methodInfo)
                }
            }
        }

        script { src = "nav.js" }
        script { src = "scroll.js" }
    }
}

private fun Map.Entry<String, PathItem>.methods(): Map<String, Operation> {
    val pathInfo = value
    return mapOf(
        "GET" to pathInfo.get,
        "DELETE" to pathInfo.delete,
        "PUT" to pathInfo.put,
        "POST" to pathInfo.post,
        "PATCH" to pathInfo.patch
    ).filterValues { it != null }
}

private fun String.toId() = lowercase().replace(Regex("[^a-zA-Z]"), "-")

private fun HtmlBlockTag.parametersTable(operation: Operation) {
    if (operation.parameters.isNotEmpty()) {
        table {
            tr { td { +"Name" }; td { +"Type" }; td { +"Required" } }
            operation.parameters.forEach { parameter ->
                tr {
                    td { +parameter.name }
                    td { +(parameter?.schema?.type ?: "") }
                    td { +if (parameter.required) "Yes" else "No" }
                }
            }
        }
    }
}

@Suppress("SameParameterValue")
private fun generate(out: String, content: TagConsumer<String>.() -> Unit) = createHTML().apply(content)
    .finalize()
    .let{ Path(out).writeText(it) }