package com.swctools.interfaces;

import android.os.Bundle;

public interface YesNoAlertCallBack {
    void onYesNoDialogYesClicked(String activity_command, Bundle bundle);

    void onYesNoDialogNoClicked(String activity_command);
}
