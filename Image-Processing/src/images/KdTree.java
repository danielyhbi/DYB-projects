package images;

import java.util.Arrays;
import java.util.Comparator;

/**
 * This is my interpretation of the K-D tree. This is specifically for a 2 dimensional tree.
 *
 * Within this 2D tree data structure, users are able to:
 * - Add nodes with two coordinates in integers (x and y)
 * - Search to see if a node/point exists
 * - Find the closest existing node to a provided coordinate
 * - Batch add nodes to a balanced condition
 *
 * Limitations:
 * - This tree does not self balance. Adding nodes after initialization would break the balance
 * - Cannot delete a node
 * - Integer numbers only
 *
 * Inspirations:
 * - Computerphile (YouTube): https://youtu.be/BK5x7IUTIyU
 * - Stable Sort (YouTube): https://youtu.be/Glp7THUpGow
 * - Construct a KD Tree: https://courses.engr.illinois.edu/cs225/sp2019/notes/kd-tree/
 */
public class KdTree {
  
  /*
      Node Definition
   */
  
  /**
   * defines a node specific for the kd tree - intger
   */
  public class NodeKd {
    
    // define fields
    private int[] coordinates;
    private int[] colorChannels;
    private int seedCount;
    private NodeKd left;
    private NodeKd right;
    
    // constructor
    public NodeKd() {
      this.coordinates = new int[2];
      this.colorChannels = new int[3];
      this.seedCount = 0;
      this.left = null;
      this.right = null;
    }
  
    /**
     * Constructs a node for the KD tree.
     * @param inputX  x-coordinate of the node
     * @param inputY  y-coordinate of the node
     */
    public NodeKd(int inputX, int inputY) {
      this.coordinates = new int[] {inputX, inputY};
      this.colorChannels = new int[3];
      this.seedCount = 0;
      this.left = null;
      this.right = null;
    }
    
    public NodeKd(int[] inputs) {
      this(inputs[0], inputs[1]);
    }
    
    @Override
    public String toString() {
      return "KD Node: (x=" + this.coordinates[0] + ", y=" + this.coordinates[1] + ")";
    }
    
    public int[] toArray() {
      return coordinates;
    }
    
    public void updateColors() {
    
    }
    
  }
  
  /*
      KD Tree Definition
   */
  
  // define fields
  private int size;
  private NodeKd root;
  
  /**
   * Initialize a constructor by defining a root node. More node shall be added
   * using the add function.
   *
   * @param inputX  x-coordinate of the root node
   * @param inputY  y-coordinate of the root node
   */
  public KdTree(int inputX, int inputY) {
    this.root = new NodeKd(inputX, inputY);
    this.size = 1;
  }
  
  /**
   * Initialize a kd-tree by importing a list of coordinates.
   * Coordinates will sorted and a balanced tree will be created.
   *
   * @param points  list of points in 2D integer array
   */
  public KdTree(int[][] points) {
    
    // the incoming 2D int array will be sorted first and them construct
    this.arrayAddHelper(points, 0, points.length, 0);
    
  }
  
  /**
   * An empty constructor.
   */
  public KdTree() {
    this.root = null;
    this.size = 0;
  }
  
  /**
   * Add a point to the kd tree.
   * <p>WARNING: addition of a node may cause imbalance of the tree
   *
   * @param inputX x-coordinate of the new point
   * @param inputY y-coordinate of the new point
   */
  public void add(int inputX, int inputY) {
    // adding nodes will be done recursively
    // check if the tree is null
    if (this.root == null) {
      size++;
      this.root = new NodeKd(inputX, inputY);
    } else {
      // kick off the recursive steps
      // going off from the root node, it will start from step 0 (even)
      this.root = addHelper(this.root, new int[] {inputX, inputY}, 0);
      
    }
  }
  
  public void add(int[] inputs) {
    this.add(inputs[0], inputs[1]);
  }
  
  private NodeKd addHelper(NodeKd parent, int[] inputs, int steps) {
    // check if the parent node is empty
    if (parent == null) {
      // create a new node here
      size++;
      // note again that inputs[0] is y coordinate, input[1] is x coordinate
      return new NodeKd(inputs[0], inputs[1]);
    }
    
    // with non-empty parent nodes, compare the insertion
    // with odd steps (1, 3, 5), compare the x coordinate (mod = 1)
    // with even steps (2, 4, 6), compare the y coordinate (mod = 0)
    if (inputs[steps % 2] <= parent.coordinates[steps % 2]) {
      // send it to the left of the tree
      parent.left = this.addHelper(parent.left, inputs, steps + 1);
    } else {
      // send it to the right of the tree
      parent.right = this.addHelper(parent.right, inputs, steps + 1);
    }
    
    return parent;
  }
  
  private void arrayAddHelper(int[][] inputs, int startIndex, int endIndex, int steps) {
    // traversal order: root, left, right
    // assume the recursion starts with a median defined
    
    // check the array
    if (inputs.length == 1) {
      // there's only one array left, add it
      this.add(inputs[0]);
      return;
    }
    
    if (inputs.length == 0) {
      // an empty array. add nothing and just return
      return;
    }
    
    // sort the array with streams based on steps:
    // (0, 2, 4, ...) - based on y
    // (1, 3, 5, ...) - based on x
    Arrays.sort(inputs, Comparator.comparingInt(a -> a[steps % 2]));
    
    // get the median of the array
    int medianIndex = inputs.length / 2;
    // add the point at median index to the tree
    this.add(inputs[medianIndex]);
    // recurse to left of the index
    this.arrayAddHelper(Arrays.copyOfRange(inputs,0, medianIndex), 0, 0, steps + 1);
    // recurse to right of the index
    this.arrayAddHelper(Arrays.copyOfRange(inputs,medianIndex + 1, inputs.length), 0, 0, steps + 1);

    // end of recursive steps
  }
  
  /**
   * Checks if a node is present in the tree.
   *
   * @param inputX x-coordinate of the new point
   * @param inputY y-coordinate of the new point
   * @return  true/false statement of if the point exists
   */
  public boolean present(int inputX, int inputY) {
    // similar to the add method, traverse to each point to find out if it exists
    return this.presentHelper(this.root, new int[]{inputX, inputY}, 0);
  }
  
  public boolean presentHelper(NodeKd parent, int[] inputs, int steps) {
    // similar to the add method, traverse to each point to find out if it exists
    if (parent == null) {
      // reached to the end of the search, if nothing is there, means nothing is found
      return false;
    }
    
    // otherwise, check the parent node to see if it fits the loop up
    // first, check equality now
    if (parent.coordinates[0] == inputs[0] && parent.coordinates[1] == inputs[1]) {
      return true;
    }
    
    // if not equal, then go down the tree
    if (inputs[steps % 2] <= parent.coordinates[steps % 2]){
      // go left
      return presentHelper(parent.left, inputs, steps+1);
    } else {
      // go right
      return presentHelper(parent.right, inputs, steps+1);
    }
  }
  
  /**
   * Given a point, it will compute the shortest distance to a point in the tree.
   * @param inputX x-coordinate of the new point
   * @param inputY y-coordinate of the new point
   * @return  the shortest distance
   */
  public NodeKd getShortestDistance(int inputX, int inputY) {
    
    return this.shortestDistanceHelper(root, new int[]{inputX, inputY}, 0);
  
  }
  
  private NodeKd shortestDistanceHelper(NodeKd parent, int inputs[], int steps) {
    
    /*
     traverse to the end of points as usual. However this time keep track of
     the shortest distance of the parent node as well.
     
     If the parent node distance (horiz/vert) is less than the computed shortest
     distance (from end of the recursion), then we have to jump to the other branch
     of the parent node to keep computing and comparing the distance.
     */
    
    // Traverse to a leaf node
    if (parent == null) {
      // tell the previous call stack that it has reached end of the recursion.
      return null;
    }
    
    NodeKd toBeTraversed;
    NodeKd theOtherNode;
    NodeKd nextNode;
    NodeKd closestNode;
    
    // during the traversal, compare the nodes
    if (inputs[steps % 2] <= parent.coordinates[steps % 2]){
      // go left: record the nodes information
      toBeTraversed = parent.left;
      theOtherNode = parent.right;

    } else {
      // go right: record the node information
      toBeTraversed = parent.right;
      theOtherNode = parent.left;
    }
    
    // perform the actual traversal here:
    // it kicks down the recursion tree, or end up return null indicating end of recursion
    nextNode = shortestDistanceHelper(toBeTraversed, inputs, steps + 1);
    // so now we have the parent node, and a "next node" (could be null). Will now decide
    // which point has the shortest path to that node
    closestNode = getClosestNode(parent, nextNode, inputs);
    
    // Note that the other branch might have closer points as well
    // ====
    // if the vertical/horizontal distance between parent and lookup node is less than the shortest
    // distance, there's a possibility that other nodes on the other side (theOtherNode).
    double closestDistTemp = getDistance(closestNode, inputs);
    double vertOrHorizDistance = Math.abs(inputs[steps % 2] - parent.coordinates[steps % 2]);
    
    if (vertOrHorizDistance < closestDistTemp) {
      // preserve the previous temp node
      NodeKd tempNextNode = nextNode;
      // traverse and do the same thing to the other side
      // doing so might overwrite that the closestNode is from previous computation
      nextNode = shortestDistanceHelper(theOtherNode, inputs, steps + 1);
      // compare the current nextNode to previous next node to see which one is closer
      nextNode = getClosestNode(tempNextNode, nextNode, inputs);
      // compute the final node that is the closest (at this call stack)
      closestNode = getClosestNode(parent, nextNode, inputs);
    }
    
    return closestNode;
  }
  
  private NodeKd getClosestNode(NodeKd node1, NodeKd node2, int[] inputs) {
    // check input validity. Technically both nodes shall not be null at the same time
    if (node1 == null && node2 == null) {
      return null;
    }
    // check if nodes are null
    if (node1 == null) {
      return node2;
    }
    
    if (node2 == null) {
      return node1;
    }
    
    // do the computation
    double distance1 = getDistance(node1, inputs);
    double distance2 = getDistance(node2, inputs);
    
    if (distance1 <= distance2) {
      return node1;
    } else {
      return node2;
    }
  }
  
  // returns the distance between two points via pythagorean theorem
  private double getDistance(NodeKd node, int[] inputs) {
    double output = Math.sqrt(Math.pow((node.coordinates[0] - inputs[0]), 2)
            + Math.pow((node.coordinates[1] - inputs[1]), 2));
    return output;
  }
  
}
