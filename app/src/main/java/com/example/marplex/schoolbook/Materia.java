package com.example.marplex.schoolbook;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.example.marplex.schoolbook.models.Obiettivo;
import com.example.marplex.schoolbook.models.Voto;
import com.example.marplex.schoolbook.utilities.ObjectiveUtil;
import com.example.marplex.schoolbook.utilities.Votes;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Materia extends AppCompatActivity{

    @Bind(R.id.votoOrale) TextView orale;
    @Bind(R.id.mediaVoti) TextView media;
    @Bind(R.id.votoScritto) TextView scritto;
    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.card_view) CardView mCardView;

    @Bind(R.id.card_objective) CardView mObjectiveCard;
    @Bind(R.id.txt_currentAverage) TextView mCurrentAverageText;
    @Bind(R.id.txt_objective) TextView mObjecivetiveText;
    @Bind(R.id.txt_objectiveTitle) TextView mObjectiveTitleText;
    @Bind(R.id.pbar_objective) RoundCornerProgressBar mObjectiveProgressBar;

    @Bind(R.id.pbar_materiaPrimo) CircularProgressBar progress;
    @Bind(R.id.fab_addObjective) FloatingActionButton mAddObjective;

    private double totalAverage;
    private int nOfVotes;
    private double sum;
    private String[] mObjecivesArray;

    private int periodo;
    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_materia);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        mAddObjective.setAlpha(0f);
        mAddObjective.setScaleX(0f);
        mAddObjective.setScaleY(0f);
        mAddObjective.animate()
                .alpha(1f)
                .scaleX(1f)
                .scaleY(1f)
                .setDuration(500)
                .start();

        Bundle b = getIntent().getExtras();
        title = b.getString("materia");
        periodo = b.getInt("periodo");

        String materia = title.toUpperCase().substring(0,1)+title.toLowerCase().substring(1, title.length());
        getSupportActionBar().setTitle(materia);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        List<Voto> materiaVoti = Votes.getNumericalVotesByMateria(this, title, periodo);
        nOfVotes = materiaVoti.size();

        double sumScritto = 0;
        int scrittoTimes = 0;
        double sumOrale = 0;
        int oraleTimes = 0;

        for(int i=0; i<materiaVoti.size(); i++){
            if(materiaVoti.get(i).tipo.equals("Scritto") || materiaVoti.get(i).tipo.equals("Pratico")){
                sumScritto += Votes.getNumericalVoteByString(materiaVoti.get(i).voto);
                System.out.println(Votes.getNumericalVoteByString(materiaVoti.get(i).voto));
                scrittoTimes++;
            }else{
                sumOrale += Votes.getNumericalVoteByString(materiaVoti.get(i).voto);
                System.out.println(Votes.getNumericalVoteByString(materiaVoti.get(i).voto));
                oraleTimes++;
            }
            sum += Votes.getNumericalVoteByString(materiaVoti.get(i).voto);
            System.out.println(Votes.getNumericalVoteByString(materiaVoti.get(i).voto));
        }

        totalAverage = sum/materiaVoti.size();
        double scrittoAverage = sumScritto/scrittoTimes;
        double oraleAverage = sumOrale/oraleTimes;

        scritto.setText("" + arrotondaRint(scrittoAverage, 1));
        orale.setText("" + arrotondaRint(oraleAverage, 1));
        media.setText("" + arrotondaRint(totalAverage, 1));

        ObjectAnimator animator = new ObjectAnimator().ofFloat(progress, "progress", (float) totalAverage * 10);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.setDuration(1500).start();

        mCardView.setAlpha(0f);
        mCardView.setY(mCardView.getBottom() + 70);
        PropertyValuesHolder holderY = PropertyValuesHolder.ofFloat("y", 0);
        PropertyValuesHolder holderAlpha = PropertyValuesHolder.ofFloat("alpha", 1f);

        new ObjectAnimator().ofPropertyValuesHolder(mCardView, holderY, holderAlpha)
                .setDuration(600)
                .start();

        if(ObjectiveUtil.isObiettivoSaved(this, materia, periodo)){
            Obiettivo obiettivo = ObjectiveUtil.getObiettivoByMateria(this, title, periodo);
            showAndConfigureObjective(obiettivo.getObiettivo());
        }

    }

    @OnClick(R.id.img_removeObjective)
    public void removeObjective(View view){
        ObjectiveUtil.removeObiettivo(this, title, periodo);
        mObjectiveCard.animate()
                .alpha(0f)
                .setDuration(600)
                .start();

        Snackbar.make(view, "Obiettivo eliminato", Snackbar.LENGTH_SHORT).show();
    }

    @OnClick(R.id.fab_addObjective)
    public void showObjectives(View myView){
        View view = LayoutInflater.from(this).inflate(R.layout.ordina, null, false);
        final Dialog mBottomSheetDialog = new Dialog (this, R.style.MaterialDialogSheet);

        setLayoutForDialog(mBottomSheetDialog, view);
        ListView numberList = (ListView)view.findViewById( R.id.materie);

        mObjecivesArray = new String[10-(int)totalAverage];
        for(int i = (int) totalAverage; i<10; i++){
            mObjecivesArray[10-i-1] = (i + 1) + "";
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mObjecivesArray);

        numberList.setAdapter(arrayAdapter);
        numberList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mBottomSheetDialog.cancel();
                showAndConfigureObjective(Integer.parseInt(mObjecivesArray[i]));
                ObjectiveUtil.setObiettivo(Materia.this, title, periodo, new Obiettivo(Integer.parseInt(mObjecivesArray[i])));
            }
        });
    }

    private void showAndConfigureObjective(int obiettivo){
        if(mObjectiveCard.getVisibility() == View.GONE){
            mObjectiveCard.setAlpha(0f);
            mObjectiveCard.setY(mObjectiveCard.getY() + mObjectiveCard.getHeight() + 70);
            mObjectiveCard.animate()
                    .translationYBy(-70)
                    .alpha(1f)
                    .setDuration(600)
                    .start();
        }
        mObjectiveCard.setAlpha(1f);
        mObjectiveCard.setVisibility(View.VISIBLE);


        int objective = obiettivo;
        mCurrentAverageText.setText(String.valueOf(arrotondaRint(totalAverage, 1)));
        mObjecivetiveText.setText(obiettivo+"");

        mObjectiveProgressBar.setMax(100);
        mObjectiveProgressBar.setProgress(0);

        float percentFromDelta = ( (float) totalAverage - (int) totalAverage ) / ( objective - (int) totalAverage ) * 100;
        ObjectAnimator objectAnimator = new ObjectAnimator().ofFloat(mObjectiveProgressBar, "progress", percentFromDelta);
        objectAnimator.setInterpolator(new AccelerateDecelerateInterpolator());

        objectAnimator
                .setDuration(1000)
                .start();

        double voteToGet = arrotondaRint( ( objective * ( nOfVotes + 1 ) ) - sum, 1);
        mObjectiveTitleText.setText("Devi prendere almeno un " + voteToGet);
    }

    private void setLayoutForDialog(Dialog dialog, View view){
        view.findViewById(R.id.dialog_title).setVisibility(View.VISIBLE);
        dialog.setContentView(view);
        dialog.setCancelable(true);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                onBackPressed();
            default: return super.onOptionsItemSelected(menuItem);
        }
    }

    public static double arrotondaRint(double value, int numCifreDecimali) {
        double temp = Math.pow(10, numCifreDecimali);
        return Math.rint(value * temp) / temp;
    }
}
