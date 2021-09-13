package com.imooc.ad.service;

import com.github.shyiko.mysql.binlog.BinaryLogClient;
import com.github.shyiko.mysql.binlog.event.EventData;
import com.github.shyiko.mysql.binlog.event.UpdateRowsEventData;
import com.github.shyiko.mysql.binlog.event.WriteRowsEventData;

public class BinlogServiceTest {
    public static void main(String[] args) throws Exception {
        BinaryLogClient client = new BinaryLogClient("47.115.46.226",3306,"imooc","admin");
        client.registerEventListener(event -> {
            EventData data = event.getData();
            if (data instanceof UpdateRowsEventData){
                System.out.println("Update......");
                System.out.println(data.toString());
            }else if (data instanceof WriteRowsEventData){
                System.out.println("Writer......");
                System.out.println(data.toString());
            }else {
                System.out.println("Delete......");
                System.out.println(data.toString());
            }
        });
        client.connect();
    }
}
