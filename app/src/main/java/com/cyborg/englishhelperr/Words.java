package com.cyborg.englishhelperr;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.persistence.BackendlessDataQuery;

import java.util.Date;


/**
 * Created by Cyborg on 8/30/2016.
 */
public class Words {

    public int getId(){return id;}

    public void setId(int id) {
        this.id = id;
    }

    private int id;
    private String objectId;

    public void setCreated(Date created) {
        this.created = created;
    }

    private java.util.Date created;

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    private String ownerId;
    private java.util.Date updated;
    private String Word;
    private String Translate;
    private java.util.List<BackendlessUser> User;

    public Words(){}

    public Words(int id, String word, String translate, String ownerId, java.util.Date created){
        this.id = id;
        this.Word = word;
        this.Translate = translate;
        this.ownerId = ownerId;
        this.created = created;
    }

    public String getObjectId()
    {
        return objectId;
    }

    public java.util.Date getCreated()
    {
        return created;
    }

    public String getOwnerId()
    {
        return ownerId;
    }

    public java.util.Date getUpdated()
    {
        return updated;
    }

    public String getWord()
    {
        return Word;
    }

    public void setWord( String Word )
    {
        this.Word = Word;
    }

    public String getTranslate()
    {
        return Translate;
    }

    public void setTranslate( String Translate )
    {
        this.Translate = Translate;
    }

    public java.util.List<BackendlessUser> getUser()
    {
        return User;
    }

    public void setUser( java.util.List<BackendlessUser> User )
    {
        this.User = User;
    }


    public Words save()
    {
        return Backendless.Data.of( Words.class ).save( this );
    }

    public Future<Words> saveAsync()
    {
        if( Backendless.isAndroid() )
        {
            throw new UnsupportedOperationException( "Using this method is restricted in Android" );
        }
        else
        {
            Future<Words> future = new Future<Words>();
            Backendless.Data.of( Words.class ).save( this, future );

            return future;
        }
    }

    public void saveAsync( AsyncCallback<Words> callback )
    {
        Backendless.Data.of( Words.class ).save( this, callback );
    }

    public Long remove()
    {
        return Backendless.Data.of( Words.class ).remove( this );
    }

    public Future<Long> removeAsync()
    {
        if( Backendless.isAndroid() )
        {
            throw new UnsupportedOperationException( "Using this method is restricted in Android" );
        }
        else
        {
            Future<Long> future = new Future<Long>();
            Backendless.Data.of( Words.class ).remove( this, future );

            return future;
        }
    }

    public void removeAsync( AsyncCallback<Long> callback )
    {
        Backendless.Data.of( Words.class ).remove( this, callback );
    }

    public static Words findById( String id )
    {
        return Backendless.Data.of( Words.class ).findById( id );
    }

    public static Future<Words> findByIdAsync( String id )
    {
        if( Backendless.isAndroid() )
        {
            throw new UnsupportedOperationException( "Using this method is restricted in Android" );
        }
        else
        {
            Future<Words> future = new Future<Words>();
            Backendless.Data.of( Words.class ).findById( id, future );

            return future;
        }
    }

    public static void findByIdAsync( String id, AsyncCallback<Words> callback )
    {
        Backendless.Data.of( Words.class ).findById( id, callback );
    }

    public static Words findFirst()
    {
        return Backendless.Data.of( Words.class ).findFirst();
    }

    public static Future<Words> findFirstAsync()
    {
        if( Backendless.isAndroid() )
        {
            throw new UnsupportedOperationException( "Using this method is restricted in Android" );
        }
        else
        {
            Future<Words> future = new Future<Words>();
            Backendless.Data.of( Words.class ).findFirst( future );

            return future;
        }
    }

    public static void findFirstAsync( AsyncCallback<Words> callback )
    {
        Backendless.Data.of( Words.class ).findFirst( callback );
    }

    public static Words findLast()
    {
        return Backendless.Data.of( Words.class ).findLast();
    }

    public static Future<Words> findLastAsync()
    {
        if( Backendless.isAndroid() )
        {
            throw new UnsupportedOperationException( "Using this method is restricted in Android" );
        }
        else
        {
            Future<Words> future = new Future<Words>();
            Backendless.Data.of( Words.class ).findLast( future );

            return future;
        }
    }

    public static void findLastAsync( AsyncCallback<Words> callback )
    {
        Backendless.Data.of( Words.class ).findLast( callback );
    }

    public static BackendlessCollection<Words> find(BackendlessDataQuery query )
    {
        return Backendless.Data.of( Words.class ).find( query );
    }

    public static Future<BackendlessCollection<Words>> findAsync( BackendlessDataQuery query )
    {
        if( Backendless.isAndroid() )
        {
            throw new UnsupportedOperationException( "Using this method is restricted in Android" );
        }
        else
        {
            Future<BackendlessCollection<Words>> future = new Future<BackendlessCollection<Words>>();
            Backendless.Data.of( Words.class ).find( query, future );

            return future;
        }
    }

    public static void findAsync( BackendlessDataQuery query, AsyncCallback<BackendlessCollection<Words>> callback )
    {
        Backendless.Data.of( Words.class ).find( query, callback );
    }
}
