package br.com.pereiraeng.graph;

import java.util.Set;

/**
 * Interface que indica que um dado objeto pode ser representado topologicamente
 * como um subgrafo que se interliga diversos vértices (seria como se fosse uma
 * 'super-aresta' que ao invés de se ligar a dois vértices, liga-se a vários,
 * segundo uma dada topologia)
 * <p>
 * 
 * Ver mais detalhes em
 * <a href="https://en.wikipedia.org/wiki/Hypergraph">Hipergrafo</a>.
 * 
 * @author Philipe PEREIRA
 *
 */
public interface Hyperedge extends Edge {

	/**
	 * Retorna as arestas que partem de um vértice de fronteira
	 * 
	 * @param v vértice da super-aresta que são acessíveis
	 * @return conjunto de arestas simples internas
	 */
	public Set<Edge> getEdges(Vertex v);

	/**
	 * Função que retorna os vértices ligados pela hiperaresta
	 * 
	 * @return vértices
	 */
	public Set<? extends Vertex> getVertices();
}
