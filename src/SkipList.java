import java.util.Random;

/**
 * Created by vanson on 3/27/15.
 */
public class SkipList<T extends Comparable<T>> implements TreeLin {
    SLNode head;
    SLNode tail;

    int nNode = 0;
    int h = 0; // overall height, initially only logic level exist
    int maxLevel = 16; // 16 level can allow 2^16 nodes in the skiplist

    double p = 0.5; // the default probability
    Random r;

    public SkipList(){
        head = new SLNode(null);
        tail = new SLNode(null);
        head.right = tail;
        tail.left = head;
        r = new Random();
    }

    @Override
    public <T extends Comparable<T>> boolean insert(T val) {
        SLNode p = findNode(val);
        if (p.right.key != null && p.right.key.compareTo(val) == 0) // the node to be inserted exists already, no insertion was performed.
            return false;

        SLNode newNode = new SLNode(val);
        int height = randomLevel(); // height of the new node, a number from 1 to maxLevel (default 16)
        for (int i = this.h; i < height; i++) // pre-build level until reach the desired overall height
            addLevel();

        // p is the node right before the to-be inserted node in the bottom level
        // build bottom level first
        newNode.left = p;
        newNode.right = p.right;
        p.right.left = newNode;
        p.right = newNode;

        height--;

        for (int i = 0; i < height; i++){
            while (p.up == null) // find the left first node in the next level
                p = p.left;
            p = p.up;
            SLNode newNode1 = new SLNode(val);
            newNode1.right = p.right;
            newNode1.left = p;
            newNode1.down = newNode;

            newNode.up = newNode1;

            p.right.left = newNode1;
            p.right = newNode1;

            newNode = newNode1;
        }

        nNode++;
        return true;
    }

    @Override
    public <T extends Comparable<T>> boolean delete(T val) {
        SLNode p = findNode(val);
        if (p.right.key == null || p.right.key.compareTo(val) != 0) // node not found, no deletion was performed.
            return false;

        p = p.right;
        p.left.right = p.right;
        p.right.left = p.left;
        while (p.up != null){
            p = p.up;
            p.left.right = p.right;
            p.right.left = p.left;
        }
        if (p.left == head.down && p.right == tail.down){
            head.down.up = null;
            tail.down.up = null;
            head = head.down;
            tail = tail.down;
            h--;
        }
        while (p.down != null){
            p = p.down;
            if (p.left == head.down && p.right == tail.down){
                head.down.up = null;
                tail.down.up = null;
                head = head.down;
                tail = tail.down;
                h--;
            }
        }
        nNode--;
        return true;
    }

    @Override
    public <T extends Comparable<T>> T search(T val) {
        if (this.nNode == 0) return null; // empty lsit

        SLNode p = findNode(val);
        if (p.right.key == null)
            return null;
        return (T) p.right.key;
    }

    @Override
    public void print() {
        SLNode bottomCurrent = head;
        while (bottomCurrent.down != null)
            bottomCurrent = bottomCurrent.down;
        while (bottomCurrent.right.key != null){
            bottomCurrent = bottomCurrent.right;
            System.out.println(bottomCurrent.key);
        }
    }

    @Override
    public void printTree() {
        System.out.println("\n== Print SkipList content in List format (90 degree rotated clockwise) ==\n");

        SLNode bottomCurrent = head;
        while (bottomCurrent.down != null)
            bottomCurrent = bottomCurrent.down;

        while (bottomCurrent != null){
            SLNode towerCurrent = bottomCurrent;
            while (towerCurrent != null) {
                if (towerCurrent.key == null)
                    System.out.print("nan\t");
                else
                    System.out.print(towerCurrent.key + "\t");
                towerCurrent = towerCurrent.up;
            }
            System.out.print("\n");
            bottomCurrent = bottomCurrent.right;
        }


    }

    @Override
    public int getHeight(){
        return this.h;
    }

    @Override
    public int getnNode() {
        return this.nNode;
    }

    /**
     *
     * @return some number between 1~maxLevel(default 16)
     */
    private int randomLevel(){
        int level = 1;
        while (r.nextDouble() < p && level < maxLevel){
            level++;
        }
        return level;
    }

    /**
     *
     * @param val
     * @param <T>
     * @return the node on the bottom level with the closest but smaller than (or equal to) the request value (return.key <= val)
     */
    private <T extends Comparable<T>> SLNode findNode(T val){
        SLNode current = head;

        while (true){
            while (current.right.key != null && current.right.key.compareTo(val) < 0)
                current = current.right;
            if (current.down != null)
                current = current.down;
            else
                break;
        }
        return current;
    }

    private void addLevel(){
        SLNode newhead = new SLNode(null);
        SLNode newtail = new SLNode(null);

        newhead.down = head;
        newhead.right = newtail;

        head.up = newhead;

        newtail.down = tail;
        newtail.left = head;

        tail.up = newtail;

        this.head = newhead;
        this.tail = newtail;
        h++;
    }

    class SLNode<T extends Comparable<T>> implements Comparable<SLNode<T>>{
        T key;
        SLNode up;
        SLNode down;
        SLNode left;
        SLNode right;

        SLNode(T key){
            this.key = key;
        }

        @Override
        public int compareTo(SLNode<T> o) {
            return 0;
        }
    }

}
