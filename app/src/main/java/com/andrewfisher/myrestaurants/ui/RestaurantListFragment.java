package com.andrewfisher.myrestaurants.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

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


public class RestaurantListFragment extends Fragment {
    @Bind(R.id.recyclerView)
    RecyclerView mRecyclerView;
    private RestaurantListAdapter mAdapter;
    public ArrayList<Restaurant> mRestaurants = new ArrayList<>();
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;
    private String mRecentAddress;
    private OnRestaurantSelectedListener mOnRestaurantSelectedListener;


    public RestaurantListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savredInstanceState){
        super.onCreate(savredInstanceState);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mEditor = mSharedPreferences.edit();
        // Instructs fragment to include menu options:
        setHasOptionsMenu(true);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=  inflater.inflate(R.layout.fragment_restaurant_list, container, false);
        ButterKnife.bind(this,view);
        mRecentAddress=mSharedPreferences.getString(Constants.PREFERENCES_LOCATION_KEY,null);

        if (mRecentAddress != null) {
            getRestaurants(mRecentAddress);
        }

        return view;
    }


    private void getRestaurants(String location) {
        final YelpService yelpService = new YelpService();

        yelpService.findRestaurants(location, new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) {
                mRestaurants = yelpService.processResults(response);

                getActivity().runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

                        // Line above states 'getActivity()' instead of previous 'RestaurantListActivity.this'
                        // because fragments do not have own context, and must inherit from corresponding activity.

                        mAdapter = new RestaurantListAdapter(getActivity(), mRestaurants,mOnRestaurantSelectedListener);
                        // Line above states `getActivity()` instead of previous
                        // 'getApplicationContext()' because fragments do not have own context,
                        // must instead inherit it from corresponding activity.

                        mRecyclerView.setAdapter(mAdapter);
                        RecyclerView.LayoutManager layoutManager =
                                new LinearLayoutManager(getActivity());
                        mRecyclerView.setLayoutManager(layoutManager);
                        mRecyclerView.setHasFixedSize(true);
                    }
                });
            }
        });
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        try{
            mOnRestaurantSelectedListener = (OnRestaurantSelectedListener) context;
        }catch(ClassCastException e){
            throw new ClassCastException(context.toString()+ e.getMessage());
        }
    }

    @Override
    // Method is now void, menu inflater is now passed in as argument:
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu,inflater);
        inflater.inflate(R.menu.menu_search, menu);


        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                addToSharedPreferences(query);
                getRestaurants(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }

        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private void addToSharedPreferences(String location) {
        mEditor.putString(Constants.PREFERENCES_LOCATION_KEY, location).apply();
    }
}