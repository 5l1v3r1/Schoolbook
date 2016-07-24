package com.marco.marplex.schoolbook;

import com.marco.marplex.schoolbook.enums.InputType;
import com.marco.marplex.schoolbook.models.SettingHeader;
import com.marco.marplex.schoolbook.models.SettingInput;

public class SettingsActivity extends BaseSettingActivity {

    @Override
    public void onInit() {
        addHeader(new SettingHeader(
                "Generale",
                new SettingInput(
                        "Cifre decimali dopo il voto",
                        InputType.DIALOG,
                        "setting_digits",
                        "2",
                        new String[]{
                                "0", "1", "2"
                        }
                )
        ));
        addHeader(new SettingHeader(
                "Aspetto",
                new SettingInput(
                        "Tema",
                        InputType.DIALOG,
                        "setting_theme",
                        "Chiaro",
                        new String[]{
                                "Chiaro", "Scuro"
                        }
                )
        ));
        addHeader(new SettingHeader(
                "Sincronizzazione",
                new SettingInput(
                        "Aggiornamento automatico",
                        InputType.SWITCH,
                        "setting_sync"
                )
        ));
        addHeader(new SettingHeader(
                "Notifiche",
                new SettingInput(
                        "Notifiche abilitate",
                        InputType.SWITCH,
                        "setting_notification",
                        true
                ),
                new SettingInput(
                        "Suona quando arriva una notifica",
                        InputType.SWITCH,
                        "setting_suona",
                        "setting_notification"
                ),
                new SettingInput(
                        "Vibra quando arriva una notifica",
                        InputType.SWITCH,
                        "setting_vibra",
                        "setting_notification"
                )
        ));
    }
}
