# AndroidWebSocket
Help to use webSocket in Android  
based on "org.java-websocket:Java-WebSocket:1.3.4"
#### dependencies

Maven

````
<dependency>
  <groupId>xyz.leohan</groupId>
  <artifactId>AndroidWebSocket</artifactId>
  <version>1.1.3-beta</version>
  <type>pom</type>
</dependency>
````

Gradle

````
compile 'xyz.leohan:AndroidWebSocket:1.1.3-beta'
````

#### How to Use

1. write your own BroadcastReceiver extends xyz.leohan.websocketlib.WebSocketReceiver like this:

````java
public class MyReceiver extends WebSocketReceiver {
    @Override
    public void onMessage(String msg) {
        //Here is message from webSocket
        //you can deal it with eventBus、RxBus、Notification and so on
        Log.i("webSocket", msg);
    }
}
````
2. write these in your AndroidManifest.xml:
````
 <service android:name="xyz.leohan.websocketlib.WebSocketService" />
 <receiver android:name=".MyReceiver">
    <intent-filter>
        <action android:name="xyz.leohan.Broadcast.WebSocket" />
     </intent-filter>
 </receiver>
````
3. call init() :

```java
 //do this in your BaseApplication or MainActivity
 //Make sure you called this only once
 //WebSocketAndroidClient.init(this,"ws://192.168.1.108:9898");
 WebSocketAndroidClient.init(this,"ws://192.168.1.108:9898",3600);
 WebSocketAndroidClient.getInstance().connect(new WebSocketAndroidClient.onWebSocketOpenListener() {
              @Override
              public void onOpen(ServerHandshake handshakedata) {
                 //TODO connected
              }
          } );
````
4. call disconnect:

it will auto reconnect by error or server offline,but when you call disconnect,you need call connect again
```java
   WebSocketAndroidClient.getInstance().disConnect();
```
5. send message:
```java
   WebSocketAndroidClient.getInstance().sendMsg(editText.getText().toString());
```

#### History
* v1.1.4-beta
    1.update gradle to 4.1
    2.update buildTool to 26.0.2
    3.add isConnected function.
* v1.1.3-beta
    1. add init function. add timeout time param.
* v1.1.2-beta   
    1. add connenct listener
    2. fix disconnect and reconnect bug
    3. throw NotYetConnectException when send msg
#### Contact  me

if you have any trouble in use this lib ,you can send me an e-mail :leo@leohan.xyz
