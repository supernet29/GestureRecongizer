package com.example.asuper.gesturerecognizer;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
        holder.setSampleTitle("sample " + position);
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
        private View view;
        private TextView mTextView;
        private EditText mCode;
        private Button mButton;

        public ViewHolder(final View view){
            super(view);
            this.view = view;
            this.mTextView = view.findViewById(R.id.sample_element_txt);
            this.mCode = view.findViewById(R.id.code);
            this.mButton = view.findViewById(R.id.code_change_btn);
            this.mButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int code = Integer.parseInt(mCode.getText().toString());
                    GestureSample sample = (GestureSample)view.getTag();
                    sample.setGestureCode(code);
                }
            });
        }

        public void setSampleTitle(String title) {
            mTextView.setText(title);
        }

        public void setGestureSample(GestureSample sample) {
            view.setTag(sample);
        }

        public void setOnclickListener(View.OnClickListener listener) {
            view.setOnClickListener(listener);
        }
    }
}
