import java.util.*;

public class WordMarkovModel {

    // Local class to handle each value entry in the table
    private class TableEntry {
        // the frequency that each word appears after the word that is the key of this entry
        private final HashMap<String, Integer> frequencies = new HashMap<>();

        // an array of all words that appear after the word that is the key of this entry
        private List<String> words;

        // the total number of times this word has appeared
        private int totalCount = 0;

        // add a new word to all the above fields
        void add(String s) {
            frequencies.merge(s, 1, Integer::sum);
            totalCount++;
        }

        // returns a word based on the frequency that it appeared in the given text
        String getRandomWord() {
            if (words == null) {
                generateSortedList();
            }
            int choice = generator.nextInt(totalCount);
            for (String s : words) {
                choice -= frequencies.get(s);
                if (choice < 0) {
                    return s;
                }
            }
            throw new RuntimeException("Unexpected error! It appears that the summed " +
                    "frequency in the frequency table is less than the total count!");
        }

        void generateSortedList() {
            words = new ArrayList<>(frequencies.keySet());
            Collections.sort(words);
        }
    }

    // Use this to generate random numbers as needed
    private final Random generator = new Random();

    // This is a special symbol to indicate no followup word found
    public static final String NO_WORD = "[NOT FOUND]";

    // Table of all the words or phrases that precede another
    private final Map<Deque<String>, TableEntry> table = new HashMap<>();

    // The number of words to check
    private final int order;

    /**
     * Constructor for WordMarkovModel class.
     *
     * @param order the number of characters to identify for the Markov Model sequence
     * @param seed the seed used by the random number generator
     */
    public WordMarkovModel(int order, long seed) {
        // Initialize your class here
        this.order = order;

        // Initialize the random number generator
        generator.setSeed(seed);
    }

    /**
     * Builds the Markov Model based on the specified text string.
     */
    public void initializeText(String[] words) {
        // Build the Markov model here
        Deque<String> key = new LinkedList<>();
        for (int i = 0; i < words.length; i++) {
            if (i < order - 1) {
                key.addLast(words[i]);
            } else if (i != order - 1) {
                key.addLast(words[i - 1]);
                Deque<String> keyCopy = new LinkedList<>(key);
                table.computeIfAbsent(keyCopy, k -> new TableEntry());
                table.get(keyCopy).add(words[i]);
                key.removeFirst();
            }
        }
    }

    /**
     * Generates the next character from the Markov Model.
     * Return NO_STRING if the kgram is not in the table, or if there is no
     * valid character following the kgram.
     */
    public String nextCharacter(Deque<String> kgram) {
        // See the problem set description for details
        // on how to make the random selection.
        if (table.get(kgram) == null) {
            return NO_WORD;
        }
        return table.get(kgram).getRandomWord();
    }
}
