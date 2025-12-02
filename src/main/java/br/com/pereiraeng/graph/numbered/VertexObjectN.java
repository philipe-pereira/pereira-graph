package br.com.pereiraeng.graph.numbered;

import br.com.pereiraeng.graph.VertexObject;

/**
 * Objeto que representa um vértice de um dado grafo
 * 
 * @author Philipe PEREIRA
 *
 */
public class VertexObjectN extends VertexObject implements VertexN {
	private static final long serialVersionUID = 8097600629933400737L;

	public VertexObjectN(int num) {
		super(num);
	}

	@Override
	public int getNum() {
		return (int) getUserObject();
	}
}
