package com.example;

import Helper.MainLogic;
import org.apache.log4j.BasicConfigurator;

import java.net.InetAddress;
import java.net.ServerSocket;

public class App {
    public static void main( String[] args ) {

        BasicConfigurator.configure();
        try {
            InetAddress addr = InetAddress.getByName("localhost");
            ServerSocket serverSocket = new ServerSocket(8888, 5);
            MainLogic mainLogic = new MainLogic();
            mainLogic.startServer(serverSocket);
        } catch (Exception ex) {
           ex.printStackTrace();
        }
    }
}
