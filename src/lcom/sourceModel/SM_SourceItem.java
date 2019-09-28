package lcom.sourceModel;

import java.io.PrintWriter;
import java.lang.reflect.Modifier;

public abstract class SM_SourceItem {
	protected String name;
	AccessStates accessModifier;

	/**
	 * This is the first pass of parsing a source code entity.
	 */
	public abstract void parse();

	/**
	 * This method establishes relationships among source-code entities. Such
	 * relationships include variable types, super/sub types, etc.
	 */
	public abstract void resolve();

	public String getName() {
		return name;
	}

	void setAccessModifier(int modifier) {
		if (Modifier.isPublic(modifier))
			accessModifier = AccessStates.PUBLIC;
		else if (Modifier.isProtected(modifier))
			accessModifier = AccessStates.PROTECTED;
		else if (Modifier.isPrivate(modifier))
			accessModifier = AccessStates.PRIVATE;
		else
			accessModifier = AccessStates.DEFAULT;
	}

	void print(PrintWriter writer, String str) {
		if (writer != null)
		{
			writer.println(str);
			writer.flush();
		}
		else
			System.out.println(str);
	}
}
