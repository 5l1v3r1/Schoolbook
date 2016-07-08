package com.marco.marplex.schoolbook.fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.marco.marplex.schoolbook.R;
import com.marco.marplex.schoolbook.utilities.Connection;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class FeedbackActivity extends AppCompatActivity {

    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.radio_feedback) RadioGroup mRadioGroup;
    @Bind(R.id.input_bug) EditText mBugInput;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        setTitle("Feedback");

    }

    @OnClick(R.id.btn_send)
    public void sendToServer(View view){
        if(Connection.isNetworkAvailable(this)) {
            String rate = ((RadioButton) findViewById(mRadioGroup.getCheckedRadioButtonId())).getText().toString();
            String bugText = mBugInput.getText().toString();

            OkHttpClient client = new OkHttpClient();

            final ProgressDialog dialog = new ProgressDialog(this);
            dialog.setTitle("Sto inviando il tuo feedback");
            dialog.setIndeterminate(true);

            final Request request = new Request.Builder()
                    .url("http://marcoweb12.altervista.org/schoolbook/addFeedback.php?rate=" + rate + "&bugs=" + bugText.replaceAll(" ", "+"))
                    .build();

            Callback callback = new Callback() {
                @Override public void onFailure(Call call, IOException e) { }
                @Override public void onResponse(Call call, Response response) throws IOException {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Grazie per il tuo feedback!", Toast.LENGTH_SHORT).show();
                            dialog.cancel();
                            finish();
                        }
                    });
                }
            };

            client.newCall(request).enqueue(callback);
            dialog.show();
        }else{
            Snackbar.make(view, "Collegati a internet e riprova.", Snackbar.LENGTH_SHORT).show();
        }

    }

}
