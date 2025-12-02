package br.com.pereiraeng.graph.numbered;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import br.com.pereiraeng.graph.Vertex;
import br.com.pereiraeng.graph.VertexObject;

public interface VertexN extends Vertex {

	/**
	 * Função que retorna o número do vértice dentro da numeração do grafo
	 * 
	 * @return número do vértice
	 */
	public int getNum();

	public static VertexN get(Collection<? extends VertexN> buses, int num) {
		for (VertexN vn : buses)
			if (vn.getNum() == num)
				return vn;
		return null;
	}

	/**
	 * Função que retorna uma tabela que associa as {@link VertexN#getNum()
	 * coordenadas do vértice} para cada vértice a partir de vértices que não são
	 * numerados a priori. Estes grafos estão descritos pelas suas relações de
	 * vértices e os vértices não numerados devem ser do tipo {@link VertexN} ou
	 * {@link VertexObject} (neste último caso, com o
	 * {@link VertexObject#getUserObject() objeto} designando o número do nó).
	 * 
	 * @param vs vértices
	 * @return tabela que associa as coordenadas do vértice para cada vértice
	 */
	public static Map<Integer, Vertex> toMap(Collection<? extends Vertex> vs) {
		Map<Integer, Vertex> out = new HashMap<>(vs.size(), 1f);
		for (Vertex v : vs) {
			if (v instanceof VertexN) { // prioridade é VertexN
				VertexN vn = (VertexN) v;
				out.put(vn.getNum(), vn);
			} else if (v instanceof VertexObject) {
				VertexObject vo = (VertexObject) v;
				out.put((Integer) (vo.getUserObject()), vo);
			}
		}
		return out;
	}
}
