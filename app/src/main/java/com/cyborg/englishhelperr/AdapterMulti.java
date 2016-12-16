package com.cyborg.englishhelperr;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cyborg.englishhelper.R;
import com.cyborg.englishhelperr.MultiChoiceMode.MultiChoiceMode;
import com.cyborg.englishhelperr.MultiChoiceMode.SelectableAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class AdapterMulti extends SelectableAdapter<AdapterMulti.CustomViewHolder> {


    //
    private Words word;
    private DatabaseHelper dbh;
    private int size;
    private List<Words> words;
    //

    private LayoutInflater mLayoutInflater;
    private List<Integer> mDataModel;    //some data that supposed to be represented by Views

    public AdapterMulti(Context context, MultiChoiceMode multiChoiceMode, DatabaseHelper dbh) {
        super(context,multiChoiceMode);
        this.mLayoutInflater=LayoutInflater.from(context);
        this.dbh = dbh;
    }


    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parentGroup, int viewType) {

        View v = mLayoutInflater.inflate(R.layout.text_item, parentGroup, false);

        return new CustomViewHolder(v);
    }

    public class CustomViewHolder extends SelectableAdapter.SelectableViewHolder {

        private TextView mTextView1;
        private TextView mTextView2;

        public CustomViewHolder(View itemView) {
            super(itemView);
            this.mTextView1=(TextView)(itemView.findViewById(R.id.itemText1));
            this.mTextView2=(TextView)(itemView.findViewById(R.id.itemText2));
        }

        /* Helpful method */
        private Drawable getAppropriateDrawable(int position) {
            return isSelected(position) ? mSelectedItemBackground : mDefaultItemBackground;
        }


        /*Set background here */
        @Override
        public void setBackground(int position) {
            if (Build.VERSION.SDK_INT >= 16)
                itemView.setBackground(getAppropriateDrawable(position));
            else
                itemView.setBackgroundDrawable(getAppropriateDrawable(position));
        }

        @Override
        public void onBindData(int position) {
            //here goes code that initializes Views with it's data representation
            words = dbh.getAllWords();
            word = words.get(position);


            //setBackground(position);
            mTextView1.setText(word.getWord());
            mTextView2.setText(word.getTranslate());
        }

    }

    @Override
    public void onUpdateAfterDeletion(int[] mCheckedIndices) {
        for(int index:mCheckedIndices) {
            words.remove(index);
            notifyItemRemoved(index);
        }
    }

    @Override
    public void onUpdateAll() {
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return dbh.getWordsCountByOwner();
    }



}