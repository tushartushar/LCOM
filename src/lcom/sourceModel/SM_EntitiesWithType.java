package lcom.sourceModel;

public abstract class SM_EntitiesWithType extends SM_SourceItem {

    TypeInfo typeInfo;

    boolean isPrimitiveType() {
        return typeInfo.isPrimitiveType();
    }

    public SM_Type getParentType() {
        return null;
    }

    public SM_Type getType() {
        return typeInfo.getTypeObj();
    }

    @Override
    public void parse() {
    }
}
