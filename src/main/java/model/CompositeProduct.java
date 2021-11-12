package model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class CompositeProduct extends MenuItem {
    public ArrayList<MenuItem> products;

    public CompositeProduct() {
        products = new ArrayList<>();
    }

    @Override
    public int computePrice() {
        int price = 0;
        for (MenuItem product : products) {
            price += product.computePrice();
        }
        return price;
    }

    @Override
    public Float getRating() {
        float rating = 0;
        for (MenuItem product : products) {
            rating += product.getRating();
        }
        return rating;
    }

    @Override
    public Float getCalories() {
        float calories = 0;
        for (MenuItem product : products) {
            calories += product.getCalories();
        }
        return calories;
    }

    @Override
    public Float getFat() {
        float fat = 0;
        for (MenuItem product : products) {
            fat += product.getFat();
        }
        return fat;
    }

    @Override
    public Float getSodium() {
        float sodium = 0;
        for (MenuItem product : products) {
            sodium += product.getSodium();
        }
        return sodium;
    }

    @Override
    public Float getProtein() {
        float protein = 0;
        for (MenuItem product : products) {
            protein += product.getProtein();
        }
        return protein;
    }

    @Override
    public String toString() {
        return title;
    }
}
