package com.example.franzemil.rrhhmovil.services;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.example.franzemil.rrhhmovil.R;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.TimeoutException;

/**
 * Service basico para a conexion con los servidores de RabbitMQ
 */
public class MQService extends IntentService {
    private static final String EXCHANGE_NAME = "direct_logs";
    public MQService(){
        super("MQService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost(getString(R.string.RABBIRMQ_SERVER));
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();

            channel.exchangeDeclare(EXCHANGE_NAME, "direct");
            String queueName = channel.queueDeclare().getQueue();


            channel.queueBind(queueName, EXCHANGE_NAME, "info");
            Consumer consumer = new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope,
                                           AMQP.BasicProperties properties, byte[] body) throws IOException {
                    String message = new String(body, "UTF-8");
                    android.support.v4.app.NotificationCompat.Builder mBuilder =
                            new NotificationCompat.Builder(getApplicationContext())
                                    .setSmallIcon(R.drawable.ic_face_black_24dp)
                                    .setContentTitle("My notification")
                                    .setContentText(message);

                    int mNotificationId = new Date().getSeconds() + new Date().getMinutes();
                    NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    mNotifyMgr.notify(mNotificationId, mBuilder.build());
                }
            };
            while (true) {
                channel.basicConsume(queueName, true, consumer);
                Thread.sleep(4000);
            }

        }catch (IOException e){} catch (TimeoutException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
