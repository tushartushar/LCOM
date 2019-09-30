package lcom.metrics.algorithms;

import lcom.sourceModel.SM_Field;
import lcom.sourceModel.SM_Method;
import lcom.sourceModel.SM_Type;
import lcom.utils.models.Edge;
import lcom.utils.models.Graph;

public class LCOM3 implements ILCOM {
    private double lcom = 0;
    private SM_Type type;
    private Graph graph;

    @Override
    public double compute(SM_Type type) {
        this.type = type;
        initializeGraph();
        lcom = computeLCOM();
        return lcom;
    }

    private void initializeGraph() {
        initializeVertices();
        initializeEdges();
    }

    private void initializeVertices() {
        graph = new Graph();
        for (SM_Method method : type.getMethodList()) {
            graph.addVertex(method);
        }
        for (SM_Field field : type.getFieldList()) {
            if (!field.isStatic())
                graph.addVertex(field);
        }
    }

    private void initializeEdges() {
        for (SM_Method method : type.getMethodList()) {
            addAdjacentFields(method);
        }
    }

    private void addAdjacentFields(SM_Method method) {
        for (SM_Field fieldVertex : method.getDirectFieldAccesses()) {
            if (!fieldVertex.isStatic())
                graph.addEdge(new Edge(method, fieldVertex));
        }
    }

    private double computeLCOM() {
        graph.computeConnectedComponents();
        return graph.getConnectedComponnents().size();
    }
}
