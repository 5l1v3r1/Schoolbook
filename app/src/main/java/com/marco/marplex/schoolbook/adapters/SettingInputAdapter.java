package com.marco.marplex.schoolbook.adapters;

import android.content.Context;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.marco.marplex.schoolbook.R;
import com.marco.marplex.schoolbook.models.SettingInput;
import com.marco.marplex.schoolbook.utilities.SharedPreferences;

import java.util.List;

/**
 * Created by marco on 05/07/16.
 */

public class SettingInputAdapter extends ArrayAdapter<SettingInput> {

    public SettingInputAdapter(Context context, List<SettingInput> objects) {
        super(context, 0, objects);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final SettingInput input = getItem(position);
        switch (input.type){
            case SWITCH:
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.settings_model_switch, parent, false);
                break;
            case DIALOG:
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.settings_model_normal, parent, false);
                break;
        }

        TextView inputTitle = (TextView) convertView.findViewById(R.id.settingsInputTitle);
        inputTitle.setText(input.inputTitle);
        switch (input.type){
            case SWITCH:
                final SwitchCompat switchCompat = (SwitchCompat) convertView.findViewById(R.id.switchCompat);
                switchCompat.setChecked(false);

                if(!SharedPreferences.keyExist(getContext(), "pref", input.key)){
                    if(input.defaultValue != null)
                        switchCompat.setChecked((boolean)input.defaultValue);
                }else {
                    switchCompat.setChecked(SharedPreferences.loadBoolean(getContext(), "pref", input.key));
                }

                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switchCompat.setChecked(switchCompat.isChecked() ? false : true);
                    }
                });

                switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        SharedPreferences.saveBoolean(getContext(), "pref", input.key, isChecked);
                    }
                });

                break;
            case DIALOG:
                final TextView previewText = (TextView) convertView.findViewById(R.id.previewText);
                if(!SharedPreferences.keyExist(getContext(), "pref", input.key)){
                    if(input.defaultValue != null)
                        previewText.setText((String) input.defaultValue);
                }else {
                    previewText.setText(SharedPreferences.loadString(getContext(), "pref", input.key));
                }

                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new MaterialDialog.Builder(getContext())
                                .title(input.inputTitle)
                                .items(input.dialogValues)
                                .alwaysCallSingleChoiceCallback()
                                .autoDismiss(true)
                                .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                                    @Override
                                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                        SharedPreferences.saveString(getContext(), "pref", input.key, text.toString());
                                        previewText.setText(text.toString());
                                        return true;
                                    }
                                })
                                .show();
                    }
                });


        }

        return convertView;
    }
}
