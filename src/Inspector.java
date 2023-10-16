import java.lang.reflect.*;
import java.util.Vector;

public class Inspector {

	public void inspect(Object obj, boolean recursive)
	{
		Vector<Class<?>> classObjects = new Vector<Class<?>>();
		//TODO INSPECT target AND DISPLAY INFORMATION ABOUT IT
		Class<?> classObject;

	
		classObjects.add(obj.getClass());
		while(!classObjects.isEmpty())
		{
		classObject = classObjects.firstElement();
		classObjects.remove(0);
		
		Class<?> superClassObj = classObject.getSuperclass();
		Class<?>[] interfaces = classObject.getInterfaces();
		Constructor<?>[] constructors = classObject.getDeclaredConstructors();
		Method[] methods = classObject.getDeclaredMethods();
		Field[] fields = classObject.getDeclaredFields();
		
		System.out.println("Class Name = " + classObject.getName());
		if(superClassObj != null) {
			System.out.println("Superclass = " + superClassObj.getName());
			classObjects.add(superClassObj);
		}
		if(interfaces.length > 0) {
			System.out.println("Interfaces: ");
			for(Class<?> i: interfaces) {
				System.out.println(i.getName());
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
				try{
					f.setAccessible(true);
					
					Object fieldObject;
					if(!Modifier.isStatic(f.getModifiers()))
						fieldObject = f.get(obj);
					else
						fieldObject = f.get(null);
						
					System.out.print(" = ");

					if(fieldObject != null ) {
						if(fieldObject.getClass() == Integer.class) {
							System.out.print(((Integer)fieldObject).intValue());
						}
						else if(fieldObject.getClass() == Double.class) {
							System.out.print(((Double)fieldObject).doubleValue());
						}	
						else if(fieldObject.getClass() == Float.class) {
							System.out.print(((Float)fieldObject).floatValue());
						}
						else if(fieldObject.getClass() == Byte.class) {
							System.out.print(((Byte)fieldObject).byteValue());
						}		
						else if(fieldObject.getClass() == Short.class) {
							System.out.print(((Short)fieldObject).shortValue());
						}	
						else if(fieldObject.getClass() == Long.class) {
							System.out.print(((Long)fieldObject).longValue());
						}		
						else if(fieldObject.getClass() == Character.class) {
							System.out.print(((Character)fieldObject).charValue());
						}			
						else if(fieldObject.getClass() == Boolean.class) {
							System.out.print(((Boolean)fieldObject).booleanValue());
						}				
						else {
							System.out.print(fieldObject.getClass().getName()+"@"+fieldObject.hashCode());
						}
					}
					else
						System.out.print("null");
				}
				catch(Exception e){
					System.out.print(" !FAILED ACCESS!\n" + e.toString());
				}
				System.out.println();
			}
		}
		}
	}
	public String modifierString(int m) {
		String modifiers = "";
		if(Modifier.isPublic(m))
			modifiers += "public ";
		if(Modifier.isProtected(m))
			modifiers += "protected";
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

}