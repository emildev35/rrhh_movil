package com.example.franzemil.rrhhmovil.session;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.franzemil.rrhhmovil.LoginActivity;
import com.example.franzemil.rrhhmovil.MainActivity;
import com.example.franzemil.rrhhmovil.R;
import com.example.franzemil.rrhhmovil.models.Usuario;
import com.example.franzemil.rrhhmovil.services.AuthService;
import com.example.franzemil.rrhhmovil.services.BoletasService;
import com.example.franzemil.rrhhmovil.services.NotificationService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Gestion el ingreso al sistema
 */
public class SessionManager {

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;

    private Usuario usuario;

    int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "RecursoHumanosRef";
    private static final String IS_LOGIN = "IS_LOGIN";

    /**
     * Gestion el contexto
     * @param context
     */
    public SessionManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    /**
     * Se inicia la session almacenando dentro del sharedpreferences
     * @param token
     */
    public void createLoginSession(final String token) throws JSONException {

        JSONObject parameters = new JSONObject();
        parameters.put("token", token);

        RequestQueue queue = Volley.newRequestQueue(_context);

        JsonObjectRequest jsonRequest = new JsonObjectRequest(
                Request.Method.POST,
                _context.getString(R.string.SERVER)+ "/seguridad/api/movil/login/", parameters,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response) {
                        Intent intent = new Intent(_context, MainActivity.class);
                        try {
                            editor.putString("FULL_NAME", response.getJSONObject("persona").getString("nombre_completo"));
                            editor.putString("CI", response.getJSONObject("persona").getString("ci"));
                            editor.putInt("REACION_LABORAL", response.getJSONObject("persona").getInt("relacion_laboral_id"));
                            editor.putString("CARGO", response.getJSONObject("persona").getString("cargo"));
                            editor.putString("UNIDAD", "");
                            editor.putInt("USUARIO_ID", response.getInt("id_usuario"));
                            editor.putBoolean(IS_LOGIN, true);
                            editor.commit();

                            JSONArray exchanges = response.getJSONArray("exchanges");

                            for(int i = 0; i < exchanges.length(); i++)
                            {
                                JSONObject exchange = exchanges.getJSONObject(i);
                                startNoticationService(exchange.getString("exchange_name"), exchange.getString("type"), token);
                            }

                            _context.startActivity(intent);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("LOGIN", new String(error.networkResponse.data));
                    }
                });

        queue.add(jsonRequest);

    }

    public void checkLogin(){
        if(!this.isLoggedIn()){
            Intent i = new Intent(_context, LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            _context.startActivity(i);
        }
    }

    /**
     * Retorn los datos del usaurio que se encuentra dentro de
     * la session
     * @return El Usuario que se encuentra logueado
     */
    public Usuario getUserDetails(){
        Usuario usuario = new Usuario();
        usuario.setFullName(pref.getString("FULL_NAME", ""));
        usuario.setCi(pref.getString("CI", ""));
        usuario.setRelacionLaboral(pref.getInt("REACION_LABORAL", 0));
        usuario.setCargo(pref.getString("CARGO", ""));
        usuario.setUnidad(pref.getString("UNIDAD", ""));
        usuario.setId(pref.getInt("USUARIO_ID", 0));
        return usuario;
    }

    /**
     * Elimina la session del Usuario
     */
    public void logoutUser(){
        editor.clear();
        editor.commit();

        Intent i = new Intent(_context, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        _context.startActivity(i);
        stopAllNotificationService();
    }

    /**
     * Verifica que exista un login
     * @return El valor que este
     */
    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }


    /**
     * Inicia un service para la captura de notificaiones
     * @param exchangeName
     * @param exchangeType
     * @param routingKey
     */
    public void startNoticationService(String exchangeName, String exchangeType, String routingKey){
        Intent intent = new Intent();
        if (exchangeName.equals("auth")){
             intent = new Intent(this._context, AuthService.class);
        } else if (exchangeName.equals("boletas")){
             intent = new Intent(this._context, BoletasService.class);
        }
        intent.putExtra("EXCHANGE_NAME", exchangeName);
        intent.putExtra("EXCHANGE_TYPE", exchangeType);
        intent.putExtra("ROUTING_KEY", routingKey);
        this._context.startService(intent);
    }


    /**
     * Cierra todos lo servicio que reciben notificacioens
     */
    public void stopAllNotificationService(){
        Intent auth = new Intent(this._context.getApplicationContext(), AuthService.class);
        Intent boletas = new Intent(this._context.getApplicationContext(), BoletasService.class);
        Intent notifications = new Intent(this._context.getApplicationContext(), NotificationService.class);
        this._context.getApplicationContext().stopService(auth);
        this._context.getApplicationContext().stopService(boletas);
        this._context.getApplicationContext().stopService(notifications);
    }
}
