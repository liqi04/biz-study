import com.rabbitmq.client.*;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

/**
 * Created by liqi1 on 2017/4/13.
 */
public class ChatByRabbitMQ {
    private static final String EXCHANGE_NAME = "chat";
    private static Channel channel;
    private static Connection connection;
    private static ConnectionFactory factory ;
    private String nickname;

    public void receiveMessage() throws IOException, TimeoutException {
        channel = getChanel();
        //指定路由类型
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT);
        //创建一个匿名队列
        String queueName = channel.queueDeclare().getQueue();
        //绑定
        channel.queueBind(queueName, EXCHANGE_NAME, "");

        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope,
                                       AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println(message);
            }
        };
        //消息自动确认
        channel.basicConsume(queueName, false, consumer);
    }

    public void sendMessage() throws IOException, TimeoutException {
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT);
        channel.basicQos(1);
        boolean flag = true;
        while (flag){
            //绑定路由，指定路由类型
            Scanner scanner = new Scanner(System.in);
            if(StringUtils.isBlank(nickname)){
                System.out.println("Welcome to ChatRoom\n(Type [-q] exit)");
                System.out.println("Please Input Your Nickname：");
                nickname = scanner.nextLine();
                System.out.println("Hello ："+nickname+",Now enjoy Chat!!");
            }
            String message = scanner.nextLine();
            if(message.equals("-q")){
                flag=false;
                message = "Exit Success";
                break;
            }
            message = StringUtils.join(nickname,":",message);
            channel.basicPublish(EXCHANGE_NAME, "", MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes("UTF-8"));
        }

        channel.close();
        connection.close();
    }

    public static Channel getChanel(){
        factory = new ConnectionFactory();
        factory.setHost("192.168.2.25");
        factory.setPort(5672);
        factory.setUsername("tao");
        factory.setPassword("1234");
        try {
            connection = factory.newConnection();
            channel = connection.createChannel();
            return channel;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) throws IOException, TimeoutException {
        ChatByRabbitMQ chat = new ChatByRabbitMQ();
        chat.receiveMessage();
        chat.sendMessage();
    }
}
