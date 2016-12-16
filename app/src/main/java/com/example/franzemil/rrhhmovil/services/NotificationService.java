package com.example.franzemil.rrhhmovil.services;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageInstaller;
import android.support.v4.app.NotificationCompat;

import com.example.franzemil.rrhhmovil.R;
import com.example.franzemil.rrhhmovil.session.SessionManager;
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
 * Service que nos permitira recebir las notificacion del backend atravez del
 * sistema backend
 */
public class NotificationService extends IntentService {

    final String NOTIFICATION_TITLE = "SISTEMAS DE RECURSOS HUMANOS";
    final int ICON = R.drawable.ic_face_black_24dp;


    public NotificationService() {
        super("BaseNotificationRRHH");
    }

    public NotificationService(String name) {
        super(name);
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        final String EXCHANGE_NAME = intent.getStringExtra("EXCHANGE_NAME");
        final String EXCHANGE_TYPE = intent.getStringExtra("EXCHANGE_TYPE");
        final String ROUTING_KEY = intent.getStringExtra("ROUTING_KEY");

        try {
            SessionManager  sessionManager = new SessionManager(this);
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost(getString(R.string.RABBIRMQ_SERVER));
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();

            channel.exchangeDeclare(EXCHANGE_NAME, EXCHANGE_TYPE);
            String queueName = channel.queueDeclare().getQueue();


            channel.queueBind(queueName, EXCHANGE_NAME, ROUTING_KEY);
            Consumer consumer = new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope,
                                           AMQP.BasicProperties properties, byte[] body) throws IOException {
                    String message = new String(body, "UTF-8");
                    android.support.v4.app.NotificationCompat.Builder mBuilder =
                            new NotificationCompat.Builder(getApplicationContext())
                                    .setSmallIcon(ICON)
                                    .setContentTitle(NOTIFICATION_TITLE)
                                    .setContentText(message);
                    int mNotificationId = new Date().getSeconds() + new Date().getMinutes();
                    NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    mNotifyMgr.notify(mNotificationId, mBuilder.build());
                }
            };
            while (true) {
                if (!sessionManager.isLoggedIn()) break;
                channel.basicConsume(queueName, true, consumer);
                Thread.sleep(4000);
            }
        }catch (IOException e){} catch (TimeoutException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
}
