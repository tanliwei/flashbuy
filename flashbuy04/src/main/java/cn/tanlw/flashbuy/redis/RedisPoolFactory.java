package cn.tanlw.flashbuy.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Service
public class RedisPoolFactory {
    
    @Autowired
    RedisConfig config;
    
    @Bean
    public JedisPool jedisPoolFacotry(){
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxIdle(config.getPoolMaxIdle());
        poolConfig.setMaxTotal(config.getPoolMaxTotal());
        poolConfig.setMaxWaitMillis(config.getPoolMaxWait());
        JedisPool jp = new JedisPool(poolConfig, config.getHost(), config.getPort(),
                config.getTimeout()*1000, config.getPassword(),0);
        return jp;
    }
}
