package br.com.pereiraeng.graph.numbered;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import br.com.pereiraeng.graph.GraphUtils;
import br.com.pereiraeng.graph.Vertex;
import br.com.pereiraeng.math.DuplaEmeio;
import br.com.pereiraeng.core.collections.set.FixPosSet;

/**
 * Classe dos objetos que representam um conjunto de {@link DuplaEmeio arestas}
 * que definem um corte no grafo
 * 
 * @author Philipe PEREIRA
 * @version September 14h, 2020
 *
 */
public class CutN extends FixPosSet<DuplaEmeio> {
	private static final long serialVersionUID = -961831702687046012L;

	/**
	 * <p>
	 * Função que indica se os objetos que representam as {@link DuplaEmeio arestas}
	 * estão {@link DuplaEmeio#isOrdered() orientadas}.
	 * </p>
	 * 
	 * <p>
	 * Em aplicações em grafos, isso pode ser utilizado para indicar que as arestas
	 * indicadas neste objeto estão ordenadas de modo tal que este corte é um corte
	 * orientado.
	 * </p>
	 * 
	 * @return <code>true</code> se o corte for orientado, <code>false</code> se não
	 */
	public boolean isOrdered() {
		for (DuplaEmeio dm : this)
			if (!dm.isOrdered())
				return false;
		return true;
	}

	@Override
	public boolean add(DuplaEmeio e) {
		if (!e.isOrdered())
			throw new IllegalArgumentException("Aresta cuja ordenação não foi confirmada");
		return super.add(e);
	}

	@Override
	public boolean contains(Object anObject) {
		if (anObject instanceof DuplaEmeio)
			return super.contains(anObject);
		else if (anObject instanceof Integer) {
			Integer num = (Integer) anObject;
			for (DuplaEmeio d : this) {
				boolean c = d.contains(num);
				if (c)
					return true;
			}
			return false;
		} else
			return false;
	}

	/**
	 * Função que retorna os {@link CutN#isOrdered() cortes orientados} obtidos a
	 * partir da subtração de todos os vértices de um dado conjunto. . É a função
	 * equivalente à
	 * {@link outils.mat.gph.obj.Cut#getOrderedCuts(java.util.Collection, Set)
	 * esta}, utilizada para grafos não numerados.
	 * 
	 * 
	 * @param graphN grafo
	 * @param outter arestas a serem removidas
	 * @return conjunto de {@link CutN#isOrdered() cortes orientados} que definem os
	 *         subgrafos resultantes da subtração. As primeiras posições das
	 *         {@link DuplaEmeio arestas} sempre serão ocupadas por um vértice do
	 *         subgrafo resultante da subtração (Ao passo que a segunda posição
	 *         conterá um vértice do subgrafo subtraído). Cada um desses cortes está
	 *         associado com o subgrafo resultante
	 */
	public static Map<CutN, GraphN> getOrderedCuts(GraphN graphN, Set<Integer> outter) {
		if (graphN == null || outter == null)
			new IllegalArgumentException("nenhum dos args pode ser null");
		// 1) reunir as arestas do corte e orientá-las

		GraphN inner = new GraphN();
		Set<DuplaEmeio> frs = new HashSet<>();
		for (DuplaEmeio e : graphN.getEns()) {
			int d = e.get1();
			int p = e.get2();
			boolean od = outter.contains(d);
			boolean op = outter.contains(p);
			if (op ^ od) {
				if (od) { // swap
					int d0 = p;
					p = d;
					d = d0;
				}
				DuplaEmeio t = new DuplaEmeio(d, p, e.get3());
				t.setOrdered(true);
				frs.add(t);
				// há casos em que há só um vértice
				inner.add(d);
			} else if (!op && !od)
				inner.add(e);
		}
		for (Integer num : graphN.getIsolated())
			inner.add(num);

		// 2) analisar conectividade dos sub-grafos resultantes da subtração
		Map<CutN, GraphN> out = new HashMap<>();

		// para poder usar bfs, tem de transformar em VertexObject
		Map<Integer, Vertex> graph = GraphN.toGraphObject(inner);

		while (frs.size() > 0) {
			Iterator<DuplaEmeio> it = frs.iterator();
			DuplaEmeio t = it.next();
			it.remove();

			CutN c = null;
			Vertex v = graph.get(t.get1());
			if (v != null) {
				c = new CutN();
				c.add(t);

				GraphN sg = GraphN.toGraphN(GraphUtils.bfs(v));

				// procurar as arestas de frs que formam este subgrafo
				Set<Integer> vs = sg.getVs();
				for (DuplaEmeio t0 : frs) {
					int iv = t0.get1();
					if (vs.contains(iv))
						c.add(t0);
				}
				frs.removeAll(c);

				out.put(c, sg);
			} else
				System.err.println("Verificar padrão ONS:\t" + t.get1() + "\t" + t.get2());
		}
		return out;
	}

	/**
	 * Função que retorna se o conjunto de arestas define um corte num dado grafo
	 * 
	 * @param graphN grafo
	 * @param cut    conjunto de arestas
	 * @return <code>true</code> se o conjunto define um corte, <code>false</code>
	 *         se não
	 */
	public static boolean isCut(GraphN graphN, CutN cut) {
		// TODO viva a matemática discreta
		return false;
	}

	/**
	 * Função que retorna se as arestas do conjunto estão ordenadas de forma que o
	 * corte definido é orientado
	 * 
	 * @param graphN grafo
	 * @param cut    conjunto de arestas
	 * @return <code>true</code> se as arestas do conjuntos orientam um corte,
	 *         <code>false</code> se não
	 */
	public static boolean isOrdered(GraphN graphN, CutN cut) {
		// TODO viva a matemática discreta
		return false;
	}
}