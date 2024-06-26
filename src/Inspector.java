import java.lang.reflect.*;
import java.util.ArrayList;

public class Inspector {
	ArrayList<Integer> inspectedIDS = new ArrayList<Integer>();

	public void inspect(Object obj, boolean recursive)
	{
		if(obj!= null) {
			if(inspectedIDS.contains(obj.hashCode())) {
				System.out.print("Object " + obj.getClass().getName() + "@" + obj.hashCode() + " has already been inspected. Skipping inspection...");
			}
			else
			{
				inspectedIDS.add(obj.hashCode());
				ArrayList<Class<?>> classObjects = new ArrayList<Class<?>>();
				Class<?> classObject;


				classObjects.add(0, obj.getClass());
				while(!classObjects.isEmpty())
				{
					classObject = classObjects.get(0);
					classObjects.remove(0);

					System.out.print("Class Name: ");
					if(classObject.isArray())
						System.out.println(getArrayInfo(classObject, obj));
					else
						System.out.println(classObject.getName());
					System.out.println("Hash Code: " + obj.hashCode());

					handleSuperclass(classObjects, classObject);
					handleInterfaces(classObjects, classObject);
					handleConstructors(classObject);
					handleMethods(classObject);
					handleFields(classObject, obj, recursive);
					inspectArray(classObject, obj, recursive);
					System.out.println();

				}
			}
		}
		else
		{
			System.out.println("*NULL POINTER*");
		}
	}

	public String modifierString(int m) {
		String modifiers = "";
		if(Modifier.isPublic(m))
			modifiers += "public ";
		if(Modifier.isProtected(m))
			modifiers += "protected ";
		if(Modifier.isPrivate(m))
			modifiers += "private ";
		if(Modifier.isAbstract(m))
			modifiers += "abstract ";
		if(Modifier.isStatic(m))
			modifiers += "static ";
		if(Modifier.isFinal(m))
			modifiers += "final ";
		if(Modifier.isInterface(m))
			modifiers += "interface ";
		if(Modifier.isNative(m))
			modifiers += "native ";
		if(Modifier.isStrict(m))
			modifiers += "strict ";
		if(Modifier.isSynchronized(m))
			modifiers += "synchronized ";
		if(Modifier.isTransient(m))
			modifiers += "transient ";
		if(Modifier.isVolatile(m))
			modifiers += "volatile ";
		return modifiers;
	}

	public String getArrayInfo(Class<?> classObject, Object obj) {
		if(classObject.isArray()) {
			String arrayInfo = "";
			String classObjString = classObject.getName();
			Object arrElement = obj;

			arrayInfo = arrayInfo + "[" + Array.getLength(arrElement) + "]";
			classObjString = classObjString.substring(1);
			while(classObjString.charAt(0) == '[')
			{
				arrElement = Array.get(arrElement, 0);
				arrayInfo = arrayInfo + "[" + Array.getLength(arrElement) + "]";
				classObjString = classObjString.substring(1);
			}
			
			return  getArrayType(classObjString) + arrayInfo;

		}
		else
		{
			return classObject.getName();
		}
	}

	public String getArrayInfo(Class<?> classObject) {
		if(classObject.isArray()) {
			String arrayInfo = "";
			String classObjString = classObject.getName();

			arrayInfo = arrayInfo + "[]";
			classObjString = classObjString.substring(1);
			while(classObjString.charAt(0) == '[')
			{
				arrayInfo = arrayInfo + "[]";
				classObjString = classObjString.substring(1);
			}
			
			return  getArrayType(classObjString) + arrayInfo;

		}
		else
		{
			return classObject.getName();
		}
	}
	
	public String getFieldVal(Class<?> fieldClass, Object obj) {
		String output = "";

		if(obj != null ) {
			Class<?> objClass = obj.getClass();

			if(objClass.isArray()) {
				output += "[";
				for(int i = 0; i < Array.getLength(obj); i++){
					Object elem = Array.get(obj, i);
					if(elem != null)
						output += getFieldVal(elem.getClass(), elem);
					else
						output += "null";

					if(i < Array.getLength(obj)-1)
						output += ", ";
				}
				output += "]";
			}
			else if(obj.getClass() == Integer.class) {
				output += ((Integer)obj).intValue();
			}
			else if(obj.getClass() == Double.class) {
				output += ((Double)obj).doubleValue();
			}	
			else if(obj.getClass() == Float.class) {
				output += ((Float)obj).floatValue();
			}
			else if(obj.getClass() == Byte.class) {
				output += ((Byte)obj).byteValue();
			}		
			else if(obj.getClass() == Short.class) {
				output += ((Short)obj).shortValue();
			}	
			else if(obj.getClass() == Long.class) {
				output += ((Long)obj).longValue();
			}		
			else if(obj.getClass() == Character.class) {
				output += ((Character)obj).charValue();
			}			
			else if(obj.getClass() == Boolean.class) {
				output += ((Boolean)obj).booleanValue();
			}
			else if(obj.getClass() == String.class) {
				output += obj.toString();
			}	
			else {
				output += objClass.getName()+"@"+obj.hashCode();
			}
		}
		else
			output = "null";
		return output;
	}

	private void handleSuperclass(ArrayList<Class<?>> classObjects, Class<?> classObject) {
		Class<?> superClassObj = classObject.getSuperclass();			
		if(superClassObj != null) {
			System.out.println("Superclass: " + superClassObj.getName());
			classObjects.add(superClassObj);
		}
	}

	private void handleInterfaces(ArrayList<Class<?>> classObjects, Class<?> classObject) {
		Class<?>[] interfaces = classObject.getInterfaces();
		if(interfaces.length > 0) {
			System.out.print("Interfaces: ");
			for(Class<?> i: interfaces) {
				System.out.print(i.getName() + " ");
				classObjects.add(i);
			}
			System.out.println();
		}
	}

	private void handleConstructors(Class<?> classObject){
		Constructor<?>[] constructors = classObject.getDeclaredConstructors();
		if(constructors.length > 0)
		{
			System.out.println("Declared Constructors:");
			for(Constructor<?> c: constructors) {
				System.out.println(getConstructorString(c));	
			}
		}
		System.out.println();
	}


	private void handleMethods(Class<?> classObject) {
		Method[] methods = classObject.getDeclaredMethods();
		if(methods.length > 0)
		{
			System.out.println("Declared Methods: ");
			for(Method m: methods) {
				System.out.println(getMethodString(m));
			}
			System.out.println();
		}
	}

	private void handleFields(Class<?> classObject, Object obj, boolean recursive) {
		Field[] fields = classObject.getDeclaredFields();
		Object fieldObject;

		if(fields.length > 0)
		{
			System.out.println("Declared Fields:");
			for(Field f: fields) {


				try{
					f.setAccessible(true);

					if(!Modifier.isStatic(f.getModifiers()))
						fieldObject = f.get(obj);
					else
						fieldObject = f.get(null);
					
					System.out.print(getFieldString(f, fieldObject) + " = ");

					System.out.print(getFieldVal(f.getType(), fieldObject));
					if(recursive && !(f.getType().isPrimitive()) && fieldObject != null) {
						System.out.println("\n=========BEGINNING INSPECTION OF FIELD: " + f.getName() + " in " + classObject.getName() + "============");
						inspect(fieldObject, recursive);
						System.out.println("\n=========INSPECTION OF FIELD: " + f.getName() + " in " + classObject.getName() + " COMPLETED============");
					}
				}
				catch(Exception e){
					System.out.print(modifierString(f.getModifiers()) + getArrayInfo(f.getType()) + ' ' + f.getName() + " = !FAILED ACCESS!\n");
				}
				System.out.println();
			}
		}
	}


	private void inspectArray(Class<?> classObject, Object obj, boolean recursive){
		if(classObject.isArray())
		{
			System.out.println("Array contents: " + getFieldVal(classObject, obj));
			for(int i = 0; i < Array.getLength(obj); i++) {
				if(Array.get(obj, i) != null) {
					System.out.println("INSPECTING ELEMENT " + i + " OF ARRAY " + classObject.getName());
					inspect(Array.get(obj, i), recursive);
					System.out.println("END OF INSPECTION FOR ELEMENT " + i + " OF ARRAY " + classObject.getName() + '\n');
				}
				else
					System.out.println("ELEMENT " + i + " OF ARRAY " + classObject.getName() + " IS NULL. SKIPPING INSPECTION...");
			}
		}
	}
	public String getParameterString(Parameter[] parameters) {
		String output = "";
		if(parameters.length > 0) {
			for(int i = 0; i < parameters.length; i++) {
				output += getArrayInfo(parameters[i].getType());
				if(i + 1 < parameters.length)
					output += ", ";
			}
		}
		return output;
	}

	public String getMethodString(Method m) {
		String output = "";
		Class<?>[] exceptions = m.getExceptionTypes();
		output += modifierString(m.getModifiers());
		
		output += getArrayInfo(m.getReturnType()) +  " ";
		output += m.getName()+ "(";
		output += getParameterString(m.getParameters()) + ") ";
		if(exceptions.length > 0)
		{
			output += "throws ";
			for(Class<?> e: exceptions)
			{
				output += e.getName() + " ";
			}
		}
		return output;
	}

	public String getConstructorString(Constructor c) {
		String output;
		output = modifierString(c.getModifiers());
		output += c.getName()+ "(";
		output += getParameterString(c.getParameters()) + ") ";
		return output;
	}

	public String getFieldString(Field f, Object fObject) {
		String output = modifierString(f.getModifiers());
		if(f.getType().isArray())
			output+= getArrayInfo(f.getType(), fObject) + " ";
		else
			output += f.getType().getName()+ " ";
		return output + f.getName();	
	}
	
	public String getArrayType(String typeName) {
		switch(typeName.charAt(0))
		{
		case 'B':
			return "byte";
		case 'C':
			return "char";
		case 'D':
			return "double";
		case 'F':
			return "float";
		case 'I':
			return "int";
		case 'J':
			return "long";
		case 'S':
			return "short";
		case 'Z':
			return "boolean";
		case 'L':
			return typeName.substring(1, typeName.length() - 1);
		default:
			return "";
		}
	}
}