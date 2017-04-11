import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by liqi1 on 2017/4/11.
 */
public class Producer {
    private static final String QUEUE_NAME="task_queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory=new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection=factory.newConnection();
        Channel channel=connection.createChannel();
        channel.queueDeclare(QUEUE_NAME,true,false,false,null);
        //分发信息
        for (int i=0;i<10;i++){
            String message="Hello RabbitMQ"+i;
            channel.basicPublish("",QUEUE_NAME,
                    MessageProperties.PERSISTENT_TEXT_PLAIN,message.getBytes());
            System.out.println("NewTask send '"+message+"'");
        }
        channel.close();
        connection.close();
    }
}
