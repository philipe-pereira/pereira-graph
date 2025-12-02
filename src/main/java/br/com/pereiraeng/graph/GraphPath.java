package br.com.pereiraeng.graph;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.LinkedList;

import br.com.pereiraeng.core.collections.ListUtils;

/**
 * Função que designa um conjunto de arestas que possuem vértices em comum,
 * formando assim um caminho num grafo
 * 
 * @author Philipe PEREIRA
 *
 */
public class GraphPath extends LinkedHashSet<Edge> {
	private static final long serialVersionUID = 1L;

	private Vertex start;

	public GraphPath(Vertex start) {
		this.start = start;
	}

	public GraphPath(GraphPath gp) {
		super(gp);
		this.start = gp.getStart();
	}

	public Vertex getStart() {
		return start;
	}

	public Vertex getEnd() {
		Vertex v = start;
		for (Edge e : this)
			v = e.getOpposite(v);
		return v;
	}

	public Edge getEdge(int pos) {
		return ListUtils.getElementAt(this, pos);
	}

	public boolean isCycle() {
		Vertex v = start;
		for (Edge e : this)
			v = e.getOpposite(v);
		return start.equals(v);
	}

	public Collection<? extends Vertex> getVertexes() {
		LinkedList<Vertex> out = new LinkedList<>();
		Vertex v = start;
		out.add(v);
		for (Edge e : this) {
			v = e.getOpposite(v);
			out.add(v);
		}
		return out;
	}

	public boolean visited(Vertex v) {
		for (Edge e : this)
			if (e.contains(v))
				return true;
		return false;
	}

	@Override
	public boolean remove(Object obj) {
		boolean b = super.remove(obj);
		if (b) {
			Edge e = (Edge) obj;
			Vertex v = e.getOpposite(start);
			if (v != null)
				start = v;
		}
		return b;
	}

	@Override
	public String toString() {
		Vertex v = start;
		String out = v.toString();
		for (Edge e : this) {
			Vertex next = e.getOpposite(v);
			out += "->" + next.toString();
			v = next;
		}
		return out;
	}
}
