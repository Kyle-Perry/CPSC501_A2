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

							printFieldVal(f.getType(), fieldObject, recursive);
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
						System.out.println("=============INSPECTING ELEMENT " + i + "=============");
						inspect(Array.get(obj, i), recursive);
						System.out.println("========INSPECTION OF ELEMENT " + i + " COMPLETED======");
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

	public void printFieldVal(Class fieldClass, Object obj, boolean recursive) {
		if(obj != null ) {
			Class objClass = obj.getClass();

			if(objClass.isArray()) {
				System.out.print("[");
				for(int i = 0; i < Array.getLength(obj); i++){
					Object elem = Array.get(obj, i);
					if(elem != null)
					{
						printFieldVal(elem.getClass(), elem, recursive);
					}
					if(i < Array.getLength(obj)-1)
						System.out.print(", ");
				}
				System.out.print("]");
			}
			else if(obj.getClass() == Integer.class) {
				System.out.print(((Integer)obj).intValue());
			}
			else if(obj.getClass() == Double.class) {
				System.out.print(((Double)obj).doubleValue());
			}	
			else if(obj.getClass() == Float.class) {
				System.out.print(((Float)obj).floatValue());
			}
			else if(obj.getClass() == Byte.class) {
				System.out.print(((Byte)obj).byteValue());
			}		
			else if(obj.getClass() == Short.class) {
				System.out.print(((Short)obj).shortValue());
			}	
			else if(obj.getClass() == Long.class) {
				System.out.print(((Long)obj).longValue());
			}		
			else if(obj.getClass() == Character.class) {
				System.out.print(((Character)obj).charValue());
			}			
			else if(obj.getClass() == Boolean.class) {
				System.out.print(((Boolean)obj).booleanValue());
			}				
			else {
				System.out.print(objClass.getName()+"@"+obj.hashCode());
				if(recursive) {
					System.out.println("\n=========BEGINNING INSPECTION OF FIELD: " + objClass.getName() + "============");
					inspect(obj, recursive);
					System.out.println("\n=========INSPECTION OF FIELD: " + objClass.getName() + " COMPLETED============");
				}
			}
		}
		else
			System.out.print("null");
	}
}