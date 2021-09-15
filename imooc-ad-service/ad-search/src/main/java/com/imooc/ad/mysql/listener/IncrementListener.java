package com.imooc.ad.mysql.listener;

import com.github.shyiko.mysql.binlog.event.EventType;
import com.imooc.ad.mysql.constant.Constant;
import com.imooc.ad.mysql.constant.OpType;
import com.imooc.ad.mysql.dto.BinlogRowData;
import com.imooc.ad.mysql.dto.MysqlRowData;
import com.imooc.ad.mysql.dto.TableTemplate;
import com.imooc.ad.mysql.sender.ISender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class IncrementListener implements Ilistener {
    @Resource(name = "")
    private ISender sender;

    private final AggregationListener aggregationListener;

    public IncrementListener(AggregationListener aggregationListener) {
        this.aggregationListener = aggregationListener;
    }
//  TODO 迷的不得了
    @Override
    @PostConstruct
    public void register() {
        Constant.table2Db.forEach((k,v)->aggregationListener.register(v,k,this));
    }

    @Override
    public void onEvent(BinlogRowData eventData) {
//      TODO debug
        TableTemplate table = eventData.getTable();
        EventType eventType = eventData.getEventType();

        MysqlRowData rowData = new MysqlRowData();
        rowData.setTableName(table.getTableName());
        rowData.setLevel(eventData.getTable().getLevel());
        OpType opType = OpType.to(eventType);
        rowData.setOpType(opType);

        List<String> fieldList = table.getOpTypeFieldSetMap().get(opType);
        if (fieldList == null){
            return;
        }
        for (Map<String, String> afterMap : eventData.getAfter()) {
            Map<String,String> _afterMap = new HashMap<>();
            for (Map.Entry<String, String> entry : afterMap.entrySet()) {
                String colName = entry.getKey();
                String colValue = entry.getValue();
                _afterMap.put(colName,colValue);
            }
            rowData.getFieldValueMap().add(_afterMap);
        }
        sender.sender(rowData);
    }
}
