package br.com.pereiraeng.graph.tree;

import java.util.Arrays;
import java.util.Enumeration;
import java.util.NoSuchElementException;

import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;

/**
 * Classe do objeto do nó de uma árvore binária
 * 
 * @author Philipe Pereira
 *
 */
public class BinaryNode implements MutableTreeNode {

	/**
	 * userObject
	 */
	protected transient Object userObject;

	/**
	 * The parent of this node (possibly <code>null</code>).
	 */
	protected BinaryNode parent;

	/**
	 * The left child node for this node (may be empty).
	 */
	protected BinaryNode left;

	/**
	 * The right child node for this node (may be empty).
	 */
	protected BinaryNode right;

	/**
	 * Creates a <code>BinaryNode</code> object with the given user object attached
	 * to it.
	 *
	 * @param userObject the user object (<code>null</code> permitted).
	 */
	public BinaryNode(Object userObject) {
		this.userObject = userObject;
	}

	@Override
	public Enumeration<? extends TreeNode> children() {
		return new Enumeration<TreeNode>() {

			private int i = 0;

			@Override
			public boolean hasMoreElements() {
				return i < getChildCount();
			}

			@Override
			public TreeNode nextElement() {
				if (i >= getChildCount())
					throw new NoSuchElementException();
				return i++ == 0 ? (left == null ? right : left) : right;
			}
		};
	}

	@Override
	public boolean getAllowsChildren() {
		return true;
	}

	@Override
	public TreeNode getChildAt(int childIndex) {
		return childIndex == 0 ? left : (childIndex == 1 ? right : null);
	}

	@Override
	public int getChildCount() {
		return (left == null ? 0 : 1) + (right == null ? 0 : 1);
	}

	@Override
	public int getIndex(TreeNode node) {
		if (node == null)
			throw new IllegalArgumentException("Null 'node' argument.");
		return node == left ? 0 : (node == right ? 1 : -1);
	}

	@Override
	public TreeNode getParent() {
		return parent;
	}

	@Override
	public boolean isLeaf() {
		return left == null && right == null;
	}

	@Override
	public void insert(MutableTreeNode child, int index) {
		if (index == 0) {
			if (left != null)
				left.setParent(null);
			left = (BinaryNode) child;
		} else if (index == 1) {
			if (right != null)
				right.setParent(null);
			right = (BinaryNode) child;
		}
		child.setParent(this);
	}

	@Override
	public void remove(int index) {
		MutableTreeNode child = null;
		if (index == 0) {
			child = left;
			left = null;
		} else if (index == 1) {
			child = right;
			right = null;
		}
		child.setParent(null);
	}

	@Override
	public void remove(MutableTreeNode node) {
		if (node == null)
			throw new IllegalArgumentException("Null 'node' argument.");
		if (node.getParent() != this)
			throw new IllegalArgumentException("The given 'node' is not a child of this node.");

		if (node == left)
			remove(0);
		else if (node == right)
			remove(1);
	}

	@Override
	public void removeFromParent() {
		parent.remove(this);
		parent = null;
	}

	@Override
	public void setParent(MutableTreeNode newParent) {
		parent = (BinaryNode) newParent;
	}

	@Override
	public void setUserObject(Object object) {
		this.userObject = object;
	}

	/**
	 * Returns the user object attached to this node. <code>null</code> is returned
	 * when no user object is set.
	 * 
	 * @return the user object
	 */
	public Object getUserObject() {
		return userObject;
	}

	public BinaryNode getPredecessor() {
		// achar um ancestral deste nó que tem esquerda diferente
		BinaryNode child = this;
		BinaryNode parent = (BinaryNode) child.getParent();
		while (parent != null ? parent.left == child : false) {
			child = parent;
			parent = (BinaryNode) child.getParent();
		}
		if (parent == null)
			return null;
		else
			child = parent.left;

		// descer pela direita até achar uma folha
		while (!child.isLeaf())
			child = child.right;
		return child;
	}

	public BinaryNode getSuccessor() {
		// achar um ancestral deste nó que tem direita
		BinaryNode child = this;
		BinaryNode parent = (BinaryNode) child.getParent();
		while (parent != null ? parent.right == child : false) {
			child = parent;
			parent = (BinaryNode) child.getParent();
		}
		if (parent == null)
			return null;
		else
			child = parent.right;

		// descer pela esquerda até achar uma folha
		while (!child.isLeaf())
			child = child.left;
		return child;
	}

	public BinaryNode getCommonParent() {
		BinaryNode child = this;
		BinaryNode parent = (BinaryNode) child.getParent();
		int pos = parent.getIndex(child);

		while (parent != null ? (pos == 0 ? parent.left : parent.right) == child : false) {
			child = parent;
			parent = (BinaryNode) child.getParent();
		}

		return parent;
	}

	@Override
	public String toString() {
		return this.userObject instanceof Object[] ? Arrays.toString((Object[]) this.userObject)
				: this.userObject.toString();
	}
}
