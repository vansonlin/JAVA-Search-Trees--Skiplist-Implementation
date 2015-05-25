README for Algorithm Project by Tzu-Chun (Vanson) Lin
4/22, 2015

-----------------
-- Quick Start --
-----------------
The submitted files inlcude the compiled files and source. Thus, you can start directly.

$ tar -xvf lintz.tgz  // Extract the tgz file
$ cd lintz  // move to the lintz folder

// start running the experiment
$ java lintz_Integer
	Total time: 1 milliseconds.
	Data structure contains 9 elements.

$ java lintz_Float -f inputfile.txt -d splaytree
	Total time: 1 milliseconds.
	Data structure contains 9 elements.

$java lintz -f inputfile.txt -d rbtree -p -pt
	== Print RB-Tree in Tree format (rotated 90 degree counterclockwise) ==

	|	|	|-------432
	|	|-------338
	|	|	|-------321
	|-------223
	|	|-------19
	15
	|	|-------123
	|-------122
	|	|-------12

	12
	122
	123
	15
	19
	223
	321
	338
	432

*************************
If re-compile is required
*************************
1. Make any desired changez to lintz.java , lintz_Integer.java ,or lintz_Float.java

2. Compile by typing
	
	$ bash build.sh // for lintz.java, string type
	$ bash build.sh integer // for lintz_Integer.java, integer type
	$ bash build.sh float // for lintz_Float.java, float type

3. Execute the files by
	$ java lintz // for running string type
	$ java lintz_Integer // for running integer type
	$ java lintz_Float // for running float type

----------------------
-- Parameter Option --
----------------------
The following arguments are be specified in random order. And it's okay if none of them are specified at all.

-f <filename> 		: Filename. Default 'inputfile.txt' if not specified.
This specifies the file containing input queries. Each line for one query, and two tab-separated field constructs one line. The first field is query type, 1 for insertion and 0 for deletion. The second field is the string to be passed with the query.

-d <data structure> : Data Structure. Default 'bst' if not specified.
There are 4 options, 'bst' for binary search tree; 'rbtree' for RB-Tree; 'skiplist' for Skip List; and 'splaytree' for Splay Tree.

-h 					: Height. 
If specified, the program will print the result for each operation simultaneously. The result includes whether the insertion/deletion was performed, and the resulting height in the data structure after the operations. Better to use this argument only for small number of queries, since the program print result for each query, it may decrease the overall performance for huge number of queries.

-p 					: Print Elements.
If specified, the program will print all the existing elements in sorted order. Default false;

-pt 				: Print Tree.
All the data structures support the option. If specified, the program will print the whole tree in tree structure (or list structure for skip list). This is very interesting, you might want to take a look. But it looks much better for small data set (and shorter string, less or equal 3 chars for skip list, since each layer are printed in tab-separated).
Only the printTree method for SkipList was written by myself, while the other main printTree algorithm are credited to http://stackoverflow.com/questions/4965335/how-to-print-binary-tree-diagram (specified in the code comment as well :) ) 

Examples:
./java lintz -f inputfile.txt -d rbtree -h -pt -p
./java lintz -f query_insert_delete.txt -d rbtree -pt 

Input: (if nothing specified)
	./java lintz

Output:
	Total time: 2 milliseconds.
	Data structure contains 9 elements.

----------------
-- Input File --
----------------
As example file, inputfile.txt, the file contains query for each line. Two tab-separated fields construct one line. The first one is query type, second one is the string to be queried.

For example:

1	dx1
1	wAC
0	doF
0	x6C
1	jko

--------------------
-- Public Methods --
--------------------
boolean insert(T)
Insert some data T, return true if the data was inserted. Returning false if the data to be inserted exists already, thus no insertion was performed.

boolean delete(T)
Delete some data T, return true if the data was deleted. Return false if the data to be deleted does not exist, thus no deletion was performed.

T search(T)
Search for some data T, re turn the data if it was found. Return null if no such data was found.

void print()
Print out all the data in the data structure in sorted order.

void printTree()
Print out the data structure in tree format (or list format for skip list).

int getHeight()
Return an int for current height in the data strcuture.

-------------
-- Details --
-------------

1. Execution Time:
	Only the time for insertion or deletion operations are measured. Time for file loading and print/printTree are excluded.

2. The performance generally reduced significantly by specifying '-h' on huge query file. Mainly because of large number of stdout process. For example, the time for query_insert_delete.txt on RB-Tree without '-h' specified is 6572 milliseconds, while with '-h' specified is more than 30 minutes (I didn't finish the testing tho...).

3. In my code, there are plenty of comments explaining how I implemented the algoriths into details, particularly for splay tree one, plenty of time was spenting on the visualization of different situation.

4. All 4 data structures implemented a Tree interface (self-defined), thus the public methods can be called easily.

5. For RB-Tree, the color for each node was stored in boolean type (true for red while false for black), because the color are binary. by doing this can increase performance in space use and condition statement (such as if statement).

6. For SkipList, I implemented SkipList Node in the class since the details were not specified in the paper. The ideas came from the paper, as well as online resource (http://www.mathcs.emory.edu/~cheung/Courses/323/Syllabus/Map/skip-list-impl.html He explained the idea very clearly with visualization). After understood the concept, I implemented the code by myself, not from any existing code.

7. For SkipList, the height of empty list is initilized as 0, instead of 1 in the paper. In my code, I consider the top layer (only head and tail nodes) are not physically existing. Thus the empty list was not counted into height. The presenting height may (or may not) lower than other implementation by 1.

8. For SplayTree, I implemented 6 (2 zigs, 2 zigzig + 2 zigzag) rotation supporting methods, as well as transplant method copied from textbook pseudo code. Each situation of each rotation method was written in the comment part of the top of the according method. You may want to take a look (cus I spent lots of time on it...).

9. For SplayTree, since there are plenty of details not given from the paper, I figured some small details on my own. There is a chance that other (or standard) implementations differ from my code. Thus the output height after each operation may be different. But the overall idea, and height should be similar.

-------------
-- Generic --
-------------
The program can accept generic type if you modify the main method. The default type (like using argument) is string. While, the program can always take any data type as long as it implements Comparable and provide compareTo() method, such as int or any pre-design data types.

You can always use the data structure class individially for any desires.
For example, simply import the bst, and follows command will work.

Input:

	SkipList skiplist = new SkipList();
	skiplist.insert(6); // data type is fixed as String
	skiplist.insert(7);
	skiplist.insert(3);
	skiplist.insert(2);
	skiplist.insert(5);
	skiplist.delete(3);
	skiplist.printTree();
	skiplist.print();
	System.out.println("\nHeight = " + skiplist.getHeight());

Output:

	== Print SkipList content in List format (90 degree rotated clockwise) ==

	nan	nan	nan	nan	nan	nan	nan	
	2	2	
	5	5	5	5	5	
	6	6	6	
	7	7	7	7	7	7	
	nan	nan	nan	nan	nan	nan	nan	

	== Print SkipList content in softed order ==

	2
	5
	6
	7

	Height = 6

* be careful for selecting your data type. Once the first query was in, the data type is fixed and can not change anymore. For example, if the first quest is int type (like the example above), the query  

skiplist.insert(1.5); // double type

is not gonna work, since the program cannot take any other data type but Integer.

************************************ (Important!!!)
Example for Self-Defined Data Type.
************************************
The following designs a student class implementing Comparable() by student id, that you can use for the data structure. (The code is included in the lintz.java)

	class Student implements Comparable<Student>{
		int id;
		String name;

	    Student(int id, String name){
	    	this.id = id;
	        this.name = name;
	    }

	    @Override
	    public int compareTo(Student student) {
	        return this.id.compareTo(student.id);
	    }

	    public String toString(){ // implement toString method if you wish to print out the element.
	    	return id + ", " + name;
	    }
	}

	public static void main(){
	    RBTree rbtree = new RBTree();
	    rbtree.insert(new Student(10, "Hasan"));
	    rbtree.insert(new Student(5, "Vanson"));
	    rbtree.insert(new Student(3, "Mary"));
	    rbtree.insert(new Student(2, "Tom"));
	    rbtree.insert(new Student(1, "John"));
	    rbtree.insert(new Student(4, "Jordan"));
	    rbtree.printTree();
	    rbtree.print();
	    System.out.println("\nHeight = " + rbtree.getHeight());
	}

Output:
	== Print RB-Tree in Tree format (rotated 90 degree counterclockwise) ==

	|-------10, Hasan
	5, Vanson
	|	|	|-------4, Jordan
	|	|-------3, Mary
	|-------2, Tom
	|	|-------1, John

	== Print RB-Tree content in sorted order ==
	1, John
	2, Tom
	3, Mary
	4, Jordan
	5, Vanson
	10, Hasan

	Height = 4

----------------------
-- Test Performance --
----------------------
The followings show the peformance on the provided test dataset for each data structure: query_insert_delete.txt, 

$ java lintz -f query_insert_delete.txt -d bst
	Experiment with 'bst' structure. Total time: 6726 milliseconds.
	Data structure contains 251757 elements, while overall height = 46.

$ java lintz -f query_insert_delete.txt -d rbtree
	Experiment with 'rbtree' structure. Total time: 4968 milliseconds.
	Data structure contains 251757 elements, while overall height = 23.

$ java lintz -f query_insert_delete.txt -d skiplist
	Experiment with 'skiplist' structure. Total time: 8447 milliseconds.
	Data structure contains 251757 elements, while overall height = 16.

$ java lintz -f query_insert_delete.txt -d splaytree
	Experiment with 'splaytree' structure. Total time: 8494 milliseconds.
	Data structure contains 251757 elements, while overall height = 50.
