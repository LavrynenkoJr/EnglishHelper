package com.cyborg.englishhelperr;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.app.Fragment;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.ToolbarWidgetWrapper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.cyborg.englishhelper.R;

import org.w3c.dom.Text;

import java.util.Random;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, FloatBut {

    final Random random = new Random();
    private int i = random.nextInt(4);
    private DatabaseHelper databaseHelper;

    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        databaseHelper = new DatabaseHelper(this, "englishdatabase.db", null, 1);

        Synchoniz synchroniz = new Synchoniz();
        synchroniz.downloadWordsServer(this);                         // При первом запуске скачивает всю бд.. при след. если необходимо докачивает новые слова

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.hide();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    public void onBackPressed() {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Fragment fragment = null;
        int id = item.getItemId();

        if (id == R.id.my_dictionary) {
            fragment = new MyDictionaryFragment();
            trans(fragment);
        } else if (id == R.id.test) {
            fragment = new TestMenuFragment();
            trans(fragment);
        } else if (id == R.id.logout) {
            System.out.println("NAZATO!!!!!!");

            Backendless.UserService.logout( new AsyncCallback<Void>()
            {
                public void handleResponse( Void response )
                {
                    logout();
                }

                public void handleFault( BackendlessFault fault )
                {
                    System.out.println("FAULT LOGOUT!");
                    Toast toast = Toast.makeText(MainActivity.this,
                            "Для смены пользователя необходимо подключение к интеренету ", Toast.LENGTH_SHORT);
                    toast.show();
                    // something went wrong and logout failed, to get the error code call fault.getCode()
                }
            });
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void trans(Fragment fragment){
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.content_main, fragment);
        ft.addToBackStack(null);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
    }

    @Override
    public void showBut() {
        fab.show();
    }

    @Override
    public void hideBut() {
        fab.hide();
    }

    @Override
    public void clickBut(Fragment fragment) {
        trans(fragment);
    }

    public void logout(){
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        Synchoniz synchoniz = new Synchoniz();
        synchoniz.deleteUpdateTime(this);
        finish();
    }
}
