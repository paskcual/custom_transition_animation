package com.pkdevelopers.customtransitionanimation;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ViewAnimator;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Si no hay ninguna instancia guardada, entramos y creamos un nuevo fragmento de transicion
        // personalizado
        if (savedInstanceState == null) {

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            CustomTransitionFragment ctf = new CustomTransitionFragment();

            // Remplazamos el fragmento default, por el ya creado
            ft.replace(R.id.contenido_fragmento, ctf);
            // Aplicamos los cambios
            ft.commit();

        }

    }

}
