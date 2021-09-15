package com.imooc.ad.mysql.sender;

import com.imooc.ad.mysql.dto.MysqlRowData;

public interface ISender {
    void sender(MysqlRowData rowData);
}
