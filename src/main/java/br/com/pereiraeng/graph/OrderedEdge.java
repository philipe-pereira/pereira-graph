package br.com.pereiraeng.graph;

/**
 * Interface que indica que um dado objeto pode ser representado topologicamente
 * como uma aresta, ligando dois {@link Vertex vértices}, havendo a definição de
 * uma direção preferencial, com um vértice sendo definido como o de
 * {@link #getFrom() partida} e outro como o de {@link #getTo() chegada}
 * 
 * @author Philipe PEREIRA
 *
 */
public interface OrderedEdge extends Edge {

	/**
	 * Função que retorna o vértice de partida
	 * 
	 * @return vértice de partida
	 */
	public Vertex getFrom();

	/**
	 * Função que retorna o vértice de chegada
	 * 
	 * @return vértice de chegada
	 */
	public Vertex getTo();

}
