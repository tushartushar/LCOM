package lcom.sourceModel;

import java.util.ArrayList;
import java.util.List;

public class TypeInfo {

	private SM_Type typeObj;
	private boolean primitiveType;
	private String objPrimitiveType;
	private boolean parametrizedType;
	private List<SM_Type> nonPrimitiveTypeParameters = new ArrayList<>();

	SM_Type getTypeObj() {
		return typeObj;
	}
	
	void setTypeObj(SM_Type typeObj) {
		this.typeObj = typeObj;
	}
	
	boolean isPrimitiveType() {
		return primitiveType;
	}
	
	void setPrimitiveType(boolean primitiveType) {
		this.primitiveType = primitiveType;
	}

	void setObjPrimitiveType(String objType) {
		this.objPrimitiveType = objType;
	}

	void setParametrizedType(boolean parametrizedType) {
		this.parametrizedType = parametrizedType;
	}
	
	List<SM_Type> getNonPrimitiveTypeParameters() {
		return nonPrimitiveTypeParameters;
	}
	
	private String getStringOfNonPrimitiveParameters() {
		String output = "[";
		for (SM_Type type : nonPrimitiveTypeParameters) {
			output += type.getName() + ", "; 
		}
		return removeLastComma(output) + "]";
	}
	
	private String removeLastComma(String str) {
		return (str.length() > 2) ? str.substring(0, str.length() - 2) : str;
	}
	 
	int getNumOfNonPrimitiveParameters() {
		return getNonPrimitiveTypeParameters().size();
	}
	
	void addNonPrimitiveTypeParameter(SM_Type element) {
		nonPrimitiveTypeParameters.add(element);
	}

	@Override
	public String toString() {
		return "TypeInfo [typeObj=" + typeObj + ", primitiveType=" + primitiveType + ", objPrimitiveType=" + objPrimitiveType
						+ ", parametrizedType=" + parametrizedType + ", nonPrimitiveTypeParameters="
						+ getStringOfNonPrimitiveParameters() + "]";
	}

}
