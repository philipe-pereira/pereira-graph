package br.com.pereiraeng.graph;

/**
 * Interface que indica que um dado objeto pode ser representado topologicamente
 * como uma aresta, ligando dois {@link Vertex vértices}
 * 
 * @author Philipe PEREIRA
 *
 */
public interface Edge extends GraphElement {

	/**
	 * Função que retorna o vértice ligado por esta aresta que não é aquele
	 * indicado como argumento
	 * 
	 * @return um dos vértice da aresta que não é aquele passado como argumento
	 */
	public Vertex getOpposite(Vertex v);

	/**
	 * Função que indica se uma dada aresta liga um dado vértice ou não
	 * 
	 * @param v
	 *            vértice a ser analisado
	 * @return <code>true</code> esta aresta parte do dado vértice,
	 *         <code>false</code> senão
	 */
	public boolean contains(Vertex v);
}
