import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.net.ConnectException;
import java.util.concurrent.TimeoutException;

/**
 * Created by liqi1 on 2017/4/11.
 */
public class Producer {
    private static final String QUEUE_NAME="rabbitMQ.test";

    public static void main(String[] args) throws IOException, TimeoutException {
        //创建连接工厂
        ConnectionFactory connectionFactory = new ConnectionFactory();
        //设置Connection相关信息
        connectionFactory.setHost("localhost");
        //创建连接
        Connection connection = connectionFactory.newConnection();
        //创建一个通道
        Channel channel = connection.createChannel();
        //创建一个队列
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);
        for (int i = 0; i < 10; i++) {
            String message = "Hello RabbitMQ"+i;
            //发送消息到队列
            channel.basicPublish("",QUEUE_NAME,null,message.getBytes("UTF-8"));
        }
        String message = "Hello RabbitMQ";
        //发送消息到队列
        channel.basicPublish("",QUEUE_NAME,null,message.getBytes("UTF-8"));
        System.out.println("Producer send "+message);
        //关闭通道和连接
        channel.close();
        connection.close();
    }
}
