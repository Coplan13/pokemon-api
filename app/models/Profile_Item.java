package models;

public class Profile_Item {

    public Integer stock;
    public String item_id;
    public String profile_id;

    public Profile_Item() {

    }

    public Profile_Item(Integer stock, String item_id, String profile_id) {
        this.stock = stock;
        this.item_id = item_id;
        this.profile_id = profile_id;
    }
}
