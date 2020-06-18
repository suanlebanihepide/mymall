/*
 * @Author: shenzheng
 * @Date: 2020/6/9 3:35
 */

package com.example.mymmal.mq;

import com.alibaba.fastjson.JSON;
import com.aliyun.mq.http.MQClient;
import com.aliyun.mq.http.MQProducer;
import com.aliyun.mq.http.model.TopicMessage;
import org.apache.rocketmq.client.exception.MQClientException;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;


@Component
public class MqProducer {
    private MQProducer producer;
    private String nameAddr = "127.0.0.1:9376";
    String topicName = "demo";
    MQClient mqClient;
    MQProducer mqProducer = null;

    @PostConstruct
    public void init() throws MQClientException {
//        producer = new DefaultMQProducer("producerGroup");
//        producer.setNamesrvAddr(nameAddr);
//        producer.start();
        mqClient = new MQClient(
                // 设置 HTTP 接入域名（此处以公共云生产环境为例）
                "http://1605171301640623.mqrest.cn-beijing.aliyuncs.com",
                // AccessKeyId 阿里云身份验证，在阿里云服务器管理控制台创建
                "LTAI4GArWWzLeSaZVQMhdmu2",
                // AccessKeySecret 阿里云身份验证，在阿里云服务器管理控制台创建
                "qBAe2GerPzGoruSqiBS17QspDgGJfN"
        );
        // 所属的 Topic
        final String topic = "demo";
        // Topic 所属实例 ID，默认实例为空
        final String instanceId = "MQ_INST_1605171301640623_BcrGAiJk";
        // 获取 Topic 的生产者


        if (instanceId != null && instanceId != "") {
            producer = mqClient.getProducer(instanceId, topic);
        } else {
            producer = mqClient.getProducer(topic);
        }

    }

    //消息可能发送失败所以采用事务类型异步消息发是
    public boolean TransactionAsyncReduceStock(Integer itemId, Integer amount) {

        final String topic = "demo";
        // Topic 所属实例 ID，默认实例为空
        final String instanceId = "MQ_INST_1605171301640623_BcrGAiJk";
        // 获取 Topic 的生产者
        if (instanceId != null && instanceId != "") {
            producer = mqClient.getTransProducer(instanceId, topic);
        }
        Map<String, Object> bodyMap = new HashMap<>();
        bodyMap.put("itemId", itemId);
        bodyMap.put("amount", amount);

        try {
            TopicMessage pubMsg = null;
            // 普通消息
            pubMsg = new TopicMessage(
                    // 消息内容
                    JSON.toJSON(bodyMap).toString().getBytes(),
                    // 消息标签
                    "A"
            );
            pubMsg.setMessageKey(String.valueOf("item_stock"));
            // 同步发送消息，只要不抛异常就是成功
            TopicMessage pubResultMsg = producer.publishMessage(pubMsg);
        } catch (Throwable e) {
            // 消息发送失败，需要进行重试处理，可重新发送这条消息或持久化这条数据进行补偿处理
            e.printStackTrace();
            return false;
        }
        return true;

    }


    //同步库存消息
    public boolean asyncReduceStock(Integer itemId, Integer amount) {
        Map<String, Object> bodyMap = new HashMap<>();
        bodyMap.put("itemId", itemId);
        bodyMap.put("amount", amount);

        try {
            TopicMessage pubMsg = null;
            // 普通消息
            pubMsg = new TopicMessage(
                    // 消息内容
                    JSON.toJSON(bodyMap).toString().getBytes(),
                    // 消息标签
                    "A"
            );
            pubMsg.setMessageKey(String.valueOf("item_stock"));
            // 同步发送消息，只要不抛异常就是成功
            TopicMessage pubResultMsg = producer.publishMessage(pubMsg);
        } catch (Throwable e) {
            // 消息发送失败，需要进行重试处理，可重新发送这条消息或持久化这条数据进行补偿处理
            e.printStackTrace();
            return false;
        }

        return true;
    }

}
