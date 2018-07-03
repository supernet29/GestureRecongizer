package com.example.asuper.gesturerecognizer;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import java.util.LinkedList;
import java.util.List;

public class SampleListViewAdapter extends RecyclerView.Adapter<SampleListViewAdapter.ViewHolder>{
    private LinkedList<GestureSample> samples;
    private View.OnClickListener elementListener;

    public SampleListViewAdapter(View.OnClickListener elementListener) {
        this.samples = new LinkedList<>();
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
        holder.mTextView.setText("sample " + position);
        holder.setOnclickListener(elementListener);
        holder.setGestureSample(samples.get(position));
    }

    @Override
    public int getItemCount() {
        return samples.size();
    }

    public void setSamples(List<GestureSample> samples) {
        this.samples.clear();
        this.samples.addAll(samples);
        notifyDataSetChanged();
    }

    public void setElementListener(View.OnClickListener elementListener) {
        this.elementListener = elementListener;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private View v;
        public TextView mTextView;
        public ViewHolder(View v){
            super(v);
            this.v = v;
            mTextView = v.findViewById(R.id.sample_element_txt);
        }

        public void setGestureSample(GestureSample sample) {
            v.setTag(sample);
        }

        public void setOnclickListener(View.OnClickListener listener) {
            v.setOnClickListener(listener);
        }
    }
}
