/**
 * Created by vanson on 3/27/15.
 */
public interface TreeLin {

    public <T extends Comparable<T>> boolean insert(T val);

    public <T extends Comparable<T>> T search(T val);

    public <T extends Comparable<T>> boolean delete(T val);

    public void print();

    public void printTree();

    public int getHeight();

    public int getnNode();
}
