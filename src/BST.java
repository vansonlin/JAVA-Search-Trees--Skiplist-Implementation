/**
 * Created by vanson on 3/20/15.
 */
public class BST<T extends Comparable<T>> implements TreeLin {
    BNode root = null;
    int nNode = 0;

    public BST(){
    }

    /**
     * return false for insertion, true for search
     */
    @Override
    public <T extends Comparable<T>> boolean insert(T val){ // insert of search
        BNode newNode = new BNode(val);
        if (this.root == null) { // empty tree, insert as root
            this.root = newNode;
            nNode++;
            return true;
        }

        // find location
        BNode p = findNode(val);
        if (p.key.compareTo((T) val) == 0) return false; // node was found existing, no insertion performed.

        newNode.p = p;
        if (newNode.compareTo(p) < 0){ // smaller
            p.left = newNode;
        }else{
            p.right = newNode;
        }
        nNode++;
        return true;
    }

    @Override
    public <T extends Comparable<T>> boolean delete(T val) {
        BNode z = findNode(val);
        if (z == null || z.key.compareTo(val) != 0) { // node was not found, no deletion was performed
            return false;
        }

        // node was found, start deleting
        if (z.left == null)
            transplant(z, z.right);
        else if (z.right == null)
            transplant(z, z.left);
        else {
            BNode y = findMin(z.right);
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
        BNode p = findNode(val);
        if (p.key.compareTo(val) == 0) return (T) p.key; // if found
        return null;
    }

    @Override
    public void print(){
        print(this.root);
    }
    private void print(BNode node){
        if (node == null)
            return;
        print(node.left);
        System.out.println(node.key);
        print(node.right);
    }

    @Override
    public void printTree(){
        System.out.println("\n== Print BST in Tree format (rotated 90 degree counterclockwise) ==\n");
        printTree(this.root, 0);
    }
    /**
     * credited to http://stackoverflow.com/questions/4965335/how-to-print-binary-tree-diagram
     */
    private void printTree(BNode root, int level){
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
        return getHeight(this.root) + 1;
    }
    private int getHeight(BNode node){
        if (node == null) return - 1;
        return Math.max(getHeight(node.left), getHeight(node.right)) + 1;
    }

    @Override
    public int getnNode() {
        return this.nNode;
    }

    private  <T extends Comparable<T>> BNode findNode(T val){
        BNode p = null;
        BNode current = this.root;
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

    private  <T extends Comparable<T>> BNode findMin(BNode x){
        while (x.left != null)
            x = x.left;
        return x;
    }

    private void transplant(BNode u, BNode v){
        if (u.p == null)
            this.root = v;
        else if (u == u.p.left)
            u.p.left = v;
        else u.p.right = v;
        if (v != null)
            v.p = u.p;
    }

    class BNode<T extends Comparable<T>> implements Comparable<BNode<T>>{
        T key = null;
        BNode left = null;
        BNode right = null;
        BNode p = null;

        BNode(T key){
            this.key = key;
        }

        public int compareTo(BNode<T> node){
            if (node.key == null) return 1;
            return this.key.compareTo(node.key);
        }

    }
}


