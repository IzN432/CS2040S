public class Test {
    public static void main(String[] args) {
        args = new String[] {
                "2",
                "10000",
                "harry_potter.txt"
        };

        WordTextGenerator.main(args);

    }
}
interface F<T> {
    T run(T t);
}

interface G<S, T> {
    T run(S s, T t);
}


class A {
    int foo(int x) {
        return x + 1;
    }

    int bar(String s, int i) {
        return s.length() + i;
    }

    public static void main(String[] args) {

    }

    void foo() {
        int x = 0;

        F<Integer> f = y -> x;

    }
}
