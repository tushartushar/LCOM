package lcom.sourceModel;

import java.lang.reflect.Modifier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

import lcom.utils.models.Vertex;

public class SM_Field extends SM_EntitiesWithType implements Vertex {
	private FieldDeclaration fieldDeclaration;
	private SM_Type parentType;
	private SM_Type nestedParentType = null;
	private boolean finalField = false;
	private boolean staticField = false;

	SM_Field(FieldDeclaration fieldDeclaration, VariableDeclarationFragment varDecl, SM_Type parentType) {
		this.fieldDeclaration = fieldDeclaration;
		this.parentType = parentType;
		setAccessModifier(fieldDeclaration.getModifiers());
		setFieldInfo(fieldDeclaration);
		name = varDecl.getName().toString();
		assignToNestedTypeIfNecessary();
	}
	
	private void setFieldInfo(FieldDeclaration field){
		int modifiers = field.getModifiers();
		if (Modifier.isFinal(modifiers)) 
			finalField =  true;
		if (Modifier.isStatic(modifiers)) 
			staticField =  true;
	}
	
	private void assignToNestedTypeIfNecessary() {
		if (parentType.getNestedTypes().size() < 1) {
		} else {
			String typeName = getNestedParentName();
			if(typeName != null) {
				typeName = typeName.trim();
				this.nestedParentType = parentType.getNestedTypeFromName(typeName);
				if(this.nestedParentType != null) {
				}
			}
		}
	}
	
	private String getNestedParentName() {
		final String regex = "public|private[ ]{1,}class[ ]{1,}([^\\{]*)[\\{]{1}";
		final String inputString = this.fieldDeclaration.getParent().toString();
		final Pattern pattern = Pattern.compile(regex);
		final Matcher matcher = pattern.matcher(inputString);
		
		String typeName = "";
		while (matcher.find()) {
			typeName = matcher.group(1);
			return typeName;
		}
		return "";
	}
	
	public boolean isFinal() {
		return finalField;
	}
	
	public boolean isStatic() {
		return staticField;
	}
	
	@Override
	public SM_Type getParentType() {
		return parentType;
	}

	@Override
	public void resolve() {
		Resolver resolver = new Resolver();
		typeInfo = resolver.resolveVariableType(fieldDeclaration.getType(), getParentType().getParentPkg().getParentProject(), getParentType());
	}
}
