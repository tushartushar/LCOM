package lcom.sourceModel;

import java.io.PrintWriter;

import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;

public class SM_LocalVar extends SM_EntitiesWithType {
	private SM_Method parentMethod;
	private VariableDeclarationStatement localVarDecl;

	SM_LocalVar(VariableDeclarationStatement varDecl, VariableDeclarationFragment localVar, SM_Method method) {
		parentMethod = method;
		name = localVar.getName().toString();
		localVarDecl = varDecl;
	}

	@Override
	public SM_Type getParentType() {
		return this.parentMethod.getParentType();
	}

	@Override
	public void resolve() {
		Resolver resolver = new Resolver();
		typeInfo = resolver.resolveVariableType(localVarDecl.getType(), parentMethod.getParentType().getParentPkg().getParentProject(), getParentType());
	}
	
	@Override
	public String toString() {
		return "Local variable name=" + name
		+ ", type=" + localVarDecl.getType()
		+ ", isParameterizedType=" + localVarDecl.getType().isParameterizedType();
	}
	
}
