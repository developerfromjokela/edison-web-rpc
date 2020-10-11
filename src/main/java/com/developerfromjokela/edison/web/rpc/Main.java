package com.developerfromjokela.edison.web.rpc;

import com.developerfromjokela.edison.web.rpc.server.CommunicationServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

    public static void main(String[] args) {
        try {
            CommunicationServer communicationServer = new CommunicationServer(4551);
            communicationServer.start();
            BufferedReader sysin = new BufferedReader( new InputStreamReader( System.in ) );
            while ( true ) {
                String in = sysin.readLine();
                if( in.equals( "exit" ) ) {
                    System.out.println("Exiting...");
                    communicationServer.stop(2000);
                    System.exit(0);
                    break;
                }
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }


}
