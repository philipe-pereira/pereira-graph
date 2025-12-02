package br.com.pereiraeng.graph;

import java.util.Arrays;

import br.com.pereiraeng.math.Vec;

/**
 * Objeto que representa uma aresta de um dado grafo
 * 
 * @author Philipe PEREIRA
 *
 */
public class EdgeObject implements OrderedEdge {

	private Object obj;

	private Vertex from;

	private Vertex to;

	/**
	 * Função que cria uma aresta a partir somente de um de seus vértices. É
	 * importante que o outro vértice seja adicionado o mais breve possível no
	 * código de modo que os algoritmos que associados ao grafo não retornem
	 * exceções ({@link VertexObject#VertexObject(Object, EdgeObject...) um dos
	 * construtores} da classe que representa os vértices é capaz de já preencher
	 * essa lacuna com o vértice criado).
	 * 
	 * @param obj  objeto da aresta
	 * @param from um dos vértices
	 */
	public EdgeObject(Object obj, Vertex from) {
		this(obj, from, null);
	}

	/**
	 * Função que cria uma aresta a partir de seus dois vértices e de seu objeto
	 * 
	 * @param obj  objeto da aresta
	 * @param from um dos vértices
	 * @param to   o outro vértice
	 */
	public EdgeObject(Object obj, Vertex from, Vertex to) {
		this.obj = obj;
		this.setFrom(from);
		this.setTo(to);
	}

	// ---------------- getter n' setters ----------------

	@Override
	public Vertex getFrom() {
		return from;
	}

	public void setFrom(Vertex from) {
		this.from = from;
		// TODO se for VertexObject, from.add(this)
	}

	@Override
	public Vertex getTo() {
		return to;
	}

	/**
	 * Função que define um dos vértices da aresta
	 * 
	 * @param to vértice a ser adicionado
	 */
	public void setTo(Vertex to) {
		this.to = to;
		// TODO se for VertexObject, from.add(this)
	}

	public Object getUserObject() {
		return obj;
	}

	public void setUserObject(Object obj) {
		this.obj = obj;
	}

	@Override
	public String toString() {
		String out = "";// to.toString() + "<->" + from.toString();
		if (obj != null) {
			if (obj.getClass().isArray()) {
				// para não poluir muito, só o primeiro elemento (convenciona-se que seja o
				// número desambiguador de circuitos) e o segundo (se houver, o nome)
				Object[] objs = (Object[]) obj;

				out += "(";
				if (objs != null) {
					if (objs[0] != null)
						out += objs[0].toString();
					if (objs.length > 1 ? objs[1] != null : false)
						out += ";" + objs[1].toString();
				}
				out += ")";
			} else
				out += "(" + obj.toString() + ")";
		}
		return out;
	}

	/**
	 * Função que substitui um dos vértices da extremidade desta aresta por outro
	 * vértice
	 * 
	 * @param oldV vértice a ser substituído
	 * @param newV novo vértice
	 */
	public void replaceVertex(Vertex oldV, Vertex newV) {
		if (oldV.equals(to))
			this.to = newV;
		else if (oldV.equals(from))
			this.from = newV;
	}

	@Override
	public boolean equals(Object anObject) {
		if (this == anObject)
			return true;
		if (anObject instanceof EdgeObject) {
			EdgeObject eo = (EdgeObject) anObject;

			Object obj1 = eo.getUserObject();
			Object obj2 = this.getUserObject();

			boolean content;
			if (obj1 == null && obj2 == null)
				content = true;
			else if (obj1.getClass().isArray() && obj2.getClass().isArray())
				content = Arrays.deepEquals((Object[]) obj1, (Object[]) obj2);
			else
				content = obj1.equals(obj2);

			// se o conteúdo da aresta E os seus vértices forem os mesmos, então
			// são iguais
			return content && Vec.equalBinary(this.getTo(), this.getFrom(), eo.getTo(), eo.getFrom());
		}
		return false;
	}

	@Override
	public int hashCode() {
		int hash = this.getFrom().hashCode() + this.getFrom().hashCode();
		Object obj = this.getUserObject();
		if (obj != null) {
			if (obj.getClass().isArray()) {
				Object[] array = (Object[]) obj;
				// TODO java.lang.ClassCastException: [F cannot be cast to [Ljava.lang.Object;
				// TODO java.lang.ClassCastException: [I cannot be cast to [Ljava.lang.Object;
				for (int i = 0; i < array.length; i++)
					if (array[i] != null)
						hash += array[i].hashCode();
			} else
				hash += obj.hashCode();
		}
		return hash;
	}

	// ---------------- INTERFACE EDGE ----------------

	@Override
	public Vertex getOpposite(Vertex v) {
		return from != null ? (from.equals(v) ? to : from) : (to.equals(v) ? from : to);
	}

	@Override
	public boolean contains(Vertex v) {
		if (v == null)
			return false;
		else
			return v.equals(from) || v.equals(to);
	}
}
