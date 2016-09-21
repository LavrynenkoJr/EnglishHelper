package com.cyborg.englishhelperr;


import android.content.Context;
import android.content.SharedPreferences;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.BackendlessDataQuery;
import com.backendless.persistence.QueryOptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class Synchoniz {

    private long lastDateServ;
    private SharedPreferences mSettings;
    public static final String APP_PREFERENCES = "mysettings";
    public static final String SAVED_UPDATE_TIME = "saved_update_time";
    private BackendlessCollection<Words> wordsCllection;
    private List<Words> listWords = new ArrayList<>();
    private long updateTime;
    private DatabaseHelper databaseHelper;
    private String idCurrUser;
    private boolean firstResponse = true;


    public void downloadWordsServer(Context context) {

        mSettings = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

        updateTime = mSettings.getLong(SAVED_UPDATE_TIME, 0);

        if (updateTime == 0) {
            System.out.println("Происходит загрузка всех слов с сервера!!!!!!!!!!");

            String owner = "'" + LoginActivity.objectId + "'";
            String whereClause = "ownerId = " + owner;

            BackendlessDataQuery dataQuery = new BackendlessDataQuery();
            dataQuery.setWhereClause( whereClause );
            //dataQuery.setQueryOptions(queryOptions);

            requestToServWithParams(dataQuery, context);
        } else {
            synchronizFromServ(context);
        }

        //////////////////////////////////////////////////////////////////////
       /* mSettings = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

        updateTime = mSettings.getLong(SAVED_UPDATE_TIME, 0);

        if (updateTime == 0) {
            System.out.println("Происходит загрузка всех слов с сервера!!!!!!!!!!");


            QueryOptions queryOptions = new QueryOptions();
            queryOptions.setRelated(Arrays.asList("Word"));
            queryOptions.setPageSize(50);
            queryOptions.setOffset(0);

            idCurrUser = LoginActivity.objectId;
            databaseHelper = new DatabaseHelper(context, "englishdatabase.db", null, 1);

            BackendlessDataQuery query = new BackendlessDataQuery();
            query.setQueryOptions(queryOptions);

            SharedPreferences.Editor editor = mSettings.edit();
            editor.putLong(SAVED_UPDATE_TIME, System.currentTimeMillis());
            editor.apply();

            requestToServWithParams(query, context);
        } else {
            synchronizFromServ(context);
        }*/
    }

    public void synchronizFromServ(final Context context){

        mSettings = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        Backendless.Persistence.of( "WORDS" ).findLast(new AsyncCallback<Map>(){
            @Override
            public void handleResponse( Map contact )
            {
                Map<String, String> map = contact;
                for (Map.Entry e : map.entrySet()) {
                    if (e.getKey().equals("created")) {
                        Date date = (Date) e.getValue();
                        lastDateServ = date.getTime();
                        if(lastDateServ < mSettings.getLong(SAVED_UPDATE_TIME, 0)) {
                            System.out.println("Не нужно скачивать с сервера !!!!!!!!!!!!!!!!!!!!!");

                        }else{
                            System.out.println("Нужно скаачать Новые слова с сервера !!!!!!!!!!!!!!!!!!");
                            String whereClause = "created after " + mSettings.getLong(SAVED_UPDATE_TIME, 0);

                            BackendlessDataQuery dataQuery = new BackendlessDataQuery();
                            dataQuery.setWhereClause( whereClause );

                            requestToServWithParams(dataQuery, context);
                        }
                    }
                }
            }
            @Override
            public void handleFault( BackendlessFault fault )
            {

            }
        });

    }

    public void requestToServWithParams(BackendlessDataQuery dataQuery, final Context context){

        Backendless.Data.of( Words.class ).find( dataQuery, new LoadingCallback<BackendlessCollection<Words>>(context)
        {
            @Override
            public void handleResponse( BackendlessCollection<Words> wordsBackendlessCollection )
            {
                setUpdateTime();

                int size = wordsBackendlessCollection.getCurrentPage().size();

                /* При возврате с сервера коллекции размером больше 0 )
                    Создается лист в который записываються данные с полученной коллекции (данные приходят страницами по 10 записей)
                    после чего лист передается в метод saveToDb который сохраняет данные в базу данных
                    при наличии на слеедующей странице записей действия повторяются
                 */

                if (size > 0){
                    List<Words> list = new ArrayList<>();
                    wordsBackendlessCollection.nextPage(this);
                    listWords.addAll( wordsBackendlessCollection.getCurrentPage() );
                    list.addAll(wordsBackendlessCollection.getCurrentPage());
                    saveToDb(list, context);
                }
                //super.handleResponse( wordsBackendlessCollection );
            }
        } );
    }

    public void setUpdateTime(){

        SharedPreferences.Editor editor = mSettings.edit();
        editor.putLong(SAVED_UPDATE_TIME, System.currentTimeMillis());
        editor.apply();

    }

    public void saveToDb(List<Words> listWords, Context context){

        for(int i = 0; i < listWords.size(); i++) {

            databaseHelper = new DatabaseHelper(context, "englishdatabase.db", null, 1);

            //BackendlessUser user = Backendless.UserService.CurrentUser();
            Words wd = new Words();
            if (listWords.get(i).getOwnerId().equals(LoginActivity.objectId)) {
                wd.setWord(listWords.get(i).getWord());
                wd.setTranslate(listWords.get(i).getTranslate());
                wd.setOwnerId(listWords.get(i).getOwnerId());
                wd.setCreated(listWords.get(i).getCreated());

                //DatabaseHelper dbh = new DatabaseHelper(getActivity(), "englishhelper.db", null, 1);
                databaseHelper.addWord(wd);

            }
            else
                System.out.println("слово не этого юзера" + listWords.get(i).getOwnerId() + "id User = " + idCurrUser);

            //System.out.println("слово не этого юзера");
        }
        //loadToAdapter();
    }

    public void deleteUpdateTime(Context context){

        mSettings = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putLong(SAVED_UPDATE_TIME, 0);
        editor.apply();

    }
}
