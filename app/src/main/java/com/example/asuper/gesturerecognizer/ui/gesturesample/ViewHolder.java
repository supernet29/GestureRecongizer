package com.example.asuper.gesturerecognizer.ui.gesturesample;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.asuper.gesturerecognizer.Gesture;
import com.example.asuper.gesturerecognizer.GestureSample;
import com.example.asuper.gesturerecognizer.R;

import java.util.List;


public class ViewHolder extends RecyclerView.ViewHolder{
    private TextView mTitle;
    private TextView mCode;
    private Spinner mGestureSpinner;

    private GestureSample gestureSample;

    private boolean userSelection;

    public ViewHolder(final View view){
        super(view);
        this.mTitle = view.findViewById(R.id.sample_element_txt);
        this.mCode =view.findViewById(R.id.sample_code);
        this.mGestureSpinner = view.findViewById(R.id.gesture_list);
        this.gestureSample = null;
        this.mGestureSpinner.setOnItemSelectedListener(new ChangeGestureCodeListener(this));
        this.userSelection = false;
    }

    public void setSampleTitle(String title) {
        mTitle.setText(title);
    }

    public void setGestureSample(GestureSample sample) {
        this.gestureSample = sample;
        this.mTitle.setTag(sample);
        setCodeText();
    }

    public void setGestureSampleCode(int code) {
        gestureSample.setGestureCode(code);
        setCodeText();
    }

    public void setGestureList(List<Gesture> gestureList) {
        ArrayAdapter<Gesture> gestureListAdapter = new ArrayAdapter<Gesture>(mGestureSpinner.getContext(), R.layout.support_simple_spinner_dropdown_item, gestureList);
        mGestureSpinner.setAdapter(gestureListAdapter);
    }

    public void setNameClickListener(View.OnClickListener listener) {
        mTitle.setOnClickListener(listener);
    }

    public boolean isUserSelection() {
        return userSelection;
    }
    private void setCodeText() {
       userSelection = false;
       mCode.setText(String.valueOf(gestureSample.getGestureCode()));
       if(gestureSample.getGestureCode() > -1){
           mGestureSpinner.setSelection(gestureSample.getGestureCode());
       }
       userSelection = true;
    }

}
