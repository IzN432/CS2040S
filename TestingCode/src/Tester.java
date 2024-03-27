import java.util.Random;

public class Tester {

    public interface Solution<T, S> {

        T solve(S s);

    }

    public interface Verifier<T, S> {
        boolean verify(T t, S s);
    }

    public interface Generate<S> {

        S generate(Random rng);
    }

    public interface Display<T> {
        String display(T t);
    }

    private static long seed = 102L;
    private static final Random rng = new Random(seed);

    public static <T, S> boolean test(Solution<T, S> solution, Verifier<T, S> verifier, Generate<S> generator,
                                      Display<T> displaySol, Display<S> displayIn, int iterations) {
        boolean flag = true;
        for (int i = 0; i < iterations; i++) {
            S testCase = generator.generate(rng);
            T testSolution = solution.solve(testCase);

            if (!verifier.verify(testSolution, testCase)) {
                System.out.printf("Yours: %s for input %s\n", displaySol.display(testSolution), displayIn.display(testCase));
                flag = false;
            }
        }
        return flag;
    }
}


