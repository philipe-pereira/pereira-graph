package br.com.pereiraeng.graph;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import br.com.pereiraeng.graph.numbered.EdgeN;
import br.com.pereiraeng.graph.numbered.EdgeObjectN;
import br.com.pereiraeng.graph.numbered.GraphN;
import br.com.pereiraeng.math.DuplaEmeio;

/**
 * Classe do objeto do grafo padrão
 * 
 * @author Philipe PEREIRA
 * @version September 14h, 2020
 *
 */
public class Graph extends GraphN {

	public Graph() {
		this(new HashMap<DuplaEmeio, EdgeObjectN>());
	}

	/**
	 * Construtor do grafo
	 * 
	 * @param es arestas do grafo
	 */
	public Graph(Map<DuplaEmeio, EdgeObjectN> es) {
		super.setEns(es.values());
	}

	public Graph(Collection<EdgeObjectN> ens) {
		super.setEns(ens);
	}

	public Map<DuplaEmeio, EdgeObjectN> getEs() {
		Map<DuplaEmeio, EdgeObjectN> es = new HashMap<>();
		for (Entry<DuplaEmeio, ? extends EdgeN> e : super.ens.entrySet())
			es.put(e.getKey(), (EdgeObjectN) e.getValue());
		return es;
	}
}
