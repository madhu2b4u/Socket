package com.android.socket

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.JsonObject
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import org.json.JSONObject


class MainActivity : AppCompatActivity() {


    lateinit var mSocket: Socket

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Let's connect to our Chat room! :D
        try {
           // mSocket = IO.socket("http://visakhafabricare.in:1989")

            //This address is the way you can connect to localhost with AVD(Android Virtual Device)
            mSocket = SocketConnection().openSocketConnection()
           // Log.e("success", mSocket.id())

        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("fail", "Failed to connect")
        }

        mSocket.connect()
        //Register all the listener and callbacks here.
        mSocket.on(Socket.EVENT_CONNECT, onConnect)

        mSocket.on("updateCoordinatesResponse", updateCoordinates)
        mSocket.on("connectionResponse", onResponse)

    }


    // <----- Callback functions ------->

    var onConnect = Emitter.Listener {
        Log.e("onConnect", "onConnect")
        val gson = Gson()
        //After getting a Socket.EVENT_CONNECT which indicate socket has been connected to server,
        //send userName and roomName so that they can join the room.
         val data = LatLngitudte("12.4569","15.8999","Auckland")
         val jsonData = gson.toJson(data) // Gson changes data object to Json type.*/
         mSocket.emit("updateCoordinates", jsonData)
    }

    //after connecting

    var onResponse = Emitter.Listener {
        val name = it[0] as JSONObject //This pass the userName!
        val convertedObject = Gson().fromJson(name.toString(), Test::class.java)
        Log.d("onResponse",convertedObject.message)
        Log.d("onResponse",convertedObject.data.connectionId)
        Log.d("onResponse",name.toString())
    }


    var updateCoordinates = Emitter.Listener {
        val name = it[0] as String //This pass the userName!
      /*  val convertedObject = Gson().fromJson(name.toString(), Test::class.java)
        Log.d("response",convertedObject.message)*/
        val convertedObject = Gson().fromJson(name.toString(), LatLngitudte::class.java)
        Log.d("updateLocResponse",convertedObject.latitude)
        Log.d("updateLocResponse",convertedObject.longitude)
        Log.d("updateLocResponse",convertedObject.locoationName)
        mSocket.emit("unsubscribe", "close")
        mSocket.disconnect()
    }

    override fun onDestroy() {
        super.onDestroy()
        mSocket.emit("unsubscribe", "close")
      //  mSocket.disconnect()
    }

}

data class Test( val status :Boolean, val message :String, val data :Data )
data class Data( val connectionId :String)
data class LatLngitudte( val latitude :String, val longitude:String, val locoationName :String)