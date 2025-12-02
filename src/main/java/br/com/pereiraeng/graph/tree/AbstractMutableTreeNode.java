package br.com.pereiraeng.graph.tree;

import java.util.Collections;
import java.util.Enumeration;
import java.util.NoSuchElementException;
import java.util.Stack;
import java.util.Vector;

import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;

public abstract class AbstractMutableTreeNode extends PrimitiveMutableTreeNode {

	protected Object object;

	public void setUserObject(Object object) {
		this.object = object;
	}

	public Object getUserObject() {
		return object;
	}

	/**
	 * The child nodes for this node (may be empty).
	 */
	protected Vector<MutableTreeNode> children = new Vector<MutableTreeNode>();

	/**
	 * allowsChildren
	 */
	protected boolean allowsChildren;

	/**
	 * Adds a new child node to this node and sets this node as the parent of* the
	 * child node.The child node must not be an ancestor of this node.* If the tree
	 * uses the {@link DefaultTreeModel}, you must subsequently*call
	 * {@link DefaultTreeModel#reload(TreeNode)}.**
	 * 
	 * @param child * the child node (<code>null</code> not permitted).
	 *
	 * @throws IllegalStateException    if {@link #getAllowsChildren()} returns
	 *                                  <code>false</code>.
	 * @throws IllegalArgumentException if {@link #isNodeAncestor} returns
	 *                                  <code>true</code>.
	 * @throws IllegalArgumentException if <code>child</code> is <code>null</code>.
	 */
	public void add(MutableTreeNode child) {
		if (!allowsChildren)
			throw new IllegalStateException();

		if (child == null)
			throw new IllegalArgumentException();

		if (isNodeAncestor(child))
			throw new IllegalArgumentException("Cannot add ancestor node.");

		children.add(child);
		child.setParent(this);
	}

	/**
	 * Removes the child with the given index from this node.
	 *
	 * @param index the index (in the range <code>0</code> to
	 *              <code>getChildCount() - 1</code>).
	 * 
	 * @throws ArrayIndexOutOfBoundsException if <code>index</code> is outside the
	 *                                        valid range.
	 */
	@Override
	public void remove(int index) {
		MutableTreeNode child = (MutableTreeNode) children.remove(index);
		child.setParent(null);
	}

	/**
	 * Removes the given child from this node and sets its parent to
	 * <code>null</code>.
	 *
	 * @param node the child node (<code>null</code> not permitted).
	 * 
	 * @throws IllegalArgumentException if <code>node</code> is not a child of this
	 *                                  node.
	 * @throws IllegalArgumentException if <code>node</code> is null.
	 */
	@Override
	public void remove(MutableTreeNode node) {
		if (node == null)
			throw new IllegalArgumentException("Null 'node' argument.");
		if (node.getParent() != this)
			throw new IllegalArgumentException("The given 'node' is not a child of this node.");
		children.remove(node);
		node.setParent(null);
	}

	/**
	 * Inserts given child node at the given index.
	 *
	 * @param node  the child node (<code>null</code> not permitted).
	 * @param index the index.
	 * 
	 * @throws IllegalArgumentException if <code>node</code> is </code>null</code>.
	 */
	@Override
	public void insert(MutableTreeNode node, int index) {
		if (!allowsChildren)
			throw new IllegalStateException();

		if (node == null)
			throw new IllegalArgumentException("Null 'node' argument.");

		if (isNodeAncestor(node))
			throw new IllegalArgumentException("Cannot insert ancestor node.");

		children.insertElementAt(node, index);
	}

	/**
	 * Returns an enumeration containing all children of this node.
	 * <code>EMPTY_ENUMERATION</code> is returned if this node has no children.
	 *
	 * @return an enumeration of tree nodes
	 */
	@Override
	public Enumeration<MutableTreeNode> children() {
		if (children.size() == 0)
			return Collections.emptyEnumeration();
		return children.elements();
	}

	/**
	 * Returns the child node at a given index.
	 *
	 * @param index the index
	 *
	 * @return the child node
	 */
	@Override
	public TreeNode getChildAt(int index) {
		return (TreeNode) children.elementAt(index);
	}

	/**
	 * Returns the number of children of this node.
	 *
	 * @return the number of children
	 */
	@Override
	public int getChildCount() {
		return children.size();
	}

	/**
	 * Returns the index of the specified child node, or -1 if the node is not in
	 * fact a child of this node.
	 * 
	 * @param node the node (<code>null</code> not permitted).
	 * 
	 * @return The index of the specified child node, or -1.
	 * 
	 * @throws IllegalArgumentException if <code>node</code> is <code>null</code>.
	 */
	@Override
	public int getIndex(TreeNode node) {
		if (node == null)
			throw new IllegalArgumentException("Null 'node' argument.");
		return children.indexOf(node);
	}

	/**
	 * Sets the flag that controls whether or not this node allows the addition /
	 * insertion of child nodes. If the flag is set to <code>false</code>, any
	 * existing children are removed.
	 *
	 * @param allowsChildren the flag.
	 */
	public void setAllowsChildren(boolean allowsChildren) {
		if (!allowsChildren)
			removeAllChildren();
		this.allowsChildren = allowsChildren;
	}

	/**
	 * getAllowsChildren
	 *
	 * @return boolean
	 */
	@Override
	public boolean getAllowsChildren() {
		return allowsChildren;
	}

	/**
	 * getDepth
	 *
	 * @return int
	 */
	public int getDepth() {
		if ((!allowsChildren) || children.size() == 0)
			return 0;

		Stack<Integer> stack = new Stack<Integer>();
		stack.push(Integer.valueOf(0));
		TreeNode node = getChildAt(0);
		int depth = 0;
		int current = 1;

		while (!stack.empty()) {
			if (node.getChildCount() != 0) {
				node = node.getChildAt(0);
				stack.push(Integer.valueOf(0));
				current++;
			} else {
				if (current > depth)
					depth = current;

				int size;
				int index;

				do {
					node = node.getParent();
					size = node.getChildCount();
					index = ((Integer) stack.pop()).intValue() + 1;
					current--;
				} while (index >= size && node != this);

				if (index < size) {
					node = node.getChildAt(index);
					stack.push(Integer.valueOf(index));
					current++;
				}
			}
		}

		return depth;
	}

	/**
	 * Returns the first child node belonging to this tree node.
	 *
	 * @return The first child node.
	 * 
	 * @throws NoSuchElementException if this tree node has no children.
	 */
	public TreeNode getFirstChild() {
		return (TreeNode) children.firstElement();
	}

	/**
	 * Returns the last child node belonging to this tree node.
	 *
	 * @return The last child node.
	 * 
	 * @throws NoSuchElementException if this tree node has no children.
	 */
	public TreeNode getLastChild() {
		return (TreeNode) children.lastElement();
	}

	/**
	 * Returns <code>true</code> if this tree node is a lead node (that is, it has
	 * no children), and <code>false</otherwise>.
	 *
	 * @return A boolean.
	 */
	@Override
	public boolean isLeaf() {
		return children.size() == 0;
	}

	/**
	 * getLeafCount
	 *
	 * @return int
	 */
	public int getLeafCount() {
		int count = 0;
		Enumeration<?> e = depthFirstEnumeration();

		while (e.hasMoreElements()) {
			TreeNode current = (TreeNode) e.nextElement();

			if (current.isLeaf())
				count++;
		}

		return count;
	}

	/**
	 * depthFirstEnumeration
	 *
	 * @return Enumeration
	 */
	public Enumeration<?> depthFirstEnumeration() {
		return postorderEnumeration();
	}

	/**
	 * postorderEnumeration
	 *
	 * @return Enumeration
	 */
	public Enumeration<?> postorderEnumeration() {
		return new PostorderEnumeration(this);
	}

	/**
	 * Provides an enumeration of a tree traversing it postordered (= depth-first).
	 */
	private static class PostorderEnumeration implements Enumeration<Object> {

		Stack<TreeNode> nodes = new Stack<TreeNode>();
		Stack<Enumeration<MutableTreeNode>> childrenEnums = new Stack<Enumeration<MutableTreeNode>>();

		@SuppressWarnings("unchecked")
		PostorderEnumeration(TreeNode node) {
			nodes.push(node);
			childrenEnums.push((Enumeration<MutableTreeNode>) node.children());
		}

		public boolean hasMoreElements() {
			return !nodes.isEmpty();
		}

		public Object nextElement() {
			if (nodes.isEmpty())
				throw new NoSuchElementException("No more elements left!");

			Enumeration<MutableTreeNode> children = (Enumeration<MutableTreeNode>) childrenEnums.peek();

			return traverse(children);
		}

		private Object traverse(Enumeration<MutableTreeNode> children) {
			if (children.hasMoreElements()) {
				TreeNode node = (TreeNode) children.nextElement();
				nodes.push(node);

				@SuppressWarnings("unchecked")
				Enumeration<MutableTreeNode> newChildren = (Enumeration<MutableTreeNode>) node.children();
				childrenEnums.push(newChildren);

				return traverse(newChildren);
			} else {
				childrenEnums.pop();

				// Returns the node whose children
				// have all been visited. (= postorder)
				Object next = nodes.peek();
				nodes.pop();

				return next;
			}
		}
	}
}