package com.example.asuper.gesturerecognizer.ui.gesturesample;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.example.asuper.gesturerecognizer.Gesture;

public class ChangeGestureCodeListener implements AdapterView.OnItemSelectedListener{
    private ViewHolder viewHolder;

    public ChangeGestureCodeListener(ViewHolder viewHolder) {
        this.viewHolder = viewHolder;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(viewHolder.isUserSelection()) {
            Gesture gesture = (Gesture) parent.getItemAtPosition(position);
            viewHolder.setGestureSampleCode(gesture.getCode());
            Log.i("GestureSpinner", gesture.toString());
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) { }
}
