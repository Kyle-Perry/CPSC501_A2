import java.lang.reflect.*;
import java.util.Vector;

public class Inspector {

	public void inspect(Object obj, boolean recursive)
	{
		Vector<Class<?>> classObjects = new Vector<Class<?>>();

		Class<?> classObject;

		if(obj!= null) {
			classObjects.insertElementAt(obj.getClass(), 0);
			while(!classObjects.isEmpty())
			{
				classObject = classObjects.firstElement();
				classObjects.remove(0);

				Class<?> superClassObj = classObject.getSuperclass();
				Class<?>[] interfaces = classObject.getInterfaces();
				Constructor<?>[] constructors = classObject.getDeclaredConstructors();
				Method[] methods = classObject.getDeclaredMethods();
				Field[] fields = classObject.getDeclaredFields();

				System.out.print("Class Name: ");
				if(classObject.isArray())
					System.out.println(getArrayInfo(classObject, obj));
				else
					System.out.println(classObject.getName());

				if(superClassObj != null) {
					System.out.println("Superclass: " + superClassObj.getName());
					classObjects.add(superClassObj);
				}
				if(interfaces.length > 0) {
					System.out.print("Interfaces: ");
					for(Class<?> i: interfaces) {
						System.out.print(i.getName() + " ");
						classObjects.add(i);
					}
					System.out.println();
				}

				if(constructors.length > 0)
				{
					System.out.println("Declared Constructors:");
					for(Constructor<?> c: constructors) {
						Class<?>[] params = c.getParameterTypes();
						System.out.print(modifierString(c.getModifiers()));
						System.out.print(c.getName()+ "(");
						if(params.length > 0) {
							for(int i = 0; i < params.length; i++) {
								System.out.printf(params[i].getName());
								if(i + 1 < params.length)
									System.out.print(", ");
							}
						}	
						System.out.println(") ");
					}
					System.out.println();
				}
				if(methods.length > 0)
				{
					System.out.println("Declared Methods: ");
					for(Method m: methods) {
						Class<?>[] params = m.getParameterTypes();
						Class<?>[] exceptions = m.getExceptionTypes();
						System.out.print(modifierString(m.getModifiers()));
						System.out.print(m.getReturnType().getName() + " ");
						System.out.print(m.getName()+ "(");
						if(params.length > 0) {
							for(int i = 0; i < params.length; i++) {
								System.out.printf(params[i].getName());
								if(i + 1 < params.length)
									System.out.print(", ");
							}
						}	
						System.out.print(") ");
						if(exceptions.length > 0)
						{
							System.out.print("throws ");
							for(Class<?> e: exceptions)
							{
								System.out.print(e.getName() + " ");
							}
						}
						System.out.println();
					}
					System.out.println();
				}

				if(fields.length > 0)
				{
					System.out.println("Declared Fields:");
					for(Field f: fields) {
						System.out.print(modifierString(f.getModifiers()));
						System.out.print(f.getType().getName()+ " ");
						System.out.print(f.getName());					
						System.out.print(" = ");
						try{
							f.setAccessible(true);

							Object fieldObject;
							if(!Modifier.isStatic(f.getModifiers()))
								fieldObject = f.get(obj);
							else
								fieldObject = f.get(null);

							if(f.getType().isArray())
								System.out.print(getArrayInfo(f.getType(),fieldObject)+ " ");

							System.out.print(printFieldVal(f.getType(), fieldObject));
							if(recursive && !(f.getType().isPrimitive())) {
								System.out.println("\n=========BEGINNING INSPECTION OF FIELD: " + f.getName() + " in " + classObject.getName() + "============");
								inspect(fieldObject, recursive);
								System.out.println("\n=========INSPECTION OF FIELD: " + f.getName() + " in " + classObject.getName() + " COMPLETED============");
							}
						}
						catch(Exception e){
							System.out.print(" !FAILED ACCESS!\n");
							e.printStackTrace();
						}
						System.out.println();
					}
				}
				if(classObject.isArray())
				{
					for(int i = 0; i < Array.getLength(obj); i++) {
						System.out.println("INSPECTING ELEMENT " + i + " OF ARRAY " + classObject.getName());
						inspect(Array.get(obj, i), recursive);
						System.out.println("END OF INSPECTION FOR ELEMENT " + i + " OF ARRAY " + classObject.getName() + '\n');
					}
				}
				System.out.println();
			}
		}
		else
		{
			System.out.println("null");
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
		if(Modifier.isFinal(m))
			modifiers += "final ";
		if(Modifier.isStatic(m))
			modifiers += "static ";
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

	public String getArrayInfo(Class classObject, Object obj) {
		if(classObject.isArray()) {
			String arrayInfo = "";
			String classObjString = classObject.getName();
			Object arrElement = obj;
			char cur;

			arrayInfo = arrayInfo + "[" + Array.getLength(arrElement) + "]";
			classObjString = classObjString.substring(1);
			while(!classObjString.isEmpty())
			{
				switch(cur = classObjString.charAt(0))
				{
				case '[':
					arrElement = Array.get(arrElement, 0);
					arrayInfo = arrayInfo + "[" + Array.getLength(arrElement) + "]";
					classObjString = classObjString.substring(1);
					break;
				case 'B':
					return "byte" + arrayInfo;
				case 'C':
					return "char" + arrayInfo;
				case 'D':
					return "double" + arrayInfo;
				case 'F':
					return "float" + arrayInfo;
				case 'I':
					return "int" + arrayInfo;
				case 'J':
					return "long" + arrayInfo;
				case 'S':
					return "short" + arrayInfo;
				case 'Z':
					return "boolean" + arrayInfo;
				case 'L':
					classObjString = classObjString.substring(1, classObjString.length() - 1);
					return classObjString + arrayInfo;
				}
			}
			return arrayInfo;

		}
		else
		{
			return classObject.getName();
		}
	}

	public String printFieldVal(Class fieldClass, Object obj) {
		String output = "";

		if(obj != null ) {
			Class objClass = obj.getClass();

			if(objClass.isArray()) {
				output += "[";
				for(int i = 0; i < Array.getLength(obj); i++){
					Object elem = Array.get(obj, i);
					if(elem != null)
						printFieldVal(elem.getClass(), elem);
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
			else {
				output += objClass.getName()+"@"+obj.hashCode();
			}
		}
		else
			output += "null";
		return output;
	}
	
}