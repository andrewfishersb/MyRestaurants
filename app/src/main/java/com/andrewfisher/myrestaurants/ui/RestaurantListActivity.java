package com.andrewfisher.myrestaurants.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.andrewfisher.myrestaurants.Constants;
import com.andrewfisher.myrestaurants.R;
import com.andrewfisher.myrestaurants.adapter.RestaurantListAdapter;
import com.andrewfisher.myrestaurants.model.Restaurant;
import com.andrewfisher.myrestaurants.services.YelpService;
import com.andrewfisher.myrestaurants.util.OnRestaurantSelectedListener;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Guest on 12/12/16.
 */

//slowly being split into fragments so all commented out code has been moved
public class RestaurantListActivity extends AppCompatActivity implements OnRestaurantSelectedListener {
    private Integer mPosition;
    ArrayList<Restaurant> mRestaurants;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurants);

    }

    @Override
    public void onRestaurantsSelected(Integer position, ArrayList<Restaurant> restaurants) {
        mPosition = position;
        mRestaurants = restaurants;
    }





}
