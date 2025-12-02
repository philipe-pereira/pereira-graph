package br.com.pereiraeng.graph.tree;

import java.util.ArrayList;

import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;

public abstract class PrimitiveMutableTreeNode implements MutableTreeNode {

	/**
	 * The parent of this node (possibly <code>null</code>).
	 */
	private MutableTreeNode parent;

	/**
	 * Returns the parent node of this node.
	 *
	 * @return The parent node (possibly <code>null</code>).
	 */
	@Override
	public TreeNode getParent() {
		return parent;
	}

	/**
	 * Set the parent node for this node.
	 *
	 * @param node
	 *            the parent node
	 */
	@Override
	public void setParent(MutableTreeNode node) {
		parent = node;
	}

	/**
	 * Returns a path to this node from the root.
	 *
	 * @return an array of tree nodes
	 */
	public TreeNode[] getPath() {
		return getPathToRoot(this, 0);
	}

	public static TreeNode[] getPathToRoot(TreeNode node, int depth) {
		if (node == null) {
			if (depth == 0)
				return null;

			return new TreeNode[depth];
		}

		TreeNode[] path = getPathToRoot(node.getParent(), depth + 1);
		path[path.length - depth - 1] = node;
		return path;
	}

	/**
	 * Removes this node from its parent.
	 */
	@Override
	public void removeFromParent() {
		parent.remove(this);
		parent = null;
	}

	/**
	 * Removes all child nodes from this node.
	 */
	public void removeAllChildren() {
		for (int i = getChildCount() - 1; i >= 0; i--)
			remove(i);
	}

	/**
	 * Returns <code>true</code> if <code>node</code> is an ancestor of this
	 * tree node, and <code>false</code> otherwise. An ancestor node is any of:
	 * <ul>
	 * <li>this tree node;</li>
	 * <li>the parent node (if there is one);</li>
	 * <li>any ancestor of the parent node;</li>
	 * </ul>
	 * If <code>node</code> is <code>null</code>, this method returns
	 * <code>false</code>.
	 * 
	 * @param node
	 *            the node (<code>null</code> permitted).
	 *
	 * @return A boolean.
	 */
	public boolean isNodeAncestor(TreeNode node) {
		return isNodeAncestor(this, node);
	}

	public static boolean isNodeAncestor(TreeNode current, TreeNode node) {
		if (node == null)
			return false;
		while (current != null && current != node)
			current = current.getParent();
		return current == node;
	}

	/**
	 * Returns <code>true</code> if <code>node</code> is a descendant of this
	 * tree node, and <code>false</code> otherwise. A descendant node is any of:
	 * <ul>
	 * <li>this tree node;</li>
	 * <li>the child nodes belonging to this tree node, if there are any;</li>
	 * <li>any descendants of the child nodes;</li>
	 * </ul>
	 * If <code>node</code> is <code>null</code>, this method returns
	 * <code>false</code>.
	 * 
	 * @param node
	 *            the node (<code>null</code> permitted).
	 *
	 * @return A boolean.
	 */
	public boolean isNodeDescendant(PrimitiveMutableTreeNode node) {
		if (node == null)
			return false;

		TreeNode current = node;

		while (current != null && current != this)
			current = current.getParent();

		return current == this;
	}

	public TreeNode getSharedAncestor(PrimitiveMutableTreeNode node) {
		TreeNode current = this;
		ArrayList<TreeNode> list = new ArrayList<TreeNode>();

		while (current != null) {
			list.add(current);
			current = current.getParent();
		}

		current = node;

		while (current != null) {
			if (list.contains(current))
				return current;

			current = current.getParent();
		}

		return null;
	}

	public boolean isNodeRelated(PrimitiveMutableTreeNode node) {
		if (node == null)
			return false;

		return node.getRoot() == getRoot();
	}

	/**
	 * getLevel
	 *
	 * @return int
	 */
	public int getLevel() {
		int count = -1;
		TreeNode current = this;

		do {
			current = current.getParent();
			count++;
		} while (current != null);

		return count;
	}

	/**
	 * Returns the root node by iterating the parents of this node.
	 *
	 * @return the root node
	 */
	public TreeNode getRoot() {
		TreeNode current = this;
		TreeNode check = current.getParent();

		while (check != null) {
			current = check;
			check = current.getParent();
		}

		return current;
	}

	/**
	 * Tells whether this node is the root node or not.
	 *
	 * @return <code>true</code> if this is the root node,
	 *         <code>false</code>otherwise
	 */
	public boolean isRoot() {
		return parent == null;
	}

	/**
	 * getNextNode
	 *
	 * @return AbstractMutableTreeNode
	 */
	public PrimitiveMutableTreeNode getNextNode() {
		// Return first child.
		if (getChildCount() != 0)
			return (PrimitiveMutableTreeNode) getChildAt(0);

		// Return next sibling (if needed the sibling of some parent).
		PrimitiveMutableTreeNode node = this;
		PrimitiveMutableTreeNode sibling;

		do {
			sibling = node.getNextSibling();
			node = (PrimitiveMutableTreeNode) node.getParent();
		} while (sibling == null && node != null);

		// Return sibling.
		return sibling;
	}

	/**
	 * getPreviousNode
	 *
	 * @return AbstractMutableTreeNode
	 */
	public PrimitiveMutableTreeNode getPreviousNode() {
		// Return null if no parent.
		if (parent == null)
			return null;

		PrimitiveMutableTreeNode sibling = getPreviousSibling();

		// Return parent if no sibling.
		if (sibling == null)
			return (PrimitiveMutableTreeNode) parent;

		// Return last leaf of sibling.
		if (sibling.getChildCount() != 0)
			return sibling.getLastLeaf();

		// Return sibling.
		return sibling;
	}

	/**
	 * Returns <code>true</code> if <code>node</code> is a child of this tree
	 * node, and <code>false</code> otherwise. If <code>node</code> is
	 * <code>null</code>, this method returns <code>false</code>.
	 *
	 * @param node
	 *            the node (<code>null</code> permitted).
	 *
	 * @return A boolean.
	 */
	public boolean isNodeChild(TreeNode node) {
		if (node == null)
			return false;

		return node.getParent() == this;
	}

	/**
	 * Returns the next child after the specified <code>node</code>, or
	 * <code>null</code> if there is no child after the specified
	 * <code>node</code>.
	 *
	 * @param node
	 *            a child of this node (<code>null</code> not permitted).
	 *
	 * @return The next child, or <code>null</code>.
	 * 
	 * @throws IllegalArgumentException
	 *             if <code>node</code> is not a child of this node, or is
	 *             <code>null</code>.
	 */
	public TreeNode getChildAfter(TreeNode node) {
		if (node == null || node.getParent() != this)
			throw new IllegalArgumentException();

		int index = getIndex(node) + 1;

		if (index == getChildCount())
			return null;

		return getChildAt(index);
	}

	/**
	 * Returns the previous child before the specified <code>node</code>, or
	 * <code>null</code> if there is no child before the specified
	 * <code>node</code>.
	 *
	 * @param node
	 *            a child of this node (<code>null</code> not permitted).
	 *
	 * @return The previous child, or <code>null</code>.
	 * 
	 * @throws IllegalArgumentException
	 *             if <code>node</code> is not a child of this node, or is
	 *             <code>null</code>.
	 */
	public TreeNode getChildBefore(TreeNode node) {
		if (node == null || node.getParent() != this)
			throw new IllegalArgumentException();

		int index = getIndex(node) - 1;

		if (index < 0)
			return null;

		return getChildAt(index);
	}

	/**
	 * Returns <code>true</code> if this tree node and <code>node</code> share
	 * the same parent. If <code>node</code> is this tree node, the method
	 * returns <code>true</code> and if <code>node</code> is <code>null</code>
	 * this method returns <code>false</code>.
	 *
	 * @param node
	 *            the node (<code>null</code> permitted).
	 *
	 * @return A boolean.
	 */
	public boolean isNodeSibling(TreeNode node) {
		if (node == null)
			return false;
		if (node == this)
			return true;
		return node.getParent() == getParent() && getParent() != null;
	}

	/**
	 * Returns the number of siblings for this tree node. If the tree node has a
	 * parent, this method returns the child count for the parent, otherwise it
	 * returns <code>1</code>.
	 *
	 * @return The sibling count.
	 */
	public int getSiblingCount() {
		if (parent == null)
			return 1;

		return parent.getChildCount();
	}

	/**
	 * Returns the next sibling for this tree node. If this node has no parent,
	 * or this node is the last child of its parent, this method returns
	 * <code>null</code>.
	 *
	 * @return The next sibling, or <code>null</code>.
	 */
	public PrimitiveMutableTreeNode getNextSibling() {
		if (parent == null)
			return null;

		int index = parent.getIndex(this) + 1;

		if (index == parent.getChildCount())
			return null;

		return (PrimitiveMutableTreeNode) parent.getChildAt(index);
	}

	/**
	 * Returns the previous sibling for this tree node. If this node has no
	 * parent, or this node is the first child of its parent, this method
	 * returns <code>null</code>.
	 *
	 * @return The previous sibling, or <code>null</code>.
	 */
	public PrimitiveMutableTreeNode getPreviousSibling() {
		if (parent == null)
			return null;

		int index = parent.getIndex(this) - 1;

		if (index < 0)
			return null;

		return (PrimitiveMutableTreeNode) parent.getChildAt(index);
	}

	/**
	 * Returns the first leaf node that is a descendant of this node. Recall
	 * that a node is its own descendant, so if this node has no children then
	 * it is returned as the first leaf.
	 *
	 * @return The first leaf node.
	 */
	public PrimitiveMutableTreeNode getFirstLeaf() {
		TreeNode current = this;

		while (current.getChildCount() > 0)
			current = current.getChildAt(0);

		return (PrimitiveMutableTreeNode) current;
	}

	/**
	 * Returns the last leaf node that is a descendant of this node. Recall that
	 * a node is its own descendant, so if this node has no children then it is
	 * returned as the last leaf.
	 *
	 * @return The first leaf node.
	 */
	public PrimitiveMutableTreeNode getLastLeaf() {
		TreeNode current = this;
		int size = current.getChildCount();

		while (size > 0) {
			current = current.getChildAt(size - 1);
			size = current.getChildCount();
		}

		return (PrimitiveMutableTreeNode) current;
	}

	/**
	 * Returns the next leaf node after this tree node.
	 *
	 * @return The next leaf node, or <code>null</code>.
	 */
	public PrimitiveMutableTreeNode getNextLeaf() {
		// if there is a next sibling, return its first leaf
		PrimitiveMutableTreeNode sibling = getNextSibling();
		if (sibling != null)
			return sibling.getFirstLeaf();
		// otherwise move up one level and try again...
		if (parent != null)
			return ((PrimitiveMutableTreeNode) parent).getNextLeaf();
		return null;
	}

	/**
	 * Returns the previous leaf node before this tree node.
	 *
	 * @return The previous leaf node, or <code>null</code>.
	 */
	public PrimitiveMutableTreeNode getPreviousLeaf() {
		// if there is a previous sibling, return its last leaf
		PrimitiveMutableTreeNode sibling = getPreviousSibling();
		if (sibling != null)
			return sibling.getLastLeaf();
		// otherwise move up one level and try again...
		if (parent != null)
			return ((PrimitiveMutableTreeNode) parent).getPreviousLeaf();
		return null;
	}
}