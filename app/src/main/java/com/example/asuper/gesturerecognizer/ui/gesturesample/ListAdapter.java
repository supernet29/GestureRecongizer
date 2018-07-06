package com.example.asuper.gesturerecognizer.ui.gesturesample;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.asuper.gesturerecognizer.Gesture;
import com.example.asuper.gesturerecognizer.GestureSample;
import com.example.asuper.gesturerecognizer.R;

import java.util.LinkedList;
import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ViewHolder>{
    private List<GestureSample> samples;
    private List<Gesture> gestureList;
    private View.OnClickListener elementListener;

    public ListAdapter(View.OnClickListener elementListener) {
        this.samples = new LinkedList<>();
        this.gestureList = new LinkedList<>();
        this.elementListener = elementListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.sample_list_element, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setNameClickListener(elementListener);
        holder.setGestureSample(samples.get(position));
        holder.setGestureList(gestureList);
        holder.setSampleTitle("sample " + position);
    }

    @Override
    public int getItemCount() {
        return samples.size();
    }

    public void setSamples(List<GestureSample> samples) {
        this.samples = samples;
        notifyDataSetChanged();
    }

    public void setGestures(List<Gesture> gestures) {
        this.gestureList = gestures;
        notifyDataSetChanged();
    }

    public void setElementListener(View.OnClickListener elementListener) {
        this.elementListener = elementListener;
        notifyDataSetChanged();
    }
}
