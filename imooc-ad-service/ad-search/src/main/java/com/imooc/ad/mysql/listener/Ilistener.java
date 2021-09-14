package com.imooc.ad.mysql.listener;

import com.imooc.ad.mysql.dto.BinlogRowData;

/**
 * 对Binlog日志数据实现增量索引更新的接口
 */
public interface Ilistener {
    void register();
    void onEvent(BinlogRowData eventData);
}
