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
 * Modelo de conexion para gestionar el login
 */
public class AuthService extends NotificationService {

    final String NOTIFICATION_TITLE = "SEGURIDAD RECURSOS HUMANOS";
    final int ICON = R.drawable.ic_face_black_24dp;

    public AuthService() {
        super("auth.notifications");
    }

}
