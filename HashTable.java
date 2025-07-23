/**
 * A simple hasht table is an array of linked lists. In its simplest form, a
 * linked list is represented by its first node. Typically we label this node as
 * "head". Here, however, we'll know it's the first node of the list because it
 * will be placed in an array element. For example, if we have 4 linked lists,
 * we know that the "head" of the third one can be found in position [2] of the
 * underlying array.
 */
public class HashTable<E extends Comparable<E>> {

    /**
     * Underlying array of nodes. Each non empty element of this array is the first
     * node of a linked list.
     */
    private Node<E>[] underlying;

    /** Counts how many places in the underlying array are occupied */
    private int usage;

    /** Counts how many nodes are stored in this hashtable */
    private int totalNodes;

    /** Tracks underlying array's load factor */
    private double loadFactor;

    /**
     * Default size for the underlying array.
     */
    private static final int DEFAULT_SIZE = 4;

    /** Default load factor threshold for resizing */
    private static double LOAD_FACTOR_THRESHOLD = 0.75;

    /**
     * Basic constructor with user-specified size. If size is absurd, the
     * constructor will revert to the default size.
     */
    public HashTable(int size) {
        if (size <= 0)
            size = DEFAULT_SIZE;
        this.underlying = new Node[size];
        this.usage = 0;
        this.totalNodes = 0;
        this.loadFactor = 0.0;
    } // basic constructor

    /** Default constructor, passes defauilt size to basic constructor */
    public HashTable() {
        this(DEFAULT_SIZE);
    } // default constructor

    /**
     * Adds a node, with the specified content, to a linked list in the underlying
     * array.
     * 
     * @param content E The content of a new node, to be placed in the array.
     */
    public void add(E content) {
    //recalculate load factor before putting it in 
    this.loadFactor = (double) this.usage / this.underlying.length;

    //check if you need a rehash and if the current load factor is equal or higher than the threshhold then you need to rehash
    if (this.loadFactor >= LOAD_FACTOR_THRESHOLD) {
        rehash(); //rehashing
    }

    // make a new node to to hold what we are adding 
    Node<E> newNode = new Node<E>(content);
    //calculate where the node should go
    int position = Math.abs(content.hashCode()) % this.underlying.length;
//put the node in the correct position and if it is empty place a new node there 
    if (this.underlying[position] == null) {
        this.underlying[position] = newNode;
        this.usage++;
    } else { //if the spot is being used add a new node in front of the list
        newNode.setNext(this.underlying[position]); //point the new node to the existing list
        this.underlying[position] = newNode; //put the new node at the head 
    }
    //increase the total number of nodes 
    this.totalNodes++;
    //recalulate the load factor
    this.loadFactor = (double) this.usage / this.underlying.length; // update load factor after insert
}

    /**
     * Searches the underlying array of linked lists for the target value. If the
     * target value is stored in the underlying array, the position of its
     * corresponding linked list can be obtained immediately through the target's
     * hashcode. The linked list must then be traversed to determine if a node with
     * similar content and the target value is present or not.
     * 
     * @param target E value to searc for
     * @return true if target value is present in one of the linked lists of the
     *         underlying array; false otherwise.
     */
    public boolean contains(E target) {
        //get the position 
        int position = Math.abs(target.hashCode()) % this.underlying.length;
        //get the head of the node at that position
        Node<E> current = this.underlying[position];
        // go across the linked list
        while (current != null) {
            if (current.getContent().compareTo(target) == 0) {
                //the target that was found
                return true; 
            }
            //move to the next node
            current = current.getNext();
        }
        //target that was not found
        return false;
    } // method contains

    private void rehash() {
        //double the size 
    Node<E>[] newArray = new Node[this.underlying.length * 2];
    //make sure to reset it
    int newUsage = 0;
    //rehash all the nodes 
    for (int i = 0; i < this.underlying.length; i++) {
        Node<E> current = this.underlying[i]; //start at the head
        while (current != null) { // go through all the nodes in the list
            //save the next one
            Node<E> nextNode = current.getNext();
            //calculate the position of the current node 
            int newPos = Math.abs(current.getContent().hashCode()) % newArray.length;
            //if the position is empty pleace the current node  and if occupied put the current node at the front of the list
            if (newArray[newPos] == null) {
                newUsage++; //increment
                current.setNext(null); //take off current node 
                newArray[newPos] = current; //insert
            } else {
                current.setNext(newArray[newPos]); //put current node to the head
                newArray[newPos] = current; //make that the new head
            }
            //move to the next node in the old list
            current = nextNode;
        }
    }
    //replace the underlying array 
    this.underlying = newArray;
    this.usage = newUsage;
    //recalculate the loadfactor 
    this.loadFactor = (double) this.usage / this.underlying.length;
}

    /** Constants for toString */
    private static final String LINKED_LIST_HEADER = "\n[ %2d ]: ";
    private static final String EMPTY_LIST_MESSAGE = "null";
    private static final String ARRAY_INFORMATION = "Underlying array usage / length: %d/%d";
    private static final String NODES_INFORMATION = "\nTotal number of nodes: %d";
    private static final String NODE_CONTENT = "%s --> ";

    /** String representationf for the object */
    public String toString() {
        // Initialize the StringBuilder object with basic info
        StringBuilder sb = new StringBuilder(
                String.format(ARRAY_INFORMATION,
                        this.usage, this.underlying.length));
        sb.append(String.format(NODES_INFORMATION, this.totalNodes));
        // Iterate the array
        for (int i = 0; i < underlying.length; i++) {
            sb.append(String.format(LINKED_LIST_HEADER, i));
            Node head = this.underlying[i];
            if (head == null) {
                // message that this position is empty
                sb.append(EMPTY_LIST_MESSAGE);
            } else {
                // traverse the linked list, displaying its elements
                Node cursor = head;
                while (cursor != null) {
                    // update sb
                    sb.append(String.format(NODE_CONTENT, cursor));
                    // move to the next node of the ll
                    cursor = cursor.getNext();
                } // done traversing the linked list
            } // done checking the current position of the underlying array
        } // done iterating the underlying array
        return sb.toString();
    } // method toString

} // class HashTable
