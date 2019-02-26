package com.pkdevelopers.customtransitionanimation;

import android.animation.Animator;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.transition.Transition;
import android.transition.TransitionValues;
import android.view.View;
import android.view.ViewGroup;

public class CambiarColor extends Transition {

    // LLAVE----------------------------------------------------------------------------------------
    // Definimos una llave para almacenar apropiadamente el valor en TransitionValues.values
    // con la sintaxis package_name:transition_class:property_name para evitar coliciones
    private static final String PROPNAME_BACKGROUND =
            "customtransitionanimation:change_color:background";
    // ---------------------------------------------------------------------------------------------

    // CAPTURAR LOS DATOS---------------------------------------------------------------------------
    // Para la vista en transitionValues.view, obten la vista que tu quieres y ponlas en
    // transitionValues.values
    private void captureValues(TransitionValues transitionValues) {
        // Obtenemos la referencia de la vista
        View view = transitionValues.view;
        // Guardamos la propiedad de su fondo en los valores del mapa
        transitionValues.values.put(PROPNAME_BACKGROUND, view.getBackground());

    }

    @Override
    public void captureStartValues(TransitionValues transitionValues) {
        captureValues(transitionValues);
    }

    // Capture el valor de la propiedad dibujable de fondo para un objetivo en la escena final.
    @Override
    public void captureEndValues(TransitionValues transitionValues) {
        captureValues(transitionValues);
    }
    // ---------------------------------------------------------------------------------------------

    // CREAR LA ANIMACION---------------------------------------------------------------------------
    // Crea una animación para cada objetivo que se encuentre tanto en la escena inicial como en la final.
    // Para cada par de objetivos, si su valor de propiedad de fondo es un color (en lugar de un gráfico),
    // crea un ValueAnimator basado en un ArgbEvaluator que se interpola entre el inicio y
    // color final. También crea un escucha de actualización que establece el color de fondo de la
    // Vista para cada uno cuadro de animación
    @Override
    public Animator createAnimator(ViewGroup sceneRoot,
                                   TransitionValues startValues, TransitionValues endValues) {

        // Esta transición solo se puede aplicar a las vistas que se encuentran en las escenas
        // inicial y final.
        if (null == startValues || null == endValues) {
            return null;
        }

        // Almacena una referencia conveniente al objetivo.
        // Tanto el diseño inicial como el final tienen el mismo objetivo.
        final View view = endValues.view;

        // Almacena el objeto que contiene la propiedad de fondo tanto para el inicio
        // como para el final de los diseños.
        Drawable startBackground = (Drawable) startValues.values.get(PROPNAME_BACKGROUND);
        Drawable endBackground = (Drawable) endValues.values.get(PROPNAME_BACKGROUND);

        // Esta transición cambia los colores de fondo para un objetivo. No anima a ningún otro.
        // cambia el fondo. Si la propiedad no es un ColorDrawable, ignora el objetivo.
        if (startBackground instanceof ColorDrawable && endBackground instanceof ColorDrawable) {

            ColorDrawable startColor = (ColorDrawable) startBackground;
            ColorDrawable endColor = (ColorDrawable) endBackground;

            // Si el color de fondo para el objetivo en los diseños inicial y final es
            // diferente, crea una animación.
            if (startColor.getColor() != endColor.getColor()) {
                // Crear un nuevo objeto Animator para aplicar a los objetivos como el marco de las
                // transiciones cambia desde el inicio hasta el diseño final. Utilice la clase
                // ValueAnimator,que proporciona un impulso de temporización para cambiar los
                // valores de propiedad que se le proporcionan. la animación se ejecuta en el hilo
                // de la interfaz de usuario. El evaluador controla qué tipo de interpolacion esta hecha.
                // En este caso, un ArgbEvaluator interpola entre dos Valores #argb,
                // que se especifican como los argumentos de entrada 2 y 3.
                ValueAnimator animator = ValueAnimator.ofObject(new ArgbEvaluator(),
                        startColor.getColor(), endColor.getColor());

                // Agregamos un listener que actualizara el objeto de animacion
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        Object value = animation.getAnimatedValue();
                        // Cada vez que ValueAnimator produce una nueva frame en la animacion,
                        // cambia el color de fondo del objetivo. Hay que asegurarnos de que el
                        // valor no sea null
                        if (null != value) {
                            view.setBackgroundColor((Integer) value);
                        }
                    }
                });

                // Regresamos el objeto Animator a las transiciones del framework, mientras el
                // framework cambie entre los layouts de inicio y de fin, este se aplica a la
                // animacion que se ha creado.
                return animator;
            }
        }
        // Para fondos que no sean de ColorDrawable, simplemente devolvemos null y no se
        // realizará ninguna animación.
        return null;
    }
    // ---------------------------------------------------------------------------------------------

}
