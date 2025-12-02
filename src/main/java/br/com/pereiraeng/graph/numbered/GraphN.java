package br.com.pereiraeng.graph.numbered;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import br.com.pereiraeng.graph.EdgeObject;
import br.com.pereiraeng.graph.GraphUtils;
import br.com.pereiraeng.graph.Vertex;
import br.com.pereiraeng.graph.VertexObject;
import br.com.pereiraeng.math.DuplaEmeio;

/**
 * Classe do objeto que representa um grafo numerado a partir do seu conjunto de
 * {@link DuplaEmeio arestas numeradas}
 * 
 * @author Philipe PEREIRA
 *
 */
public class GraphN {

	/**
	 * Conjunto de arestas (pode ter a ordenação que for)
	 */
	protected Map<DuplaEmeio, EdgeN> ens;

	protected Set<Integer> isolated;

	public GraphN() {
		this(new HashSet<DuplaEmeio>());
	}

	public int size() {
		return ens.size();
	}

	/**
	 * Construtor do grafo
	 * 
	 * @param ens arestas do grafo
	 */
	public GraphN(Set<DuplaEmeio> ens) {
		this.setEns(ens);
	}

	public GraphN(Collection<? extends EdgeN> ens) {
		HashSet<DuplaEmeio> es = new HashSet<>();
		for (EdgeN e : ens)
			es.add(e.getNums());
		this.setEns(es);
	}

	// -------------- getter's n setter's --------------

	protected void setEns(Set<DuplaEmeio> ens) {
		if (ens == null)
			new IllegalArgumentException("Grafo não pode ser nulo");
		this.ens = new HashMap<DuplaEmeio, EdgeN>();
		this.isolated = new HashSet<>();
		for (DuplaEmeio d : ens)
			add(d);
	}

	public void setEns(Collection<? extends EdgeN> ensf) {
		HashMap<DuplaEmeio, EdgeN> ens = new HashMap<>();
		for (EdgeN e : ensf)
			ens.put(e.getNums(), e);
		this.ens = ens;
	}

	public Set<DuplaEmeio> getEns() {
		return this.ens.keySet();
	}

	public void add(DuplaEmeio e) {
		this.ens.put(e, null);
		this.isolated.remove(e.get1());
		this.isolated.remove(e.get2());
	}

	public void add(int num) {
		if (!contains(num))
			this.isolated.add(num);
	}

	public Set<Integer> getIsolated() {
		return this.isolated;
	}

	public void clear() {
		this.ens.clear();
		this.isolated.clear();
	}

	public boolean contains(int num) {
		if (this.isolated.contains(num))
			return true;
		for (DuplaEmeio e : this.ens.keySet())
			if (e.get1() == num || e.get2() == num)
				return true;
		return false;
	}

	public Set<Integer> getVs() {
		Set<Integer> out = new HashSet<>();
		for (DuplaEmeio e : this.ens.keySet()) {
			out.add(e.get1());
			out.add(e.get2());
		}
		out.addAll(this.isolated);
		return out;
	}

	public Set<Integer> getOrderedVs() {
		Set<Integer> out = new LinkedHashSet<>();
		// TODO tem de ter critério de ordenação (i.e., Fronteira-Ilha-Radial)
		return out;
	}

	// ---------------- AUXILIAR ----------------

	public static Collection<Vertex> filter(Collection<? extends VertexN> vns, GraphN sg) {
		Collection<Vertex> out = new HashSet<>();
		for (VertexN vn : vns)
			if (sg.contains(vn.getNum()))
				out.add(vn);
		return out;
	}

	/**
	 * Gera um grafo usando {@link VertexObject vértices} e {@link EdgeObject
	 * arestas} padrão a partir do grafo numerado. O
	 * {@link VertexObject#getUserObject() objeto guardado nos vértices} é seu
	 * {@link VertexN#getNum() número} e na {@link EdgeObject#getUserObject() objeto
	 * guardado nas arestas} é a sua {@link EdgeN#getNums() trinca}. É a função
	 * inversa de {@link GraphN#toGraphN(Set)}.
	 * 
	 * @param g grafo numerado
	 * @return tabela de dispersão que associa para cada inteiro um nó
	 */
	public static Map<Integer, Vertex> toGraphObject(GraphN g) {
		Map<Integer, Vertex> out = new HashMap<>();

		Set<DuplaEmeio> ens = g.getEns();
		for (DuplaEmeio e : ens) {
			VertexObject v1 = (VertexObject) out.get(e.get1());
			if (v1 == null)
				v1 = new VertexObject(e.get1());
			VertexObject v2 = (VertexObject) out.get(e.get2());
			if (v2 == null)
				v2 = new VertexObject(e.get2());

			EdgeObject ed = new EdgeObject(e.get3(), v1, v2);
			v1.add(ed);
			v2.add(ed); // TODO um dia fazer isso auto

			out.put(e.get1(), v1);
			out.put(e.get2(), v2);
		}

		for (Integer i : g.isolated)
			out.put(i, new VertexObject(i));

		return out;
	}

	/**
	 * Gera um grafo numerado a partir a partir de seus {@link VertexObject
	 * vértices} padrão. O {@link VertexObject#getUserObject() objeto guardado nos
	 * vértices} deve ser seu {@link VertexN#getNum() número} no grafo. É a função
	 * inversa de {@link GraphN#toGraphObject(GraphN)}.
	 * 
	 * @param vs grafo descrito pelo seu conjunto de vértice, contendo como
	 *           {@link VertexObject#getUserObject() objeto} o inteiro relativo à
	 *           sua {@link VertexN#getNum() numeração} no grafo
	 * @return grafo numerado
	 */
	public static GraphN toGraphN(Set<Vertex> vs) {
		Set<DuplaEmeio> ds = EdgeN.toMap(GraphUtils.getEdges(vs)).keySet();
		GraphN out = new GraphN(ds);
		for (Vertex v : vs) { // procurar vértices isolad
			int num = -1;
			if (v instanceof VertexN) {
				VertexN vn = (VertexN) v;
				num = vn.getNum();
			} else if (v instanceof VertexObject) {
				VertexObject vo = (VertexObject) v;
				num = (Integer) vo.getUserObject();
			}
			if (!out.contains(num))
				out.add(num);
		}
		return out;
	}
}
