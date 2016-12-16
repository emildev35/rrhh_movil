package com.example.franzemil.rrhhmovil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.franzemil.rrhhmovil.session.SessionManager;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnIniarSession;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.btnIniarSession = (Button) findViewById(R.id.btnIniciarSession);
        this.btnIniarSession.setOnClickListener(this);
        sessionManager = new SessionManager(getApplicationContext());

        if (sessionManager.isLoggedIn()){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onClick(View v) {
        Activity activity = this;
        IntentIntegrator integrator = new IntentIntegrator(activity);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setCameraId(0);
        integrator.setBeepEnabled(false);
        integrator.setBarcodeImageEnabled(false);
        integrator.setPrompt("LEA EL CODIGO EN ADM. SEGURIDAD > MOVIL");
        integrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() != null) {
                String token = result.getContents();
                try {
                    sessionManager.createLoginSession(token);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Toast.makeText(this, "Cancelado", Toast.LENGTH_LONG).show();
            } else {
                String token =  "28eae97ab6e0832e7682e840db2b7ff9735fa8dc";
                try {
                    sessionManager.createLoginSession(token);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            finish();
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (sessionManager.isLoggedIn()){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }
}
