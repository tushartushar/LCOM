package lcom.sourceModel;

import org.eclipse.jdt.core.dom.*;

import java.util.ArrayList;
import java.util.List;

public class NewStatementVisitor extends ASTVisitor {
    private final MethodDeclaration methodDeclaration;
    private List<Type> typeList = new ArrayList<>();

    NewStatementVisitor(MethodDeclaration methodDeclaration) {
        this.methodDeclaration = methodDeclaration;
    }

    public boolean visit(ClassInstanceCreation instanceCreation){
        if(instanceCreation.getType() != null)
            typeList.add(instanceCreation.getType());
        return super.visit(instanceCreation);
    }

    List<Type> getTypeList(){
        return typeList;
    }
}
