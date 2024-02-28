import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

public class Trie {

    // Wildcards
    final char WILDCARD = '.';


    private static class TrieNode {
        // TODO: Create your TrieNode class here.
        private static int getIndex(char c) {
            if ('0' <= c && c <= '9') {
                return c - '0';
            } else if ('A' <= c && c <= 'Z') {
                return c - 'A' + 10;
            } else if ('a' <= c && c <= 'z') {
                return c - 'a' + 10 + 26;
            }
            return -1;
        }
        private static char getChar(int i) {
            if (0 <= i && i <= 9) {
                return (char) (i + '0');
            } else if (10 <= i && i <= 35) {
                return (char) (i - 10 + 'A');
            } else if (36 <= i && i <= 61) {
                return (char) (i - 36 + 'a');
            }
            return '\\';
        }
        TrieNode[] children = new TrieNode[62];
        boolean end = false;

        public TrieNode get(char c) {
            return children[getIndex(c)];
        }

        public void set(char c, TrieNode n) {
            children[getIndex(c)] = n;
        }
    }

    private final TrieNode root;
    public Trie() {
        // TODO: Initialise a trie class here.
        root = new TrieNode();
    }

    /**
     * Inserts string s into the Trie.
     *
     * @param s string to insert into the Trie
     */
    void insert(String s) {
        TrieNode curr = root;
        for (int i = 0; i < s.length(); i++) {
            if (curr.get(s.charAt(i)) == null) {
                curr.set(s.charAt(i), new TrieNode());
            }
            curr = curr.get(s.charAt(i));
        }
        curr.end = true;
    }

    /**
     * Checks whether string s exists inside the Trie or not.
     *
     * @param s string to check for
     * @return whether string s is inside the Trie
     */
    boolean contains(String s) {
        TrieNode curr = root;
        for (int i = 0; i < s.length(); i++) {
            curr = curr.get(s.charAt(i));
            if (curr == null) return false;
        }
        return curr.end;
    }

    /**
     * Searches for strings with prefix matching the specified pattern sorted by lexicographical order. This inserts the
     * results into the specified ArrayList. Only returns at most the first limit results.
     *
     * @param s       pattern to match prefixes with
     * @param results array to add the results into
     * @param limit   max number of strings to add into results
     */
    void prefixSearch(String s, ArrayList<String> results, int limit) {
        prefixSearchHelper(new StringBuilder(), s, 0, results, root, limit);
    }

    private int prefixSearchHelper(StringBuilder prefix, String string, int index,
                                   ArrayList<String> res, TrieNode n, int limit) {
        if (limit == 0) return 0;
        if (index >= string.length()) {
            if (n.end) {
                res.add(prefix.toString());
                limit--;
            }

            for (int i = 0; i < 62; i++) {
                if (n.children[i] == null) continue;
                StringBuilder s = new StringBuilder(prefix);
                s.append(TrieNode.getChar(i));
                limit = prefixSearchHelper(s, string, index + 1, res,
                        n.children[i], limit);
            }

            return limit;
        }
        char currentChar = string.charAt(index);
        if (currentChar == WILDCARD) {
            for (int i = 0; i < 62; i++) {
                if (n.children[i] == null) continue;
                StringBuilder s = new StringBuilder(prefix);
                s.append(TrieNode.getChar(i));
                limit = prefixSearchHelper(s, string, index + 1, res,
                        n.children[i], limit);
            }
        } else {
            if (n.get(currentChar) == null) {
                return limit;
            }
            StringBuilder s = new StringBuilder(prefix);
            s.append(currentChar);
            limit = prefixSearchHelper(s, string, index + 1, res,
                    n.get(currentChar), limit);
        }
        return limit;
    }



    // Simplifies function call by initializing an empty array to store the results.
    // PLEASE DO NOT CHANGE the implementation for this function as it will be used
    // to run the test cases.
    String[] prefixSearch(String s, int limit) {
        ArrayList<String> results = new ArrayList<String>();
        prefixSearch(s, results, limit);
        return results.toArray(new String[0]);
    }


    public static void main(String[] args) {
        Trie t = new Trie();
        t.insert("peter");
        t.insert("piper");
        t.insert("picked");
        t.insert("a");
        t.insert("peck");
        t.insert("of");
        t.insert("pickled");
        t.insert("peppers");
        t.insert("pepppito");
        t.insert("pepi");
        t.insert("pik");

        int i1 = TrieNode.getIndex('p');
        char c = TrieNode.getChar(i1);
        String[] result1 = t.prefixSearch("pe", 10);
        String[] result2 = t.prefixSearch("pe.", 10);

        // result1 should be:
        // ["peck", "pepi", "peppers", "pepppito", "peter"]
        // result2 should contain the same elements with result1 but may be ordered arbitrarily
    }
}
