package xyz.leohan.androidwebsocket;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import xyz.leohan.websocketlib.WebSocketAndroidClient;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final EditText editText = (EditText) findViewById(R.id.et_msg);
        Button button = (Button) findViewById(R.id.btn_send);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    WebSocketAndroidClient.getInstance().sendMsg(editText.getText().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "发送失败，未连接状态", Toast.LENGTH_SHORT).show();
                }
            }
        });
        Button close = (Button) findViewById(R.id.btn_disconnect);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebSocketAndroidClient.getInstance().disConnect();
            }
        });
    }
}
