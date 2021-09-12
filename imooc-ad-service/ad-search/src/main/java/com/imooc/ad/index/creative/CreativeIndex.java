package com.imooc.ad.index.creative;

import com.imooc.ad.index.IndexAware;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class CreativeIndex implements IndexAware<Long,CreativeObject> {
    private static Map<Long,CreativeObject> objectMap;
    static {
        objectMap = new ConcurrentHashMap<>();
    }
    /**
     * 通过K获取索引
     *
     * @param key
     * @return
     */
    @Override
    public CreativeObject get(Long key) {
        return objectMap.get(key);
    }

    /**
     * 往索引添加内容
     *
     * @param key
     * @param value
     */
    @Override
    public void add(Long key, CreativeObject value) {
        objectMap.put(key,value);
    }

    @Override
    public void update(Long key, CreativeObject value) {
        CreativeObject creativeObject = objectMap.get(key);
        if (creativeObject == null){
            objectMap.put(key,value);
        }else {
            creativeObject.update(value);
        }
    }

    @Override
    public void delete(Long key, CreativeObject value) {
        objectMap.remove(key);
    }
}
