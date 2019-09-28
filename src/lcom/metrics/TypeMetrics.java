package lcom.metrics;

import java.util.ArrayList;
import java.util.List;

import lcom.sourceModel.SM_Field;
import lcom.sourceModel.SM_Method;
import lcom.sourceModel.SM_Type;
import lcom.utils.models.Edge;
import lcom.utils.models.Graph;
import lcom.utils.models.Vertex;

public class TypeMetrics {
    private double lcom;
    private SM_Type type;
    private Graph graph;

    public TypeMetrics(SM_Type type) {
        this.type = type;
    }

    public void extractMetrics() {
        extractLCOM();
    }

    private void extractLCOM() {
        if (isNotLcomComputable()) {
            lcom = -1.0;
            return;
        }
        initializeGraph();
        lcom = computeLCOM();
    }

    private boolean isNotLcomComputable() {
        return type.isInterface()
                || type.getFieldList().size() == 0
                || type.getMethodList().size() == 0;
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
            graph.addVertex(field);
        }
    }

    private void initializeEdges() {
        for (SM_Method method : type.getMethodList()) {
            addAdjacentFields(method);
            addAdjacentMethods(method);
        }
    }

    private void addAdjacentFields(SM_Method method) {
        for (SM_Field fieldVertex : method.getDirectFieldAccesses()) {
            graph.addEdge(new Edge(method, fieldVertex));
        }
    }

    private void addAdjacentMethods(SM_Method method) {
        for (SM_Method methodVertex : type.getMethodList()) {
            if (!method.equals(methodVertex) && method.getCalledMethods().contains(methodVertex)) {
                graph.addEdge(new Edge(method, methodVertex));
            }
        }
    }

    private double computeLCOM() {
        graph.computeConnectedComponents();
        List<List<Vertex>> nonSingleElementFieldComponents = getNonSingleElementFieldComponents();
        if (nonSingleElementFieldComponents.size() > 1) {
            return ((double) getNonSingleElementFieldComponents().size()) / type.getMethodList().size();
        }
        return 0.0;
    }

    private List<List<Vertex>> getNonSingleElementFieldComponents() {
        List<List<Vertex>> cleanComponents = new ArrayList<>();
        for (List<Vertex> component : graph.getConnectedComponnents()) {
            if (component.size() != 1 || !(component.get(0) instanceof SM_Field)) {
                cleanComponents.add(component);
            }
        }
        return cleanComponents;
    }

    public double getLcom() {
        return lcom;
    }
}
