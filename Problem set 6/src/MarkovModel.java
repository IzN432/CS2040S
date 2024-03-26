import java.util.*;

/**
 * This is the main class for your Markov Model.
 *
 * Assume that the text will contain ASCII characters in the range [1,255].
 * ASCII character 0 (the NULL character) will be treated as a non-character.
 *
 * Any such NULL characters in the original text should be ignored.
 */
public class MarkovModel {

	private class TableEntry {
		private int totalCount = 0;
		private final Map<Character, Integer> frequencies = new HashMap<>();

		// List of words because the pdf said that TreeSet is not allowed ._.
		// initialized and sorted on first getRandomCharacter call to this kgram
		private List<Character> sorted;
		void add(char c) {
            frequencies.merge(c, 1, Integer::sum);
			totalCount++;
		}

		int getFrequency(char c) {
			return frequencies.getOrDefault(c, 0);
		}

		int getTotalCount() {
			return totalCount;
		}

		char getXthCharacter(int i) {
			if (sorted == null) {
				generateSortedList();
			}
			for (char character : sorted) {
				i -= frequencies.get(character);
				if (i < 0) {
					return character;
				}
			}
			throw new IllegalArgumentException("Value passed into getXthCharacter should not exceed totalCount!");
		}

		char getRandomCharacter() {
			return getXthCharacter(generator.nextInt(totalCount));
		}

		void generateSortedList() {
			sorted = new ArrayList<>(frequencies.keySet());
			Collections.sort(sorted);
		}
	}

	// Use this to generate random numbers as needed
	private final Random generator = new Random();

	// This is a special symbol to indicate no character
	public static final char NO_CHARACTER = (char) 0;

	private final Map<String, TableEntry> table = new HashMap<>();

	private final int order;
	/**
	 * Constructor for MarkovModel class.
	 *
	 * @param order the number of characters to identify for the Markov Model sequence
	 * @param seed the seed used by the random number generator
	 */
	public MarkovModel(int order, long seed) {
		// Initialize your class here
		this.order = order;

		// Initialize the random number generator
		generator.setSeed(seed);
	}

	/**
	 * Builds the Markov Model based on the specified text string.
	 */
	public void initializeText(String text) {
		// Build the Markov model here
		StringBuilder builder = new StringBuilder(text.substring(0,  order - 1));
		char[] chars = text.toCharArray();
		for (int i = order; i < chars.length; i++) {
			builder.append(chars[i - 1]);
			String key = builder.toString();
			if (table.get(key) == null) {
				table.put(key, new TableEntry());
			}
			table.get(key).add(chars[i]);
			builder.deleteCharAt(0);
		}
	}

	/**
	 * Returns the number of times the specified kgram appeared in the text.
	 */
	public int getFrequency(String kgram) {
		if (kgram.length() != order) {
			throw new IllegalArgumentException("kgram must be of length " + order);
		}

		if (table.get(kgram) == null) {
			return 0;
		}

		return table.get(kgram).getTotalCount();
	}

	/**
	 * Returns the number of times the character c appears immediately after the specified kgram.
	 */
	public int getFrequency(String kgram, char c) {
		if (kgram.length() != order) {
			throw new IllegalArgumentException("kgram must be of length " + order);
		}

		if (table.get(kgram) == null) {
			return 0;
		}

		return table.get(kgram).getFrequency(c);
	}

	/**
	 * Generates the next character from the Markov Model.
	 * Return NO_CHARACTER if the kgram is not in the table, or if there is no
	 * valid character following the kgram.
	 */
	public char nextCharacter(String kgram) {
		// See the problem set description for details
		// on how to make the random selection.
		if (table.get(kgram) == null) {
			return NO_CHARACTER;
		}
		return table.get(kgram).getRandomCharacter();
	}

	public static void main(String[] args) {
		MarkovModel model = new MarkovModel(1, 1);
		model.initializeText("abcabdabeaba");
		System.out.println(model.nextCharacter("abc"));
		System.out.println(model.nextCharacter("abc"));
		System.out.println(model.nextCharacter("abc"));
		System.out.println(model.nextCharacter("abc"));
		System.out.println(model.nextCharacter("abc"));
	}
}
