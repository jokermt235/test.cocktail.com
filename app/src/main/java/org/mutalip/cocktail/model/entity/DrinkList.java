package org.mutalip.cocktail.model.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DrinkList {
    @SerializedName("drinks")
    private List<Drink> drinks;

    public List<Drink> getDrinks() {
        return drinks;
    }

    public void setDrinks(List<Drink> drinks) {
        this.drinks = drinks;
    }
}
