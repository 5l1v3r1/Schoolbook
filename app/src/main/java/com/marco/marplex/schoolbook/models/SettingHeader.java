package com.marco.marplex.schoolbook.models;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by marco on 05/07/16.
 */

public class SettingHeader {
    public String title;
    public ArrayList<SettingInput> inputs;

    public SettingHeader(String title, SettingInput... inputs) {
        this.title = title;
        this.inputs = new ArrayList<SettingInput>(Arrays.asList(inputs));
    }
}
