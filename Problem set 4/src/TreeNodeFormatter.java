public class TreeNodeFormatter {
    private final char[] template;

    public TreeNodeFormatter(char[] template) {
        this.template = template;
    }

    public String format(CustomTreeNode node) {
        StringBuilder result = new StringBuilder(" ");
        char[] chars = node.name.toCharArray();

        for (char character : template) {
            if (character >= 'A' && character <= 'Z') {
                result.append(chars[((int) character) - 65]);
            } else {
                result.append(character);
            }
            result.append(" ");
        }

        return result.toString();
    }

    public static final char[] TIC_TAC_TOE = new char[] {
            ' ', ' ', ' ', '|', ' ', ' ', ' ', '|', ' ', ' ', ' ', '\n',
            ' ', 'A', ' ', '|', ' ', 'B', ' ', '|', ' ', 'C', ' ', '\n',
            ' ', ' ', ' ', '|', ' ', ' ', ' ', '|', ' ', ' ', ' ', '\n',
            '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '\n',
            ' ', ' ', ' ', '|', ' ', ' ', ' ', '|', ' ', ' ', ' ', '\n',
            ' ', 'D', ' ', '|', ' ', 'E', ' ', '|', ' ', 'F', ' ', '\n',
            ' ', ' ', ' ', '|', ' ', ' ', ' ', '|', ' ', ' ', ' ', '\n',
            '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '\n',
            ' ', ' ', ' ', '|', ' ', ' ', ' ', '|', ' ', ' ', ' ', '\n',
            ' ', 'G', ' ', '|', ' ', 'H', ' ', '|', ' ', 'I', ' ', '\n',
            ' ', ' ', ' ', '|', ' ', ' ', ' ', '|', ' ', ' ', ' '
    };
}
