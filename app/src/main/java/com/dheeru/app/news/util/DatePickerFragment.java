package com.dheeru.app.news.util;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.widget.DatePicker;

import com.dheeru.app.news.activity.SearchFilterFragmentActivity;

import java.util.Calendar;

/**
 * Created by dkthaku on 5/29/16.
 */
public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        month++;
        SearchFilterFragmentActivity f = (SearchFilterFragmentActivity) getTargetFragment();
        //YYYYMMDD for begindate
        f.fromDate(year, month, day);

        Log.i("Date Picker", "Date picker in action: " + year + "/" + month + "/" + day);
    }
}
