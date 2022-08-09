package com.cst2335.finalproject;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, BottomNavigationView.OnNavigationItemSelectedListener{
    Fragment welcomeScreenFragment;
    Fragment searchFragment;
    Fragment cocktailCategoriesFragment;
    Fragment favoriteCocktailsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        welcomeScreenFragment = new HomeFragment();
        searchFragment = new SearchFragment();
        cocktailCategoriesFragment = new CocktailCategoriesFragment();
      favoriteCocktailsFragment = new FavoriteCocktailListFragment();

        loadFragment(welcomeScreenFragment);

//This gets the toolbar from the layout:
        Toolbar tBar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(tBar);
        //calls onCreateOptionsMenu()


        //For NavigationDrawer:
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawer, tBar, R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        //For bottomNavigationBar


    }
    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.example_menu, menu);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String message = null;
        //Look at your menu XML file. Put a case for every id in that file:
        switch(item.getItemId())
        {
            //what to do when the menu item is selected:
            case R.id.About:
                message = "You clicked item 1";
                new AlertDialog.Builder(this)
                        .setTitle("Really Exit?")
                        .setMessage("Search by name: insert a name to search!\nSearch by 1st character: pick a character from the list\nProgram by Minh Hoang Tran")
                        .setNegativeButton("Ok", null).create().show();
                break;
            case R.id.home:
                message = "Back to home page";
                loadFragment(welcomeScreenFragment);
                break;
            case R.id.to_search_by_name:
                message = "You clicked on to search by name";
                loadFragment(searchFragment);
                break;
            case R.id.favorite:
                message = "You clicked on to favorite list";
                loadFragment(favoriteCocktailsFragment);
                break;
            case R.id.to_serch_by_char:
                message = "You clicked on to search by first character";
                loadFragment(cocktailCategoriesFragment);
                break;
        }
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        return true;
    }


    // Needed for the OnNavigationItemSelected interface:
    @Override
    public boolean onNavigationItemSelected( MenuItem item) {

        String message = null;

        switch(item.getItemId())
        {
            case R.id.About:
                message = "You clicked item 1";
                new AlertDialog.Builder(this)
                        .setTitle("About!")
                        .setMessage("Search by name: insert a name to search!\nSearch by 1st character: pick a character from the list\nProgram by Minh Hoang Tran")
                        .setNegativeButton("Ok", null).create().show();
                break;
            case R.id.home:
                message = "Back to home page";
                loadFragment(welcomeScreenFragment);
                break;
            case R.id.to_search_by_name:
                message = "You clicked on to search by name";
                loadFragment(searchFragment);
                break;
            case R.id.favorite:
                message = "You clicked on to favorite list";
                loadFragment(favoriteCocktailsFragment);
                break;
            case R.id.to_serch_by_char:
                message = "You clicked on to search by first character";
                loadFragment(cocktailCategoriesFragment);
                break;
        }

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);

        Toast.makeText(this, "NavigationDrawer: " + message, Toast.LENGTH_LONG).show();
        return false;
    }
}


