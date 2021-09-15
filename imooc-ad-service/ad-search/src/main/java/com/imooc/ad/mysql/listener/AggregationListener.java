package com.imooc.ad.mysql.listener;

import com.github.shyiko.mysql.binlog.BinaryLogClient;
import com.github.shyiko.mysql.binlog.event.*;
import com.imooc.ad.mysql.TemplateHolder;
import com.imooc.ad.mysql.dto.BinlogRowData;
import com.imooc.ad.mysql.dto.TableTemplate;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class AggregationListener implements BinaryLogClient.EventListener {
    private String dbName;
    private String tableName;
    private Map<String,Ilistener> listenerMap = new HashMap<>();
    private final TemplateHolder templateHolder;

    public AggregationListener(TemplateHolder templateHolder) {
        this.templateHolder = templateHolder;
    }
    private String getKey(String dbName,String tableName){
        return dbName+":"+tableName;
    }
    public void  register(String _dbName,String _tableName,Ilistener ilistener){
        this.listenerMap.put(getKey(_dbName,_tableName),ilistener);
    }
    @Override
    public void onEvent(Event event) {
//      TODO debug 好迷
        EventType type = event.getHeader().getEventType();
        if (type == EventType.TABLE_MAP){
            TableMapEventData data = event.getData();
            this.tableName = data.getTable();
            this.dbName = data.getDatabase();
            return;
        }
        if (type != EventType.EXT_UPDATE_ROWS && type != EventType.EXT_WRITE_ROWS
                && type != EventType.EXT_DELETE_ROWS){
            return;
        }
        if (StringUtils.isEmpty(dbName) || StringUtils.isEmpty(tableName)){
            return;
        }
//      找出对应表有兴趣的监听器
        String key = getKey(this.dbName, this.tableName);
        Ilistener listener = this.listenerMap.get(key);
        if (listener == null){
            return;
        }
        try {
            BinlogRowData rowData = buildRowData(event.getData());
            if (rowData == null){
                return;
            }
            rowData.setEventType(type);
            listener.onEvent(rowData);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            this.dbName = "";
            this.tableName = "";
        }
    }
    private List<Serializable[]> getAfterValues(EventData eventData){
        if (eventData instanceof WriteRowsEventData){
            return ((WriteRowsEventData)eventData).getRows();
        }
        if (eventData instanceof UpdateRowsEventData){
            return ((UpdateRowsEventData)eventData).getRows().stream().map(Map.Entry::getValue).collect(Collectors.toList());
        }
        if (eventData instanceof DeleteRowsEventData){
            return ((DeleteRowsEventData)eventData).getRows();
        }
        return Collections.emptyList();
    }
    private BinlogRowData buildRowData(EventData eventData){
//      TODO debug 好迷
        TableTemplate table = templateHolder.getTable(tableName);
        if (table == null){
            return null;
        }
        List<Map<String,String>> afterMapList = new ArrayList<>();
        for (Serializable[] after : getAfterValues(eventData)) {
            Map<String,String> afterMap = new HashMap<>();
            int colLen = after.length;
            for (int ix = 0;ix <colLen;++ix){
//              取出当前位置对应的列名
                String colName = table.getPosMap().get(ix);
                if (colName == null){
                    continue;
                }
                String colValue = after[ix].toString();
                afterMap.put(colName,colValue);
            }
            afterMapList.add(afterMap);
        }
        BinlogRowData rowData = new BinlogRowData();
        rowData.setAfter(afterMapList);
        rowData.setTable(table);
        return rowData;
    }
}
