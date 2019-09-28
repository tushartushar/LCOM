package lcom.sourceModel;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;

public class VariableVisitor extends ASTVisitor {
	private List<SM_Parameter> parameters = new ArrayList<SM_Parameter>();
	private SM_Method parentMethod;
	
	VariableVisitor(SM_Method methodObj) {
		super();
		this.parentMethod = methodObj;
	}

	@Override
	public boolean visit(SingleVariableDeclaration variable) {
		SM_Parameter newParameter = new SM_Parameter(variable, parentMethod);
		parameters.add(newParameter);

		return super.visit(variable);
	}

	List<SM_Parameter> getParameterList() {
		return parameters;
	}
}
