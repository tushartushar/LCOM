package lcom.sourceModel;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.SuperMethodInvocation;

public class MethodInvVisitor extends ASTVisitor {
	List<MethodInvocation> calledMethods = new ArrayList<MethodInvocation>();

	public MethodInvVisitor(MethodDeclaration methodDeclaration) {
	}

	@Override
	public boolean visit(MethodInvocation method) {
		calledMethods.add(method);
		method.resolveMethodBinding();
		return super.visit(method);
	}
	
	public boolean visit(SuperMethodInvocation method) {
		return super.visit(method);
	}

	public List<MethodInvocation> getCalledMethods() {
		return calledMethods;
	}
}
