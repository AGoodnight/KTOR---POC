package DBActions

import aws.sdk.kotlin.services.dynamodb.DynamoDbClient
import aws.sdk.kotlin.services.dynamodb.model.ListTablesRequest
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.util.*

class DBConnection {
    suspend fun getTables(){
        val tableListKey = AttributeKey<String>("tables")
        var tableNameString: String = ""
        DynamoDbClient { region = "us-east-1" }.use { ddb ->
            val response = ddb.listTables(ListTablesRequest {})
            response.tableNames?.forEach { tableName ->
                tableNameString = tableName
            }
        }
    }

    fun getMessages(){

    }
}

val DBActionsPlugin = createApplicationPlugin(name = "DynamoDBActions") {

    onCall { call ->
        val path = call.request.path()
        println("path ---> $path")
        if(path == "/"){

        }
    }


}
