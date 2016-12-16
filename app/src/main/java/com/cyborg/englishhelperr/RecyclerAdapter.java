package com.cyborg.englishhelperr;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cyborg.englishhelper.R;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private final View.OnClickListener mOnClickListener;

    private Words[] words;
    private Words word;

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView variantsView;

        public ViewHolder(View itemView) {
            super(itemView);
            variantsView = (TextView) itemView.findViewById(R.id.variantsItem);
        }
    }

    public RecyclerAdapter(Words[] words, View.OnClickListener onClickListener){
        this.words = words;
        mOnClickListener = onClickListener;
    }

    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.variants_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        word = words[position];

        holder.variantsView.setText(word.getTranslate());
        holder.variantsView.setOnClickListener(mOnClickListener);
        holder.variantsView.setTag(word);

    }

    @Override
    public int getItemCount() {
        return words.length;
    }

    public void setWords(Words[] words) {
        this.words = words;
        notifyDataSetChanged();
    }
}
