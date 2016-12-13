package com.andrewfisher.myrestaurants.util;

import com.andrewfisher.myrestaurants.model.Restaurant;

import java.util.ArrayList;

/**
 * Created by Guest on 12/12/16.
 */
public interface OnRestaurantSelectedListener {
    public void onRestaurantsSelected(Integer position, ArrayList<Restaurant> restaurants);
}
