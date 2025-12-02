package br.com.pereiraeng.graph;

import br.com.pereiraeng.core.DisplayableFields;

/**
 * Classe que representa a {@link VertexObject aresta de um grafo} que contém um
 * vetor de objetos
 * 
 * @author Philipe PEREIRA
 *
 */
public class MultiObjEdge extends EdgeObject implements DisplayableFields {

	public MultiObjEdge(Object obj, Vertex from) {
		super(obj, from);
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
}
