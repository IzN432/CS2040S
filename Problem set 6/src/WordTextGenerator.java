import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.util.*;

/**
 * This class is used to generated text using a Markov Model
 */
public class WordTextGenerator {

    // For testing, we will choose different seeds
    private static long seed = 1;

    // Sets the random number generator seed
    public static void setSeed(long s) {
        seed = s;
    }

    /**
     * Reads in the file and builds the WordMarkovModel.
     *
     * @param order the order of the Word Markov Model
     * @param fileName the name of the file to read
     * @param model the Word Markov Model to build
     * @return the first {@code order} words of the file to be used as the seed text
     */
    public static Deque<String> buildModel(int order, String fileName, WordMarkovModel model) {

        String[] text;

        // Loop through the text
        try {
            // Create a BufferedReader
            BufferedReader reader = new BufferedReader(new FileReader(fileName));

            // Create a scanner
            Scanner sc = new Scanner(reader);

            // List to hold all the words
            List<String> words = new ArrayList<>();

            // Read in the file, one character at a time.
            while (sc.hasNext()) {
                words.add(sc.next());
            }

            text = new String[words.size()];
            words.toArray(text);

            // Make sure that length of input text is longer than requested Markov order
            if (text.length < order) {
                System.out.println("Text is shorter than specified Markov Order.");
                return null;
            }
        } catch (Exception e) {
            System.out.println("Problem reading file " + fileName + ".");
            return null;
        }

        // Build Markov Model of order from text
        model.initializeText(text);

        Deque<String> queue = new LinkedList<>();
        for (int i = 0; i < order; i++) {
            queue.addLast(text[i]);
        }
        return queue;
    }

    /**
     * generateText outputs to stdout text of the specified length based on the specified seedText
     * using the given Markov Model.
     *
     * @param model the Markov Model to use
     * @param seedText the initial kgram used to generate text
     * @param order the order of the Markov Model
     * @param length the length of the text to generate
     */
    public static void generateText(WordMarkovModel model, Deque<String> seedText, int order, int length) {
        // Generate length characters
        String stringToAppend;

        // Keep track of the number of words so that we have a limit to stop at
        int outLength = seedText.size();

        // Build the output string
        StringBuilder outputString = new StringBuilder();
        int size = seedText.size();
        for (int i = 0; i < size; i++) {
            String temp = seedText.removeFirst();
            outputString.append(temp).append(" ");
            seedText.addLast(temp);
        }
        int wordsToBreak = 30;
        while (outLength < length) {
            // Get the next character from kgram sequence. The kgram sequence to use
            // is the sequence starting from ith position.
            stringToAppend = model.nextCharacter(seedText);

            // If there is no next character, restart generation with initial kgram value which
            // Starts from 0th position.
            if (!Objects.equals(stringToAppend, WordMarkovModel.NO_WORD)) {
                seedText.addLast(stringToAppend);
                seedText.removeFirst();
                outputString.append(stringToAppend).append(" ");
                if (wordsToBreak <= 0) {
                    outputString.append("\n");
                    wordsToBreak = 30;
                }
                outLength++;
                wordsToBreak --;
            } else {
                // This prefix has never appeared in the text.
                // Give up?
                System.out.println("PREFIX NOT FOUND, GIVE UP");
                System.out.println(outputString);
                return;
            }
        }

        // Output the generated string including the initial seed text.
        System.out.println(outputString.substring(0, outputString.length() - 1));
    }

    /**
     * The main routine.  Takes 3 arguments:
     * args[0]: the order of the Markov Model
     * args[1]: the length of the text to generate
     * args[2]: the filename for the input text
     */
    public static void main(String[] args) {

        // Check that we have three parameters
        if (args.length != 3) {
            System.out.println("Number of input parameters are wrong.");
        }

        // Get the input:
        int order = Integer.parseInt(args[0]);
        int length = Integer.parseInt(args[1]);
        String fileName = args[2];

        // Create the model
        WordMarkovModel markovModel = new WordMarkovModel(order, seed);
        Deque<String> seedText = buildModel(order, fileName, markovModel);

        // Generate text
        if (seedText == null) {
            System.out.println("Something went wrong");
        } else {
            generateText(markovModel, seedText, order, length);
        }
    }
}