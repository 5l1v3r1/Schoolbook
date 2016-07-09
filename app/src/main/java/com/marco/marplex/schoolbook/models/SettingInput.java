package com.marco.marplex.schoolbook.models;

import com.marco.marplex.schoolbook.enums.InputType;

/**
 * Created by marco on 05/07/16.
 */

public class SettingInput {
    public String inputTitle;
    public InputType type;
    public String key;
    public boolean defaultValue;
    public String depends;

    public SettingInput(String inputTitle, InputType type, String key, boolean defaultValue) {
        this.inputTitle = inputTitle;
        this.type = type;
        this.key = key;
        this.defaultValue = defaultValue;
    }

    public SettingInput(String inputTitle, InputType type, String key, String depends) {
        this.inputTitle = inputTitle;
        this.type = type;
        this.key = key;
        this.depends = depends;
    }

    public SettingInput(String inputTitle, InputType type, String key) {
        this.inputTitle = inputTitle;
        this.type = type;
        this.key = key;
    }
}
