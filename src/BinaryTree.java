import java.io.Serializable;
import java.util.Iterator;
import java.util.Queue;

/**
  * A class representing a Binary Tree data structure 
  * 
  * @author Kai Uerlichs
  * @version 1.0
  */
public class BinaryTree<T extends Comparable<T>> implements Iterable<T>, Serializable {

	/**
	 * Class is serialisable
	 */
	private static final long serialVersionUID = 3914790424687763034L;
	
	/**
	 * The root node of the tree
	 */
	public TreeNode<T> root;
	
	/**
	 * Default constructor for empty Binary Tree
	 */
	public BinaryTree(){
		root = null;
	}
	
	/**
	 * Add a new data node to the tree
	 * 
	 * @param newData The data to add
	 * @throws NodeExistsAlreadyException If the node already exists in the tree
	 */
	public void add(T newData) throws NodeExistsAlreadyException {
		if(isEmpty()) {
			root = new TreeNode<T>(newData);
		}
		else {
			root.add(newData);
		}
	}
	
	/**
	 * Find a node in the tree and return it
	 * @param dataKey The data to search for
	 * @return The data to search for
	 * @throws TreeIsEmptyException If no nodes are in the tree
	 * @throws NodeNotFoundException If the node does not exist in the tree
	 */
	public T find(T dataKey) throws TreeIsEmptyException, NodeNotFoundException {
		// If the tree is not empty
		if(!isEmpty()) {
			// Recursively search the tree for the node
			TreeNode<T> temp = root.find(dataKey);
			if(temp != null) {
				return temp.getData();
			}
			else {
				// Throw exception if node was not found
				throw new NodeNotFoundException();
			}
		}
		// If the tree is empty
		else {
			throw new TreeIsEmptyException();
		}
	}
	
	/**
	 * Print the nodes of the tree in in-order
	 * @throws TreeIsEmptyException If the tree is empty
	 */
	public void printInorder() throws TreeIsEmptyException {
		if(!isEmpty()) {
			// Use default iterator to go through each element
			for(T el : this) {
				System.out.println(el);
			}
		}
		else {
			throw new TreeIsEmptyException();
		}
	}
	
	/**
	 * Print the nodes of the tree in pre-order
	 * @throws TreeIsEmptyException If the tree is empty
	 */
	public void printPreorder() throws TreeIsEmptyException {
		if(!isEmpty()) {
			Iterator<T> i = preorderIterator();
			while(i.hasNext()) {
				System.out.println(i.next());
			}
		}
		else {
			throw new TreeIsEmptyException();
		}
	}
	
	/**
	 * Print the nodes of the tree in post-order
	 * @throws TreeIsEmptyException If the tree is empty
	 */
	public void printPostorder() throws TreeIsEmptyException {
		if(!isEmpty()) {
			Iterator<T> i = postorderIterator();
			while(i.hasNext()) {
				System.out.println(i.next());
			}
		}
		else {
			throw new TreeIsEmptyException();
		}
	}
	
	/**
	 * Check if the tree is empty or not
	 * @return True or false if the tree is empty or not empty respectively
	 */
	public boolean isEmpty() {
		return root == null;
	}
	
	/**
	 * Remove a node from the tree
	 * @param key The data to remove
	 * @throws NodeNotFoundException If the node does not exist
	 * @throws TreeIsEmptyException If the tree is empty
	 * @return The node that was removed
	 */
	public T remove(T key) throws NodeNotFoundException, TreeIsEmptyException {
		// If the tree is not empty
		if(!isEmpty()) {
			// Check whether root is node to remove
			int cmp = root.getData().compareTo(key);
			if(cmp == 0) {
				T result = root.getData();
				// Remove root node
				// Two children
				if(root.getLeft() != null && root.getRight() != null) {
					T maxValue = root.getLeft().findMax().getData();
					remove(maxValue);
					root.setData(maxValue);
				}
				// One or no children
				else {
					if(root.getLeft() != null) {
						root = root.getLeft();
					}
					else {
						root = root.getRight();
					}
				}
				return result;
			}
			// If root is not the node to remove
			else {
				TreeNode<T> previous = root;
				TreeNode<T> current = null;
				// Select next node
				if(cmp > 0) {
					current = root.getLeft();
				}
				else {
					current = root.getRight();
				}
				
				// Search tree using while loop
				boolean found = false;
				while(!found) {
					// Node does not exist
					if(current == null) {
						throw new NodeNotFoundException();
					}
					// Node might exist
					else {
						cmp = current.getData().compareTo(key);
						if(cmp > 0) {
							previous = current;
							current = current.getLeft();
						}
						else if(cmp < 0) {
							previous = current;
							current = current.getRight();
						}
						else {
							found = true;
						}
					}
				}
				
				T result = current.getData();
				// If node has two children
				if(current.getLeft() != null && current.getRight() != null) {
					T maxValue = current.getLeft().findMax().getData();
					remove(maxValue);
					current.setData(maxValue);
				}
				// If node has one or no children
				else {
					if(previous.getData().compareTo(key) > 0) {
						if(current.getLeft() != null) {
							previous.setLeft(current.getLeft());
						}
						else {
							previous.setLeft(current.getRight());
						}
					}
					else {
						if(current.getLeft() != null) {
							previous.setRight(current.getLeft());
						}
						else {
							previous.setRight(current.getRight());
						}
					}
				}
				return result;
			}
		}
		// If tree is empty
		else {
			throw new TreeIsEmptyException();
		}
	}

	/**
	 * Override for the iterator() method of the Iterable interface
	 * and returns the iterator in in-order
	 */
	@Override
	public Iterator<T> iterator() {
		return new InorderIterator();
	}
	
	/**
	 * Alternative iterator in pre-order
	 * @return an Iterator over type T
	 */
	public Iterator<T> preorderIterator() {
		return new PreorderIterator();
	}
	
	/**
	 * Alternative iterator in post-order
	 * @return an Iterator over type T
	 */
	public Iterator<T> postorderIterator() {
		return new PostorderIterator();
	}
	
	/**
	 * Class definining an in-order iterator for the BinaryTree
	 * @author Kai Uerlichs
	 * @version 1.0
	 */
	public class InorderIterator implements Iterator<T> {

		private Queue<T> i;
		
		/**
		 * Default constructor
		 */
		public InorderIterator(){
			i = root.getInorderQueue();
		}
		
		@Override
		public boolean hasNext() {
			return !i.isEmpty();
		}

		@Override
		public T next() {
			return i.remove();
		}
		
	}
	
	/**
	 * Class definining an pre-order iterator for the BinaryTree
	 * @author Kai Uerlichs
	 * @version 1.0
	 */
	public class PreorderIterator implements Iterator<T> {

		private Queue<T> i;
		
		/**
		 * Default constructor
		 */
		public PreorderIterator(){
			i = root.getPreorderQueue();
		}
		
		@Override
		public boolean hasNext() {
			return !i.isEmpty();
		}

		@Override
		public T next() {
			return i.remove();
		}
		
	}
	
	/**
	 * Class definining an post-order iterator for the BinaryTree
	 * @author Kai Uerlichs
	 * @version 1.0
	 */
	public class PostorderIterator implements Iterator<T> {

		private Queue<T> i;
		
		/**
		 * Default constructor
		 */
		public PostorderIterator(){
			i = root.getPostorderQueue();
		}
		
		@Override
		public boolean hasNext() {
			return !i.isEmpty();
		}

		@Override
		public T next() {
			return i.remove();
		}
	}
	
}
