#秒杀接口优化  
1. Redis预减库存减少数据库访问  
2. 内存标记减少Redis访问  
3. 请求先入队列缓存，异步下单，增强用户体验  
4. RabbitMQ安装与Spring Boot集成  
5. Nginx水平扩展
  反向代理，负载均衡，缓存，LVS->M Nginx->N Tomcat  
6. 压测  

Mycat 分库分表中间件  

思路：减少数据库访问 
1. 系统初始化，  商品库存数量加载到Redis  
2. 收到请求， 尝试预减库存， 成功则跳 3.  
3. 请求入队， 返回排队中  
4. 请求出队， 减少库存  生成订单  
5. 客户端轮训，是否秒杀成功  

Rabbitmq管理后台 http://IPADDRESS:15672/  
秒杀的业务特点，大量的用户重复请求，即可能是用户点击请求，也可能是刷的    
发送者发消息 -> 交换机 -> Queue  

SpringBoot2使用Druid开启监控  
https://blog.csdn.net/yhflyl/article/details/81228669

Redis监控工具  