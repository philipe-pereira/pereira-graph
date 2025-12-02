package br.com.pereiraeng.graph;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Stack;
import java.util.TreeMap;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

import br.com.pereiraeng.graph.tree.AbstractMutableTreeNode;
import br.com.pereiraeng.graph.tree.PrimitiveMutableTreeNode;
import br.com.pereiraeng.core.StringUtils;
import br.com.pereiraeng.core.collections.ArrayUtils;

/**
 * Função de manipulação de árvore: grafos radiais
 * 
 * @author Philipe PEREIRA
 *
 */
public class TreeUtils {

	/**
	 * Função que adiciona um nó a outro nó ou, se o pai já possui este filho, este
	 * é retornado.<br>
	 * 
	 * PS.: considera-se que este pai já tem o filho se um de destes possuir como
	 * {@link DefaultMutableTreeNode#getUserObject() objeto característico} o objeto
	 * indicado (<strong>se o objeto for um vetor, será feita a
	 * {@link Arrays#deepEquals(Object[], Object[]) verificação profunda} de cada
	 * uma das posições</strong>)
	 * 
	 * @param parent nó que será pai
	 * @param object objeto do nó que será ou já é filho
	 * @return nó filho
	 */
	public static DefaultMutableTreeNode getOrCreateChild(DefaultMutableTreeNode parent, Object object) {
		DefaultMutableTreeNode n = null;
		for (int i = 0; i < parent.getChildCount(); i++) {
			DefaultMutableTreeNode c = (DefaultMutableTreeNode) parent.getChildAt(i);
			Object o = c.getUserObject();

			boolean eq = false;
			if (o instanceof Object[] && object instanceof Object[])
				eq = Arrays.deepEquals((Object[]) o, (Object[]) object);
			else
				eq = o.equals(object);

			if (eq) {
				n = c;
				break;
			}
		}
		// se o nó não existir, cria
		if (n == null) {
			n = new DefaultMutableTreeNode(object);
			parent.add(n);
		}
		return n;
	}

	/**
	 * Função que retorna o nó a jusante de um outro dado nó que
	 * {@link DefaultMutableTreeNode#getUserObject() contém} um dado objeto
	 * 
	 * @param node nó cujos filhos serão examinados
	 * @param obj  objeto procurado
	 * @return nó que contém o objeto, ou <code>null</code> caso o nó não exista
	 */
	public static DefaultMutableTreeNode getNode(DefaultMutableTreeNode node, Object obj) {
		for (int i = 0; i < node.getChildCount(); i++) {
			DefaultMutableTreeNode child = (DefaultMutableTreeNode) node.getChildAt(i);
			// se for um dos filhos...
			if (child.getUserObject().equals(obj))
				return child;

			DefaultMutableTreeNode d = getNode(child, obj);

			// se for um dos descendentes...
			if (d != null)
				return d;
		}
		return null;
	}

	/**
	 * Função que retorna um vetor com todos os nós-folhas a jusante de um dado nó
	 * 
	 * @param node nó a partir do qual serão reunidas as folhas
	 * @return vetor com as folhas
	 */
	public static DefaultMutableTreeNode[] getLeaves(DefaultMutableTreeNode node) {
		if (node.isLeaf())
			return new DefaultMutableTreeNode[] { node };
		else {
			List<DefaultMutableTreeNode> out = new LinkedList<>();
			for (int i = 0; i < node.getChildCount(); i++) {
				DefaultMutableTreeNode[] nodes = getLeaves((DefaultMutableTreeNode) node.getChildAt(i));
				for (int j = 0; j < nodes.length; j++)
					out.add(nodes[j]);
			}
			return out.toArray(new DefaultMutableTreeNode[out.size()]);
		}
	}

	public static Object[] getLeavesObjects(DefaultMutableTreeNode node) {
		if (node.isLeaf())
			return new Object[] { node.getUserObject() };
		else {
			List<Object> out = new LinkedList<>();
			for (int i = 0; i < node.getChildCount(); i++) {
				Object[] nodes = getLeavesObjects((DefaultMutableTreeNode) node.getChildAt(i));
				for (int j = 0; j < nodes.length; j++)
					out.add(nodes[j]);
			}
			return out.toArray(new Object[out.size()]);
		}
	}

	/**
	 * Função que retorna o número de nós-folhas a jusante de um dado nó
	 * 
	 * @param node nó a partir do qual serão contadas as folhas
	 * @return número de folhas
	 */
	public static int countLeaves(DefaultMutableTreeNode node) {
		if (node.isLeaf())
			return 1;
		else {
			int out = 0;
			for (int i = 0; i < node.getChildCount(); i++)
				out += countLeaves((DefaultMutableTreeNode) node.getChildAt(i));
			return out;
		}
	}

	public static void removeLevel(DefaultMutableTreeNode node, int level) {
		if (level == 0)
			throw new IllegalArgumentException("Não se pode remover a raiz");

		Stack<DefaultMutableTreeNode> stack = new Stack<>();
		stack.push(node);

		Map<DefaultMutableTreeNode, DefaultMutableTreeNode> node2newParent = new HashMap<>();

		while (stack.size() > 0) {
			DefaultMutableTreeNode n = stack.pop();

			if (n.getLevel() == level) {
				DefaultMutableTreeNode p = (DefaultMutableTreeNode) n.getParent();
				for (int i = 0; i < n.getChildCount(); i++)
					node2newParent.put((DefaultMutableTreeNode) n.getChildAt(i), p);
				p.remove(n);
			}

			for (int i = 0; i < n.getChildCount(); i++)
				stack.push((DefaultMutableTreeNode) n.getChildAt(i));
		}

		for (Entry<DefaultMutableTreeNode, DefaultMutableTreeNode> e : node2newParent.entrySet())
			e.getValue().add(e.getKey());
	}

	public static DefaultMutableTreeNode getDeepestNode(DefaultMutableTreeNode node, int depth) {
		if (node.isLeaf()) {
			if (node.getLevel() > depth)
				return node;
			else
				return null;
		} else {
			DefaultMutableTreeNode deepest = null;
			for (int i = 0; i < node.getChildCount(); i++) {
				DefaultMutableTreeNode child = (DefaultMutableTreeNode) node.getChildAt(i);
				DefaultMutableTreeNode d = getDeepestNode(child, depth);
				if (d != null) {
					deepest = d;
					depth = d.getLevel();
				}
			}
			return deepest;
		}
	}

	/**
	 * Função que retorna, para cada nó da lista, o caminho até a raiz. Se um nós
	 * estiver no caminho do outro, ele não estará na lista.
	 * 
	 * @param ls lista de nós (compreendendo folhas ou não)
	 * @return tabela de dispersão que associa para cada nó o caminho até a raiz
	 */
	public static Map<DefaultMutableTreeNode, TreeNode[]> deepest2root(Collection<DefaultMutableTreeNode> ls) {
		Map<DefaultMutableTreeNode, TreeNode[]> out = new HashMap<>();
		TreeMap<Integer, Set<DefaultMutableTreeNode>> depth2nodes = new TreeMap<>(Collections.reverseOrder());

		Iterator<DefaultMutableTreeNode> it = ls.iterator();
		while (it.hasNext()) {
			DefaultMutableTreeNode d = it.next();

			Set<DefaultMutableTreeNode> ns = depth2nodes.get(d.getLevel());
			if (ns == null)
				depth2nodes.put(d.getLevel(), ns = new HashSet<>());
			ns.add(d);

			it.remove();
		}

		// agrupar nós de um mesmo caminho, começando pelos mais profundos
		while (depth2nodes.size() > 0) {
			// enquanto houver nós...
			Iterator<Entry<Integer, Set<DefaultMutableTreeNode>>> it1 = depth2nodes.entrySet().iterator();

			// pegar o nó mais profundo
			int d;
			TreeNode[] tn;
			DefaultMutableTreeNode leaf;
			{
				Entry<Integer, Set<DefaultMutableTreeNode>> e2 = it1.next();
				d = e2.getKey();

				Set<DefaultMutableTreeNode> ns = e2.getValue();
				Iterator<DefaultMutableTreeNode> it2 = ns.iterator();
				leaf = it2.next();

				it2.remove();
				if (ns.size() == 0)
					it1.remove();

				tn = PrimitiveMutableTreeNode.getPathToRoot(leaf, 0);
			}

			// verificar se dentre os nós restantes algo deles está no mesmo
			// caminho que o nó processado nesta iteração
			it1 = depth2nodes.entrySet().iterator();
			while (it1.hasNext()) {
				Entry<Integer, Set<DefaultMutableTreeNode>> e3 = it1.next();
				if (e3.getKey() < d) {
					Set<DefaultMutableTreeNode> ns = e3.getValue();
					Iterator<DefaultMutableTreeNode> it2 = ns.iterator();
					while (it2.hasNext()) {
						DefaultMutableTreeNode node = it2.next();
						int p = ArrayUtils.indexOf(tn, node);
						if (p >= 0) {
							// merge
							d = e3.getKey();
							it2.remove();
							if (ns.size() == 0)
								it1.remove();
						}
					}
				}
			}
			out.put(leaf, tn);
		}
		return out;
	}

	// ==========================================================

	public static boolean search(TreeNode up, TreeNode down) {
		if (up.equals(down))
			return true;
		else {
			for (int i = 0; i < up.getChildCount(); i++)
				if (search(up.getChildAt(i), down))
					return true;
		}
		return false;
	}

	/**
	 * Função que faz uma busca ao longo de uma árvore (Preorder)
	 * 
	 * @param start         nó de onde se começa a busca
	 * @param searchedValue valor procurado no objeto característico do nó
	 * @return nó encontrado, ou <code>null</code> se nenhum nó foi encontrado
	 */
	public static DefaultMutableTreeNode searchTree(DefaultMutableTreeNode start, Object searchedValue) {
		if (searchedValue == null)
			return null;
		Object obj = start.getUserObject();
		if (obj != null)
			if (isSameNode(searchedValue, obj))
				return start;
		return TreeUtils.searchTree(start, searchedValue, 0);
	}

	/**
	 * Função recursiva que procede com a busca ao longo dos filhos do nó inicial
	 * 
	 * @param start         nó de onde se começa a busca
	 * @param searchedValue valor procurado no objeto característico do nó
	 * @param pos           se for -1, procura-se somente dentre os descendentes do
	 *                      nó de partida. Se for diferente, pode-se investigar os
	 *                      irmãos e tios do nó (ou seja, aqueles que se relacionam
	 *                      com o nó de partida a partir do pai), sendo que este
	 *                      número será igual ao índice do nó irmão
	 * @return nó encontrado, ou <code>null</code> se nenhum nó foi encontrado
	 */
	private static DefaultMutableTreeNode searchTree(DefaultMutableTreeNode start, Object searchedValue, int pos) {

		int startIndex = pos == -1 ? 0 : pos;

		// verifica todos os filhos
		boolean flag = true;

		// buscar circular
		for (int i = startIndex; flag ? i < start.getChildCount()
				: i != startIndex; i = (i + 1) % start.getChildCount()) {
			flag = false;
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) start.getChildAt(i);
			Object obj = node.getUserObject();

			if (obj != null)
				if (isSameNode(searchedValue, obj))
					return node;

			// se não for esse o nó, inspeciona-se os filhos (só que não se pode retroceder
			// na busca, por isso a posição -1)
			DefaultMutableTreeNode found = searchTree(node, searchedValue, -1);
			if (found != null)
				return found;
		}

		if (pos != -1) {
			// se pos for diferente de menos -1, pode-se retroceder na busca (i.e.
			// inspecionar os irmãos, tios, etc.)

			DefaultMutableTreeNode parent = (DefaultMutableTreeNode) start.getParent();
			if (parent != null) {
				// se houver um pai (condição necessária para se ter um irmão)
				DefaultMutableTreeNode found = searchTree(parent, searchedValue, parent.getIndex(start) + 1);
				if (found != null)
					return found;
			}
		}

		// se o nó já for a raiz ou se não houver mais irmãos, então não foi encontrado
		return null;
	}

	private static boolean isSameNode(Object value1, Object value2) {
		if (value1.getClass().isArray() && value2.getClass().isArray()) {
			Object[] searched = (Object[]) value1;
			Object[] startObj = (Object[]) value2;
			if (Arrays.deepEquals(searched, startObj))
				return true;
		} else if (value1 instanceof String) {
			if (value2.toString().toUpperCase().contains(((String) value1).toUpperCase()))
				return true;
		} else if (value2.equals(value1))
			return true;
		return false;
	}

	// ==========================================================

	/**
	 * Lowest Common Ancestor
	 * 
	 * @param values
	 * @return
	 */
	public static TreeNode lca(Collection<? extends TreeNode> nodes) {
		TreeNode out = null;
		Iterator<? extends TreeNode> it = nodes.iterator();
		while (it.hasNext())
			out = lca(out, it.next());
		return out;
	}

	private static TreeNode lca(TreeNode n1, TreeNode n2) {
		if (n1 == null)
			return n2;
		if (n2 == null)
			return n1;
		TreeNode[] path1 = getPath(n1);
		TreeNode[] path2 = getPath(n2);
		int l = Math.min(path1.length, path2.length);
		TreeNode lowest = null;
		for (int i = 0; i < l; i++) {
			if (path1[i].equals(path2[i]))
				lowest = path1[i];
			else
				break;
		}
		return lowest;
	}

	private static TreeNode[] getPath(TreeNode node) {
		LinkedList<TreeNode> s = new LinkedList<>();
		TreeNode p = node;
		while (p != null) {
			s.addFirst(p);
			p = p.getParent();
		}
		return s.toArray(new TreeNode[s.size()]);
	}

	// ==========================================================

	/**
	 * Função que faz o percurso de um dado grafo a partir de um dado nó,
	 * considerado como sendo a raiz, e partir dessa busca monta uma árvore. O
	 * percurso é feito em profundidade (depth-first search).
	 * 
	 * @param node          nó que contém como objeto o {@link Vertex vértice} a
	 *                      partir do qual se iniciará o percurso
	 * @param edgeInTheTree
	 *                      <ul>
	 *                      <li><code>true</code> somente os vértices constarão na
	 *                      árvore;</i>
	 *                      <li><code>false</code> as arestas e os vértices são
	 *                      incluídos como nós da árvore.</i>
	 *                      </ul>
	 * 
	 */
	public static void dfs(DefaultMutableTreeNode node, boolean edgeInTheTree) {
		dfs(node, null, edgeInTheTree);
	}

	/**
	 * Função que faz o percurso de um dado grafo a partir de um dado nó,
	 * considerado como sendo a raiz, e partir dessa busca monta uma árvore. O
	 * percurso é feito em profundidade (depth-first search).
	 * 
	 * @param node          nó que contém como objeto o {@link Vertex vértice} a
	 *                      partir do qual se iniciará o percurso
	 * @param discovered    nós já visitados e que não o serão de novo
	 * @param edgeInTheTree
	 *                      <ul>
	 *                      <li><code>true</code> somente os vértices constarão na
	 *                      árvore;</i>
	 *                      <li><code>false</code> as arestas e os vértices são
	 *                      incluídos como nós da árvore.</i>
	 *                      </ul>
	 */
	public static void dfs(DefaultMutableTreeNode node, Set<Vertex> discovered, boolean edgeInTheTree) {
		if (discovered == null)
			discovered = new LinkedHashSet<>();
		Vertex v = (Vertex) node.getUserObject();
		discovered.add(v);

		Set<? extends Edge> edges = v.getEdges();
		for (Edge e : edges) {
			// candidato à filho
			Vertex vo = e.getOpposite(v);
			if (vo == null)
				vo = e.getOpposite(v);

			if (!discovered.contains(vo)) {
				// se ele já não foi tratado

				// nó do vértice
				DefaultMutableTreeNode nv = new DefaultMutableTreeNode(vo);

				if (edgeInTheTree) {
					// nó da aresta
					DefaultMutableTreeNode ne = new DefaultMutableTreeNode(e);
					node.add(ne);
					ne.add(nv);
				} else
					node.add(nv);

				dfs(nv, discovered, edgeInTheTree);
			}
			// TODO ver a questão das arestas paralelas
//			else if (edgeInTheTree) { // se o vértice oposto já foi tratado, verificar
//				System.out.println();
//				// TODO
//			}
		}
	}

	// ==========================================================

	/**
	 * Função que faz com que um outro nó seja a raiz da árvore
	 * 
	 * @param newRoot nova raiz
	 */
	public static void changeRoot(DefaultMutableTreeNode newRoot) {
		Stack<DefaultMutableTreeNode> stack = new Stack<DefaultMutableTreeNode>();

		DefaultMutableTreeNode oldParent = newRoot;
		while (oldParent != null) {
			stack.push(oldParent);
			oldParent = (DefaultMutableTreeNode) oldParent.getParent();
		}

		oldParent = stack.pop();
		while (!stack.isEmpty()) {
			DefaultMutableTreeNode node = stack.pop();
			oldParent.remove(node);
			node.add(oldParent);

			oldParent = node;
		}
	}

	/**
	 * Função que determina os subgrafos de um grafo que são árvores
	 * 
	 * @param rootObject   objeto que estará no nó raiz da arborescência
	 * @param graphOrRoots grafo inteiro a ser analisado (representa por todos os
	 *                     seus vértices) OU conjunto de nós de onde partem as
	 *                     árvores
	 * @param sendRoots
	 *                     <ol start="0">
	 *                     <li>se a relação é o grafo inteiro e não se conhece a
	 *                     priori raiz alguma;</i>
	 *                     <li>se a relação de vértices enviada são todas as raízes
	 *                     das árvores;</i>
	 *                     <li>se além do grafo inteiro são enviadas algumas raízes
	 *                     (e deseja-se encontrar as demais).</i>
	 *                     </ol>
	 * @param someRoots    algumas raízes (<code>sendRoots==2</code>, pode ser
	 *                     <code>null</code>)
	 * @return nó raiz da arborescência
	 */
	public static DefaultMutableTreeNode getSubTrees(Object rootObject, Collection<? extends Vertex> graphOrRoots,
			int sendRoots, Collection<? extends Vertex> someRoots) {
		if (graphOrRoots == null)
			return null;

		Collection<? extends Vertex> roots = null;

		switch (sendRoots) {
		case 0:// se a lista de vértices de partida da árvore não foi reunida...
			roots = getRoots(graphOrRoots);
			break;
		case 1: // se a lista de vértices enviada como argumento é a de nós
				// raízes...
			roots = graphOrRoots;
			break;
		case 2:
			roots = getRoots(graphOrRoots, someRoots);
			break;
		}

		// se o grafo for uma árvore (i.e., qualquer aresta removida parte o
		// grafo em dois) em qualquer nó pode ser raiz...
		if (graphOrRoots.size() == 0)
			return null;

		// o que não for proibido, é permitido...
		DefaultMutableTreeNode out = new DefaultMutableTreeNode(rootObject);
		for (Vertex root : roots) {
			DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(root);
			dfs(rootNode, new HashSet<>(roots), false);
			out.add(rootNode);
		}

		return out;
	}

	public static Collection<Vertex> getRoots(Collection<? extends Vertex> graph) {
		// reunir todos as arestas
		Map<Edge, Vertex> allEdges = new HashMap<>();
		for (Vertex v : graph)
			for (Edge e : v.getEdges())
				allEdges.put(e, v);

		// reunir as arestas que pertencem a ciclos (isso é feito procurando
		// se há uma maneira de se chegar aos dois vértices de uma aresta
		// sem ser por esta aresta)
		Map<Edge, Vertex> belongToAcycle = new HashMap<>();
		for (Entry<Edge, Vertex> e : allEdges.entrySet()) {
			Edge edge = e.getKey();
			if (!belongToAcycle.containsKey(edge)) {
				Vertex v1 = e.getValue();
				Vertex v2 = edge.getOpposite(v1);

				GraphPath gp = GraphUtils.bfs(v1, v2, new HashSet<Edge>(Arrays.asList(edge)), 2);
				if (gp != null) {
					// se há um caminho alternativo entre os nós
					Vertex v = gp.getStart();
					for (Edge ec : gp) {
						belongToAcycle.put(ec, v);
						v = ec.getOpposite(v);
					}
					belongToAcycle.put(edge, v2);
				}
			}
		}
		allEdges.clear();

		// os vértices unidos por arestas pertencentes a ciclos são as
		// raízes das possíveis árvores
		Set<Vertex> possibleRoots = new HashSet<>();
		for (Entry<Edge, Vertex> entry : belongToAcycle.entrySet()) {
			Vertex v1 = entry.getValue();
			Vertex v2 = entry.getKey().getOpposite(v1);

			possibleRoots.add(v1);
			possibleRoots.add(v2);
		}
		return possibleRoots;
	}

	/**
	 * Função que retorna as possíveis raizes de sub-grafos radias do grafo. Isso é
	 * feito partindo do princípio que todos os vértices que pertencem a sub-grafos
	 * não radiais (i.e., há mais de um caminho entre tais vértices) são possível
	 * pontos de partida de sistemas radiais.
	 * 
	 * @param graph grafo
	 * @return vértices do grafo que pode ser raízes de sistemas radias
	 */
	public static Collection<Vertex> getRoots(Collection<? extends Vertex> graph,
			Collection<? extends Vertex> someRoots) {
		Set<Vertex> out = new HashSet<>(someRoots);
		out.retainAll(graph);
		if (out.size() == 0)
			throw new IllegalArgumentException("Não se indicou nenhuma raiz");
		Set<Edge> visited = new HashSet<>();
		Set<Vertex> remaining = new HashSet<>(graph);
		while (remaining.size() > 0) {
			Iterator<Vertex> it = remaining.iterator();
			while (it.hasNext()) {
				Vertex v = it.next();

				if (out.contains(v)) {
					it.remove();
					continue;
				}

				Set<? extends Edge> es = v.getEdges();
				int c = 0;
				for (Edge e : es) {
					if (!visited.contains(e)) {
						c++;
						visited.add(e);
						Vertex vo = e.getOpposite(v);
						boolean vr = out.contains(v);
						boolean vor = out.contains(vo);
						if (vr ^ vor) {
							// se o vértice está diretamente ligado a uma barra
							// forte do sistema, para ele ser também considerado
							// forte ele deve achar pelo menos um caminho até
							// outro ponto forte do sistema (pode ser inclusive
							// este mesmo, só não pode usar a mesma aresta)

							Set<GraphPath> gps = GraphUtils.bfs(out, vr ? vo : v, new HashSet<Edge>(Arrays.asList(e)));
							if (gps != null) {
								for (GraphPath gp : gps) {
									// se há um caminho alternativo entre o nó e
									// as barras fortes
									Vertex v0 = gp.getStart();
									out.add(v0);
									for (Edge ec : gp) {
										v0 = ec.getOpposite(v0);
										out.add(v0);
									}
								}
							}
							it.remove();
							break;
						}
					}
				}
				if (c == 0)
					it.remove();
			}
		}

		return out;
	}

	// ==========================================================

	public static DefaultMutableTreeNode copy(DefaultMutableTreeNode r) {
		return copy(null, r);
	}

	public static DefaultMutableTreeNode copy(DefaultMutableTreeNode p, DefaultMutableTreeNode r) {
		DefaultMutableTreeNode out = new DefaultMutableTreeNode(r.getUserObject());
		if (p != null)
			p.add(out);
		for (int i = 0; i < r.getChildCount(); i++)
			copy(out, (DefaultMutableTreeNode) r.getChildAt(i));
		return out;
	}

	// ==========================================================

	/**
	 * Gera um string representativo da árvore
	 * 
	 * @param rootNode raiz da árvore
	 * @return string representativo
	 */
	public static String generateString(TreeNode rootNode) {
		StringBuilder s = new StringBuilder(TreeUtils.getExtractString(rootNode));
		s = TreeUtils.nodeName(s, rootNode, 1);
		return s.toString();
	}

	/**
	 * Função recursiva que percurre a árvore
	 * 
	 * @param s     string representativo
	 * @param node  nó cujos filhos estão sendo lidos
	 * @param level profundidade dos filhos do nó lido
	 * @return
	 */
	private static StringBuilder nodeName(StringBuilder s, TreeNode node, int level) {
		int n = node.getChildCount();
		for (int i = 0; i < n; i++) {
			s.append(StringUtils.repeatChar('+', level));
			TreeNode child = node.getChildAt(i);
			s.append(TreeUtils.getExtractString(child));
			s = new StringBuilder(nodeName(s, child, level + 1));
		}
		return s;
	}

	private static StringBuilder getExtractString(TreeNode node) {
		Object nodeObject;
		if (node instanceof DefaultMutableTreeNode)
			nodeObject = ((DefaultMutableTreeNode) node).getUserObject();
		else if (node instanceof AbstractMutableTreeNode)
			nodeObject = ((AbstractMutableTreeNode) node).getUserObject();
		else
			nodeObject = node.toString();

		StringBuilder out = new StringBuilder();
		if (nodeObject.getClass().isArray())
			out.append(StringUtils.addSeparator((Object[]) nodeObject, "\t"));
		else
			out.append(nodeObject.toString());
		out.append("\n");
		return out;
	}

	/**
	 * Função que retorna uma árvore a partir de seu string representativo
	 * 
	 * @param tree string representativo
	 * @return raiz da árvore
	 */
	public static DefaultMutableTreeNode generateTreeModel(String tree) {
		String[] nodesNames = tree.split("\n");

		// obter a máxima profundidade da árvore
		int max = -1;
		for (String s : nodesNames)
			max = Math.max(max, TreeUtils.getPlusCount(s));

		DefaultMutableTreeNode[] node = new DefaultMutableTreeNode[max + 1];

		DefaultMutableTreeNode root = new DefaultMutableTreeNode(TreeUtils.getContent(nodesNames[0]));

		for (int i = 1; i < nodesNames.length; i++) {
			int p = TreeUtils.getPlusCount(nodesNames[i]);

			if (p < 2) {
				// se o nó estiver conectado à raiz
				node[0] = new DefaultMutableTreeNode(TreeUtils.getContent(nodesNames[i].substring(1)));
				root.add(node[0]);
			} else {
				// senão
				node[p - 1] = new DefaultMutableTreeNode(TreeUtils.getContent(nodesNames[i].substring(p)));
				node[p - 2].add(node[p - 1]);
			}
		}
		return root;
	}

	private static Object getContent(String text) {
		if (text.indexOf('\t') > -1)
			return text.split("\t");
		else
			return text;
	}

	private static int getPlusCount(String s) {
		if (s.length() == 0)
			return 0;
		boolean toogle = true;
		int p = 0;
		while (toogle) { // contagem do número de "+" antes do nome do nó
			if (s.charAt(p) == '+')
				p++;
			else
				toogle = false;
		}
		return p;
	}

	/**
	 * Versão melhorada de {@link #generateTreeModel(String)}
	 * 
	 * @param tree
	 * @param root
	 * @param c    indicador de nível
	 * @return
	 */
	public static DefaultMutableTreeNode generateTree(String tree, String root, char c) {
		String[] ss = tree.split("\n");
		DefaultMutableTreeNode parent = null, n = null;
		final int start;
		if (root != null) {
			parent = new DefaultMutableTreeNode(root);
			start = 0;
		} else {
			n = new DefaultMutableTreeNode(ss[0]);
			start = 1;
		}
		int h = 0;
		for (int j = start; j < ss.length; j++) {
			String s = ss[j];
			if (s.length() <= h ? true : s.charAt(h) != c) {
				while (h > 0) {
					if (s.length() <= (h - 1) ? false : s.charAt(h - 1) == c)
						break;
					else {
						parent = (DefaultMutableTreeNode) parent.getParent();
						h--;
					}
				}
			} else {
				h++;
				parent = n;
			}
			parent.add(n = new DefaultMutableTreeNode(s.substring(h).trim()));
		}
		while (parent.getParent() != null)
			parent = (DefaultMutableTreeNode) parent.getParent();

		return parent;
	}
}
