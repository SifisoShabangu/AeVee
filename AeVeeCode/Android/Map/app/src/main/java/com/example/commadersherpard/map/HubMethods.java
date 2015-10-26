package com.example.commadersherpard.map;

/**
 * Created by Commader on 2015/10/26.
 */
public class HubMethods {
    public HubMethods(){
        Globals.serverHub.on("register", new Alert());
    }
}
