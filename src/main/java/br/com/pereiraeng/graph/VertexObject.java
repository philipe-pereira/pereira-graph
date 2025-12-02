package br.com.pereiraeng.graph;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Objeto que representa um vértice de um dado grafo
 * 
 * @author Philipe PEREIRA
 *
 */
public class VertexObject extends HashSet<Edge> implements Vertex {
	private static final long serialVersionUID = 1L;

	protected Object obj;

	public VertexObject(Object obj) {
		this.obj = obj;
	}

	/**
	 * Função que cria um dado vértice e já adiciona algumas arestas, sendo que
	 * o vértice criado entrará como sendo o '{@link EdgeObject#setTo(Vertex)
	 * to}' das arestas adicionadas.
	 * 
	 * @param obj
	 *            objeto do vértice
	 * @param edges
	 *            arestas a serem adicionadas e que receberão este vértice
	 */
	public VertexObject(Object obj, EdgeObject... edges) {
		this(obj);
		for (EdgeObject e : edges) {
			if (e != null) {
				e.setTo(this);
				super.add(e);
			}
		}
	}

	// ---------------- edges operation ----------------

	@Override
	public boolean add(Edge edge) {
		if (edge.contains(this))
			return super.add(edge);
		else
			throw new IllegalArgumentException("Essa aresta não possui esse nó em uma de suas extremidades.");
	}

	/**
	 * Função que adiciona uma nova aresta ao grafo, que liga este vértice a um
	 * outro a ser criado
	 * 
	 * @param obj
	 *            objeto da nova aresta
	 * @param vertexObject
	 *            objeto do novo vértice
	 */
	public void add(Object obj, Object vertexObject) {
		this.add(obj, new VertexObject(vertexObject));
	}

	/**
	 * Função que adiciona uma nova aresta ao grafo, que liga este vértice a um
	 * outro
	 * 
	 * @param obj
	 *            objeto da nova aresta
	 * @param otherVertex
	 *            vértice que será ligado a este através da nova aresta
	 */
	public void add(Object obj, Vertex otherVertex) {
		super.add(new EdgeObject(obj, this, otherVertex));
	}

	// ---------------- getter n' setters ----------------

	public Object getUserObject() {
		return obj;
	}

	public void setUserObject(Object obj) {
		this.obj = obj;
	}

	@Override
	public String toString() {
		if (obj != null) {
			if (obj.getClass().isArray())
				// para não poluir muito, só o primeiro elemento
				return ((Object[]) obj)[0].toString();
			else
				return obj.toString();
		} else
			return super.toString();
	}

	@Override
	public boolean equals(Object anObject) {
		if (this == anObject)
			return true;
		if (anObject instanceof VertexObject) {
			VertexObject vo = (VertexObject) anObject;

			Object obj1 = vo.getUserObject();
			Object obj2 = this.getUserObject();
			if (obj1.getClass().isArray() && obj2.getClass().isArray())
				return Arrays.deepEquals((Object[]) obj1, (Object[]) obj2);
			return obj1.equals(obj2);
		}
		return false;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		Object obj = this.getUserObject();
		if (obj != null) {
			if (obj.getClass().isArray()) {
				Object[] array = (Object[]) obj;
				for (int i = 0; i < array.length; i++)
					if (array[i] != null)
						hash += array[i].hashCode();
			} else
				hash += obj.hashCode();
		} else
			hash = super.hashCode();
		return hash;
	}

	// ---------------- INTERFACE VertexObject ----------------

	@Override
	public Set<? extends Edge> getEdges() {
		return this;
	}

	@Override
	public void remove(Edge e) {
		super.remove(e);
	}
}
