package br.com.pereiraeng.graph.numbered;

import br.com.pereiraeng.graph.EdgeObject;
import br.com.pereiraeng.graph.Vertex;
import br.com.pereiraeng.math.DuplaEmeio;

/**
 * Objeto que representa uma aresta de um dado grafo
 * 
 * @author Philipe PEREIRA
 *
 */
public class EdgeObjectN extends EdgeObject implements EdgeN {

	public EdgeObjectN(DuplaEmeio num, Vertex from) {
		this(num, from, null);
	}

	public EdgeObjectN(DuplaEmeio num, Vertex from, Vertex to) {
		super(num, from, to);
	}

	@Override
	public DuplaEmeio getNums() {
		return (DuplaEmeio) getUserObject();
	}

}
