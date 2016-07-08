package com.marco.marplex.schoolbook.fragments;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.marco.marplex.schoolbook.R;
import com.marco.marplex.schoolbook.fragments.custom.DrawerFragment;
import com.marco.marplex.schoolbook.models.Library;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class AboutFragment extends DrawerFragment {

    private View mView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView =  inflater.inflate(R.layout.fragment_about, container, false);

        setTabGone();
        removeMenuItems();

        //Prepare a libraries list
        List<Library> list = new ArrayList<>();
        list.add(new Library("Ozodrukh", "CircularReveal", R.drawable.circularreveal, "https://github.com/ozodrukh/CircularReveal"));
        list.add(new Library("Simbiose", "Encryption", R.drawable.simbiose, "https://github.com/simbiose/Encryption"));
        list.add(new Library("Lopez Mikhael", "Circular Progress Bar", R.drawable.circular_progress, "https://github.com/lopspower/CircularProgressBar"));
        list.add(new Library("Prolific Interactive", "Material Calendar View", R.drawable.material_calendar_view, "https://github.com/prolificinteractive/material-calendarview"));
        list.add(new Library("Jake Wharton", "Butter Knife", R.drawable.butterknife, "https://github.com/JakeWharton/butterknife"));
        list.add(new Library("Zachary Reik", "DilatingDotsProgressBar", R.drawable.github, "https://github.com/JustZak/DilatingDotsProgressBar"));
        list.add(new Library("Akexorcist", "RoundCornerProgressBar", R.drawable.roundprogress, "https://github.com/akexorcist/Android-RoundCornerProgressBar"));
        list.add(new Library("Square", "OkHttp", R.drawable.square, "http://square.github.io/okhttp/"));

        //Static list, add each model view with corresponding datas in the LinearLayout
        LinearLayout layout = (LinearLayout) mView.findViewById(R.id.lib_list);
        layout.setOrientation(LinearLayout.VERTICAL);
        for (final Library library : list) {
            View convertView = inflater.inflate(R.layout.model_libray, null);

            TextView author = (TextView) convertView.findViewById(R.id.txt_creator_library);
            TextView name = (TextView) convertView.findViewById(R.id.txt_library_name);
            ImageView icon = (ImageView) convertView.findViewById(R.id.img_icon);

            author.setText(library.author);
            name.setText(library.name);
            icon.setImageResource(library.image);

            convertView.findViewById(R.id.lib_model_container).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(library.url));
                    startActivity(intent);
                }
            });

            if(convertView.getParent()!=null)
                ((ViewGroup)convertView.getParent()).removeView(convertView);

            layout.setOrientation(LinearLayout.VERTICAL);
            layout.addView(convertView);
        }

        return mView;
    }

    @Override
    public String getTitle() {
        return "About";
    }
}
