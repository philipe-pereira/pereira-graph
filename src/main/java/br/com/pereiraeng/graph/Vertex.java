package br.com.pereiraeng.graph;

import java.util.Set;

/**
 * Interface que indica que um dado objeto pode ser representado topologicamente
 * como um vértice
 * 
 * @author Philipe PEREIRA
 *
 */
public interface Vertex extends GraphElement {

	/**
	 * Função que retorna o conjunto de arestas que partem desse vértice
	 * 
	 * @return conjunto de arestas que possuem este vértice
	 */
	public Set<? extends Edge> getEdges();

	/**
	 * Função que remove uma aresta deste nó
	 * 
	 * @param e
	 *            aresta
	 */
	public void remove(Edge e);

}
