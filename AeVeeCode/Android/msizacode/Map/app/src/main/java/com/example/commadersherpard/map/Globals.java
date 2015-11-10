package com.example.commadersherpard.map;

import microsoft.aspnet.signalr.client.hubs.HubProxy;

/**
 * Created by Commader on 2015/10/26.
 */
public class Globals {
    public static String url = "http://192.168.43.169/Image";
    public static String hub = "serverHub";
    public static HubProxy serverHub = null;
    public static ServerConnection con = new ServerConnection();
}
