/*
 * @Author: shenzheng
 * @Date: 2020/6/17 22:27
 */

package com.example.mymmal.task;

import com.example.mymmal.config.RedissonManager;
import jodd.util.PropertiesUtil;
import lombok.extern.java.Log;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.TimeUnit;

@Component
@Log
public class CloseOrder_Time {

    @Autowired
    private RedissonManager redissonManager;
    @Autowired
    private RedisTemplate redisTemplate;

    @PreDestroy
    public void delLock() {
        redisTemplate.delete("REDIS_BLOCK.CLOSE_ORDER_TASK_LOCK");
    }

//    @Scheduled(cron = "*/10 * * * * ?")
    public void cloesOrderTask() {

        RLock lock = redissonManager.getRedisson().getLock("REDIS_BLOCK.CLOSE_ORDER_TASK_LOCK");
        boolean getLock = false;
        try {
            if (getLock = lock.tryLock(0, 50, TimeUnit.SECONDS)) {

                log.info("Redisson获取分布式的锁 name{} ThreadName{}");
//                int hour = Integer.parseInt(PropertiesUtil.getProperty("close.order.task.time.hour"));
////                iOrderService.closeOrder(hour);
            } else {
                log.info("Redisson未获取分布式的锁 name{} ThreadName{}");
            }
        } catch (InterruptedException e) {
            log.info("分布式锁获取异常");
            e.printStackTrace();
        } finally {
            if (!getLock) {
                return;
            }
            lock.unlock();
            log.info("分布式锁释放");
        }
    }
}
