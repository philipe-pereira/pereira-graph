package br.com.pereiraeng.graph;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Classe dos objetos que representam um corte no grafo (i.e., operação que
 * remove arestas de modo a criar duas ou mais subgrafos)
 * 
 * @author Philipe PEREIRA
 *
 */
public class Cut extends HashMap<Edge, Vertex> {
	private static final long serialVersionUID = 8200478441292885758L;

	/**
	 * Função que retorna os corte de grafo obtidos a partir da subtração de um
	 * subgrafo. É a função equivalente à
	 * {@link outils.mat.gph.gphn.CutN#getOrderedCuts(outils.mat.gph.gphn.GraphN, Set)
	 * esta}, utilizada para grafos numerados.
	 * 
	 * @param graph
	 *            grafo
	 * @param outter
	 *            subgrafo a ser removido
	 * @return tabela de dispersão que associa para cada {@link Cut corte} os
	 *         subgrafos resultantes da subtração. Os vértices do corte são
	 *         aqueles que pertencem a um dos subgrafos resultantes
	 */
	public static Map<Cut, Collection<Vertex>> getOrderedCuts(Collection<? extends Vertex> graph, Set<Vertex> outter) {
		if (graph == null || outter == null)
			new IllegalArgumentException("nenhum dos args pode ser null");
		// 1) reunir as arestas do corte e orientá-las

		Collection<Vertex> inner = new HashSet<>();
		Map<Edge, Vertex> frs = new HashMap<>();
		{
			Set<Edge> all = new HashSet<>();
			for (Vertex v : graph) {
				Set<? extends Edge> es = v.getEdges();
				for (Edge e : es) {
					if (!all.contains(e)) {
						all.add(e);
						Vertex vo = e.getOpposite(v);

						boolean od = outter.contains(v);
						boolean op = outter.contains(vo);
						if (op ^ od)
							frs.put(e, od ? v : vo);
						else if (!op && !od) {
							inner.add(v);
							inner.add(vo);
						}
					}
				}
			}
		}

		// 2) analisar conectividade dos sub-grafos resultantes da subtração
		Map<Cut, Collection<Vertex>> out = new HashMap<>();

		while (frs.size() > 0) {
			Iterator<Entry<Edge, Vertex>> it = frs.entrySet().iterator();
			Entry<Edge, Vertex> t = it.next();
			it.remove();

			Vertex v = t.getValue();
			Cut c = new Cut();
			c.put(t.getKey(), v);

			Collection<Vertex> sg = GraphUtils.bfs(v);

			for (Entry<Edge, Vertex> t0 : frs.entrySet()) {
				Vertex iv = t0.getValue();
				if (sg.contains(iv))
					c.put(t0.getKey(), t0.getValue());
			}
			for (Edge e : c.keySet())
				frs.remove(e);

			out.put(c, sg);
		}
		return out;
	}
}
