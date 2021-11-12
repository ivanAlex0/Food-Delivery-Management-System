package model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public abstract class MenuItem {

    public String title;

    public abstract int computePrice();
    public abstract Float getRating();
    public abstract Float getCalories();
    public abstract Float getFat();
    public abstract Float getSodium();
    public abstract Float getProtein();
}
