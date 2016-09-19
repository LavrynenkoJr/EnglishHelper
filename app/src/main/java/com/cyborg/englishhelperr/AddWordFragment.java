package com.cyborg.englishhelperr;

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cyborg.englishhelper.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class AddWordFragment extends Fragment {

    private EditText enterWord;
    private EditText enterTrans;
    private Button find;
    private FloatBut floatBut;
    private FloatingActionButton fab;

    private final String URL = "https://translate.yandex.net/";
    private final String KEY = "trnsl.1.1.20160817T200109Z.c38983c8343bed9e.dd4756f0452fa1d4301a5094fb59a7e784d087a5";

    private Gson gson;
    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add_word,
                container, false);

        enterTrans = (EditText) rootView.findViewById(R.id.editTranslate);
        enterWord = (EditText) rootView.findViewById(R.id.editWord);
        find = (Button) rootView.findViewById(R.id.findTr);

        gson = new GsonBuilder().create();
        retrofit = new Retrofit.Builder().baseUrl(URL).addConverterFactory(GsonConverterFactory.create(gson)).build();
        retrofitInterface = retrofit.create(RetrofitInterface.class);

        find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(enterWord != null) {

                    Map<String, String> mapJson = new HashMap<String, String>();
                    mapJson.put("key", KEY);
                    mapJson.put("text", enterWord.getText().toString());
                    mapJson.put("lang", "en-ru");

                    Call<Object> call = retrofitInterface.translate(mapJson);

                    call.enqueue(new Callback<Object>() {

                        @Override
                        public void onResponse(Call<Object> call, Response<Object> response) {
                            Map<String, String> map = gson.fromJson(response.body().toString(), Map.class);
                            for (Map.Entry e : map.entrySet()) {
                                if (e.getKey().equals("text")) {
                                    String s = e.getValue().toString();
                                    s = s.substring(1, s.length() - 1);  // delete first and last simbol
                                    enterTrans.setText(s);
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<Object> call, Throwable t) {
                            t.printStackTrace();
                        }
                    });
                }
            }
        });

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            floatBut = (FloatBut) activity;
            fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        }catch (ClassCastException e){
            e.printStackTrace();
        }

        if (floatBut != null && fab != null){
            floatBut.showBut();
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(enterWord != null && enterTrans != null){
                        Words wd = new Words();
                        wd.setWord(enterWord.getText().toString());
                        wd.setTranslate(enterTrans.getText().toString());
                        wd.setOwnerId(LoginActivity.objectId);
                        wd.setCreated(new Date());
                        DatabaseHelper dbh = new DatabaseHelper(getActivity(), "englishdatabase.db", null, 1);
                        dbh.addWord(wd);
                    }

                    Toast toast = Toast.makeText(getActivity(),
                            "Добавление слова", Toast.LENGTH_SHORT);
                    toast.show();
                }
            });
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        floatBut.hideBut();
    }
}
