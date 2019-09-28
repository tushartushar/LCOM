package lcom.sourceModel;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.MarkerAnnotation;

public class MarkerAnnotationVisitor extends ASTVisitor {
    private boolean isTest;
    private boolean isOveridden;

    public boolean isTest() {
        return isTest;
    }
    public boolean visit(MarkerAnnotation annotation){
        isTest = false;
        if(annotation.toString().equals("@Test"))
            isTest = true;
        if(annotation.toString().equals("@Override"))
            isOveridden = true;
        return super.visit(annotation);
    }

    public boolean isOverridden() {
        return isOveridden;
    }
}
