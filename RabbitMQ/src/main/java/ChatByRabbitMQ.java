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
    private static ConnectionFactory factory ;
    private static Connection connection;

    static {
        try {
            factory = new ConnectionFactory();
            connection = factory.newConnection();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }

    private String nickname;

    public void receiveMessage() throws IOException, TimeoutException {
        Channel recvChannel = getChanel();
        recvChannel = getChanel();
        //指定路由类型
        recvChannel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT);
        //创建一个匿名队列
        String queueName = recvChannel.queueDeclare().getQueue();
        //绑定
        recvChannel.queueBind(queueName, EXCHANGE_NAME, "");

        Consumer consumer = new DefaultConsumer(recvChannel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope,
                                       AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println(message);
            }
        };
        //消息自动确认
        recvChannel.basicConsume(queueName, false, consumer);
    }

    public void sendMessage() throws IOException, TimeoutException {
        Channel sendChannel = getChanel();
        sendChannel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT);
        boolean flag = true;
        while (flag){
            //绑定路由，指定路由类型
            Scanner scanner = new Scanner(System.in);
            if(StringUtils.isBlank(nickname)){
                System.out.println("Welcome to ChatRoom\n(Type [-q] exit)");
                System.out.print("Please Input Your Nickname：");
                nickname = scanner.nextLine();
                System.out.println("Hello ："+nickname+",Now enjoy Chat!!");
            }
            String message = scanner.nextLine();
            if(message.equals("-q")){
                flag =false;
                message = nickname+" has exit chatroom.";
            }
            message = StringUtils.join(nickname,":",message);
            sendChannel.basicPublish(EXCHANGE_NAME, "", MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes("UTF-8"));
        }
        System.out.println("Exit Succeed");
        sendChannel.close();
        connection.close();
    }

    public static Channel getChanel(){
        factory.setHost("127.0.0.1");
        factory.setPort(5672);
        factory.setUsername("guest");
        factory.setPassword("guest");
        try {
            Channel channel = connection.createChannel();
            return channel;
        } catch (IOException e) {
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
