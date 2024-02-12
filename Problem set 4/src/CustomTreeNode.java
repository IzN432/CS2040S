public class CustomTreeNode {
    public int numChildren;
    public int value;
    public boolean leaf;
    public String name;
    public CustomTreeNode[] children;
    @Override
    public String toString() {
        return name;
    }
}
