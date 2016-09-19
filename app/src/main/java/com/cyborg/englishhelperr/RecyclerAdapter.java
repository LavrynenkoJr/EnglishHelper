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

        //@Override
        //  public void onClick(View view) {
        //      int position = getAdapterPosition();
        //      if (position != RecyclerView.NO_POSITION) {
        //         listener.itemClicked(position);
        //         }
        //      }
    }


    public RecyclerAdapter(Words[] words, View.OnClickListener onClickListener){
        this.words = words;
        mOnClickListener = onClickListener;
        //mixArray(this.words);

    }

    /*public void mixArray(Word[] wordsArray){
        random = new Random();
        for (int i = wordsArray.length - 1; i > 0; i--)
        {
            int index = random.nextInt(i + 1);
            // Simple swap
            Word a = wordsArray[index];
            wordsArray[index] = wordsArray[i];
            wordsArray[i] = a;
        }
    }*/

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
/*
        holder.variantsView.setOnClickListener (new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //listener = (TestFragmentListener) ;
                testFragment.itemClicked(position, i);
                if (position == i) {
                    Toast toast = Toast.makeText(v.getContext(),
                            "Правильно!", Toast.LENGTH_SHORT);
                    toast.show();
                }
                //bus.post(new ItemClickedEvent(getItem(position)));
            }
        });
*/
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
