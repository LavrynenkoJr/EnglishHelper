package com.cyborg.englishhelperr;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cyborg.englishhelper.R;

import java.util.List;


public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private Words word;
    private DatabaseHelper dbh;
    private int size;

    public MyAdapter(DatabaseHelper dbh) {
        this.dbh = dbh;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView;
        public TextView mTextView2;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            mTextView = (TextView) itemLayoutView.findViewById(R.id.itemText1);
            mTextView2 = (TextView) itemLayoutView.findViewById(R.id.itemText2);

        }
    }

    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.text_item, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyAdapter.ViewHolder holder, int position) {

        List<Words> words = dbh.getAllWords();
        size = words.size();
        word = words.get(position);

        holder.mTextView.setText(word.getWord());
        holder.mTextView2.setText(word.getTranslate());

    }

    @Override
    public int getItemCount() {
        return dbh.getWordsCountByOwner();
    }
}
