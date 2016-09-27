package com.marco.marplex.schoolbook.fragments;


import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.marco.marplex.schoolbook.R;
import com.marco.marplex.schoolbook.adapters.ComunicationAdapter;
import com.marco.marplex.schoolbook.connections.ClassevivaCaller;
import com.marco.marplex.schoolbook.fragments.custom.DrawerFragment;
import com.marco.marplex.schoolbook.interfaces.ClassevivaCallback;
import com.marco.marplex.schoolbook.interfaces.Updateable;
import com.marco.marplex.schoolbook.models.Comunication;
import com.marco.marplex.schoolbook.utilities.Comunications;
import com.marco.marplex.schoolbook.utilities.Connection;
import com.marco.marplex.schoolbook.utilities.FileDownloader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class Circolari extends DrawerFragment implements ClassevivaCallback<Comunication>{

    @Bind(R.id.rv_circolari) RecyclerView mList;
    @Bind(R.id.swipe_refresh_layout) SwipeRefreshLayout mSwipe;
    @Bind(R.id.nothingHere) View nothingHere;

    private ClassevivaCaller mCaller;
    private View mRootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mRootView = inflater.inflate(R.layout.fragment_circolari, container, false);
        ButterKnife.bind(this, mRootView);
        mList.setLayoutManager(new LinearLayoutManager(getContext()));
        mList.setHasFixedSize(true);

        mCaller = new ClassevivaCaller(this, getContext());

        mSwipe.setColorSchemeResources(
                R.color.colorPrimaryRed,
                R.color.colorPrimaryDarkRed
        );

        mSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mCaller.getSchoolComunication();
            }
        });

        setTabGone();
        removeMenuItems();

        ArrayList<Comunication> comunications = Comunications.getSavedComunications(getContext());
        if( comunications == null || comunications.size() == 0 || !Comunications.isComunicationsSaved(getContext()) ){
            nothingHere.setVisibility(View.VISIBLE);
        }else{
            nothingHere.setVisibility(View.GONE);
            populateRecyclerView(
                    Comunications.getSavedComunications(getContext())
            );
        }

        return mRootView;
    }

    private void populateRecyclerView(ArrayList<Comunication> list){
        mList.setAdapter(new ComunicationAdapter(list, getContext(), new ComunicationAdapter.ClickEvent() {
            @Override
            public void onItemClicked(String title, final int id, final String link) {
                //Start downloading if it's possible
                if(Connection.isNetworkAvailable(getContext())){
                    new DownloadFile().execute(link, id + ".pdf");
                }else Snackbar.make(mRootView, "Collegati a internet e riprova", Snackbar.LENGTH_SHORT).show();
            }
        }));
    }

    @Override
    public String getTitle() {
        return "Circolari";
    }

    @Override
    public void onResponse(final ArrayList<Comunication> list) {
        try {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (list != null && list.size() != 0) {
                        populateRecyclerView(list);
                        Comunications.saveComunications(getContext(), list);
                    }
                    mSwipe.setRefreshing(false);
                }
            });
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class DownloadFile extends AsyncTask<String, Integer, String> implements Updateable {

        private String fileName;
        private ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(getContext());
            dialog.setMessage("Sto scaricando la circolare...");
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setIndeterminate(false);
            dialog.setCancelable(false);
            dialog.setMax(100);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String fileUrl = strings[0];
            this.fileName = strings[1];
            String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
            File folder = new File(extStorageDirectory, "SchoolbookComunications");
            folder.mkdir();

            File pdfFile = new File(folder, fileName);

            try{
                pdfFile.createNewFile();
            }catch (IOException e){
                e.printStackTrace();
            }
            FileDownloader.downloadFile(fileUrl, pdfFile, getContext(), this);
            return fileUrl;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            dialog.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            dialog.cancel();

            File pdfFile = new File(Environment.getExternalStorageDirectory() + "/SchoolbookComunications/" + this.fileName);
            Uri path = Uri.fromFile(pdfFile);
            Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
            pdfIntent.setDataAndType(path, "application/pdf");
            pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            try{
                startActivity(pdfIntent);
            }catch(ActivityNotFoundException e){
                Toast.makeText(getActivity(), "Nessuna applicazioni avviabile per la visualizzazione dei PDF", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onDownloadProgress(int percentage) {
            publishProgress(percentage);
        }
    }


}
