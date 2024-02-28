<<<<<<< HEAD
public class Main {
    public static void main(String[] args) {
        String s = "";
        Object o = s;
        o = 1;
    }
}

class B1<T, S, R> {

}
class B2<T> extends B1<T> {}
class B3 extends B2 {}
class Box<T> {
    private T x;
    private Box(T x) {
        this.x = x;
    }
    public static <T> Box<T> of(T t) {
        if (t == null) {
            return null;
        }
        return new Box<>(t);
=======
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
>>>>>>> 24e6b4405548406ebc8dc404078e3e6711218745
    }
}
