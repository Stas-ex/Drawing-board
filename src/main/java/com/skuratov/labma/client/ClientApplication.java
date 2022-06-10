package com.skuratov.labma.client;

import com.skuratov.labma.client.model.ClientThread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * ClientFactory - Class for creating client threads.
 */
public class ClientApplication {
    public static void main(String[] args) {
        ExecutorService exec = Executors.newFixedThreadPool(3);
        for (int i = 0; i < 3; i++) {
            exec.execute(new ClientThread("localhost", 29288));
        }
        exec.shutdown();
    }
}