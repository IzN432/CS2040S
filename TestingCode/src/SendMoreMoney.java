import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeSet;

public class SendMoreMoney {

    private String a;
    private String b;
    private String c;
    public HashMap<Character, Integer> solve(String a, String b, String c) {
        this.a = a;
        this.b = b;
        this.c = c;

        HashSet<Character> characterSet = new HashSet<>();
        for (char character : a.toCharArray()) {
            characterSet.add(character);
        }
        for (char character : b.toCharArray()) {
            characterSet.add(character);
        }
        for (char character : c.toCharArray()) {
            characterSet.add(character);
        }
        if (characterSet.size() > 10) {
            return null;
        }
        Character[] characters = new Character[characterSet.size()];
        characterSet.toArray(characters);
        HashMap<Character, Integer> solution = new HashMap<>();

        if (helper(0, characters, solution)) {
            return solution;
        } else {
            return null;
        }
    }

    private boolean helper(int characterIdx, Character[] characters, HashMap<Character, Integer> solution) {
        if (characterIdx == characters.length) {
            // check validity
            Collection<Integer> digits = solution.values();
            if (digits.size() != new TreeSet<>(digits).size()) {
                return false;
            }
            String first = a;
            String second =  b;
            String third = c;

            if (solution.get(first.charAt(0)) == 0 || solution.get(second.charAt(0)) == 0
                    || solution.get(third.charAt(0)) == 0) {
                return false;
            }
            for (Character c : characters) {
                first = first.replace(c, (char) (48 + solution.get(c)));
                second = second.replace(c, (char) (48 + solution.get(c)));
                third = third.replace(c, (char) (48 + solution.get(c)));
            }

            return Integer.parseInt(first) + Integer.parseInt(second) == Integer.parseInt(third);
        }

        for (int i = 0; i < 10; i++) {
            solution.put(characters[characterIdx], i);
            if (helper(characterIdx + 1, characters, solution)) {
                return true;
            }
        }

        return false;
    }

    public static void main(String[] args) {
        SendMoreMoney smm = new SendMoreMoney();
        HashMap<Character, Integer> hm = smm.solve("SEND", "MORE", "MONEY");
        System.out.println(hm);
    }

}
