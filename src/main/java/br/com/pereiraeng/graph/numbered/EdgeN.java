package br.com.pereiraeng.graph.numbered;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import br.com.pereiraeng.graph.Edge;
import br.com.pereiraeng.graph.EdgeObject;
import br.com.pereiraeng.graph.OrderedEdge;
import br.com.pereiraeng.graph.Vertex;
import br.com.pereiraeng.graph.VertexObject;
import br.com.pereiraeng.math.DuplaEmeio;

/**
 * Aresta numerada
 * 
 * @author Philipe PEREIRA
 *
 */
public interface EdgeN extends OrderedEdge {
	public DuplaEmeio getNums();

	/**
	 * Função que retorna uma tabela que associa as {@link DuplaEmeio coordenadas da
	 * aresta} para cada aresta a partir de arestas que não são numeradas a priori.
	 * Estes grafos estão descritos pelas suas relações de arestas e as arestas não
	 * numeradas devem ser do tipo {@link EdgeN} ou {@link EdgeObject} (neste último
	 * caso, com o {@link EdgeObject#getUserObject() objeto} designando o número do
	 * circuito).
	 * 
	 * @param es arestas
	 * @return tabela que associa as coordenadas da aresta para cada aresta
	 */
	public static Map<DuplaEmeio, Edge> toMap(Collection<Edge> es) {
		Map<DuplaEmeio, Edge> out = new HashMap<>(es.size(), 1f);
		for (Edge e : es) {
			DuplaEmeio d = null;
			if (e instanceof EdgeN) {
				EdgeN en = (EdgeN) e;
				d = en.getNums();
			} else if (e instanceof EdgeObject) {
				EdgeObject eo = (EdgeObject) e;
				Integer vd = (Integer) ((VertexObject) eo.getFrom()).getUserObject();
				Integer vp = (Integer) ((VertexObject) eo.getTo()).getUserObject();
				int circ = (int) eo.getUserObject();
				d = new DuplaEmeio(vd, vp, circ);
			}
			if (d != null)
				out.put(d, e);
		}
		return out;
	}

	/**
	 * Função que converte a lista de vértices de um grafo em duas tabelas, um que
	 * associa para cada número inteiro ( {@link VertexN#getNum() chave do vértice}
	 * ) o vértice correspondente e outra que associa para cada três números
	 * inteiros ( {@link EdgeN#getNums() chave da aresta } a {@link OrderedEdge
	 * aresta ordenada} correspondente
	 * 
	 * @param graph grafo
	 * @param n2v   tabela de dispersão a ser preenchida que associa um número
	 *              inteiro ( {@link VertexN#getNum() chave do vértice} ) ao seu
	 *              vértice correspondente
	 * @param d2e   tabela de dispersão a ser preenchida que associa três números
	 *              inteiros ( {@link EdgeN#getNums() chave da aresta } a
	 *              {@link OrderedEdge aresta ordenada} correspondente
	 */
	public static void toMaps(Collection<Vertex> graph, Map<Integer, Vertex> n2v, Map<DuplaEmeio, OrderedEdge> d2e) {
		for (Vertex v : graph) {
			Integer k = null;
			if (v instanceof VertexN) { // prioridade é VertexN
				VertexN vn = (VertexN) v;
				n2v.put(vn.getNum(), vn);
			} else if (v instanceof VertexObject) {
				VertexObject vo = (VertexObject) v;
				k = (Integer) (vo.getUserObject());
			}

			n2v.put(k, v);
			for (Edge e : v.getEdges()) {
				DuplaEmeio d = null;
				if (e instanceof EdgeN) { // prioridade é EdgeN
					EdgeN en = (EdgeN) e;
					d = en.getNums();
				} else if (e instanceof EdgeObject) {
					EdgeObject eo = (EdgeObject) e;
					Integer vd = (Integer) ((VertexObject) eo.getFrom()).getUserObject();
					Integer vp = (Integer) ((VertexObject) eo.getTo()).getUserObject();
					int circ = (int) eo.getUserObject();
					d = new DuplaEmeio(vd, vp, circ);
				}
				d2e.put(d, (OrderedEdge) e);
			}
		}
	}
}
