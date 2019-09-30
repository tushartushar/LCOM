package lcom.metrics.algorithms;

import lcom.sourceModel.SM_Field;
import lcom.sourceModel.SM_Method;
import lcom.sourceModel.SM_Type;
import lcom.utils.models.Edge;
import lcom.utils.models.Graph;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class LCOM1 implements ILCOM {
    private SM_Type type;
    private Graph graph;

    @Override
    public double compute(SM_Type type) {
        int totalMethods = type.getMethodList().size();
        int totalMethodPairs = (totalMethods * (totalMethods -1))/2;
        int totalPairsWithCommonAttributes = 0;
        SM_Method[] methods = type.getMethodList().toArray(new SM_Method[type.getMethodList().size()]);
        for (int i = 0; i < totalMethods; i++) {
            for (int j = i + 1; j < totalMethods; j++) {
                List<SM_Field> fields1 = methods[i].getNonStaticFieldAccesses();
                List<SM_Field> fields2 = methods[j].getNonStaticFieldAccesses();
                Set<SM_Field> fieldSet1 = new HashSet<>(fields1);
                Set<SM_Field> fieldSet2 = new HashSet<>(fields2);
                Set<SM_Field> intersectSet = fieldSet1.stream()
                        .filter(fieldSet2::contains)
                        .collect(Collectors.toSet());
                if (!intersectSet.isEmpty())
                    totalPairsWithCommonAttributes++;
            }
        }
            return totalMethodPairs - totalPairsWithCommonAttributes;
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
        for (SM_Field fieldVertex : method.getNonStaticFieldAccesses()) {
            graph.addEdge(new Edge(method, fieldVertex));
        }
    }

    private double computeLCOM() {
        graph.computeConnectedComponents();
        return graph.getConnectedComponnents().size();
    }
}
