package br.com.pereiraeng.graph.tree;

import java.util.Collections;
import java.util.Enumeration;
import java.util.NoSuchElementException;

import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;

public abstract class AbstractLeafNode extends PrimitiveMutableTreeNode {

	/**
	 * Adds a new child node to this node and sets this node as the parent of*
	 * the child node.The child node must not be an ancestor of this node.* If
	 * the tree uses the {@link DefaultTreeModel}, you must subsequently*call
	 * {@link DefaultTreeModel#reload(TreeNode)}.**
	 * 
	 * @param child
	 *            * the child node (<code>null</code> not permitted).
	 *
	 * @throws IllegalStateException
	 *             if {@link #getAllowsChildren()} returns <code>false</code>.
	 * @throws IllegalArgumentException
	 *             if {@link #isNodeAncestor} returns <code>true</code>.
	 * @throws IllegalArgumentException
	 *             if <code>child</code> is <code>null</code>.
	 */
	public void add(MutableTreeNode child) {
		throw new IllegalStateException();
	}

	/**
	 * Removes the child with the given index from this node.
	 *
	 * @param index
	 *            the index (in the range <code>0</code> to
	 *            <code>getChildCount() - 1</code>).
	 * 
	 * @throws ArrayIndexOutOfBoundsException
	 *             if <code>index</code> is outside the valid range.
	 */
	@Override
	public void remove(int index) {
		new ArrayIndexOutOfBoundsException();
	}

	/**
	 * Removes the given child from this node and sets its parent to
	 * <code>null</code>.
	 *
	 * @param node
	 *            the child node (<code>null</code> not permitted).
	 * 
	 * @throws IllegalArgumentException
	 *             if <code>node</code> is not a child of this node.
	 * @throws IllegalArgumentException
	 *             if <code>node</code> is null.
	 */
	@Override
	public void remove(MutableTreeNode node) {
		new IllegalArgumentException("The given 'node' is not a child of this node.");
	}

	/**
	 * Inserts given child node at the given index.
	 *
	 * @param node
	 *            the child node (<code>null</code> not permitted).
	 * @param index
	 *            the index.
	 * 
	 * @throws IllegalArgumentException
	 *             if <code>node</code> is </code>null</code>.
	 */
	@Override
	public void insert(MutableTreeNode node, int index) {
		new IllegalStateException();
	}

	/**
	 * Returns an enumeration containing all children of this node.
	 * <code>EMPTY_ENUMERATION</code> is returned if this node has no children.
	 *
	 * @return an enumeration of tree nodes
	 */
	@Override
	public Enumeration<MutableTreeNode> children() {
		return Collections.emptyEnumeration();
	}

	/**
	 * Returns the child node at a given index.
	 *
	 * @param index
	 *            the index
	 *
	 * @return the child node
	 */
	@Override
	public TreeNode getChildAt(int index) {
		return null;
	}

	/**
	 * Returns the number of children of this node.
	 *
	 * @return the number of children
	 */
	@Override
	public int getChildCount() {
		return 0;
	}

	/**
	 * Returns the index of the specified child node, or -1 if the node is not
	 * in fact a child of this node.
	 * 
	 * @param node
	 *            the node (<code>null</code> not permitted).
	 * 
	 * @return The index of the specified child node, or -1.
	 * 
	 * @throws IllegalArgumentException
	 *             if <code>node</code> is <code>null</code>.
	 */
	@Override
	public int getIndex(TreeNode node) {
		return -1;
	}

	@Override
	public boolean getAllowsChildren() {
		return false;
	}

	/**
	 * getDepth
	 *
	 * @return int
	 */
	public int getDepth() {
		return 0;
	}

	/**
	 * Returns the first child node belonging to this tree node.
	 *
	 * @return The first child node.
	 * 
	 * @throws NoSuchElementException
	 *             if this tree node has no children.
	 */
	public TreeNode getFirstChild() {
		return null;
	}

	/**
	 * Returns the last child node belonging to this tree node.
	 *
	 * @return The last child node.
	 * 
	 * @throws NoSuchElementException
	 *             if this tree node has no children.
	 */
	public TreeNode getLastChild() {
		return null;
	}

	/**
	 * Returns <code>true</code> if this tree node is a lead node (that is, it
	 * has no children), and <code>false</otherwise>.
	 *
	 * @return A boolean.
	 */
	@Override
	public boolean isLeaf() {
		return true;
	}

	/**
	 * getLeafCount
	 *
	 * @return int
	 */
	public int getLeafCount() {
		return 1;
	}
}