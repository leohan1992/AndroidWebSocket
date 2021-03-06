package xyz.leohan.websocketlib;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.channels.NotYetConnectedException;


/**
 * Created by leo on 2017/6/13.
 */

public class WebSocketService extends Service {
    private MyWebSocketServiceBinder mBinder;
    private WebSocketClient mWebSocketClient;
    public static final String INTENT_DATA_URI = "uri";
    public static final String INTENT_DATA_TIMEOUT_TIME = "timeoutTime";
    private WebSocketConnectionListener mListener;
    private String mUri;
    private int mTimeoutTime=0;
    public static final int USER_CLOSE_CODE = 1000;

    @Override
    public void onCreate() {
        mBinder = new MyWebSocketServiceBinder();
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        mUri = intent.getStringExtra(INTENT_DATA_URI);
        mTimeoutTime=intent.getIntExtra(INTENT_DATA_TIMEOUT_TIME,0);
        return mBinder;
    }

    public class MyWebSocketServiceBinder extends Binder {
        public void connect() {
            connectWebSocket();
        }

        public void close() {
            disconnectWebSocket();
        }

        public void setConnectListener(WebSocketConnectionListener listener) {
            mListener = listener;
        }

        public boolean isConnecting() {
            return isWebSocketConnecting();
        }

        public void sendMsg(String msg) throws NotYetConnectedException {
            try {
                send(msg);
            } catch (Exception e) {
                throw new NotYetConnectedException();
            }
        }

        public boolean isClosed() {
            return isWebSocketClosed();
        }
    }

    private boolean isWebSocketConnecting() {
        return mWebSocketClient.isConnecting();
    }

    private boolean isWebSocketClosed() {
        return mWebSocketClient.isClosed();
    }

    private void send(String msg) throws NotYetConnectedException {
        try {
            mWebSocketClient.send(msg);
        } catch (NotYetConnectedException e) {
            throw new NotYetConnectedException();
        }
    }

    private void connectWebSocket() {
        if (null != mThread && mThread.isAlive()) {
            return;
        }
        if (null != mThread) {
            mThread.interrupt();
            mThread = null;
        }
        mThread = new Thread(new MyThread());
        mThread.start();
    }

    private Thread mThread;

    class MyThread implements Runnable {

        @Override
        public void run() {
            if (null == mUri) {
                mListener.onError(new Exception("null uri Excepiton"));
                return;
            }
            URI uri = null;
            try {
                uri = new URI(mUri);
            } catch (URISyntaxException e) {
                mListener.onError(new Exception("uri format error:" + uri + "can not be format to java.net.uri"));
                return;
            }
            try {
                mWebSocketClient = createClient(uri,mTimeoutTime);
            } catch (Exception e) {
                mListener.onError(e);
                return;
            }
            mWebSocketClient.connect();
        }
    }

    private void disconnectWebSocket() {
        mWebSocketClient.close(USER_CLOSE_CODE, "userClose");
    }

    private WebSocketClient createClient(URI uri,int timeoutTime) {

        WebSocketClient client = new WebSocketClient(uri, new Draft_6455(), null, timeoutTime) {
            @Override
            public void onOpen(ServerHandshake handshakedata) {
                if (mListener != null) {
                    mListener.onOpen(handshakedata);
                }
            }

            @Override
            public void onMessage(String message) {
                if (mListener != null) {
                    mListener.onMessage(message);
                }
            }

            @Override
            public void onClose(int code, String reason, boolean remote) {
                if (mListener != null) {
                    mListener.onClose(code, reason, remote);
                }
            }

            @Override
            public void onError(Exception ex) {
                if (mListener != null) {
                    mListener.onError(ex);
                }
            }
        };
        return client;
    }
}
