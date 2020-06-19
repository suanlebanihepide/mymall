# mymall
微超市平台开发

# 功能模块
用户管理，用户商品秒杀功能，订单管理，收货地址管理，商品类别管理，购物车管理

# 主要实现
## 采用前后端分离开发，后端采用springBoot +mybatis+redis+nginx进行开发
## 前后之间采用Swagger与前端人员进行开发，使用github进行持续集成
## spring全局异常处理，使用spring schedule进行定时关单，Redisson实现分布式锁
## 部署方面采用多个tomcat集群，使用Nginx进行负载均衡
## 使用springsession实现单点登录
## 使用RockMq作为消息中间件，从而达到流量消峰的目的
## 采用多级缓存实现多级查询优化
## 使用动态验证码进行防刷限流
## 秒杀过程中使用动静分离，将静态页面部署到nginx，在后续开发中可以采用CDN将静态资源进行部署
## 秒杀中采用秒杀大闸和秒杀令牌发放限制令牌发放数量
## 秒杀---流量泄洪，采用线程池实现防止服务器压力过大 采用分布式或者本地队列可以利用降级策略
## 对服务器进行限制并发操作--使用令牌桶的算法进行限流使用googel RateLimiter