package com.example.commadersherpard.map;

import android.util.Log;

import microsoft.aspnet.signalr.client.Action;
import microsoft.aspnet.signalr.client.ErrorCallback;
import microsoft.aspnet.signalr.client.LogLevel;
import microsoft.aspnet.signalr.client.Platform;
import microsoft.aspnet.signalr.client.SignalRFuture;
import microsoft.aspnet.signalr.client.http.android.AndroidPlatformComponent;
import microsoft.aspnet.signalr.client.hubs.HubConnection;

public class ServerConnection
{
    private SignalRFuture<Void> connect = null;
    private final HubConnectionFactory hcf = HubConnectionFactory.getInstance();
    private HubConnection conn = null;

    public  ServerConnection()
    {
        microsoft.aspnet.signalr.client.Logger logger = new microsoft.aspnet.signalr.client.Logger() {

            @Override
            public void log(String message, LogLevel level) {
                Log.d("P", message);
            }
        };

        Platform.loadPlatformComponent(new AndroidPlatformComponent());
        final HubConnectionFactory hcf = HubConnectionFactory.getInstance();

        connect = hcf.connect(Globals.url);

        try {
            configConnectFuture();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void configConnectFuture()
    {
        connect.onError(new ErrorCallback() {
            @Override
            public void onError(final Throwable error) {
                //IITT_Globals.connectionState = 404; //This state is for error in connection
            }
        });

        connect.done(new Action<Void>() {
            @Override
            public void run(Void obj) throws Exception {
                conn = hcf.getHubConnection();
                Globals.serverHub = hcf.getServerHub();
                new HubMethods();
            }
        });
    }

    public void disconnect(){
        hcf.disconnect();
    }
}

