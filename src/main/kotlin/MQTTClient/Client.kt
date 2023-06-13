package MQTTClient

import org.eclipse.paho.client.mqttv3.MqttClient
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.eclipse.paho.client.mqttv3.MqttMessage
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence
import java.nio.charset.StandardCharsets
import javax.net.ssl.SSLContext


class Client(url: String) {

    private val clientId: String
    val client: MqttClient
    private val options: MqttConnectOptions

    init {
        val persistence = MemoryPersistence()
        clientId = MqttClient.generateClientId()
        client = MqttClient(url, clientId, persistence)
        options = MqttConnectOptions()
//        options.sslProperties.setProperty("protocol","TLSv1.2")
        options.connectionTimeout =2000
        options.mqttVersion = MqttConnectOptions.MQTT_VERSION_3_1
        options.isCleanSession = true
//        options.userName = "carnet"
//        options.password = "Cj3n@904gQr".toCharArray()

        println(options.debug)
//        println(client.currentServerURI)

        try {
            client.connect(options)

        } catch (e: Exception) {
            println(e)
        }
    }
}
