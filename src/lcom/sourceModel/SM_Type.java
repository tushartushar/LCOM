package lcom.sourceModel;

import lcom.visitors.StaticFieldAccessVisitor;
import org.eclipse.jdt.core.dom.*;

import java.util.ArrayList;
import java.util.List;

public class SM_Type extends SM_SourceItem {
    private boolean isAbstract = false;
    private boolean isInterface = false;
    private SM_Package parentPkg;

    private TypeDeclaration typeDeclaration;
    private boolean nestedClass;

    private List<SM_Type> superTypes = new ArrayList<>();
    private List<SM_Type> subTypes = new ArrayList<>();
    private List<SM_Type> referencedTypeList = new ArrayList<>();
    private List<SM_Type> typesThatReferenceThisList = new ArrayList<>();
    private List<SM_Type> nestedTypesList = new ArrayList<>();
    private List<ImportDeclaration> importList = new ArrayList<>();
    private List<SM_Method> methodList = new ArrayList<>();
    private List<SM_Field> fieldList = new ArrayList<>();
    private List<Name> staticFieldAccesses = new ArrayList<>();
    private List<SM_Type> staticFieldAccessList = new ArrayList<>();
    private List<SM_Type> staticMethodInvocations = new ArrayList<>();

    public SM_Type(TypeDeclaration typeDeclaration, CompilationUnit compilationUnit, SM_Package pkg) {
        parentPkg = pkg;
        if (typeDeclaration == null || compilationUnit == null)
            throw new NullPointerException();

        name = typeDeclaration.getName().toString();
        this.typeDeclaration = typeDeclaration;
        setTypeInfo();
        setAccessModifier(typeDeclaration.getModifiers());
        setImportList(compilationUnit);
    }

    public List<SM_Type> getSuperTypes() {
        return superTypes;
    }

    public List<SM_Type> getSubTypes() {
        return subTypes;
    }

    public List<SM_Type> getReferencedTypeList() {
        return referencedTypeList;
    }

    public List<SM_Type> getTypesThatReferenceThis() {
        return typesThatReferenceThisList;
    }

    TypeDeclaration getTypeDeclaration() {
        return typeDeclaration;
    }

    private void addReferencedTypeList(SM_Type type) {
        referencedTypeList.add(type);
    }

    void addStaticMethodInvocation(SM_Type type) {
        if (!this.staticMethodInvocations.contains(type)) {
            this.staticMethodInvocations.add(type);
        }
    }

    void addNestedClass(SM_Type type) {
        if (!this.nestedTypesList.contains(type)) {
            this.nestedTypesList.add(type);
        }
    }

    SM_Type getNestedTypeFromName(String typeName) {
        for (SM_Type nestedType : this.nestedTypesList) {
            if (nestedType.name.equals(typeName)) {
                return nestedType;
            }
        }
        return null;
    }

    List<SM_Type> getNestedTypes() {
        return this.nestedTypesList;
    }

    private boolean containsTypeInReferencedTypeList(SM_Type type) {
        return referencedTypeList.contains(type);
    }

    private void addTypesThatReferenceThisList(SM_Type type) {
        typesThatReferenceThisList.add(type);
    }

    private boolean containsTypeInTypesThatReferenceThisList(SM_Type type) {
        return typesThatReferenceThisList.contains(type);
    }

    private void setTypeInfo() {
        int modifier = typeDeclaration.getModifiers();
        if (Modifier.isAbstract(modifier)) {
            isAbstract = true;
        }
        if (typeDeclaration.isInterface()) {
            isInterface = true;
        }
    }

    public boolean isAbstract() {
        return isAbstract;
    }

    public boolean isInterface() {
        return isInterface;
    }

    void setNestedClass(TypeDeclaration referredClass) {
        nestedClass = true;
    }

    public boolean isNestedClass() {
        return nestedClass;
    }

    private void setImportList(CompilationUnit unit) {
        ImportVisitor importVisitor = new ImportVisitor();
        unit.accept(importVisitor);
        List<ImportDeclaration> imports = importVisitor.getImports();
        if (imports.size() > 0)
            importList.addAll(imports);
    }

    List<ImportDeclaration> getImportList() {
        return importList;
    }

    private void setSuperTypes() {
        setSuperClass();
        setSuperInterface();
    }

    private void setSuperClass() {
        Type superclass = typeDeclaration.getSuperclassType();
        if (superclass != null) {
            addSuperType(superclass);
        }
    }

    private void setSuperInterface() {
        List<Type> superInterfaces = typeDeclaration.superInterfaceTypes();
        if (superInterfaces != null) {
            for (Type superInterface : superInterfaces) {
                addSuperType(superInterface);
            }
        }
    }

    private void addSuperType(Type superInterface) {
        SM_Type inferredType = (new Resolver()).resolveType(superInterface, parentPkg.getParentProject());
        if (inferredType != null) {
            // Tushar - Added this condition to avoid adding self reference in supertype
            if (!(inferredType.getName().equals(name) && inferredType.getParentPkg().getName().equals(parentPkg.getName())))
            {
                superTypes.add(inferredType);
                inferredType.addThisAsChildToSuperType(this);
            }
        }
    }

    private void addThisAsChildToSuperType(SM_Type child) {
        if (!subTypes.contains(child)) {
            subTypes.add(child);
        }
    }

    public List<SM_Method> getMethodList() {
        return methodList;
    }

    public List<SM_Field> getFieldList() {
        return fieldList;
    }

    public SM_Package getParentPkg() {
        return parentPkg;
    }

    private void parseMethods() {
        for (SM_Method method : methodList) {
            method.parse();
        }
    }

    private void parseFields() {
        for (SM_Field field : fieldList) {
            field.parse();
        }
    }

    @Override
    public void parse() {
        FieldVisitor fieldVisitor = new FieldVisitor(this);
        typeDeclaration.accept(fieldVisitor);
        List<SM_Field> fList = fieldVisitor.getFields();
        if (fList.size() > 0)
            fieldList.addAll(fList);
        parseFields();

        StaticFieldAccessVisitor fieldAccessVisitor = new StaticFieldAccessVisitor();
        typeDeclaration.accept(fieldAccessVisitor);
        staticFieldAccesses = fieldAccessVisitor.getStaticFieldAccesses();

        MethodVisitor methodVisitor = new MethodVisitor(typeDeclaration, this);
        typeDeclaration.accept(methodVisitor);
        List<SM_Method> mList = methodVisitor.getMethods();
        if (mList.size() > 0)
            methodList.addAll(mList);
        parseMethods();
    }

    @Override
    public void resolve() {
        setSuperTypes();
        for (SM_Field field : fieldList)
            field.resolve();
        setStaticAccessList();
        for (SM_Method method : methodList)
            method.resolve();
        setReferencedTypes();
        setTypesThatReferenceThis();
    }

    private void setStaticAccessList() {
        staticFieldAccessList = (new Resolver()).inferStaticAccess(staticFieldAccesses, this);
    }

    private void setReferencedTypes() {
        for (SM_Field field : fieldList)
            if (!field.isPrimitiveType()) {
                addUniqueReference(this, field.getType(), false);
            }
        for (SM_Method method : methodList) {
            for (SM_Type refType : method.getReferencedTypeList()) {
                addUniqueReference(this, refType, false);
            }
        }
        for (SM_Type staticAccessType : staticFieldAccessList) {
            addUniqueReference(this, staticAccessType, false);
        }
        for (SM_Type methodInvocation : staticMethodInvocations) {
            addUniqueReference(this, methodInvocation, false);

        }
    }

    private void setTypesThatReferenceThis() {
        for (SM_Type refType : referencedTypeList) {
            addUniqueReference(refType, this, true);
        }
    }

    private void addUniqueReference(SM_Type type, SM_Type typeToAdd, boolean invardReference) {
        if (typeToAdd == null)
            return;
        if (invardReference) {
            if (!type.containsTypeInTypesThatReferenceThisList(typeToAdd)) {
                type.addTypesThatReferenceThisList(typeToAdd);//FAN-IN?
            }
        } else {
            if (!type.containsTypeInReferencedTypeList(typeToAdd)) {
                type.addReferencedTypeList(typeToAdd);//FAN-OUT?
            }
        }
    }

    void extractMethodMetrics() {
    }

    @Override
    public String toString() {
        return "Type=" + name;
    }
}
