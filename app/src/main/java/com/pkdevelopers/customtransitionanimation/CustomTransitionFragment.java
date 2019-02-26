package com.pkdevelopers.customtransitionanimation;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.transition.Scene;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

public class CustomTransitionFragment extends Fragment implements View.OnClickListener {

    private static final String ESTADO_ESCENA_ACTUAL = "escena_actual";

    // Escenas que usamos
    private Scene[] escenas;

    // Indice actual de escenas
    private int escenaActual;

    // Transicion personalizada
    private Transition transicion;

    // Constructor
    public CustomTransitionFragment(){
    }

    // Metodo que "Infla" la vista
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_custom_transition, container, false);
    }

    // Método que crea la vista
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        // Obtenemos el contexto de la actividad
        Context context = getActivity();

        // Vinculamos el contenedor de el xml "fragment_custom_transition"
        FrameLayout container = (FrameLayout) view.findViewById(R.id.container);
        // Agregamos listener al boton
        view.findViewById(R.id.mostrarSiguienteEscena).setOnClickListener(this);

        // Si es diferente a 0, restauramos el ultimo estado de la posicion comprobada
        if(null != savedInstanceState){
            escenaActual = savedInstanceState.getInt(ESTADO_ESCENA_ACTUAL);
        }

        // Colocamos las escenas aqui
        escenas = new Scene[]{
                Scene.getSceneForLayout(container, R.layout.escena1, context),
                Scene.getSceneForLayout(container, R.layout.escena2, context),
                Scene.getSceneForLayout(container, R.layout.escena3, context),
        };

        // Esta es la transicion personalizada
        transicion = new CambiarColor();

        Toast.makeText(context, "Escena actual " + escenaActual + " Escenas.lenght " +escenas.length, Toast.LENGTH_SHORT).show();

        //Mostramos la escena inicial
        TransitionManager.go(escenas[escenaActual % escenas.length]);

    }

    // Método para salvar la posicion actual
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(ESTADO_ESCENA_ACTUAL, escenaActual);
    }

    // Método para cuando de click en el bóton
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mostrarSiguienteEscena: {
                Toast.makeText(getContext(), "Escena actual " + escenaActual + " Escenas.lenght " +escenas.length, Toast.LENGTH_SHORT).show();

                escenaActual = (escenaActual + 1) % escenas.length;

                // Pasamos la transición personalizada como segundo agumento a TransitionManager.go
                TransitionManager.go(escenas[escenaActual], transicion);
                break;
            }
        }

    }
}
