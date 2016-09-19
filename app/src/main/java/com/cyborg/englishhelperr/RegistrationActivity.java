package com.cyborg.englishhelperr;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.BackendlessCallback;
import com.backendless.exceptions.BackendlessFault;
import com.cyborg.englishhelper.R;

public class RegistrationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        Backendless.initApp(this, BackendlessSettings.APP_ID, BackendlessSettings.APP_KEY, BackendlessSettings.APP_VERSION);

        Button registerButton = (Button) findViewById( R.id.registerButton );

        View.OnClickListener registerButtonClickListener = createRegisterButtonClickListener();

        registerButton.setOnClickListener( registerButtonClickListener );
    }

    public View.OnClickListener createRegisterButtonClickListener()
    {
        return new View.OnClickListener()
        {
            @Override
            public void onClick( View v )
            {
                EditText nameField = (EditText) findViewById( R.id.nameField );
                EditText emailField = (EditText) findViewById( R.id.emailField );
                EditText passwordField = (EditText) findViewById( R.id.passwordField );

                CharSequence name = nameField.getText();
                CharSequence email = emailField.getText();
                CharSequence password = passwordField.getText();

                if( isRegistrationValuesValid( name, email, password ) )
                {
                    registerUser( name.toString(), email.toString(), password.toString() );
                }
            }
        };
    }

    public boolean isRegistrationValuesValid( CharSequence name, CharSequence email, CharSequence password)
    {
        return Validator.isNameValid( this, name )
                && Validator.isEmailValid( this, email )
                && Validator.isPasswordValid( this, password );
    }

    public void registerUser( String name, String email, String password )
    {
        BackendlessUser user = new BackendlessUser();
        user.setEmail( email );
        user.setPassword( password );
        user.setProperty( "name", name );

        Backendless.UserService.register( user, new BackendlessCallback<BackendlessUser>()
        {
            @Override
            public void handleResponse( BackendlessUser backendlessUser )
            {
                Log.i( "Registration", backendlessUser.getEmail() + " successfully registered" );
                Toast toast = Toast.makeText(RegistrationActivity.this,
                        "Пользователь зарегестрирован!!!", Toast.LENGTH_SHORT);
                toast.show();
                startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
            }

            @Override
            public void handleFault( BackendlessFault fault )
            {
                Log.i("Registration", "FAULT!");
            }
        } );
    }
}
