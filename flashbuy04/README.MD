JMeter
1. 录好jmx
2. sh jmeter.sh -n -t XXX.jmx -l result.jtl
3. 导入 jmeter

#100个并发连接， 100000个请求
redis-benchmark -h 127.0.0.1 -p 6379 -c 100 -n 100000

#存取大小为100字节的数据包
redis-benchmark -h 127.0.0.1 -p 6379 -q -d 100
https://www.alibabacloud.com/help/faq-detail/38689.htm

redis-benchmark -t set,lpush -n 100000 -q

redis-benchmark -n 100000 -q script load "redis.call('set', 'name','daniel')"

maven-war-plugin 插件

一千个线程并发 循环10次 
1. 出现过 timeout，调大timeout时间后OK
2. 出现过以下错误
2019-09-07 23:02:55.020  INFO 24364 --- [o-8077-exec-195] c.t.flashbuy.controller.UserController   : list, user:cn.tanlw.flashbuy.domain.FlashbuyUser@335bb841
redis.clients.jedis.exceptions.JedisConnectionException: java.net.SocketException: Connection reset
	at redis.clients.util.RedisInputStream.ensureFill(RedisInputStream.java:202)
	at redis.clients.util.RedisInputStream.readByte(RedisInputStream.java:40)
	at redis.clients.jedis.Protocol.process(Protocol.java:151)
	at redis.clients.jedis.Protocol.read(Protocol.java:215)
	at redis.clients.jedis.Connection.readProtocolWithCheckingBroken(Connection.java:340)
	at redis.clients.jedis.Connection.getBinaryBulkReply(Connection.java:259)
	at redis.clients.jedis.Connection.getBulkReply(Connection.java:248)
	at redis.clients.jedis.Jedis.get(Jedis.java:153)
	at cn.tanlw.flashbuy.redis.RedisService.get(RedisService.java:23)
	at cn.tanlw.flashbuy.service.FlashbuyUserService.getByToken(FlashbuyUserService.java:68)
	at cn.tanlw.flashbuy.config.AuthInterceptor.preHandle(AuthInterceptor.java:34)
	at org.springframework.web.servlet.HandlerExecutionChain.applyPreHandle(HandlerExecutionChain.java:133)
	at org.springframework.web.servlet.DispatcherServlet.doDispatch(DispatcherServlet.java:962)
	at org.springframework.web.servlet.DispatcherServlet.doService(DispatcherServlet.java:901)
	at org.springframework.web.servlet.FrameworkServlet.processRequest(FrameworkServlet.java:970)
	at org.springframework.web.servlet.FrameworkServlet.doGet(FrameworkServlet.java:861)
	at javax.servlet.http.HttpServlet.service(HttpServlet.java:635)
	at org.springframework.web.servlet.FrameworkServlet.service(FrameworkServlet.java:846)
	at javax.servlet.http.HttpServlet.service(HttpServlet.java:742)
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:231)
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166)
	at org.apache.tomcat.websocket.server.WsFilter.doFilter(WsFilter.java:52)
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193)
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166)
	at org.springframework.web.filter.RequestContextFilter.doFilterInternal(RequestContextFilter.java:99)
	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:107)
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193)
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166)
	at org.springframework.web.filter.HttpPutFormContentFilter.doFilterInternal(HttpPutFormContentFilter.java:108)
	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:107)
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193)
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166)
	at org.springframework.web.filter.HiddenHttpMethodFilter.doFilterInternal(HiddenHttpMethodFilter.java:81)
	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:107)
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193)
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166)
	at org.springframework.web.filter.CharacterEncodingFilter.doFilterInternal(CharacterEncodingFilter.java:197)
	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:107)
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193)
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166)
	at org.apache.catalina.core.StandardWrapperValve.invoke(StandardWrapperValve.java:199)
	at org.apache.catalina.core.StandardContextValve.invoke(StandardContextValve.java:96)
	at org.apache.catalina.authenticator.AuthenticatorBase.invoke(AuthenticatorBase.java:478)
	at org.apache.catalina.core.StandardHostValve.invoke(StandardHostValve.java:140)
	at org.apache.catalina.valves.ErrorReportValve.invoke(ErrorReportValve.java:81)
	at org.apache.catalina.core.StandardEngineValve.invoke(StandardEngineValve.java:87)
	at org.apache.catalina.connector.CoyoteAdapter.service(CoyoteAdapter.java:342)
	at org.apache.coyote.http11.Http11Processor.service(Http11Processor.java:803)
	at org.apache.coyote.AbstractProcessorLight.process(AbstractProcessorLight.java:66)
	at org.apache.coyote.AbstractProtocol$ConnectionHandler.process(AbstractProtocol.java:868)
	at org.apache.tomcat.util.net.NioEndpoint$SocketProcessor.doRun(NioEndpoint.java:1459)
	at org.apache.tomcat.util.net.SocketProcessorBase.run(SocketProcessorBase.java:49)
	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1149)
	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:624)
	at org.apache.tomcat.util.threads.TaskThread$WrappingRunnable.run(TaskThread.java:61)
	at java.lang.Thread.run(Thread.java:748)
Caused by: java.net.SocketException: Connection reset
	at java.net.SocketInputStream.read(SocketInputStream.java:210)
	at java.net.SocketInputStream.read(SocketInputStream.java:141)
	at java.net.SocketInputStream.read(SocketInputStream.java:127)
	at redis.clients.util.RedisInputStream.ensureFill(RedisInputStream.java:196)
	... 55 more
2019-09-07 23:02:55.071  INFO 24364 --- [io-8077-exec-83] c.t.flashbuy.controller.UserController   : list, user:cn.tanlw.flashbuy.domain.FlashbuyUser@2faadbb3
2


nohup java -jar flashbuy04.jar --spring.profiles.active=prod &

在linux中如何解压.tgz

1，进入下载路径下，

输入命令： cd Downloads

2，解压到当前文件夹

输入：tar zxvf  文件名.tgz -C ./     比如我的是：tar zxvf simple-examples.tgz -C ./

3，解压到指定文件夹下

输入：tar  zxvf  文件名.tgz  -C /指定路径

拓展:压缩文件的命令为如下

输入: tar -cf 要压缩的文件名.tar *