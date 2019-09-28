package lcom.utils.models;

import java.util.ArrayList;
import java.util.List;

public class Edge {

	private Vertex firstVertex;
	private Vertex secondVertex;
	private List<Vertex> edge;
	
	public Edge(Vertex firstVertex, Vertex secondVertex) {
		this.firstVertex = firstVertex;
		this.secondVertex = secondVertex;
		
		edge = new ArrayList<>();
		edge.add(firstVertex);
		edge.add(secondVertex);
	}
	
	Vertex getFirstVertex() {
		return firstVertex;
	}
	
	Vertex getSecondVertex() {
		return secondVertex;
	}
	
	List<Vertex> getVertices() {
		return edge;
	}
	
	public boolean containsVertex(Vertex vertex) {
		return edge.contains(vertex);
	}
	
	Vertex getOtherVertex(Vertex vertex) {
		if (edge.get(0).equals(vertex)) {
			return edge.get(1);
		} else if (edge.get(1).equals(vertex)) {
			return edge.get(0);
		} else {
			return null;
		}
	}
}
