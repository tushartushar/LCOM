package LCOMTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import lcom.InputArgs;
import lcom.sourceModel.SM_Method;
import lcom.sourceModel.SM_Package;
import lcom.sourceModel.SM_Project;
import lcom.sourceModel.SM_Type;

public class SM_Method_CalledMethodsTests extends LCOMTests {

	private SM_Project project;

		
	@Before
	public void setUp() {
		createFileForArguments(CALLED_METHOD_TEST_INPUT_FILE_PATH, CALLED_METHOD_TEST_INPUT_FILE_CONTENT);
		project = new SM_Project(new InputArgs(getTestingPath() + File.separator + "test_inputs2", getTestingPath()));
		project.parse();
		project.resolve();
	}
	
	@Test
	public void SM_Method_CalledMethods_withinClass() {
		SM_Type type = getDesiredType();
		if (type==null)
			fail();
		SM_Method method = getDesiredMethod(type, "publicMethod");
		if (method == null)
			fail();
		if (method.getCalledMethods().size()==1)
		{
			SM_Method calledMethod = method.getCalledMethods().get(0);
			assertEquals("TestMethods", calledMethod.getParentType().getName());
			assertEquals("print", calledMethod.getName());
		}
	}
	
	@Test
	public void SM_Method_CalledMethods_outsideClass() {
		SM_Type type = getDesiredType();
		if (type==null)
			fail();
		SM_Method method = getDesiredMethod(type, "print");
		if (method == null)
			fail();
		if (method.getCalledMethods().size()==1)
		{
			SM_Method calledMethod = method.getCalledMethods().get(0);
			assertEquals("TestMethods2", calledMethod.getParentType().getName());
			assertEquals("print2", calledMethod.getName());
		}
	}

	@Test
	public void SM_Method_CalledMethods_staticMethod() {
		SM_Type type = getDesiredType();
		if (type==null)
			fail();
		SM_Method method = getDesiredMethod(type, "count");
		if (method == null)
			fail();
		if (method.getCalledMethods().size()==1)
		{
			SM_Method calledMethod = method.getCalledMethods().get(0);
			assertEquals("Logger", calledMethod.getParentType().getName());
			assertEquals("log", calledMethod.getName());
		}
	}
	
	private SM_Method getDesiredMethod(SM_Type type, String name) {
		for(SM_Method method:type.getMethodList())
			if(method.getName().equals(name))
				return method;
		return null;
	}

	private SM_Type getDesiredType() {
		for(SM_Package pkg:project.getPackageList())
			for (SM_Type aType:pkg.getTypeList())
				if(aType.getName().equals("TestMethods"))
					return aType;
		return null;
	}
}
