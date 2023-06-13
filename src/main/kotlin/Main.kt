import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.serialization.jackson.*
import io.ktor.http.*

import io.ktor.util.AttributeKey

import DBActions.DBActionsPlugin
import models.BabyWord
import utils.JsonMapper
import MQTTClient.Client
import aws.sdk.kotlin.services.dynamodb.DynamoDbClient
import aws.sdk.kotlin.services.dynamodb.model.*
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.util.JSONPObject
import com.github.f4b6a3.tsid.Tsid
import com.github.f4b6a3.tsid.TsidCreator
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.request.*
import models.Message
import org.eclipse.paho.client.mqttv3.MqttMessage
import org.w3c.dom.Attr

fun main() {
    println("hello")
    embeddedServer(Netty, port = 8000) {

        install(CORS){
            anyHost()
            allowSameOrigin = true
        }
//        install(DBActionsPlugin)
        install(ContentNegotiation) {
            register(ContentType.Application.Json, JacksonConverter(JsonMapper.mapper))
        }

        applicationEngineEnvironment {

        }

//        val client = Client("tcp://b-h-m.spr.us00.int.con-veh.net")
        val client = Client("tcp://test.mosquitto.org")
        routing {
            get("/") {
//                val word = BabyWord(1, call.attributes.get(AttributeKey<String>("tables")), 0.3);
                client.client.publish("presence", MqttMessage("Hello from Kotlin!".toByteArray()))
                call.respond(JsonMapper.mapper.writeValueAsString(("Hello")))
            }
            get("/messages"){

                val attrNameAlias = mutableMapOf<String, String>()
                attrNameAlias["#id"] = "id"

                // Set up mapping of the partition name with the value.
                val attrValues = mutableMapOf<String, AttributeValue>()
                attrValues[":id"] = AttributeValue.S("*")

                DynamoDbClient { region = "us-east-1" }.use { ddb ->
                    val response = ddb.scan(
                        ScanRequest{
                            tableName = "messages"
                            limit = 10
                        }
                    )
                    call.respond(JsonMapper.mapper.writeValueAsString((response)))
                }
            }
            post("/message") {
                var request = call.receiveText()

                var requestJSON = JsonMapper.mapper.readValue(request,Message::class.java)

                val id = TsidCreator.getTsid()
                val itemValues = mutableMapOf<String,AttributeValue>()

                itemValues["id"] = AttributeValue.S(id.toString())
                itemValues["message"] = AttributeValue.S(requestJSON.message)
                itemValues["topic"] = AttributeValue.S(requestJSON.topic)
                itemValues["timestamp"] = AttributeValue.S(requestJSON.timeStamp)

                DynamoDbClient { region = "us-east-1"}.use{ddb ->
                    val response = ddb.putItem(
                        PutItemRequest{
                            tableName = "messages"
                            item = itemValues
                        }
                    )

                    call.respond(JsonMapper.mapper.writeValueAsString(itemValues))
                }


            }
        }

    }.start(wait = true)
}