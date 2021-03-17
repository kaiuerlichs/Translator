import java.io.Serializable;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Class representing a node in a Binary Tree data structures
 * @author Kai Uerlichs
 * @version 1.0
 */
public class TreeNode<T extends Comparable<T>> implements Serializable {
	
	/**
	 * Class is serialisable
	 */
	private static final long serialVersionUID = -4948132557529734259L;
	
	/**
	 * The data of this node
	 */
	private T data;
	
	/**
	 * Reference to the left node from this node
	 */
	private TreeNode<T> left;
	
	/**
	 * Reference to the right node from this node
	 */
	private TreeNode<T> right;
	
	/**
	 * Default constructor for TreeNode object
	 * @param newData The data to set the node data field to
	 */
	public TreeNode(T newData) {
		data = newData;
		left = null;
		right = null;
	}
	
	/**
	 * Returns the data of the node
	 * @return the data
	 */
	public T getData() {
		return data;
	}
	
	/**
	 * Sets the data of the node
	 * @param newData the data to set
	 */
	public void setData(T newData) {
		data = newData;
	}
	
	/**
	 * The left child of the node
	 * @return The left child
	 */
	public TreeNode<T> getLeft(){
		return left;
	}
	
	/**
	 * The right child of the node
	 * @return The right child
	 */
	public TreeNode<T> getRight(){
		return right;
	}
	
	/**
	 * Sets the left child of the node
	 * @param node The node to set the left child to
	 */
	public void setLeft(TreeNode<T> node){
		left = node;
	}
	
	/**
	 * Sets the right child of the node
	 * @param node The node to set the right child to
	 */
	public void setRight(TreeNode<T> node){
		right = node;
	}

	/**
	 * Recursive method used to add a new node to the Binary Tree
	 * @param newData The data to set
	 * @throws NodeExistsAlreadyException When the node already exists in the tree
	 */
	public void add(T newData) throws NodeExistsAlreadyException {
		int cmp = data.compareTo(newData);
		if(cmp > 0){
			if(left == null) {
				left = new TreeNode<T>(newData);
			}
			else {
				left.add(newData);
			}
		}
		else if (cmp < 0){
			if(right == null) {
				right = new TreeNode<T>(newData);
			}
			else {
				right.add(newData);
			}
		}
		else {
			throw new NodeExistsAlreadyException();
		}
	}
	
	/**
	 * Recursive method to find a node in the tree
	 * @param dataKey The node to find
	 * @return The found node
	 */
	public TreeNode<T> find(T dataKey) {
		int cmp = data.compareTo(dataKey);
		if(cmp == 0) {
			return this;
		}
		else if(cmp > 0) {
			if(left != null) {
				return left.find(dataKey);
			}
		}
		else {
			if(right != null) {
				return right.find(dataKey);
			}
		}
		return null;
	}
	
//	Old print methods (deprecated due to Iterators)
	
//	public void printInorder(){
//		if(left != null) {
//			left.printInorder();
//		}
//		System.out.println(data);
//		if(right != null) {
//			right.printInorder();
//		}
//	}
//	
//	public void printPreorder(){
//		System.out.println(data);
//		if(left != null) {
//			left.printInorder();
//		}
//		if(right != null) {
//			right.printInorder();
//		}
//	}
//	
//	public void printPostorder(){
//		if(left != null) {
//			left.printInorder();
//		}
//		if(right != null) {
//			right.printInorder();
//		}
//		System.out.println(data);
//	}
	
	/**
	 * Returns the tree node with the maximum data value in the tree with this object as its root
	 * @return The found node
	 */
	public TreeNode<T> findMax() {
		if(right != null) {
			return right.findMax();
		}
		else {
			return this;
		}
	}
	
	/**
	 * Returns the tree with this node as root as an in-order Queue
	 * @return the Queue
	 */
	public Queue<T> getInorderQueue(){
		Queue<T> i = new LinkedList<T>();
		this.makeInorderQueue(i);
		return i;
	}
	
	/**
	 * Recursive method to traverse and fill the queue in-order
	 * @param i The queue to fill
	 */
	private void makeInorderQueue(Queue<T> i) {
		if(left != null) {
			left.makeInorderQueue(i);
		}
		i.add(data);
		if(right != null) {
			right.makeInorderQueue(i);
		}
	}
	
	/**
	 * Returns the tree with this node as root as an pre-order Queue
	 * @return the Queue
	 */
	public Queue<T> getPreorderQueue(){
		Queue<T> i = new LinkedList<T>();
		this.makePreorderQueue(i);
		return i;
	}
	
	/**
	 * Recursive method to traverse and fill the queue pre-order
	 * @param i The queue to fill
	 */
	private void makePreorderQueue(Queue<T> i) {
		i.add(data);
		if(left != null) {
			left.makePreorderQueue(i);
		}
		if(right != null) {
			right.makePreorderQueue(i);
		}
	}
	
	/**
	 * Returns the tree with this node as root as an post-order Queue
	 * @return the Queue
	 */
	public Queue<T> getPostorderQueue(){
		Queue<T> i = new LinkedList<T>();
		this.makePostorderQueue(i);
		return i;
	}
	
	/**
	 * Recursive method to traverse and fill the queue post-order
	 * @param i The queue to fill
	 */
	private void makePostorderQueue(Queue<T> i) {
		if(left != null) {
			left.makePostorderQueue(i);
		}
		if(right != null) {
			right.makePostorderQueue(i);
		}
		i.add(data);
	}
	
}
