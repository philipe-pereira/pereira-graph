package br.com.pereiraeng.graph;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;

import br.com.pereiraeng.math.probability.CombinatoricsEnum;

/**
 * Classe que reúne funções que trabalham sobre grafos
 * 
 * @author Philipe PEREIRA
 *
 */
public class GraphUtils {

	/**
	 * Função que retorna a relação de todas as arestas a partir dos vértices
	 * 
	 * @param vs
	 *            vértices
	 * @return arestas
	 */
	public static Collection<Edge> getEdges(Collection<? extends Vertex> vs) {
		Collection<Edge> out = new HashSet<>();
		for (Vertex v : vs) {
			Set<? extends Edge> es = v.getEdges();
			for (Edge e : es) {
				Vertex vo = e.getOpposite(v);
				if (vs.contains(vo))
					out.add(e);
			}
		}
		return out;
	}

	/**
	 * Função que retorna a relação de arestas que ligam dois vértices
	 * 
	 * @param v1
	 *            um vértice
	 * @param v2
	 *            outro vértice
	 * @return relação de arestas entre dois vértices
	 */
	public static Collection<? extends Edge> getEdges(Vertex v1, Vertex v2) {
		if (v1 == null || v2 == null)
			return null;
		Set<Edge> out = new HashSet<>();
		Set<? extends Edge> es = v1.getEdges();
		for (Edge e : es)
			if (v2.equals(e.getOpposite(v1)))
				out.add(e);
		return out;
	}

	/**
	 * Função que constrói um grafo a partir de uma tabela de conexões
	 * 
	 * @param conns
	 *            tabela de dispersão que associa para cada aresta seus vértices
	 *            (tanto as arestas quanto os vértices são representados pelos
	 *            objetos que neles serão inseridos). O número de entradas dessa
	 *            tabela é igual ao número de arestas.
	 * @return tabela de dispersão que associa para cada objeto do vértice o próprio
	 *         vértices (o conjunto de vértices define o grafo). O número de
	 *         entradas dessa tabela é igual ao número de vértices.
	 */
	public static Map<Object, VertexObject> buildGraph(Map<? extends Object, ? extends Object[]> conns) {
		Map<Object, VertexObject> out = new HashMap<>();
		for (Entry<? extends Object, ? extends Object[]> e : conns.entrySet()) {
			// para cada aresta
			Object[] ovs = e.getValue();

			VertexObject vo1 = out.get(ovs[0]);
			if (vo1 == null)
				out.put(ovs[0], vo1 = new VertexObject(ovs[0]));

			VertexObject vo2 = out.get(ovs[1]);
			if (vo2 == null)
				out.put(ovs[1], vo2 = new VertexObject(ovs[1]));

			EdgeObject eo = new EdgeObject(e.getKey(), vo1, vo2);
			vo1.add(eo);
			vo2.add(eo);
		}
		return out;
	}

	/**
	 * Função que constrói um grafo a partir de uma matriz com N linhas e 2 (ou 3)
	 * colunas, onde N é o número de arestas
	 * 
	 * @param edges
	 *            matriz em que cada linha representa uma aresta, sendo que nas duas
	 *            primeiras colunas estão os objetos que serão inseridos nos
	 *            vértices e na terceira coluna (caso existir) estará o objeto que
	 *            será inserido na aresta
	 * @return relação de vértices (que define o grafo)
	 */
	public static Collection<VertexObject> buildGraph(Object[][] edges) {
		Map<Object, VertexObject> out = new HashMap<>();
		for (Object[] vve : edges) {
			// para cada aresta

			VertexObject vo1 = out.get(vve[0]);
			if (vo1 == null)
				out.put(vve[0], vo1 = new VertexObject(vve[0]));

			VertexObject vo2 = out.get(vve[1]);
			if (vo2 == null)
				out.put(vve[1], vo2 = new VertexObject(vve[1]));

			EdgeObject eo = new EdgeObject(vve.length > 2 ? vve[2] : null, vo1, vo2);
			vo1.add(eo); // TODO um dia não mais fazer isso
			vo2.add(eo);
		}
		return out.values();
	}

	// ======================== depth-first search ========================

	// retorna os nós visitados

	/**
	 * Função que faz o percurso de um dado grafo a partir de um dado nó,
	 * considerado como sendo a raiz. O percurso é feito em profundidade
	 * (depth-first search).
	 * 
	 * @param v
	 *            objeto {@link Vertex vértice} a partir do qual se iniciará o
	 *            percurso
	 * @return conjunto de nó visitados
	 */
	public static Set<Vertex> dfs(Vertex v) {
		Set<Vertex> out = new LinkedHashSet<>();
		dfs(v, out);
		return out;
	}

	/**
	 * Função que faz o percurso de um dado grafo a partir de um dado nó,
	 * considerado como sendo a raiz. O percurso é feito em profundidade
	 * (depth-first search).
	 * 
	 * @param v
	 *            objeto {@link Vertex vértice} a partir do qual se iniciará o
	 *            percurso
	 * @param discovered
	 *            nós já visitados e que não o serão de novo
	 */
	private static void dfs(Vertex v, Set<Vertex> discovered) {
		if (discovered == null)
			discovered = new LinkedHashSet<>();
		discovered.add(v);

		Set<? extends Edge> edges = v.getEdges();
		for (Edge e : edges) {
			Vertex vo = e.getOpposite(v);

			if (!discovered.contains(vo))
				dfs(vo, discovered);
		}
	}

	// monta uma árvore

	// ======================== breadth-first search ========================

	// retorna os nós visitados

	/**
	 * 
	 * @param v
	 * @return
	 */
	public static Set<Vertex> bfs(Vertex v) {
		Set<Vertex> out = new LinkedHashSet<>();
		bfs(v, out);
		return out;
	}

	private static void bfs(Vertex v, Set<? super Vertex> discovered) {
		if (discovered == null)
			discovered = new LinkedHashSet<>();
		discovered.add(v);

		Queue<Vertex> queue = new ArrayDeque<>();
		queue.add(v);

		while (queue.size() != 0) {
			// Dequeue a vertex from queue and print it
			Vertex v0 = queue.poll();

			// Get all adjacent vertices of the dequeued vertex s
			// If a adjacent has not been visited, then mark it
			// visited and enqueue it
			Set<? extends Edge> edges = v0.getEdges();
			for (Edge e : edges) {
				Vertex v1 = e.getOpposite(v0);

				// Else, continue to do BFS
				if (!discovered.contains(v1)) {
					discovered.add(v1);
					queue.add(v1);
				}
			}
		}
	}

	// retorna os nós visitados - arestas proibidas

	@Deprecated
	public static void bfs(Set<Vertex> parcours, Set<? extends Vertex> forbidden, Vertex depart) {
		if (forbidden == null)
			parcours = new HashSet<>();
		else
			parcours = new HashSet<>(forbidden);
		bfs(depart, parcours);
		if (parcours.size() != forbidden.size())
			parcours.removeAll(forbidden);
	}

	// retorna um dos caminhos possíveis (o primeiro a ser achado na busca)

	// 1 partida, 1 chegada

	/**
	 * Função que retorna um possível caminho, composto por dois ou mais arestas,
	 * ligando dois vértices
	 * 
	 * @param part
	 *            vértice de partida
	 * @param dest
	 *            vértice de chegada
	 * @return caminho ligando os vértices
	 */
	public static GraphPath bfs(Vertex part, Vertex dest) {
		return bfs(part, dest, new HashSet<Edge>(), -1);
	}

	/**
	 * Função que retorna um possível caminho ligando dois vértices
	 * 
	 * @param part
	 *            vértice de partida
	 * @param dest
	 *            vértice de chegada
	 * @param forbidden
	 *            arestas proibidas (i.e., que não poderão compor o caminho)
	 * @param minimum
	 *            número de arestas a partir do qual o caminho passa a ser válido
	 * @return caminho ligando os vértices
	 */
	protected static GraphPath bfs(Vertex part, Vertex dest, Set<Edge> forbidden, int minimum) {
		Set<Vertex> discovered = new HashSet<>();
		discovered.add(part);

		GraphPath gp = new GraphPath(part);

		Queue<GraphPath> queue = new ArrayDeque<>();
		queue.add(gp);

		while (queue.size() != 0) {
			// Dequeue a vertex from queue and print it
			GraphPath gp0 = queue.poll();
			Vertex v0 = gp0.getEnd();

			// Get all adjacent vertices of the dequeued vertex s
			// If a adjacent has not been visited, then mark it
			// visited and enqueue it
			Set<? extends Edge> edges = v0.getEdges();
			for (Edge e : edges) {
				if (!forbidden.contains(e)) {
					Vertex v1 = e.getOpposite(v0);

					if (gp0.size() > minimum && v1.equals(dest)) {
						gp0.add(e);
						return gp0;
					}

					// Else, continue to do BFS
					if (!discovered.contains(v1)) {
						discovered.add(v1);

						GraphPath gp1 = new GraphPath(gp0);
						gp1.add(e);
						queue.add(gp1);
					}
				}
			}
		}
		return null;
	}

	// 1 partida, várias chegadas

	/**
	 * Função que retorna um possível caminho ligando dois vértices
	 * 
	 * @param dests
	 * @param part
	 * @return
	 */
	public static GraphPath bfs(Set<? extends Vertex> dests, Vertex part) {
		Set<Vertex> discovered = new HashSet<>();
		discovered.add(part);

		GraphPath gp = new GraphPath(part);

		Queue<GraphPath> queue = new ArrayDeque<>();
		queue.add(gp);

		while (queue.size() != 0) {
			// Dequeue a vertex from queue and print it
			GraphPath gp0 = queue.poll();
			Vertex v0 = gp0.getEnd();

			// Get all adjacent vertices of the dequeued vertex s
			// If a adjacent has not been visited, then mark it visited and
			// enqueue it
			Set<? extends Edge> edges = v0.getEdges();
			for (Edge e : edges) {
				Vertex v1 = e.getOpposite(v0);

				if (dests.contains(v1)) {
					gp0.add(e);
					return gp0;
				}

				// Else, continue to do BFS
				if (!discovered.contains(v1)) {
					discovered.add(v1);

					GraphPath gp1 = new GraphPath(gp0);
					gp1.add(e);
					queue.add(gp1);
				}
			}
		}
		return null;
	}

	// retorna todos os caminhos possíveis - vértices proibidos

	/**
	 * Função que retorna o conjunto de todos os caminhos de um vértice para outros
	 * 
	 * @param from
	 *            vértice de partida
	 * @param dests
	 *            destinos possíveis
	 * @param forbidden
	 *            vértice já visitados e que não o serão de novo
	 * @return conjunto de caminhos até os vértices indicados
	 */
	public static Set<GraphPath> bfs(Vertex from, Collection<? extends Vertex> dests, Set<? extends Vertex> forbidden) {
		// pré tratamento
		Set<GraphPath> out = new HashSet<GraphPath>();

		Set<Vertex> discovered = null;
		if (forbidden == null)
			discovered = new HashSet<>();
		else
			discovered = new HashSet<>(forbidden);

		bfs(from, dests, discovered, new GraphPath(from), out);
		return out;
	}

	private static void bfs(Vertex from, Collection<? extends Vertex> dests, Set<Vertex> discovered, GraphPath trace,
			Set<GraphPath> out) {
		discovered.add(from);

		Set<? extends Edge> edges = from.getEdges();
		for (Edge e : edges) {
			Vertex v = e.getOpposite(from);

			GraphPath newTrace = new GraphPath(trace);
			newTrace.add(e);

			if (dests.contains(v)) // se chegou em um dos destinos, adicionar à
									// lista de caminhos
				out.add(newTrace);
			else // se não chegou...
			if (!discovered.contains(v)) // ... continua procurando (mas antes
											// deve-se ver se o vértice não é
											// proibido)
				bfs(v, dests, new HashSet<>(discovered), newTrace, out);
		}
	}

	// retorna todos os caminhos possíveis - arestas proibidas

	/**
	 * Função que retorna o conjunto de todos os caminhos de um vértice para outros
	 * 
	 * @param dests
	 *            destinos possíveis
	 * @param from
	 *            vértice de partida
	 * @param forbidden
	 *            arestas que não podem ser utilizadas
	 * @return conjunto de caminhos até os vértices indicados
	 */
	public static Set<GraphPath> bfs(Collection<? extends Vertex> dests, Vertex from, Set<? extends Edge> forbidden) {
		// pré tratamento
		Set<GraphPath> out = new HashSet<GraphPath>();
		bfs(from, dests, new HashSet<Vertex>(), new GraphPath(from),
				forbidden == null ? new HashSet<Edge>() : forbidden, out, 10000);
		return out;
	}

	/**
	 * 
	 * @param from
	 * @param dests
	 * @param discovered
	 * @param trace
	 * @param forbidden
	 * @param out
	 * @param max
	 *            número máximo de caminhos abertos entre os dois nós (em grafos
	 *            fortemente conectados, este valor impede que um número
	 *            combinatorial elevado de caminhos sejam produzidos)
	 */
	private static void bfs(Vertex from, Collection<? extends Vertex> dests, Set<Vertex> discovered, GraphPath trace,
			Set<? extends Edge> forbidden, Set<GraphPath> out, final int max) {
		discovered.add(from);

		Set<? extends Edge> edges = from.getEdges();
		for (Edge e : edges) {
			if (out.size() > max)
				break;
			if (!forbidden.contains(e)) {
				Vertex v = e.getOpposite(from);

				GraphPath newTrace = new GraphPath(trace);
				newTrace.add(e);

				if (dests.contains(v)) // se chegou em um dos destinos, adicionar à lista de caminhos
					out.add(newTrace);
				else // se não chegou...
				if (!discovered.contains(v)) // ... continua procurando (mas antes deve-se ver se o vértice não é
												// proibido)
					bfs(v, dests, new HashSet<>(discovered), newTrace, forbidden, out, max);
			}
		}
	}

	// ------------------ REMOVER ARESTAS ISOLADAS ------------------

	public static void removeEndPoints(Collection<? extends Vertex> graph) {
		Iterator<? extends Vertex> it = graph.iterator();
		while (it.hasNext())
			if (it.next().getEdges().size() < 2)
				it.remove();
	}

	// =============================================================

	public static Set<GraphPath> findCycles(Collection<? extends Vertex> graph) {
		Set<GraphPath> cycles = new HashSet<>();
		for (Vertex v : graph)
			findCycles(v, new GraphPath(v), graph, cycles);
		return cycles;
	}

	private static void findCycles(Vertex current, GraphPath path, Collection<? extends Vertex> graph,
			Set<GraphPath> cycles) {

		for (Edge e : current.getEdges()) {
			// edge refers to the current node

			Vertex op = e.getOpposite(current);
			if (graph.contains(op)) {
				// o vértice que se quer analisar está no grafo...
				if (!path.visited(op)) {
					// neighbor node not on path yet
					GraphPath extendedPath = new GraphPath(path);
					extendedPath.add(e);
					// explore extended path
					findCycles(op, extendedPath, graph, cycles);
				} else if ((path.size() > 1) && (op.equals(path.getStart()))) {
					// se há mais de uma aresta e se termina no começo
					GraphPath cycle = new GraphPath(path);
					cycle.add(e);
					cycles.add(cycle);
				}
			}
		}
	}

	// =============================================================

	public static Set<Set<Edge>> getMinCuts(Vertex v1, Vertex v2) {
		Set<GraphPath> paths = bfs(v1, new HashSet<>(Arrays.asList(v2)), null);
		Set<Edge> edges = new HashSet<>();
		for (GraphPath p : paths)
			edges.addAll(p);
		return getMinCuts(edges, paths);
	}

	public static Set<Set<Edge>> getMinCuts(Set<Edge> edges, Set<GraphPath> paths) {
		Set<Set<Edge>> cuts = new LinkedHashSet<>();
		int order = 1;
		while (order <= edges.size()) {
			// todas as permutações possíveis com as arestas
			Set<Set<Edge>> perms = CombinatoricsEnum.getPerms(order, edges);

			// remover as permutações que contem cortes de ordem inferior
			Iterator<Set<Edge>> it = perms.iterator();
			while (it.hasNext()) {
				Set<Edge> perm = it.next();
				for (Set<Edge> cut : cuts) {
					if (perm.containsAll(cut)) {
						it.remove();
						break;
					}
				}
			}

			for (Set<Edge> perm : perms)
				if (checkEdges(paths, perm)) // se todos os caminhos existentes
												// dependem desta(s) arestas(s)
					cuts.add(perm);

			order++;
		}
		return cuts;
	}

	private static boolean checkEdges(Set<GraphPath> ps, Set<Edge> cols) {
		boolean b = true;
		for (GraphPath p : ps) {
			boolean pe = false;
			for (Edge e : cols) {
				pe = p.contains(e);
				if (pe)
					break;
			}
			if (!pe) {
				b = false;
				break;
			}
		}
		return b;
	}

	public static Set<GraphElement> copyGraph(Collection<GraphElement> network) {
		HashMap<GraphElement, GraphElement> newGraph = new HashMap<>();

		// primeiro, os nós
		for (GraphElement g : network) {
			if (g instanceof Vertex) {
				Vertex v = (Vertex) g;
				newGraph.put(v, new VertexObject(v));
			}
		}

		// em seguida, as arestas
		for (GraphElement g : network) {
			if (g instanceof Edge) {
				Edge e = (Edge) g;

				Vertex v1 = e.getOpposite(null);
				Vertex v2 = e.getOpposite(v1);

				// novos nós correspondentes
				VertexObject vo1 = (VertexObject) newGraph.get(v1);
				VertexObject vo2 = (VertexObject) newGraph.get(v2);

				if (v1 != null && v2 != null) {
					EdgeObject eo = new EdgeObject(e, vo1, vo2);
					vo1.add(eo);
					vo2.add(eo);
					newGraph.put(e, eo);
				}
			}
		}

		// repassar para um conjunto
		return new HashSet<>(newGraph.values());
	}

	/**
	 * Função que remove de todos os vértices as arestas que apontam para vértices
	 * que foram eventualmente removidos do grafo
	 * 
	 * @param vs
	 *            grafo
	 */
	public static void forceConnectivity(Collection<? extends Vertex> vs) {
		Map<Edge, Vertex> rem = new HashMap<>();
		for (Vertex v : vs) {
			Set<? extends Edge> es = v.getEdges();
			for (Edge e : es) {
				Vertex v0 = e.getOpposite(v);
				if (!vs.contains(v0))
					rem.put(e, v);
			}
		}
		// feito em duas partes para evitar ConcurrentException
		for (Entry<Edge, Vertex> e : rem.entrySet())
			e.getValue().remove(e.getKey());
	}
}
