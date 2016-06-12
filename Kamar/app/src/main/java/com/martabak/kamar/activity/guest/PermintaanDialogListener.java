package com.martabak.kamar.activity.guest;

import android.app.DialogFragment;

/**
 * Created by adarsh on 13/06/16.
 */
public interface PermintaanDialogListener {

    void onDialogPositiveClick(DialogFragment dialog, Boolean success);
    void onDialogNegativeClick(DialogFragment dialog);
}
