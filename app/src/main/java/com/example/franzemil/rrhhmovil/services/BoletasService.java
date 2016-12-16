package com.example.franzemil.rrhhmovil.services;

import com.example.franzemil.rrhhmovil.R;

/**
 * Created by franzemil on 16-12-16.
 */

public class BoletasService extends NotificationService {
    final String NOTIFICATION_TITLE = "GESTION DE BOLETAS RECURSOS HUMANOS";
    final int ICON = R.drawable.ic_face_black_24dp;
    public BoletasService() {
        super("boletas.notifications");
    }
}
