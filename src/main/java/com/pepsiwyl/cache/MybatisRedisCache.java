package com.pepsiwyl.cache;

import com.pepsiwyl.utils.ApplicationContextUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.cache.Cache;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.DigestUtils;

import java.util.concurrent.TimeUnit;

/**
 * @author by pepsi-wyl
 * @date 2022-07-30 9:06
 */

@Slf4j
public class MybatisRedisCache implements Cache {

    // mapper的namespace 放入缓存的ID
    private final String id;

    private static RedisTemplate redisTemplate;

    // MD5处理
    private String keyToMD5String(String key) {
        return DigestUtils.md5DigestAsHex(key.getBytes());
    }

    // 必须要存在一个以String id作为参数有参构造
    public MybatisRedisCache(String id) {
        if (id == null) {
            throw new IllegalArgumentException("缓存实例需要一个id!");
        } else {
            log.info("开启Redis缓存: id = {}", id);
            this.id = id;
        }
    }

    @Override
    public String getId() {
        return this.id;
    }

    // 放入缓存
    @Override
    public void putObject(Object key, Object value) {
        if (redisTemplate == null) redisTemplate = ApplicationContextUtils.getRedisTemplate();
        redisTemplate.opsForHash().put(this.id, keyToMD5String(key.toString()), value);
        log.info("设置Redis缓存: id = {}, key = {}, value = {}", id, keyToMD5String(key.toString()), value);

        // 设置缓存时间 尽量避免 缓存雪崩
        if (id.equals("com.pepsiwyl.mapper.UserMapper")) {
            redisTemplate.expire(this.id, 1, TimeUnit.HOURS);
        }

    }

    // 取出缓存
    @Override
    public Object getObject(Object key) {
        if (redisTemplate == null) redisTemplate = ApplicationContextUtils.getRedisTemplate();
        Object hashVal = redisTemplate.opsForHash().get(this.id, keyToMD5String(key.toString()));
        log.info("获取Redis缓存: id = {}, key = {}, hashVal = {}", id, keyToMD5String(key.toString()), hashVal);
        return hashVal;
    }

    // 根据key删除缓存  无效的方法
    @Override
    public Object removeObject(Object key) {
        if (redisTemplate == null) redisTemplate = ApplicationContextUtils.getRedisTemplate();
        redisTemplate.opsForHash().delete(this.id, key.toString());
        log.info("移除Redis缓存: id = {}, key = {}", id, key);
        return null;
    }

    // 清空缓存
    @Override
    public void clear() {
        if (redisTemplate == null) redisTemplate = ApplicationContextUtils.getRedisTemplate();
        redisTemplate.delete(this.id);
        log.info("清空Redis缓存: id = {}", id);
    }

    // 计算缓存的数量
    @Override
    public int getSize() {
        if (redisTemplate == null) redisTemplate = ApplicationContextUtils.getRedisTemplate();
        int size = Math.toIntExact(ApplicationContextUtils.getRedisTemplate().opsForHash().size(this.id));
        log.info("Redis缓存大小: id = {}, size = {}", id, size);
        return size;
    }

}
