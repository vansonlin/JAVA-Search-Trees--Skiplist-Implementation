/**
 * Created by vanson on 3/27/15.
 * Bonus part
 */
public class SplayTree<T extends Comparable<T>> implements TreeLin {
    STNode root = null;
    int nNode = 0;

    STNode p, g, gParent, a, b, c, d, y, z;

    public SplayTree() {
//        testConstructExample();
////        STNode node = findNode(6);
//        printTree();
//
//        zigzigLeft(findNode(1.));
//        System.out.println("\nAfter splay on node 3:\n");
//        printTree();
////
    }

    @Override
    public <T extends Comparable<T>> boolean insert(T val) {
        STNode newNode = new STNode(val);
        if (this.root == null) {
            this.root = newNode;
            nNode++;
            return true;
        }

        y = findNode(val);

        if (y != null && y.key.compareTo(val) == 0) { // node exist already, no insertion was performed
            splay(y);
            return false;
        }

        splay(y);
        if (this.root.key.compareTo(newNode.key) < 0) { // if current root is smaller than newNode
            newNode.left = this.root;
            newNode.right = this.root.right;
            if (this.root.right != null)
                this.root.right.p = newNode;
            this.root.right = null;
        }else{
            newNode.left = this.root.left;
            newNode.right = this.root;
            if (this.root.left != null)
                this.root.left.p = newNode;
            this.root.left = null;
        }
        this.root.p = newNode;
        this.root = newNode;
        nNode++;
        return true;
    }

    @Override
    public <T extends Comparable<T>> boolean delete(T val) {
        z = findNode(val);
        if (z == null || z.key.compareTo(val) != 0) { // node was not found, no deletion was performed
            return false;
        }

        // node was found, start deleting
        if (z.left == null)
            transplant(z, z.right);
        else if (z.right == null)
            transplant(z, z.left);
        else {
            y = findMin(z.right);
            if (y.p != z){
                transplant(y,y.right);
                y.right = z.right;
                y.right.p = y;
            }
            transplant(z, y);
            y.left = z.left;
            y.left.p = y;
        }
        nNode--;
        return true;
    }

    @Override
    public <T extends Comparable<T>> T search(T val) {
        if (this.root == null) return null; // empty tree

        p = findNode(val);
        if (p.key.compareTo(val) == 0) {
            splay(p);
            return (T) p.key;
        }
        return null;
    }

    @Override
    public void print() {
        print(this.root);
    }
    private void print(STNode node){
        if (node == null) return;
        print(node.left);
        System.out.println(node.key);
        print(node.right);
    }

    @Override
    public void printTree(){
        System.out.println("\n== Print SplayTree in Tree format (rotated 90 degree counterclockwise) ==\n");
        printTree(this.root, 0);
    }
    /**
     * credited to http://stackoverflow.com/questions/4965335/how-to-print-binary-tree-diagram
     */
    private void printTree(STNode root, int level){
        if(root == null)
            return;
        printTree(root.right, level + 1);
        if(level != 0){
            for(int i=0;i<level-1;i++)
                System.out.print("|\t");
            System.out.println("|-------"+root.key);
        }
        else
            System.out.println(root.key);
        printTree(root.left, level + 1);
    }

    @Override
    public int getHeight(){
        return getHeight(this.root)+1;
    }
    private int getHeight(STNode node){
        if (node == null) return -1;
        return Math.max(getHeight(node.left), getHeight(node.right)) + 1;
    }

    @Override
    public int getnNode() {
        return this.nNode;
    }

    private void splay(STNode x){
        while (x != root){
            if (x.p == root) { // single rotation to be performed
                if (x == root.left)
                    zigLeft(x);
                else
                    zigRight(x);
            } else {
                STNode p = x.p;
                boolean pIsLeftChild = (p == p.p.left);
                boolean xIsLeftChild = (x == p.left);

                if (xIsLeftChild && pIsLeftChild){ // x is left child of a left child,
                    // perform zig-zig left on x
                    zigzigLeft(x);
                }else if (!xIsLeftChild && !pIsLeftChild){ // x is right child of a right child
                    // perform zig-zig right on x
                    zigzigRight(x);
                }else if (xIsLeftChild && !pIsLeftChild){ // x is left child of a right child
                    // perform zig-zag right on x
                    zigzagRight(x);
                }else{ // x is right child of a left child
                    // perform zig-zag left on x
                    zigzagLeft(x);
                }
            }
        }
    }

    /**
     * only use when x is a LEFT child of root.
           p                x
          / \              / \
         x   c    --->    a   p
        / \                  / \
       a   b                b   c
     * @param
     */
    private void zigLeft(STNode x){
        p = x.p;
        a = x.left;
        b = x.right;
        c = p.right;

        x.left = a;
        x.right = p;
        p.p = x;
        p.left = b;
        p.right = c;
        if (a != null) a.p = x;
        if (b != null) b.p = p;
        if (c != null) c.p = p;

        x.p = null;
        this.root = x;
    }

    /**
     * only use when x is a RIGHT child of root.
           p                x
          / \              / \
         a   x     --->   p   c
            / \          / \
           b   c        a   b
     */
    private void zigRight(STNode x){
        p = x.p;

        a = p.left;
        b = x.left;
        c = x.right;

        x.left = p;
        p.p = x;
        x.right = c;
        p.left = a;
        p.right = b;
        if (a != null) a.p = p;
        if (b != null) b.p = p;
        if (c != null) c.p = x;

        x.p = null;
        this.root = x;
    }

    /**
     * only use when x is left child of a left child
              g                x
             / \              / \
            p   d            a   p
           / \                  / \
          x   c      --->      b    g
         / \                       / \
        a   b                     c   d
     */
    private void zigzigLeft(STNode x){
        p = x.p;
        g = p.p;
        gParent = g.p;

        a = x.left;
        b = x.right;
        c = p.right;
        d = g.right;

        x.left = a;
        x.right = p;

        p.p = x;
        p.left = b;
        p.right = g;

        g.p = p;
        g.left = c;
        g.right = d;

        if (a != null) a.p = x;
        if (b != null) b.p = p;
        if (c != null) c.p = g;
        if (d != null) d.p = g;

        x.p = gParent;
        if (gParent != null) {
            if (gParent.left == g)
                gParent.left = x;
            else
                gParent.right = x;
        }
        if (g == this.root) this.root = x;
    }

    /**
     * only use when x is right child of a right child
            g                    x
           / \                  / \
          a   p                p   d
             / \     --->     / \
            b   x            g   c
               / \          / \
              c   d        a   b
     */
    private void zigzigRight(STNode x){
        p = x.p;
        g = p.p;
        gParent = g.p;

        a = g.left;
        b = p.left;
        c = x.left;
        d = x.right;

        x.left = p;
        x.right = d;

        p.p = x;
        p.left = g;
        p.right = c;

        g.p = p;
        g.left = a;
        g.right = b;

        if (a != null) a.p = g;
        if (b != null) b.p = g;
        if (c != null) c.p = p;
        if (d != null) d.p = x;

        x.p = gParent;
        if (gParent != null) {
            if (gParent.left == g)
                gParent.left = x;
            else
                gParent.right = x;
        }
        if (g == this.root) this.root = x;
    }

    /**
     * only use when x is right child of a left child
     *
            g                   x
           / \                /   \
          p   d              p     g
         / \        --->    / \   / \
        a   x              a   b c   d
           / \
          b   c
     */
    private void zigzagLeft(STNode x){
        p = x.p;
        g = p.p;
        gParent = g.p;

        a = p.left;
        b = x.left;
        c = x.right;
        d = g.right;

        x.left = p;
        x.right = g;
        p.p = x;
        p.left = a;
        p.right = b;

        g.p = x;
        g.left = c;
        g.right = d;

        if (a != null) a.p = p;
        if (b != null) b.p = p;
        if (c != null) c.p = g;
        if (d != null) d.p = g;

        x.p = gParent;
        if (gParent != null) {
            if (gParent.left == g)
                gParent.left = x;
            else
                gParent.right = x;
        }
        if (g == this.root) this.root = x;
    }

    /**
     * only use when x is left child of a right child
           g              x
          / \            / \
         a   p    --->  g   y
            / \        / \ / \
           x   d      a  b c  d
          / \
         b   c
     */
    private void zigzagRight(STNode x){
        p = x.p;
        g = p.p;
        gParent = g.p;

        a = g.left;
        b = x.left;
        c = x.right;
        d = p.right;

        x.left = g;
        x.right = p;

        p.p = x;
        p.left = c;
        p.right = d;

        g.p = x;
        g.left = a;
        g.right = b;

        if (a != null) a.p = g;
        if (b != null) b.p = g;
        if (c != null) c.p = p;
        if (d != null) d.p = p;

        x.p = gParent;
        if (gParent != null) {
            if (gParent.left == g)
                gParent.left = x;
            else
                gParent.right = x;
        }
        if (g == this.root) this.root = x;
    }

    private void transplant(STNode u, STNode v){
        if (u.p == null)
            this.root = v;
        else if (u == u.p.left)
            u.p.left = v;
        else u.p.right = v;
        if (v != null)
            v.p = u.p;
    }

    private STNode findMin(STNode x){
        while (x.left != null)
            x = x.left;
        return x;
    }

    /**
     *
     * @param val
     * @param <T>
     * @return requested node or the node immediately left or right to the requested node.
     */
    private <T extends Comparable<T>> STNode<T> findNode(T val) {
        STNode p = null;
        STNode current = this.root;
        while (current != null){
            if (val.compareTo((T) current.key) == 0) // node found.
                break;
            else if (val.compareTo((T) current.key) < 0) {
                p = current;
                current = current.left;
            } else {
                p = current;
                current = current.right;
            }
        }
        if (current == null) return p; // if not found, return parent
        return current; // if found, return the requested node
    }

    private void testConstructExample() {
        STNode n1 = new STNode(1.);
        STNode n2 = new STNode(2.);
        STNode n3 = new STNode(3.);
        STNode n4 = new STNode(4.);
        STNode n5 = new STNode(5.);
        STNode n6 = new STNode(6.);
        STNode n7 = new STNode(7.);
        STNode n8 = new STNode(8.);
        STNode n9 = new STNode(9.);
        STNode n10 = new STNode(10.);
        STNode n11 = new STNode(11.);
        STNode n12 = new STNode(12.);
        STNode n13 = new STNode(13.);

        n1.p = n2;

        n2.left = n1;
        n2.right = n4;
        n2.p = n6;

        n3.p = n4;

        n4.left = n3;
        n4.right = n5;
        n4.p = n2;

        n5.p = n4;

        n6.left = n2;
        n6.right = n7;
        n6.p = n8;

        n7.p = n6;

        n8.left = n6;
        n8.right = n9;
        n8.p = n10;

        n9.p = n8;

        n10.left = n8;
        n10.right = n11;

        n11.p = n10;
        n11.right = n12;

        n12.p = n11;
        n12.right = n13;

        n13.p = n12;

        this.root = n10;
    }

    class STNode<T extends Comparable<T>> implements Comparable<STNode<T>>{
        T key;
        STNode left = null;
        STNode right = null;
        STNode p = null;

        STNode(T key){
            this.key = key;
        }

        @Override
        public int compareTo(STNode<T> o) {
            return key.compareTo(o.key);
        }
    }

}
