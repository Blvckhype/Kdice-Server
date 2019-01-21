package com.example;

import Helper.MainLogic;

import java.net.InetAddress;
import java.net.ServerSocket;

public class App {
    public static void main( String[] args ) {
        try {
            InetAddress addr = InetAddress.getByName("localhost");
            ServerSocket serverSocket = new ServerSocket(8888, 5, addr);
            MainLogic mainLogic = new MainLogic();
            mainLogic.startServer(serverSocket);
        } catch (Exception ex) {
           ex.printStackTrace();
        }
    }
}
