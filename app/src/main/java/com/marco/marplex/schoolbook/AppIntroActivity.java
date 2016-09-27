package com.marco.marplex.schoolbook;

import android.Manifest;
import android.os.Bundle;

import com.heinrichreimersoftware.materialintro.app.IntroActivity;
import com.heinrichreimersoftware.materialintro.slide.SimpleSlide;

public class AppIntroActivity extends IntroActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setButtonBackVisible(false);
        setButtonNextVisible(false);
        setButtonCtaVisible(false);
        setButtonCtaTintMode(BUTTON_CTA_TINT_MODE_TEXT);

        addSlide(new SimpleSlide.Builder()
                .title("Benvenuto su Schoolbook")
                .description("Il tuo moderno registro elettronico non ufficiale per Classeviva di Spaggiari Infoschool.")
                .image(R.mipmap.ic_launcher)
                .background(R.color.colorPrimaryAmber)
                .backgroundDark(R.color.colorPrimaryDarkAmber)
                .build());

        addSlide(new SimpleSlide.Builder()
                .title("Permessi")
                .description("Se sei su Marshmallow accetta i permessi. Serviranno in futuro per l'utilizzo dell'applicazione.")
                .image(R.drawable.key)
                .background(R.color.colorPrimary)
                .backgroundDark(R.color.colorPrimaryDark)
                .permission(Manifest.permission.RECORD_AUDIO)
                .scrollable(true)
                .build());

        addSlide(new SimpleSlide.Builder()
                .title("Perfetto")
                .description("Adesso puoi iniziare la tua esplorazione! Se l'app ti piacerà ricorda di votarla sul PlayStore.")
                .image(R.drawable.favorite)
                .background(R.color.colorPrimaryOrange)
                .backgroundDark(R.color.colorPrimaryDarkOrange)
                .build());
    }
}
