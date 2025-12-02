package br.com.pereiraeng.graph;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class HyperedgeObject extends EdgeObject implements Hyperedge {

	private Set<Vertex> other;

	private Set<Edge> innerEdges;

	public HyperedgeObject(Object obj, Vertex from, Vertex to, Vertex... other) {
		super(obj, from, to);
		this.other = new HashSet<Vertex>(Arrays.asList(other));
	}

	@Override
	public boolean contains(Vertex v) {
		return this.other.contains(v) || super.contains(v);
	}

	@Override
	public Set<Edge> getEdges(Vertex oposite) {
		Set<Edge> out = new HashSet<>();
		Set<? extends Vertex> allVertices = getVertices();
		for (Vertex v : allVertices) {
			Set<? extends Edge> es = v.getEdges();
			for (Edge e : es)
				if (!this.innerEdges.contains(e))
					out.add(e);
		}
		return out;
	}

	public Set<Edge> getAllEdges() {
		Set<Edge> out = new HashSet<>();
		Set<? extends Vertex> allVertices = getVertices();
		for (Vertex v : allVertices) {
			Set<? extends Edge> es = v.getEdges();
			for (Edge e : es)
				if (!this.innerEdges.contains(e))
					out.add(e);
		}
		return out;
	}

	@Override
	public Set<? extends Vertex> getVertices() {
		Set<Vertex> out = new HashSet<>(other);
		out.add(super.getFrom());
		out.add(super.getTo());
		return null;
	}
}
