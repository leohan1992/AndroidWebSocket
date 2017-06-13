# AndroidWebSocket
Help to use webSocket in Android

#### dependencies

Maven

````
<dependency>
  <groupId>xyz.leohan</groupId>
  <artifactId>AndroidWebSocket</artifactId>
  <version>1.0.0</version>
  <type>pom</type>
</dependency>
````

Gradle

````
compile 'xyz.leohan:AndroidWebSocket:1.0.0'
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
        <action android:name="xyz.leohan.Broadcase.WebSocket" />
     </intent-filter>
 </receiver>
````
3. call init() :

````java
 //do this in your BaseApplication or MainActivity
 //Make sure you called this only once
  new WebSocketAndroidClient.Builder().setUri("ws://192.168.1.1:12345").build(this).init();
````
#### contract me

if you have any trouble in use this lib ,you can send me an e-mail :leo@leohan.xyz
