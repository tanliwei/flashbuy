package cn.tanlw.flashbuy.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @Creator Tan Liwei
 * @Date 2019/9/30 8:01
 */
public class RedisUtil {

    private final static Logger logger = LoggerFactory.getLogger(RedisUtil.class);

    private static JedisPool readPool = null;
    private static JedisPool writePool = null;

    //静态代码初始化池配置
    static {
        try{
            Properties props = new Properties();
            InputStream in = RedisUtil.class.getClassLoader().getResourceAsStream("application-prod.properties");
            props.load(in);

            //创建jedis池配置实例
            JedisPoolConfig config = new JedisPoolConfig();

            //设置池配置项值
//            redis.host=47.96.130.32
//            redis.port=6379
//            redis.timeout=10
//            redis.password=redisredis123123
//            redis.poolMaxTotal=1000
//            redis.poolMaxIdle=500
//            redis.poolMaxWait=500
            config.setMaxTotal(Integer.valueOf(props.getProperty("redis.poolMaxTotal")));
            config.setMaxIdle(Integer.valueOf(props.getProperty("redis.poolMaxIdle")));
            config.setMaxWaitMillis(Long.valueOf(props.getProperty("redis.poolMaxWait")));
//            config.setTestOnBorrow(Boolean.valueOf(props.getProperty("jedis.pool.testOnBorrow")));
//            config.setTestOnReturn(Boolean.valueOf(props.getProperty("jedis.pool.testOnReturn")));

            //根据配置实例化jedis池
            readPool = new JedisPool(config, props.getProperty("redis.host"), Integer.valueOf(props.getProperty("redis.port")),
                    Integer.valueOf(props.getProperty("redis.timeout"))*1000, props.getProperty("redis.password"),0);
            writePool = new JedisPool(config, props.getProperty("redis.host"), Integer.valueOf(props.getProperty("redis.port")),
                    Integer.valueOf(props.getProperty("redis.timeout"))*1000, props.getProperty("redis.password"),0);;

        }catch (IOException e) {
            logger.info("redis连接池异常",e);
        }
    }



    /**获得jedis对象*/
    public static Jedis getReadJedisObject(){
        return readPool.getResource();
    }
    /**获得jedis对象*/
    public static Jedis getWriteJedisObject(){
        return writePool.getResource();
    }

    /**归还jedis对象*/
    public static void returnJedisOjbect(Jedis jedis){
        if (jedis != null) {
            jedis.close();
        }
    }
}
