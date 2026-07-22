import java.util.ArrayList;
import java.util.List;
/**
 * Creates user account that will be used in future versions
 * */
public class Account {

    private String username;
    private String phoneNumber;
    private String email;
    private ArrayList<Car> wishlist;


    public Account(String username, String phoneNumber, String email) {
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }


    public class User extends Account {
        private List<Car> wishlist;

        public User(String username, String phoneNumber, String emailAddress) {
            super(username, phoneNumber, emailAddress); // calls Account's constructor
            this.wishlist = new ArrayList<>();
        }

    }

    public boolean addToWishlist(Car car){
        if (!wishlist.contains(car)) {
            wishlist.add(car);
            return true;
        }
        return false;
    }

    public boolean removeFromWishlist(Car car){
        return wishlist.remove(car);
    }

    public List<Car> getWishlist() {
        return wishlist;
    }

    public String getUsername() {
        return username;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }
}