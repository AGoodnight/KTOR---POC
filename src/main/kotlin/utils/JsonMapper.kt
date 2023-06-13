package utils

import com.fasterxml.jackson.core.util.DefaultIndenter
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

// 'object' creates a singleton, like a class, but we do not make more than one during runtime.
object JsonMapper{
    val mapper = jacksonObjectMapper()
    init{
        mapper.configure(SerializationFeature.INDENT_OUTPUT,true)
        mapper.setDefaultPrettyPrinter(DefaultPrettyPrinter().apply {
            indentArraysWith(DefaultPrettyPrinter.FixedSpaceIndenter.instance)
            indentObjectsWith(DefaultIndenter("  ", "\n"))
        })
        mapper.registerModule(JavaTimeModule())
    }
}