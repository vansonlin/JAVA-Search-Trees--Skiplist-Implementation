import java.util.ArrayList;
import java.util.List;

/**
 * Created by vanson on 3/27/15.
 */
public class RBTree<T extends Comparable<T>> implements TreeLin {
    RBNode nil = new RBNode(null);
    RBNode root = nil; // initialize as nil
    int nNode = 0;

    private List<T> keys; // for testing, records all the keys in sorted order

    public RBTree(){}

    @Override
    public <T extends Comparable<T>> boolean insert(T val) {
        RBNode z = new RBNode(val); // new Node, z in textbook p 315
        RBNode y = this.nil;
        RBNode x = this.root;
        while (x != this.nil){
            y = x;
            if (z.compareTo(x) == 0){ // found equal node, insertion was not performed
                return false;
            }else if (z.compareTo(x) < 0){
                x = x.left;
            }else{
                x = x.right;
            }
        }
        z.p = y;
        if (y == this.nil){
            this.root = z;
        }else if (z.compareTo(y) < 0){
            y.left = z;
        }else{
            y.right = z;
        }
        z.left = this.nil;
        z.right = this.nil;
        z.color = true;
        insertFixup(z);
        nNode++;
        return true; // insertion was performed
    }

    @Override
    public <T extends Comparable<T>> boolean delete(T val) {
        RBNode z = findNode(val); // find node;
        if (z == nil || z.key.compareTo(val)!= 0) return false; // node not found, no deletion.

        RBNode y = z;
        RBNode x;
        Boolean color = y.color;
        if (z.left == this.nil){
            x = z.right;
            transplant(z, z.right);
        }else if (z.right == this.nil){
            x = z.left;
            transplant(z, z.left);
        }else{
            y = treeMin(z.right);
            color = y.color;
            x = y.right;
            if (y.p == z){
                x.p = y;
            }else{
                transplant(y,y.right);
                y.right = z.right;
                y.right.p = y;
            }
            transplant(z,y);
            y.left = z.left;
            y.left.p = y;
            y.color = z.color;
        }
        if (!color){ // false is black
            deleteFixup(x);
        }
        nNode--;
        return true;
    }

    @Override
    public <T extends Comparable<T>> T search(T val) {
        if (this.root == null) return null; // empty tree
        RBNode p = findNode(val);
        if (p == nil || p.key.compareTo(val) != 0) return null; // no such node was found
        return (T) p.key;
    }

    @Override
    public void print() {
        print(this.root);
    }
    private void print(RBNode node){
        if (node == nil) return;
        print(node.left);
        System.out.println(node.key);
        print(node.right);
    }

    @Override
    public void printTree(){
        System.out.println("\n\n== Print RB-Tree in Tree format (rotated 90 degree counterclockwise) ==\n");
        printTree(this.root, 0);
    }
    /**
     * credited to http://stackoverflow.com/questions/4965335/how-to-print-binary-tree-diagram
     */
    private void printTree(RBNode root, int level){
        if(root == nil)
            return;
        printTree(root.right, level + 1);
        if(level != 0){
            for(int i = 0; i < level - 1; i++)
                System.out.print("|\t");
            System.out.println("|-------"+root.key);
        }
        else
            System.out.println(root.key);
        printTree(root.left, level + 1);
    }

    @Override
    public int getHeight(){
        return getHeight(this.root) +1;
    }
    private int getHeight(RBNode node){
        if (node == nil) return - 1;
        return Math.max(getHeight(node.left), getHeight(node.right)) + 1;
    }

    @Override
    public int getnNode() {
        return this.nNode;
    }

    private void insertFixup(RBNode z){
        // page 316
        RBNode y;
        while (z.p.color){ // true for red
            if (z.p == z.p.p.left){
                y = z.p.p.right;
                if (y.color){ // true for red, case 1: z's uncle y is red
                    z.p.color = false;
                    y.color = false;
                    z.p.p.color = true;
                    z = z.p.p;
                }else{
                    if (z == z.p.right){ // case 2: z's uncle y is black, and z is a right child of p
                        z = z.p;
                        leftRotate(z);
                    }
                    // case 3: z's uncle y is black, and z is a left child of p
                    z.p.color = false;
                    z.p.p.color = true;
                    rightRotate(z.p.p);
                }
            }else{
                y = z.p.p.left;
                if (y.color){ // true for red, case 4: z's uncle y is red
                    z.p.color = false;
                    y.color = false;
                    z.p.p.color = true;
                    z = z.p.p;
                }else{
                    if (z == z.p.left){ // case 5: z's uncle y is black, and z is a left child of p
                        z = z.p;
                        rightRotate(z);
                    }
                     // case 6: z's uncle y is black, and z is a right child of p
                    z.p.color = false;
                    z.p.p.color = true;
                    leftRotate(z.p.p);
                }
            }
        }
        this.root.color = false;
    }

    private <T extends Comparable<T>> RBNode findNode(T val){
        RBNode p = null;
        RBNode current = this.root;
        while (current != this.nil && !current.key.equals(val)){
            p = current;
            if (val.compareTo((T) current.key) < 0){
                current = current.left;
            }else{
                current = current.right;
            }
        }
        if (current == nil) return p;
        return current;
    }

    private void transplant(RBNode u, RBNode v){
        if (u.p == this.nil)
            this.root = v;
        else if (u == u.p.left)
            u.p.left = v;
        else
            u.p.right = v;
        v.p = u.p;
    }

    private void deleteFixup(RBNode x){
        RBNode w;
        while (x != this.root && !x.color){
            if (x == x.p.left){
                w = x.p.right;
                if (w.color){ // case 1
                    w.color = false;
                    x.p.color = true;
                    leftRotate(x.p);
                    w = x.p.right;
                }
                if (!w.left.color && !w.right.color){ // case 2
                    w.color = true;
                    x = x.p;
                } else{
                    if (!w.right.color){ // case 3
                        w.left.color = false;
                        w.color = true;
                        rightRotate(w);
                        w = x.p.right;
                    }
                    w.color = x.p.color; // case 4
                    x.p.color = false;
                    w.right.color = false;
                    leftRotate(x.p);
                    x = this.root;
                }
            }else{
                w = x.p.left;
                if (w.color){
                    w.color = false;
                    x.p.color = true;
                    rightRotate(x.p);
                    w = x.p.left;
                }
                if (!w.right.color && !w.left.color){
                    w.color = true;
                    x = x.p;
                }
                else{
                    if (!w.left.color){
                        w.right.color = false;
                        w.color = true;
                        leftRotate(w);
                        w = x.p.left;
                    }
                    w.color = x.p.color;
                    x.p.color = false;
                    w.left.color = false;
                    rightRotate(x.p);
                    x = this.root;
                }
            }
        }
        x.color = false;
    }

    private RBNode treeMin(RBNode node){
        while (node.left != nil)
            node = node.left;
        return node;
    }

    private void leftRotate(RBNode x){
        RBNode y = x.right;
        x.right = y.left;
        if (y.left != nil){
            y.left.p = x;
        }
        y.p = x.p;
        if (x.p == nil){
            this.root = y;
        }else if (x == x.p.left){
            x.p.left = y;
        }else{
            x.p.right = y;
        }
        y.left = x;
        x.p = y;
    }

    private void rightRotate(RBNode x){
        RBNode y = x.left;
        x.left = y.right;
        if (y.right != nil){
            y.right.p = x;
        }
        y.p = x.p;
        if (x.p == nil){
            this.root = y;
        }else if (x == x.p.right){
            x.p.right = y;
        }else{
            x.p.left = y;
        }
        y.right = x;
        x.p = y;
    }

    private void testRotation(){
        // testing code: left and right rotation, see page 313
        root = new RBNode("root");
        root.p = nil;

        RBNode Y = new RBNode("Y");
        root.left = Y;
        Y.p = root;

        RBNode X = new RBNode("X");
        Y.left = X;
        X.p = Y;

        RBNode r = new RBNode("r");
        Y.right = r;
        r.p = Y;

        RBNode a = new RBNode("a");
        X.left = a;
        a.p = X;

        RBNode b = new RBNode("b");
        X.right = b;
        b.p = X;

        printNode(root);
        printNode(Y);
        printNode(X);
        printNode(r);
        printNode(a);
        printNode(b);

        System.out.println("\nAfter Right-Rotation.");
        rightRotate(Y);

        printNode(root);
        printNode(Y);
        printNode(X);
        printNode(r);
        printNode(a);
        printNode(b);

        System.out.println("\nAfter Left-Rotation.");
        leftRotate(X);

        printNode(root);
        printNode(Y);
        printNode(X);
        printNode(r);
        printNode(a);
        printNode(b);
    }

    private void printNode(RBNode node){
        if (node == nil) return;

        System.out.println("\ncurrent node is: " + node.key);
//        if (node.p != nil)
        System.out.println("parent node is: " + node.p.key);

//        if (node.left != null && node.left != nil)
        System.out.println("left node is: " + node.left.key);

//        if (node.right != null && node.right != nil)
        System.out.println("right node is: " + node.right.key);
    }

    private void count(){
        keys = new ArrayList<T>();
        count(this.root);
    }
    private void count(RBNode node){
        if (node == nil) return;
        count(node.left);
        keys.add((T) node.key);
        count(node.right);
    }

    private void testConstruct(){
        // page 329
        RBNode A = new RBNode("A");
        RBNode B = new RBNode("B");
        RBNode C = new RBNode("C");
        RBNode D = new RBNode("D");
        RBNode E = new RBNode("E");

        A.color = false;
        A.p = B;
        A.left = nil;
        A.right = nil;

        this.root = B;
        B.color = false;
        B.p = nil;
        B.left = A;
        B.right = D;

        C.color = false;
        C.p = D;
        C.left = nil;
        C.right = nil;

        D.color = true;
        D.p = B;
        D.left = C;
        D.right = E;

        E.color = false;
        E.p = D;
        E.left = nil;
        E.right = nil;

        printTree();

        System.out.println("deleteFixup:");

        deleteFixup(A);
        printTree();
    }

    protected class RBNode<T extends Comparable<T>> implements Comparable<RBNode<T>>{
        boolean color = false; // true for red, false for black
        RBNode left = nil;
        RBNode right = nil;
        RBNode p = nil;
        T key = null;

        RBNode(T key){
            this.key = key;
        }

        @Override
        public int compareTo(RBNode<T> node) {
            return this.key.compareTo(node.key);
        }
    }
}


