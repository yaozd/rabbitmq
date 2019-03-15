package com.space.rbq.store.test4mq;

import com.rabbitmq.client.Channel;
import com.space.rbq.store.service.StoreService;
import com.space.rbq.store.test4mq.taskExt.TaskConfigEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class PayConsumer {
    @Autowired
    private StoreService storeService;

    /*对列名称*/
    public final String QUEUE_NAME1 = "first-queue";

    /**
     * queues  指定从哪个队列（queue）订阅消息
     * @param message
     * @param channel
     */
    @RabbitListener(queues = {QUEUE_NAME1},containerFactory = "payContainerFactory")
    public void handleMessage(Message message, Channel channel) throws IOException {
        try {
            // 处理消息
            //System.out.println("OrderConsumer {} handleMessage :"+message);
            String data= new String(message.getBody());
            log.info("Send="+data);
            TaskConfigEnum.PAY.sendData(data);
            if(Thread.currentThread().getId()>0){
                //throw new IllegalStateException("模拟异常");
            }
            //如果使用的手动模式还是需要的ACK确认的-byArvin-2019-03-15-1639
            //推荐把channel.basicAck还是要放在TaskConfigEnum.PAY.sendData的下面。
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), true);

        }catch (Exception e){
            //log.error("OrderConsumer  handleMessage {} , error:",message,e);
            // 处理消息失败，将消息重新放回队列
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false,true);
        }
    }
}
