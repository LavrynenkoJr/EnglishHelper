package com.cyborg.englishhelperr;

import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import android.widget.Toast;

import com.cyborg.englishhelper.R;

import java.util.List;
import java.util.Random;


public class TestFragment extends Fragment {

    final Random random = new Random();
    private int i;
    private int a,b,c,d;

    public static Words[] wordsarr;

    private FloatBut floatBut;
    private TextView viewForWord;
    private DatabaseHelper dbh;
    private Words word;
    private RecyclerView testRecyclerView;
    private RecyclerAdapter variantsAdapter;
    private LinearLayoutManager mLayoutManager;
    private Toast toast;
    private List<Words> words;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_test,
                container, false);

        viewForWord = (TextView) rootView.findViewById(R.id.view_for_word);
        testRecyclerView = (RecyclerView) rootView.findViewById(R.id.test_recycler_view);
        mLayoutManager = new LinearLayoutManager(getActivity());
        testRecyclerView.setLayoutManager(mLayoutManager);

        try {
            floatBut = (FloatBut) getActivity();
        }catch (ClassCastException e){
            e.printStackTrace();
        }
        if (floatBut != null){
            floatBut.hideBut();
        }

        dbh = new DatabaseHelper(getActivity(), "englishdatabase.db", null, 1);
        if (dbh.getWordsCountByOwner() > 4) {
            words = dbh.getAllWords();
            work();
        } else {
            Toast toast = Toast.makeText(getActivity(),
                    "Недостаточно слов для работы. Пожалуйста добавьте новые слова. ", Toast.LENGTH_LONG);
            toast.show();
            try {
                floatBut = (FloatBut) getActivity();
            }catch (ClassCastException e){
                e.printStackTrace();
            }
            if (floatBut != null){
                Fragment fragment = new AddWordFragment();
                floatBut.clickBut(fragment);
            }
        }
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void work(){
        i = random.nextInt(4);

        a = random.nextInt(words.size());

        do {
            b = random.nextInt(words.size());
            c = random.nextInt(words.size());
            d = random.nextInt(words.size());
        }while (a==b || a==c || a== d || b==c || b==d || c==d);

        wordsarr = new Words[]{
                words.get(a),
                words.get(b),
                words.get(c),
                words.get(d)
        };

        word = wordsarr[i];

        variantsAdapter = new RecyclerAdapter(wordsarr, new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Words w = (Words) view.getTag();
                if (w.getTranslate().equals(word.getTranslate())){
                    toast = Toast.makeText(getActivity(),
                            "Правильно!", Toast.LENGTH_SHORT);
                    toast.show();
                    work();
                }
            }
        });

        variantsAdapter.setWords(wordsarr);

        testRecyclerView.setAdapter(variantsAdapter);

        viewForWord.setText(word.getWord());

    }
}

