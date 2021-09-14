package com.imooc.ad.mysql.dto;

import com.imooc.ad.mysql.constant.OpType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TableTemplate {
    private String tableName;
    private String level;

    /**
     * 所对应的操作类型对应的各个列
     */
    private Map<OpType, List<String>> opTypeFieldSetMap = new HashMap<>();
    /**
     * 字段索引 -> 字段名
     */
    private Map<Integer,String> posMap = new HashMap<>();
}
