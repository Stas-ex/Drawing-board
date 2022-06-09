package com.skuratov.labma.client;

import com.skuratov.labma.client.model.Client;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * ClientFactory - Class for creating client threads.
 */
public class ClientFactory {
    public static void main(String[] args) throws InterruptedException {
        ExecutorService exec = Executors.newFixedThreadPool(3);
        for (int i = 0; i < 3; i++) {
            exec.execute(new Client("localhost", 29288));
            Thread.sleep(100);
        }
        exec.shutdown();
    }
}