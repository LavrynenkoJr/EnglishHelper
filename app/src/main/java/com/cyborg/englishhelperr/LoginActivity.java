package com.cyborg.englishhelperr;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.persistence.local.UserTokenStorageFactory;
import com.cyborg.englishhelper.R;

public class LoginActivity extends AppCompatActivity {

    private SharedPreferences mLoginSetting;

    public static final String SAVED_OBJECT_ID = "saved_object_id";
    public static final String APP_PREFERENCES = "mLoginSetting";
    private Intent intent;
    private String userToken;
    public static String objectId;                            // Изменить логику!!!!!!!!!!!!! статические поля нужно убрать!!!!!!

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mLoginSetting = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

        Backendless.initApp(this, BackendlessSettings.APP_ID, BackendlessSettings.APP_KEY, BackendlessSettings.APP_VERSION);

        Button loginButton = (Button) findViewById( R.id.loginButton );
        loginButton.setOnClickListener( buttonListener() );

        userToken = UserTokenStorageFactory.instance().getStorage().get();

        objectId = mLoginSetting.getString(SAVED_OBJECT_ID, null);

        if(userToken != null && !userToken.equals("")){
            try {
                startMainActivity();

            }catch (Exception exception){
                System.out.println("ТОКЕН ПОХОДУ УСТАРЕЛ))");

            }
        }

        //if(true)
        //{

        // call Login Activity
        // }


        makeRegistrationLink();
    }

    public void makeRegistrationLink()
    {
        SpannableString registrationPrompt = new SpannableString( getString( R.string.register_prompt ) );

        //intent = new Intent(this, RegistrationActivity.class);

        ClickableSpan clickableSpan = new ClickableSpan()
        {
            @Override
            public void onClick( View widget )
            {

                startRgistrationActivity();
            }
        };

        String linkText = getString( R.string.register_link );
        int linkStartIndex = registrationPrompt.toString().indexOf( linkText );
        int linkEndIndex = linkStartIndex + linkText.length();
        registrationPrompt.setSpan( clickableSpan, linkStartIndex, linkEndIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE );

        TextView registerPromptView = (TextView) findViewById( R.id.registerPromptText );
        registerPromptView.setText( registrationPrompt );
        registerPromptView.setMovementMethod( LinkMovementMethod.getInstance() );
    }

    private void loginUser(String email, String password, AsyncCallback<BackendlessUser> loginCallback)
    {
        Backendless.UserService.login( email, password, loginCallback, true );
    }

    public View.OnClickListener buttonListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText editEmail = (EditText) findViewById(R.id.editEmail);
                EditText editPassword = (EditText) findViewById(R.id.editPassword);

                CharSequence email = editEmail.getText();
                CharSequence password = editPassword.getText();


                if( isLoginValuesValid( email, password ) ) {

                    LoadingCallback<BackendlessUser> loginCallback = createLoginCallback();
                    loginCallback.showLoading();

                    loginUser( email.toString(), password.toString(), loginCallback );

                }
            }
        };
    }

    public boolean isLoginValuesValid( CharSequence email, CharSequence password )
    {
        return Validator.isEmailValid( this, email ) && Validator.isPasswordValid( this, password );
    }

    public void startRgistrationActivity(){
        intent = new Intent(LoginActivity.this, RegistrationActivity.class);
        startActivity(intent);
        // finish();
    }

    public LoadingCallback<BackendlessUser> createLoginCallback()
    {
        return new LoadingCallback<BackendlessUser>( this, getString( R.string.loading_login ) )
        {
            @Override
            public void handleResponse( BackendlessUser loggedInUser )
            {
                super.handleResponse( loggedInUser );

                objectId = loggedInUser.getObjectId();
                SharedPreferences.Editor editor = mLoginSetting.edit();
                editor.putString(SAVED_OBJECT_ID, objectId);
                editor.apply();

                //objectId = loggedInUser.getObjectId();



                startMainActivity();
            }
        };
    }
    public void startMainActivity(){
        intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        //finish();
    }
}
