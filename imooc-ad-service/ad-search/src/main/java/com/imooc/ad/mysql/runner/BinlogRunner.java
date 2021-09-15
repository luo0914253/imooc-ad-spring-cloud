package com.imooc.ad.mysql.runner;

import com.imooc.ad.mysql.BinlogClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class BinlogRunner implements CommandLineRunner {
    private final BinlogClient client;

    public BinlogRunner(BinlogClient client) {
        this.client = client;
    }

    @Override
    public void run(String... args) throws Exception {
        client.connect();
    }
}
