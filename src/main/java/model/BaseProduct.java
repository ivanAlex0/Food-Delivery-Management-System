package model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class BaseProduct extends MenuItem {

    @JsonProperty("Title")
    public String title;
    @JsonProperty("Rating")
    public Float rating;
    @JsonProperty("Calories")
    public Float calories;
    @JsonProperty("Protein")
    public Float protein;
    @JsonProperty("Fat")
    public Float fat;
    @JsonProperty("Sodium")
    public Float sodium;
    @JsonProperty("Price")
    public Integer price;

    @Override
    public int computePrice() {
        return price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseProduct that = (BaseProduct) o;
        return title.equals(that.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title);
    }

    @Override
    public String toString() {
        return  title + '\'' +
                ", rating=" + rating +
                ", calories=" + calories +
                ", protein=" + protein +
                ", fat=" + fat +
                ", sodium=" + sodium +
                ", price=" + price;
    }

    public void setAll(Float rating, Float calories, Float protein, Float fat, Float sodium, Integer price) {
        setRating(rating);
        setCalories(calories);
        setProtein(protein);
        setFat(fat);
        setSodium(sodium);
        setPrice(price);
    }
}
