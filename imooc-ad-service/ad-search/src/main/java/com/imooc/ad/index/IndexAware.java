package com.imooc.ad.index;

/**
 * 推广计划索引
 * @param <K> 索引的键
 * @param <V> 返回值
 */
public interface IndexAware<K,V> {
    /**
     * 通过K获取索引
     * @param key
     * @return
     */
    V get(K key);

    /**
     * 往索引添加内容
     * @param key
     * @param value
     */
    void add(K key,V value);

    void update(K key,V value);

    void delete(K key,V value);
}
