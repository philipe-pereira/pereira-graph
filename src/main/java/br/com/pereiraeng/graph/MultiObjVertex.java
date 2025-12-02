package br.com.pereiraeng.graph;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import br.com.pereiraeng.math.DuplaEmeio;
import br.com.pereiraeng.core.DisplayableFields;

/**
 * Classe que representa o {@link VertexObject vértice de um grafo} que contém
 * um vetor de objetos
 * 
 * @author Philipe PEREIRA
 *
 */
public class MultiObjVertex extends VertexObject implements DisplayableFields {
	private static final long serialVersionUID = 1L;

	public MultiObjVertex(Object[] obj) {
		super(obj);
	}

	@Override
	public int getFieldCount() {
		return ((Object[]) getUserObject()).length;
	}

	@Override
	public String getFieldName(int index) {
		return String.valueOf(index + 1);
	}

	@Override
	public Object getField(int index) {
		return ((Object[]) getUserObject())[index];
	}

	@Override
	public String toString() {
		return getField(0).toString();
	}

	/**
	 * Função que cria um grafo cujos vértices e arestas contém vetores de objetos
	 * 
	 * @param vertices tabela de dispersão que associa para cada número do vértice
	 *                 seu vetor de objetos
	 * @param edges    tabela de dispersão que associa para cada {@link DuplaEmeio
	 *                 trinca} da aresta seu vetor de objetos
	 * @return conjunto de vértices que define o grafo (as referências das arestas
	 *         estão neles contidos)
	 */
	public static Collection<MultiObjVertex> getGraph(Map<Integer, Object[]> vertices,
			Map<DuplaEmeio, Object[]> edges) {
		Map<Integer, MultiObjVertex> out = new LinkedHashMap<>();

		for (Entry<Integer, Object[]> e : vertices.entrySet()) // vértices reais
			out.put(e.getKey(), new MultiObjVertex(e.getValue()));

		for (Entry<DuplaEmeio, Object[]> e : edges.entrySet()) {
			DuplaEmeio dem = e.getKey();
			Object[] eos = e.getValue();

			MultiObjVertex movD = out.get(dem.get1());
			MultiObjVertex movP = out.get(dem.get2());

			MultiObjEdge moe = new MultiObjEdge(eos, movD);
			moe.setTo(movP);
			movD.add(moe);
			movP.add(moe);
		}

		return out.values();
	}
}
