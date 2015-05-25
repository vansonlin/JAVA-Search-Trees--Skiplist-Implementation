import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

/**
 * Created by vanson on 3/20/15.
 */
public class lintz_Integer {
    // arguments
    String filename = "inputfile.txt"; // data file
    String dataStructure = "bst"; // default bst data structure
    Boolean height = false; // if true, print height of the tree (only for “bst”, “rbtree”, and “splaytree”)
    Boolean print = false; // if true, print the sorted element
    Boolean printTree = false; // if true, print tree

    public static void main(String[] args){
        (new lintz_Integer()).go(args);

//        //  For Student Data Type testing as in Readme described.
//            RBTree rbtree = new RBTree();
//            rbtree.insert(new Student(10, "Hasan"));
//            rbtree.insert(new Student(5, "Vanson"));
//            rbtree.insert(new Student(3, "Mary"));
//            rbtree.insert(new Student(2, "Tom"));
//            rbtree.insert(new Student(1, "John"));
//            rbtree.insert(new Student(4, "Jordan"));
//            rbtree.printTree();
//            rbtree.print();
//            System.out.println("Height = " + rbtree.getHeight());
    }

    public void go(String[] args){

        try {
            parseCommandLine(args); // argument processing, throw exception if the arguments not in correct format
            List<List<Integer>> dataList = readFile(); // read file, throw exception if the file not in correct format

            // initialize tree structure
            TreeLin tree;
            if (dataStructure.equals("bst")){ // if bst is specified
                tree = new BST();
            }else if (dataStructure.equals("rbtree")){ // if RBTree is specified
                tree = new RBTree();
            }else if (dataStructure.equals("skiplist")){ // if skiplist
                tree = new SkipList();
            }else{ // if splaytree
                tree = new SplayTree();
            }

            long t = System.currentTimeMillis(); // start time counting
            // start query processing
            for (List<Integer> s:dataList) {
                Integer request = s.get(0);
                Integer key = s.get(1);

                if (request.equals(1)){ // if 1, perform insertion or search depending on whether the key exists
                    tree.insert(key); // Insertion was performed due to that no equal node was found.
                }else{ // if 0, perform deletion
                    tree.delete(key); // if deletion was performed, since the target node was found
                }
                if (height) // -h print out current height
                    System.out.println(tree.getHeight());
            }
            t = System.currentTimeMillis() - t; // duration of execution

            if (this.printTree) // -pt
                tree.printTree();
            if (this.print) // -p
                tree.print();

            if (!height && !print && !printTree) { // no argument
                System.out.println("Total time: " + t + " milliseconds.");
                System.out.println("Data structure contains " + tree.getnNode() + " elements.");
            }

        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    /**
     *
     ‘-f’ argument is followed by the input file name;

     ‘-d’ argument is followed by the data structure name, which could be “bst”, “rbtree”, “skiplist” and “splaytree”.

     ‘-h’ argument only works for “bst”, “rbtree”, and “splaytree”.
            It prints (on the screen) the height of the tree (one in each line) after every operation.
            This argument is optional.

     ‘-p’ argument prints the content of the data structure (one in each line) in sorted order
            using the print() operation supported by the data structure.
            This is also an optional argument.

     '-q' if specified, the internal message will not be displayed.

     If run without the optional arguments, your program should output the number of elements in the data structure
        and the total time (wall clock time in milliseconds) that your program takes to execute.
        For example, for the example input file the output should be:
         Total time: XXX milliseconds.
         Data structure contains 3 elements.

     * @param args: user input parameters
     * @throws Exception
     */
    private void parseCommandLine(String[] args) throws ArgsException{
        int i = 0;
        while (i < args.length){
            if (args[i].equals("-f")){ // filename
                filename = args[++i];
                i++;
            }else if (args[i].equals("-d")){ // data structure
                i++;
                if (args[i].equals("bst") || args[i].equals("rbtree") || args[i].equals("skiplist") || args[i].equals("splaytree")){
                    dataStructure = args[i];
                }else throw new ArgsException();
                i++;
            }else if (args[i].equals("-h")){ // print height
                this.height = true;
                i++;
            }else if (args[i].equals("-p")){ // print content in sorted order
                this.print = true;
                i++;
            }else if (args[i].equals("-pt")){
                this.printTree = true;
                i++;
            }
        }
    }

    /**
     * Read file function, it takes the inner string variable filename and read the full file.
     * return a list of string list to go method.
     **/
    private List<List<Integer>> readFile() throws Exception{
        // read file
        BufferedReader br = new BufferedReader(new FileReader(filename)); // throw Exception if required automatically

        List<List<Integer>> dataList = new ArrayList<List<Integer>>();
        String line;
        while ((line = br.readLine()) != null) {
            if (line.length() > 1){
                List<Integer> dataLine = new ArrayList<Integer>();
                String[] s = line.split("\t"); // tab separated
//                if (s.length == 2 & (s[0].equals("1") || s[0].equals("0"))){ // only two field
                if (s[0].equals("1") || s[0].equals("0")){ // multiple field
                    dataLine.add(Integer.parseInt(s[0]));
                    dataLine.add(Integer.parseInt(s[1]));
                    dataList.add(dataLine);
                }
                else throw new Exception("Input File not in correct format!");
            }
        }
        return dataList;
    }

    class ArgsException extends Exception{
        public ArgsException() {
            super("Parameters are not Correct!");
        }
    }

    /**
     * For Generic Data Type Example, as README described.
     */
    class Student implements Comparable<Student>{
        Integer id;
        String name;

        Student(int id, String name){
            this.id = id;
            this.name = name;
        }

        @Override
        public int compareTo(Student student) {
            return this.id.compareTo(student.id);
        }

        /**
         * implement toString method if you wish to print out the element.
         * @return a string which is the format of the object to be printed out.
         */
        public String toString(){
            return id + ", " + name;
        }
    }
}

