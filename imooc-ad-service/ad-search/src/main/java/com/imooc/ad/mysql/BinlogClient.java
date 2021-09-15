package com.imooc.ad.mysql;

import com.github.shyiko.mysql.binlog.BinaryLogClient;
import com.imooc.ad.mysql.listener.AggregationListener;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class BinlogClient {
    private BinaryLogClient client;

    private final BinlogConfig config;
    private final AggregationListener listener;

    public BinlogClient(BinlogConfig config, AggregationListener listener) {
        this.config = config;
        this.listener = listener;
    }
    public void connect(){
//      TODO 呵呵，你能懂？
        new Thread(()->{
            client = new BinaryLogClient(
                    config.getHost(),
                    config.getPort(),
                    config.getUsername(),
                    config.getPassword()
            );
            if (!StringUtils.isEmpty(config.getBinlogName())&&!config.getPosition().equals(-1)){
                client.setBinlogFilename(config.getBinlogName());
                client.setBinlogPosition(config.getPosition());
            }
            client.registerEventListener(listener);
            try{
                client.connect();
            }catch (IOException e){
                e.printStackTrace();
            }
        }).start();
    }
    public void close(){
        try {
            client.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
