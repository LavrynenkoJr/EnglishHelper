package com.cyborg.englishhelperr;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.cyborg.englishhelper.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MyDictionaryFragment extends Fragment {

    private long lastDateServ;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private LinearLayoutManager mLayoutManager;
    private FloatBut floatBut;
    private FloatingActionButton fab;
    private DatabaseHelper databaseHelper;
    private String idCurrUser;

    private SharedPreferences mSettings;
    public static final String APP_PREFERENCES = "mysettings";
    public static final String SAVED_UPDATE_TIME = "saved_update_time";
    private long updateTime;

    private BackendlessCollection<Words> wordsCllection;
    private List<Words> listWords = new ArrayList<>();
    private boolean isLoadingItems = false;
    private Gson gson;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSettings = getActivity().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

        //System.out.println("TIMEEEEEEEEEEE = " + mSettings.getLong(SAVED_UPDATE_TIME, 0));

        databaseHelper = new DatabaseHelper(getActivity(), "englishdatabase.db", null, 1);
        //databaseHelper.findAfterDate(0);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_my_dictionary,
                container, false);

        gson = new GsonBuilder().create();

        idCurrUser = LoginActivity.objectId;

        recyclerView = (RecyclerView) rootView.findViewById(R.id.my_recycler_view);
        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);

        //loadToAdapter();

        if (isOnline())
        {
            loadToAdapter();
            synchronizToServ();

            //loadToAdapter();
            //downloadWordsServer();

        }else{
            loadToAdapter();

        }

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
                    Fragment fragment = new AddWordFragment();
                    floatBut.clickBut(fragment);
                }
            });
        }
    }

    public void saveToServer(List<Words> listToServ) {

        for(int i = 0; i < listToServ.size(); i++) {

            Words words = listToServ.get(i);

            HashMap word = new HashMap();
            word.put("word", words.getWord());
            word.put("translate", words.getTranslate());
            word.put("ownerId", words.getOwnerId());
            word.put("created", words.getCreated());

            Backendless.Persistence.of("Words").save(word, new AsyncCallback<Map>() {
                public void handleResponse(Map response) {
                    System.out.println("good, words upload to server");
                    setUpdateTime();                                            //Обновление времени апдейта..
                }

                public void handleFault(BackendlessFault fault) {
                    System.out.println("fuck, words fail");// an error has occurred, the error code can be retrieved with fault.getCode()
                }
            });
        }

    }

    public boolean isOnline(){
        ConnectivityManager cm = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        //NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        //boolean isConnected = activeNetwork.isConnectedOrConnecting();
        if (cm.getActiveNetworkInfo() == null) {
            return false;
        } else {
            return true;
        }
    }

    public void loadToAdapter(){

        if (databaseHelper.getWordsCountByOwner() != 0){
            adapter = new MyAdapter(databaseHelper);
            recyclerView.setAdapter(adapter);
        }else{
            Toast toast = Toast.makeText(getActivity(),
                    "Ваша БД пуста! адд ворд", Toast.LENGTH_SHORT);
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
    }


    public void synchronizToServ(){

        Words word = databaseHelper.getLastWord();

        if(word.getCreated().getTime() < mSettings.getLong(SAVED_UPDATE_TIME, 0)){
            System.out.println("Не нужно загружать на сервера !!!!!!!!!!!!!!!!!!!!!");
        }else{
            System.out.println("Нужно загружать на сервера !!!!!!!!!!!!!!!!!!!!!");
            List<Words> lastWords = databaseHelper.findAfterDate(mSettings.getLong(SAVED_UPDATE_TIME, 0));
            saveToServer(lastWords);
        }

    }

    public void setUpdateTime(){

        updateTime = System.currentTimeMillis();
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putLong(SAVED_UPDATE_TIME, updateTime);
        editor.apply();

    }
}

