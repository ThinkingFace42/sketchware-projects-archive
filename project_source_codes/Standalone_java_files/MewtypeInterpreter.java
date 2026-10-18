package com.my.cardbattle;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

class JavaMewEnvironment extends MewEnvironment {
	@Override
	public Object[] getSerializationObjects() {
		return (Object[])(null);
	}

	@Override
	public void showMessage(String message) {
		System.out.println(message);
		logMessage(message);
	}

	@Override
	public String getPlatformName() {
		return "Java CLI";
	}
	
	@Override
	public ArrayList<Class<?>> getInjectionClasses() {
		return null;
	}
}

abstract class MewEnvironment {
	public abstract Object[] getSerializationObjects();
	
	public abstract void showMessage(String message);
	
	int getMaxStackDepth() {
		// Change the maximum stack depth if you want
		return 100;
	}
	
	long getForeverLoopDetectionThreshold() {
		// Change this to change the maximum number of iterations a while loop is allowed to run
		return (long)1000000;
	}
	
	int getExecuteStringCap() {
		// Change the maximum number of times Exeucte-String can call recursively here if you want
		return 100;
	}
	
	public abstract String getPlatformName();
	
	public String getLog() {
		return sbLog.toString();
	}
	
	public void logMessage(String message) {
		sbLog.append(message);
		sbLog.append("\n");
	}
	
	public void clearLog() {
		sbLog = new StringBuilder();
	}
	
	public ArrayList<Class<?>> getInjectionClasses() {
		return new ArrayList<>();
	}
	
	public void onBeginInitClassList(MewtypeInterpreter.MewtypeClassList classList) {
		// Inject classes
		ArrayList<Class<?>> injectedClasses = this.getInjectionClasses();
		if (injectedClasses != null) {
			for (Class<?> c : injectedClasses) {
				classList.registerClass(c);
			}
		}
	}
	
	public void onEndInitClassList(MewtypeInterpreter.MewtypeClassList classList) { }
	
	public void onBeginProgramStep(MewNode step) { }
	
	public void onEndProgramStep(MewNode step) { }
	
	public HashMap<String, MewVariable> injectSpecialVariables() {
		return new HashMap<>();
	}
	
//	public MewVariable packageExternalList(java.util.List<?> list, Class<?> c) {
//		return new MewVariable(MewVariableType.List, new MewExternalList(list, false, c));
//	}
	
	public double getDoubleEpsilon() {
		return 1e-15;
	}
	
	public int getMaxCodeBlockDepth() { 
		return 150; 
	}
	
	public long getTotalIterationBudget() { 
		return 1000000; 
	}
	
//	public long getMaxTotalStringBytes() { 
//		return (long)1000000; 
//	}
	
	public int getMaxListSize() { 
		return 100000; 
	}
	
	public int getMaxStringLength() { 
		return 1 << 20; 
	}
	
	// The code below need not be changed between Java CLI (on Windows) and Sketchware (Android) releases
//	public HashMap<String, MewVariable> variables;
	public MewtypeVariableMap variables;
	public MewVariable lastExpression = new MewVariable(MewVariableType.Null, "333");
	public java.util.Random random;
	public StringBuilder sbLog;
	public HashMap<String, MewVariable> injectedVariables = new HashMap<>();
	public MewCoercion coercion = new MewCoercion();
	public RitsuJsonParser ritsu = new RitsuJsonParser();
	
	public boolean escapeLoopFlag;
	public boolean escapeProgramFlag;
	public int recursiveExecuteStringCap;
	public int codeBlockDepth;
	public long totalIterations;
//	public long totalStringBytes = 0;
	
	MewtypeInterpreter.MewtypeClassList _classList;
	
	public MewVariable getConstantValues(String name) {
		switch (name.toUpperCase().trim()) {
		case "HOLYNUMBER":
			return new MewVariable(MewVariableType.Integer, 3);
		case "BUNGOR":
			return new MewVariable(MewVariableType.String, "Tasty");
		case "LANGUAGE_NAME":
			return new MewVariable(MewVariableType.String, "Mewtype");
		case "LANGUAGE_VERSION":
			return new MewVariable(MewVariableType.String, "0.3 beta");
		case "LANGUAGE_BUILD":
			return new MewVariable(MewVariableType.String, "21 September 2026");
		case "LANGUAGE_VERSION_FLOAT":
			return new MewVariable(MewVariableType.Float, 0.3);
		case "BUILD_NUMBER":
			return new MewVariable(MewVariableType.Integer, 31);
		case "PATCH_NUMBER":
			return new MewVariable(MewVariableType.Integer, 0);
		case "MAX_STACK_DEPTH":
			return new MewVariable(MewVariableType.Integer, getMaxStackDepth());
		case "MAX_LOOP_ITERATION":
			return new MewVariable(MewVariableType.Float, (double)getForeverLoopDetectionThreshold());
		case "CURRENT_RECURSIVE_EXECUTE_STRING":
			return new MewVariable(MewVariableType.Integer, recursiveExecuteStringCap);
		case "RECURSIVE_EXECUTE_STRING_CAP":
			return new MewVariable(MewVariableType.Integer, this.getExecuteStringCap());
		case "MAX_STRING_LENGTH":
			return new MewVariable(MewVariableType.Float, (double)getMaxStringLength());
		case "MAX_ITERATIONS":
			return new MewVariable(MewVariableType.Float, (double)getTotalIterationBudget());
		case "CURRENT_ITERATIONS":
			return new MewVariable(MewVariableType.Float, (double)totalIterations);
		case "MAX_CODE_BLOCK_DEPTH":
			return new MewVariable(MewVariableType.Integer, getMaxCodeBlockDepth());
		case "CURRENT_CODE_BLOCK_DEPTH":
			return new MewVariable(MewVariableType.Integer, codeBlockDepth);
		case "MAX_LIST_SIZE":
			return new MewVariable(MewVariableType.Integer, this.getMaxListSize());
		case "IS_SKETCHWARE":
			return new MewVariable(MewVariableType.Bool, (getSerializationObjects() != null));
		case "PLATFORM_NAME":
			return new MewVariable(MewVariableType.String, getPlatformName());
		case "INTEGER_MAX_VALUE":
			return new MewVariable(MewVariableType.Integer, Integer.MAX_VALUE);
		case "INTEGER_MIN_VALUE":
			return new MewVariable(MewVariableType.Integer, Integer.MIN_VALUE);
		case "FLOAT_MAX_VALUE":
			return new MewVariable(MewVariableType.Float, Double.MAX_VALUE);
		case "FLOAT_MIN_VALUE":
			return new MewVariable(MewVariableType.Float, Double.MIN_VALUE);
		case "FLOAT_NAN":
			return new MewVariable(MewVariableType.Float, Double.NaN);
		case "FLOAT_MIN_NORMAL":
			return new MewVariable(MewVariableType.Float, Double.MIN_NORMAL);
		case "FLOAT_POSITIVE_INFINITY":
			return new MewVariable(MewVariableType.Float, Double.POSITIVE_INFINITY);
		case "FLOAT_NEGATIVE_INFINITY":
			return new MewVariable(MewVariableType.Float, Double.NEGATIVE_INFINITY);
		case "EPSILON":
			return new MewVariable(MewVariableType.Float, this.getDoubleEpsilon());
		case "PI":
			return new MewVariable(MewVariableType.Float, Math.PI);
		case "E":
			return new MewVariable(MewVariableType.Float, Math.E);
		case "ULTIMATE_ANSWER":
			return new MewVariable(MewVariableType.Integer, 42);
		case "CURRENT_MILLISECOND":
			return new MewVariable(MewVariableType.Integer, (int)(System.currentTimeMillis() % 1000));
		default:
			if (injectedVariables.containsKey(name.toUpperCase().trim())) {
				return injectedVariables.get(name.toUpperCase().trim());
			}
			return new MewVariable(MewVariableType.Error, new MewException("There is no constant value with name " + name));
		}
	}
	
	public java.util.Random getRandom() {
		return random;
	}
	
	public boolean budgetExceeded() { 
		return ++totalIterations > getTotalIterationBudget(); 
	}
	
	public MewEnvironment() {
//		variables = new HashMap<String, MewVariable>();
		variables = new MewtypeVariableMap();
		random = new java.util.Random();
		resetFlags();
		
		injectedVariables = injectSpecialVariables();
		
		// Initialize more custom variables here
	}
	
	public void resetFlags() {
		escapeLoopFlag = false;
		escapeProgramFlag = false;
		recursiveExecuteStringCap = 0;
		codeBlockDepth = 0;
		totalIterations = 0;
		_classList = null;
		sbLog = new StringBuilder();
	}
}

class MewtypeVariableMap extends java.util.AbstractMap<String, MewVariable> {
	public ArrayList<HashMap<String, MewVariable>> stack;
	boolean writeOnlyToTopmost;
	
	public MewtypeVariableMap() {
		stack = new ArrayList<>();
		writeOnlyToTopmost = false;
	}
	
	public java.util.AbstractMap<String, MewVariable> push() {
		stack.add(0, new HashMap<String, MewVariable>());
		return stack.get(0);
	}
	
	public java.util.AbstractMap<String, MewVariable> pop() {
		if (stack.size() == 0) {
			return null;
		}
		
		return stack.remove(0);
	}
	
	@Override
	public boolean containsKey(Object key) {
		for (HashMap<String, MewVariable> m : stack) {
			if (m.containsKey(key)) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean containsValue(Object key) {
		for (HashMap<String, MewVariable> m : stack) {
			if (m.containsValue(key)) {
				return true;
			}
		}
		return false;
	}
	
	public MewVariable put(int index, String key, MewVariable object) {
		if (stack.size() <= index) {
			return new MewVariable(MewVariableType.Error, new MewException("Internal error: index out of bounds in MewtypeVariableMap put(), specified index is " + index + " while size is " + stack.size()));
		}
		return stack.get(index).put(key, object);
	}
	
	@Override
	public MewVariable put(String key, MewVariable object) {
		if (stack.size() == 0) {
			push();
		}
		if (writeOnlyToTopmost) {
			return stack.get(0).put(key, object);
		}
		if (stack.size() == 1) { return stack.get(0).put(key, object); }
		if (stack.get(0).containsKey(key)) {
			return stack.get(0).put(key, object);
		} else {
			for (HashMap<String, MewVariable> m : stack) {
				if (m.containsKey(key)) {
					return m.put(key, object);
				}
			}
			return stack.get(0).put(key, object);
		}
	}
	
	@Override
	public MewVariable get(Object key) {
		if (stack.size() == 1) { return stack.get(0).get(key); }
		for (HashMap<String, MewVariable> m : stack) {
			MewVariable v = m.get(key);
			if (v != null) {
				return v;
			}
		}
		return null;
	}
	
	@Override
	public void clear() {
		for (HashMap<String, MewVariable> m : stack) {
			m.clear();
		}
	}

	@Override
	public Set<Entry<String, MewVariable>> entrySet() {
		return new Set<Entry<String, MewVariable>>() {
			@Override public Iterator<Entry<String, MewVariable>> iterator() {
//				Iterator<Entry<String, MewVariable>> it = backing.entrySet().iterator();
				return new Iterator<Entry<String, MewVariable>>() {
					private int currStack = 0;
					private java.util.Iterator<Entry<String, MewVariable>> currIterator = null;
					@Override public boolean hasNext() {
					    while (currStack < stack.size()) {
					        if (currIterator == null) {
					            currIterator = stack.get(currStack).entrySet().iterator();
					        }
					        if (currIterator.hasNext()) {
					            return true;
					        }
					        currStack++;
					        currIterator = null;
					    }
					    return false;
					}
					@Override public Entry<String, MewVariable> next() {
					    if (!hasNext()) {
					        throw new java.util.NoSuchElementException();
					    }
					    if (currIterator == null) {
					        currIterator = stack.get(currStack).entrySet().iterator();
					    }
					    return currIterator.next();
					}
					@Override public void remove() { 
						if (currIterator == null) {
							currIterator = stack.get(currStack).entrySet().iterator();
						}
						currIterator.remove();
					}
				};
			}
			@Override public int size() { 
				int i = 0;
				for (HashMap<String, MewVariable> m : stack) {
					i += m.size();
				}
				return i; 
			}
			@Override
			public boolean isEmpty() {
				boolean allEmpty = true;
				for (HashMap<String, MewVariable> m : stack) {
					if (m.entrySet().isEmpty()) {
						allEmpty = false;
						break;
					}
				}
				return allEmpty;
			}
			@Override
			public boolean contains(Object o) {
				for (HashMap<String, MewVariable> m : stack) {
					if (m.entrySet().contains(o)) {
						return true;
					}
				}
				return false;
			}
			@Override
			public Object[] toArray() {
				ArrayList<Object> a = new ArrayList<>();
				for (HashMap<String, MewVariable> m : stack) {
					a.addAll(Arrays.asList(m.entrySet().toArray()));
				}
				return a.toArray();
			}
			@Override
			public <T> T[] toArray(T[] a) {
				ArrayList<T> aa = new ArrayList<>();
				for (HashMap<String, MewVariable> m : stack) {
					aa.addAll(Arrays.asList(m.entrySet().toArray(a)));
				}
				return aa.toArray(a);
			}
			@Override
			public boolean add(Entry<String, MewVariable> e) {
				return stack.get(0).entrySet().add(e);
			}
			@Override
			public boolean remove(Object o) {
				for (HashMap<String, MewVariable> m : stack) {
					if (m.entrySet().contains(o)) {
						return m.entrySet().remove(o);
					}
				}
				return false;
			}
			@Override
			public boolean containsAll(Collection<?> c) {
				for (Object o : c) {
					boolean found = false;
					for (HashMap<String, MewVariable> m : stack) {
						if (m.entrySet().contains(o)) {
							found = true;
							break;
						} else {
							found = false;
						}
					}
					
					if (!found) {
						return false;
					}
				}
				return true;
			}
			@Override
			public boolean addAll(Collection<? extends Entry<String, MewVariable>> c) {
				return stack.get(0).entrySet().addAll(c);
			}
			@Override
			public boolean retainAll(Collection<?> c) {
				boolean b = false;
				for (HashMap<String, MewVariable> m : stack) {
					b = b || m.entrySet().retainAll(c);
				}
				return b;
			}
			@Override
			public boolean removeAll(Collection<?> c) {
				boolean b = false;
				for (HashMap<String, MewVariable> m : stack) {
					b = b || m.entrySet().removeAll(c);
				}
				return b;
			}
			@Override
			public void clear() {
				for (HashMap<String, MewVariable> m : stack) {
					m.entrySet().clear();
				}
			}
		};
	}
}

public class MewtypeInterpreter {
	final MewtypeClassList classList;
	private MewEnvironment env = null;
	private Pattern _intPattern;
	private Pattern _floatPattern;
	
	MewtypeInterpreter(MewEnvironment env) {
		this.env = env;
		initPatterns();
		
		// Creating a new classList is expensive so try fetching one from cache
		if (env._classList != null) {
			classList = env._classList;
		} else {
			classList = new MewtypeClassList(env);
		}
		
		if (env._classList == null) {
			propagateClassList();
		}
	}
	
	MewtypeInterpreter(MewEnvironment env, MewtypeClassList classList) {
		this.env = env;
		this.classList = classList;
		initPatterns();
		
		if (env._classList == null) {
			propagateClassList();
		}
	}
	
	MewEnvironment getEnvironment() {
		return env;
	}
	
	void setEnvironment(MewEnvironment newEnv) {
		env = newEnv;
		if (env._classList == null) {
			propagateClassList();
		}
	}
	
	private void propagateClassList() {
		env._classList = classList;
	}
	
	void initPatterns() {
		_intPattern = Pattern.compile("-?[0-9]{1,10}");
		_floatPattern = Pattern.compile("[+-]?([0-9]+([.][0-9]*)?|[.][0-9]+)");
	}
	
	private boolean isEmpty(StringBuilder sb) {
		if (sb == null) {
			return true; // Null strings are empty?
		}
		
		String s = sb.toString();
		return (s != null && s.trim().isEmpty());
	}
	
	boolean isInt(String _input){
		if (_input.length() > 11) { return false; }
		if (!(_intPattern.matcher(_input).matches())) {
			return false;
		}
		
		String digits = "";
		if (_input.startsWith("-")) {
			digits = _input.substring(1);
		} else {
			digits = _input;
		}
		if (digits.length() < 10) {
			return true;
		}
		
		if (_input.startsWith("-")) {
			return digits.compareTo("2147483648") <= 0;
		} else {
			return digits.compareTo("2147483647") <= 0;
		}
	}	
	boolean isFloat(String _input) {
		return ((_floatPattern.matcher(_input).matches()));
	}
	boolean isBool(String _input) {
		String _i = _input.toLowerCase().trim();
		return (_i.equals("true") 
				|| _i.equals("false"));
	}
	boolean parseBool(String _input) {
		if (isBool(_input)) {
			if (_input.length() < 5) {
				return true;
			} else {
				return false;
			}
		}
		return false; // Should not happen!
	}
	
	MewtypeProgram interpret(String source, MewEnvironment environment) {
		MewtypeProgramSource s = new MewtypeProgramSource(source);
		MewtypeProgram program = new MewtypeProgram();
		
		MewStack<Integer> parenthesesStack = new MewStack<Integer>();
		MewStack<Integer> stateStack = new MewStack<Integer>();
		stateStack.push(0);
		
		StringBuilder temp = new StringBuilder();
		
		while (s.hasNext()) {
			char n = s.next();
			if (Character.isWhitespace(n)) {
				temp.append(n);
				while (s.hasNext() && Character.isWhitespace(s.peek())) {
					temp.append(s.next());
				}
			} else if (n == '(') {
				// Flush the temp scratchpad if it is not empty
				if (!isEmpty(temp)) {
					program.steps.add(evaluate2(new MewtypeProgramSource(temp.toString().trim()), program, environment, parenthesesStack, stateStack));
					temp = new StringBuilder();
				}
				
				// Find its matching parenthesis
				String expr = consumeStatement(s);
				MewNode mn = evaluate2(new MewtypeProgramSource(expr), program, environment, parenthesesStack, stateStack);
				program.steps.add(mn);
			} else if (n == '"') {
				// decode escapes exactly once, on the raw quoted token (same path as a literal in argument position) so "\"" behaves the same everywhere
				String tok = '"' + consumeStringLiteral(s, false) + '"';
				
				// Flush the temp scratchpad if it is not empty
				if (!isEmpty(temp)) {
					program.steps.add(evaluate2(new MewtypeProgramSource(temp.toString().trim()), program, environment, parenthesesStack, stateStack));
				}
				temp = new StringBuilder();
				
				// Parse string literal
				program.steps.add(parseLiteral(tok, environment));
			} else if (n == '{') {
				// Flush the temp scratchpad if it is not empty
				if (!isEmpty(temp)) {
					program.steps.add(evaluate2(new MewtypeProgramSource(temp.toString().trim()), program, environment, parenthesesStack, stateStack));
				}
				
				MewCodeBlock block = consumeCodeBlock(s, environment);
				program.steps.add(new MewLeafNode(new MewVariable(MewVariableType.Code, block)));
			} else if (n == '/' && s.hasNext() && s.peek() == '*') {
				s.next();
				consumeComment(s);
			} else if (n == ';') {
				consumeInlineComment(s);
			} else {
				temp.append(n);
			}
		}
		
		// Flushes trailing token
		if (!isEmpty(temp)) {
			program.steps.add(evaluate2(new MewtypeProgramSource(temp.toString().trim()), program, environment, parenthesesStack, stateStack));
		}
		
		return program;
	}
	
	private MewNode evaluate(MewtypeProgramSource source, MewtypeProgram program, MewEnvironment environment, MewStack<Integer> parenthesesStack, MewStack<Integer> stateStack) {
		stateStack.push(stateStack.size());
		if (stateStack.size() > environment.getMaxStackDepth()) {
			// Stack overflow!
			stateStack.pop();
			return new MewLeafNode(new MewVariable(MewVariableType.Error, new MewException("Stack overflow. The current max stack depth is " + environment.getMaxStackDepth())));
		}
		
		// Basically we are expecting the form (+ 1 2), but without the opening parenthesis as it is already consumed.
		StringBuilder temp = new StringBuilder();
		int position = 1;
		String verb = "";
		ArrayList<String> args = new ArrayList<>(); 
		
		if (!source.hasNext()) {
			// Handle empty program
			stateStack.pop();
			return new MewNoopNode();
		}
		
		// Check for closing parenthesis first to handle empty ().
		if (source.peek() == ')') {
			source.next(); // Consume the closing parenthesis
			stateStack.pop();
			return new MewNoopNode(); // Return no-op for empty ()
		}
		
		while (source.hasNext()) {
			char next = source.next();
			
			// Check for closing parenthesis to return the node.
			if (next == ')') {
				verb = verb.trim();
				
				// Handle literals
				if (position == 1) {
					if (isEmpty(temp)) {
						// Something like ()
						stateStack.pop();
						return new MewNoopNode();
					} else {
						// Something like (literal)
						stateStack.pop();
						return parseLiteral(temp.toString(), environment);
					}
				} else {
					if (!isEmpty(temp)) {
						args.add(temp.toString());
					}
				}
								
				MewNode r = evaluateEx(verb, args, program, environment, parenthesesStack, stateStack);
				stateStack.pop();
				return r;
			} 
			
			// The verb, or a constant literal, ends with a whitespace character
			if (Character.isWhitespace(next)) {
				// Save previous StringBuilder results
				if (position == 1 && !isEmpty(temp)) {
					verb = temp.toString().trim();
					temp = new StringBuilder();
					position++;
					
					// Consume all whitespace chars until a non-whitespace char is encountered
					while (source.hasNext() && Character.isWhitespace(source.peek())) {
						source.next();
					}
				} else if (position == 2 && !isEmpty(temp)) {
					String arg1 = temp.toString();
					args.add(arg1);
					temp = new StringBuilder();
//					position++;
					
					// Consume all whitespace chars until a non-whitespace char is encountered
					while (source.hasNext() && Character.isWhitespace(source.peek())) {
						source.next();
					}
				} else {
					// Funny position!
					// Can now be reached through consuming comments like (/*Comment*/   + 1 2)
					temp.append(next);
				}
			} else if (next == '/' && source.hasNext() && source.peek() == '*') {
				source.next();
				consumeComment(source);
			} else if (next == ';') {
				consumeInlineComment(source);
			} else if (next == '"') {
//				System.out.println("Consuming string literal " + source.sb.toString().substring(source.getPosition()));
				String literal = consumeStringLiteral(source, false);
				
//				// Consume all trailing spaces
//				while (source.hasNext() && Character.isWhitespace(source.peek())) {
//					source.next();
//				}
//				
//				// Consume the trailing ) if available
//				if (source.hasNext() && source.peek() == ')') {
//					source.next();
//				}
				
				temp.append(next);
				temp.append(literal);
				temp.append(next);
//				return new MewLeafNode(new MewVariable(MewVariableType.String, literal));
			} else if (next == '(') {
				String stmt = consumeStatement(source);
				temp.append(stmt);
			} else if (next == '{') {
				String block = consumeCodeBlockEx(source);
				temp.append(next);
				temp.append(block);
			} else {
				// Write the text to the temp buffer
				temp.append(next);
			}
		}
		
		if (position == 1) {
			if (!isEmpty(temp)) {
				verb = temp.toString();
			}
		} else if (position == 2) {
			if (!isEmpty(temp)) {
				args.add(temp.toString());
			}
		}
		
		// Check if something is in arg2
		if (!verb.isEmpty() && args.size() > 0) {
			verb = verb.trim();
			
			MewNode r = evaluateEx(verb, args, program, environment, parenthesesStack, stateStack);
			stateStack.pop();
			return r;
		} else {
			if (!verb.isEmpty()) {
				MewNode r =  parseLiteral(verb, environment);
				stateStack.pop();
				return r;
			} else {
				stateStack.pop();
				return new MewNoopNode();
			}
		}
	}
	
	private MewNode evaluate2(MewtypeProgramSource source, MewtypeProgram program, MewEnvironment environment, MewStack<Integer> parenthesesStack, MewStack<Integer> stateStack) {
		while (source.hasNext() && Character.isWhitespace(source.peek())) {
			source.next();
		}
		if (source.hasNext() && source.peek() == '(') {
			source.next();
		}
		while (source.hasNext() && Character.isWhitespace(source.peek())) {
			source.next();
		}
		
		return evaluate(source, program, environment, parenthesesStack, stateStack);
	}
	
	private MewNode parseLiteral(String verb, MewEnvironment environment) {
		verb = verb.trim();
		
		// Bare verb names (e.g. "Ans") should not fall through to "some variable called Ans"
		if (classList.classesMap.containsKey(verb.toLowerCase())) {
			MewNode node = createNodeInstance(classList.classesMap.get(verb.toLowerCase()), environment);
			if (node.getMinArgumentCount() <= 0) {
				return node;
			} else {
				return new MewLeafNode(new MewVariable(MewVariableType.Error, new MewException("Cannot instantiate literal verb " + verb + " with 0 arguments, a minimum of " + node.getMinArgumentCount() + " are needed")));
			}
		}
					
		// Should be a literal, lets see what type of literal it is
		if (verb.toLowerCase().equals("null")) {
			return new MewLeafNode(new MewVariable(MewVariableType.Null, "333"));
		} else if (isBool(verb)) {
			return new MewLeafNode(new MewVariable(MewVariableType.Bool, parseBool(verb)));
		} else if (isInt(verb)) {
			return new MewLeafNode(new MewVariable(MewVariableType.Integer, Integer.parseInt(verb)));
		} else if (isFloat(verb)) {
			return new MewLeafNode(new MewVariable(MewVariableType.Float, Double.parseDouble(verb)));
		} else if ((verb.startsWith("\"") && verb.length() > 1)) {
			return new MewLeafNode(new MewVariable(MewVariableType.String, consumeStringLiteral(new MewtypeProgramSource(verb.substring(1)), true)));
		} else if (verb.startsWith("{")) {
			return new MewLeafNode(new MewVariable(MewVariableType.Code, (consumeCodeBlock(new MewtypeProgramSource(verb.substring(1)), environment))));
		} else if (verb.startsWith("/*")) {
			consumeComment(new MewtypeProgramSource(verb.substring(2)));
			return new MewLeafNode(new MewVariable(MewVariableType.Null, "Tasty Bungor"));
		} else if (verb.startsWith(";")) {
			consumeInlineComment(new MewtypeProgramSource(verb.substring(1)));
			return new MewLeafNode(new MewVariable(MewVariableType.Null, "Delicious Bungor"));
		} else {
			if (!environment.variables.containsKey(verb)) {
				environment.variables.put(verb, new MewVariable(MewVariableType.Null, "Birdy Yakitori"));
			}
			
			return new MewLeafNode(new MewVariable(MewVariableType.VariableReference, verb));
		}
	}
	
	private String consumeStringLiteral(MewtypeProgramSource source, boolean handleEscapes) {
		// Assume the first " of the string literal has already been consumed
		StringBuilder literalBuilder = new StringBuilder();
		
		while (source.hasNext()) {
			char n = source.next();
			if (n == '\\' && source.hasNext()) {
				// Escape sequence
				char next = source.next();
				if (handleEscapes) {
					switch (next) {
					case '\\':
						literalBuilder.append('\\');
						break;
					case 'q':
					case '"':
						literalBuilder.append('"');
						break;
					case 'n':
						literalBuilder.append('\n');
						break;
					case 't':
						literalBuilder.append('\t');
						break;
					case 'r':
						literalBuilder.append('\r');
						break;
					case 'b':
						literalBuilder.append('\b');
						break;
					case 'f':
						literalBuilder.append('\f');
						break;
					case 'u':
						StringBuilder usb = new StringBuilder();
						if (source.hasNext()) usb.append(source.next());
						if (source.hasNext()) usb.append(source.next());
						if (source.hasNext()) usb.append(source.next());
						if (source.hasNext()) usb.append(source.next());
						
						if (usb.toString().length() == 4) {
//							String uu = Character.toString(Integer.parseInt(usb.toString(), 16));
							try {
								String uu = new String(Character.toChars(Integer.parseInt(usb.toString(), 16)));
								literalBuilder.append(uu);							
							} catch (Exception e) {
								literalBuilder.append(n);
								literalBuilder.append(next);
								source.setPosition(source.getPosition() - usb.toString().length());
							}
						} else {
							// Not a complete unicode escape sequence, writing it as-is
							literalBuilder.append(n);
							literalBuilder.append(next);
							source.setPosition(source.getPosition() - usb.toString().length());
						}
	
						break;
					default:
						literalBuilder.append('\\');
						literalBuilder.append(next);
						break;
					}
				} else {
					literalBuilder.append(n);
					literalBuilder.append(next);
				}
				
			} else if (n == '"') {
				// End parse string literal
				// Push whatever's in temp to program first
				String ss = literalBuilder.toString();
				
				return ss;
			} else {
				literalBuilder.append(n);
			}
		}
		
		return literalBuilder.toString();
	}
	
	private void consumeComment(MewtypeProgramSource source) {
		while (source.hasNext()) {
			char next = source.next();
			if (next == '*') {
				if (source.hasNext() && source.peek() == '/') {
					source.next();
					return;
				}
			}
		}
	}
	
	private void consumeInlineComment(MewtypeProgramSource source) {
		while (source.hasNext()) {
			char next = source.next();
			if (next == '\n' || next == '\r' || next == '\u0085' || next == '\u2028' || next == '\u2029') {
				source.setPosition(source.getPosition() - 1);
				return;
			}
		}
	}
	
	public MewCodeBlock consumeCodeBlock(MewtypeProgramSource source, MewEnvironment environment) {
		int state = 0;
		int frontState = 0;
		ArrayList<String> arguments = new ArrayList<String>();
		StringBuilder builder = new StringBuilder();
		MewVariableType type = MewVariableType.Undefined;
//		boolean checkReturnType = false;
		
//		String debug1 = source.sb.substring(source.getPosition());
//		System.out.println("Consuming " + debug1);
		
		// {arg1, arg2, ... -> resultingType : (/* Code */)}
		// {-> resultingType : (/* Code */)}
		// {arg1, arg2, ... : (/* Code */)}
		// {(/* Code */)}
		while (source.hasNext()) {
			char next = source.next();
			
			if (next == '}') {
				if (state == 0) {
					// {arg1, arg2, ... -> resultingType} or {}
					if (!isEmpty(builder)) {
						if (frontState == 0) {
							arguments.add(builder.toString().trim());
						} else {
							// Parse resulting type
							String sType = builder.toString().trim().toLowerCase();
							type = parseVariableTypeName(sType);
						}
					}
					
					return new MewCodeBlock(new MewtypeProgram(), arguments, (!arguments.contains("_")), type, (type != MewVariableType.Undefined));
				} else {
					MewtypeProgram program = interpret(builder.toString(), environment);
					return new MewCodeBlock(program, arguments, (!arguments.contains("_")), type, (type != MewVariableType.Undefined));
				}
			}
			if (state == 0) {
				// The front part: (arg1, arg2, ... -> resultingType)
				if (next == ':') {
					if (frontState == 0) {
						// This code block does not have a resulting type
						// Flush StringBuilder for remaining arguments and advance state
						if (!isEmpty(builder)) {
							arguments.add(builder.toString().trim());
						}
					} else {
						// Parse resultingType
						String sType = builder.toString().trim().toLowerCase();
						type = parseVariableTypeName(sType);
					}
					
					builder = new StringBuilder();
					state++;
				} else {
					if (next == '-' && source.hasNext() && source.peek() == '>') {
						if (!isEmpty(builder)) {
							arguments.add(builder.toString().trim());
							builder = new StringBuilder();
						}
						source.next();
						frontState++;
					} else {
						if (frontState == 0) {
							if (next == ',') {
								if (!isEmpty(builder)) {
									arguments.add(builder.toString().trim());
									builder = new StringBuilder();
								}
								
								while (source.hasNext() && Character.isWhitespace(source.peek())) {
									source.next();
								}
							} else if (next == '/' && source.hasNext() && source.peek() == '*') {
								source.next();
								consumeComment(source);
							} else if (next == ';') {
								consumeInlineComment(source);
							} else if (next == '"') {
								if (!isEmpty(builder)) {
									arguments.add(builder.toString().trim());
									builder = new StringBuilder();
								}
								
								state = 1;
								source.setPosition(source.getPosition() - 1);
							} else if (next == '(') {
								if (!isEmpty(builder)) {
									arguments.add(builder.toString().trim());
									builder = new StringBuilder();
								}
								
								state = 1;
								source.setPosition(source.getPosition() - 1);
							} else {
								builder.append(next);
							}
						} else {
							builder.append(next);
						}
					}
				}
				
			} else {
				if (next == '/' && source.hasNext() && source.peek() == '*') {
					source.next();
					consumeComment(source);
				} else if (next == ';') {
					consumeInlineComment(source);
				} else if (next == '"') {
					String literal = consumeStringLiteral(source, false);
					builder.append(next);
					builder.append(literal);
					builder.append(next);
				} else if (next == '{') {
					String literal = consumeCodeBlockEx(source);
					builder.append(next);
					builder.append(literal);
				} else {
					builder.append(next);
				}
			}
		}
		
		// Okay, missing }
		if (state == 0) {
			// {arg1, arg2, ... -> resultingType} or {}
			if (!isEmpty(builder)) {
				if (frontState == 0) {
					arguments.add(builder.toString().trim());
				} else {
					// Parse resulting type
					String sType = builder.toString().trim().toLowerCase();
					type = parseVariableTypeName(sType);
				}
			}
			
			return new MewCodeBlock(new MewtypeProgram(), arguments, (!arguments.contains("_")), type, (type != MewVariableType.Undefined));
		} else {
			MewtypeProgram program = interpret(builder.toString(), environment);
			return new MewCodeBlock(program, arguments, (!arguments.contains("_")), type, (type != MewVariableType.Undefined));
		}
	}
	
	private MewVariableType parseVariableTypeName(String typeName) {
		switch (typeName) {
		case "integer":
			return MewVariableType.Integer;
		case "bool":
			return MewVariableType.Bool;
		case "float":
			return MewVariableType.Float;
		case "string":
			return MewVariableType.String;
		case "error":
			return MewVariableType.Error;
		case "code":
			return MewVariableType.Code;
		case "?":
		case "_":
		default:
			// Unrecognized type!
			// Gets converted to ?: Any type
			return MewVariableType.Undefined;
		}
	}
	
	private String consumeCodeBlockEx(MewtypeProgramSource source) {
		int position = source.getPosition();
		int numOpenBraces = 1;
		
		if (!source.hasNext()) {
			return "";
		}
		
		while (source.hasNext()) {
			char next = source.next();
			if (next == '}') {
				numOpenBraces--;
				if (numOpenBraces == 0) {
					return source.getSubstring(position, source.getPosition());
				}
			} else if (next == '{') {
				numOpenBraces++;
			} else if (next == '(') {
				consumeStatement(source);
			} else if (next == '"') {
				consumeStringLiteral(source, false);
			} else if (next == '/' && source.hasNext() && source.peek() == '*') {
				source.next();
				consumeComment(source);
			} else if (next == ';') {
				consumeInlineComment(source);
			}
		}
		
		return source.getSubstring(position, source.getPosition());
	}
	
	private String consumeStatement(MewtypeProgramSource source) {
		// Assume the ( have been already consumed, we need to find the matching )
//		System.out.println("consumeStatement() consuming " + source.sb.toString());
		int position = source.getPosition();
		int numOpenParentheses = 1;
		
		if (!source.hasNext()) {
			return "";
		}
		
		while (source.hasNext()) {
			char next = source.next();
			if (next == ')') {
				numOpenParentheses--;
				if (numOpenParentheses == 0) {
					// We found the end of the string
					// We will return the closing parenthesis
//					System.out.println("consumeStatement() [1] consumed " + source.getSubstring(position, source.getPosition() - 1));
					return source.getSubstring(position, source.getPosition());
				}
			} else if (next == '(') {
				numOpenParentheses++;
			} else if (next == '"') {
				consumeStringLiteral(source, false);
			} else if (next == '{') {
				consumeCodeBlockEx(source);
			} else if (next == '/' && source.hasNext() && source.peek() == '*') {
				source.next();
				consumeComment(source);
			} else if (next == ';') {
				consumeInlineComment(source);
			}
		}
//		System.out.println("consumeStatement() [2] consumed " + source.getSubstring(position, source.getPosition() - 1));
		return source.getSubstring(position, source.getPosition());
	}
		
	MewNode createNodeInstance(java.lang.reflect.Constructor<?> c, MewEnvironment environment) {
		try {
			Object[] objs = environment.getSerializationObjects();
			
			Object cobj;
			if (c.getParameterCount() == 0) {
				cobj = c.newInstance((Object[])null);
			} else {
				cobj = c.newInstance(objs);
			}
			if (cobj instanceof MewNode) {
				MewNode mn = (MewNode)(cobj);
				mn.nodes = new ArrayList<MewNode>();
				return mn;
			}
			return new MewLeafNode(new MewVariable(MewVariableType.Error, new MewException("Cannot instantiate class " + c.getName() + " [1]: Not a MewNode")));
		} catch (Exception e) {
			return new MewLeafNode(new MewVariable(MewVariableType.Error, new MewException("Cannot instantiate class " + c.getName() + " [2]: " + e.getMessage(), e)));
		}
	}
	
	private MewNode evaluateEx(String verb, ArrayList<String> args, MewtypeProgram program, MewEnvironment environment, MewStack<Integer> parenthesesStack, MewStack<Integer> stateStack) {
		String sVerb = verb.toLowerCase();
		if (classList.classesMap.containsKey(sVerb)) {
			MewNode mn = createNodeInstance(classList.classesMap.get(sVerb), environment);
			mn.nodes = new ArrayList<MewNode>();
			
			int minArgs = Math.min(mn.getMaxArgumentCount(), mn.getMinArgumentCount());
			int maxArgs = Math.max(mn.getMaxArgumentCount(), mn.getMinArgumentCount());
			if (args.size() < minArgs) {
				return new MewLeafNode(new MewVariable(MewVariableType.Error, new MewException("Syntax Error: " + verb + " expects a minimum of " + minArgs + " arguments but " + args.size() + " are provided")));
			}
			if (args.size() > maxArgs) {
				return new MewLeafNode(new MewVariable(MewVariableType.Error, new MewException("Syntax Error: " + verb + " expects a maximum of " + maxArgs + " arguments but " + args.size() + " are provided")));
			}
			
			for (String arg : args) {
				String argg = arg.trim();
				MewNode node = evaluate2(new MewtypeProgramSource(argg), program, environment, parenthesesStack, stateStack);
				mn.nodes.add(node);
			}
			return mn;
		} else {
			MewVariable errorVar = new MewVariable(MewVariableType.Error, new MewException("Runtime error [1]: Not a known verb: " + verb));
			return new MewLeafNode(errorVar);
		}
	}

	class MewtypeClassList {
		private ArrayList<Class<?>> classes;
		public HashMap<String, java.lang.reflect.Constructor<?>> classesMap;
		
		public ArrayList<java.lang.reflect.Constructor<?>> getClasses() {
			ArrayList<java.lang.reflect.Constructor<?>> a = new ArrayList<java.lang.reflect.Constructor<?>>();
			a.addAll(classesMap.values());
			return a;
		}
		public ArrayList<String> getClassNames() {
			ArrayList<String> a = new ArrayList<String>();
			a.addAll(classesMap.keySet());
			return a;
		}
		
		public void registerClass(Class<?> c) {
			classes.add(c);
		}
		
		private MewtypeClassList(MewEnvironment environment) {
			classes = new ArrayList<Class<?>>();
			classesMap = new HashMap<String, java.lang.reflect.Constructor<?>>();
			
			classes.add(MewNoopNode.class);
			classes.add(MewNoop2Node.class);
			classes.add(MewExec.class);
			classes.add(MewQuit.class);
			classes.add(MewAns.class);
			classes.add(MewAssignment.class);
			classes.add(MewGetVariable.class);
			classes.add(MewGetVariables.class);
			classes.add(MewLogMessage.class);
			classes.add(MewToastMessage.class);
			classes.add(MewGetSpecialVariable.class);
			classes.add(MewLogEnvironment.class);
			classes.add(MewAddition.class);
			classes.add(MewSubtraction.class);
			classes.add(MewMultiplication.class);
			classes.add(MewDivision.class);
			classes.add(MewAdditionAssign.class);
			classes.add(MewSubtractionAssign.class);
			classes.add(MewMultiplicationAssign.class);
			classes.add(MewDivisionAssign.class);
			classes.add(MewConcat.class);
			classes.add(MewEquals.class);
			classes.add(MewEqualsStrict.class);
			classes.add(MewEqualsIdentity.class);
			classes.add(MewCompare.class);
			classes.add(MewNot.class);
			classes.add(MewAnd.class);
			classes.add(MewAndAlso.class);
			classes.add(MewOr.class);
			classes.add(MewOrAlso.class);
			classes.add(MewIf.class);
			classes.add(MewThen.class);
			classes.add(MewWhile.class);
			classes.add(MewBreak.class);
			classes.add(MewLessThan.class);
			classes.add(MewLessEqualsThan.class);
			classes.add(MewGreaterThan.class);
			classes.add(MewGreaterEqualsThan.class);
			classes.add(MewModulus.class);
			classes.add(MewExponent.class);
			classes.add(MewFactorial.class);
			classes.add(MewAbs.class);
			classes.add(MewMax.class);
			classes.add(MewMin.class);
			classes.add(MewClamp.class);
			classes.add(MewFloor.class);
			classes.add(MewLogarithm.class);
			classes.add(MewLogarithm10.class);
			classes.add(MewSqrt.class);
			classes.add(MewMathSin.class);
			classes.add(MewMathSinh.class);
			classes.add(MewMathCos.class);
			classes.add(MewMathCosh.class);
			classes.add(MewMathTan.class);
			classes.add(MewMathTanh.class);
			classes.add(MewMathRound.class);
			classes.add(MewMathSign.class);
			classes.add(MewTruncate.class);
			classes.add(MewRandom.class);
			classes.add(MewRandomFloat.class);
			classes.add(MewCharacterAt.class);
			classes.add(MewFindString.class);
			classes.add(MewSplitString.class);
			classes.add(MewStringLeft.class);
			classes.add(MewStringRight.class);
			classes.add(MewStringLength.class);
			classes.add(MewStringUppercase.class);
			classes.add(MewStringLowercase.class);
			classes.add(MewStringTrim.class);
			classes.add(MewStringReverse.class);
			classes.add(MewStringStartsWith.class);
			classes.add(MewStringEndsWith.class);
			classes.add(MewStringContains.class);
			classes.add(MewStringRepeat.class);
			classes.add(MewStringPadLeft.class);
			classes.add(MewStringPadRight.class);
			classes.add(MewSubstring.class);
			classes.add(MewStringReplace.class);
			classes.add(MewExecuteString.class);
			classes.add(MewIsError.class);
			classes.add(MewCreateError.class);
			classes.add(MewGetErrorMessage.class);
			classes.add(MewHasInnerError.class);
			classes.add(MewGetInnerError.class);
			classes.add(MewDebugInspectNull.class);
			classes.add(MewExecute.class);
			classes.add(MewExecuteSaved.class);
			classes.add(MewInterpret.class);
			classes.add(MewIsCode.class);
			classes.add(MewGetMinArgumentCount.class);
			classes.add(MewGetMaxArgumentCount.class);
			classes.add(MewGetReturnType.class);
			classes.add(MewCreateList.class);
			classes.add(MewListAppend.class);
			classes.add(MewListAppendAt.class);
			classes.add(MewListRemove.class);
			classes.add(MewListRemoveAt.class);
			classes.add(MewListRemoveLast.class);
			classes.add(MewListGet.class);
			classes.add(MewListGetLast.class);
			classes.add(MewListClear.class);
			classes.add(MewListSublist.class);
			classes.add(MewListIndexOf.class);
			classes.add(MewListLength.class);
			classes.add(MewListSort.class);
			classes.add(MewListReverse.class);
			classes.add(MewListContains.class);
			classes.add(MewListClone.class);
			classes.add(MewListJoin.class);
			classes.add(MewForEach.class);
			classes.add(MewListIsMutable.class);
			classes.add(MewListMakeImmutable.class);
			classes.add(MewIsList.class);
			classes.add(MewIsCursor.class);
			classes.add(MewCursorHasNext.class);
			classes.add(MewCursorGetNext.class);
			classes.add(MewCursorPeekNext.class);
			classes.add(MewCursorGetPosition.class);
			classes.add(MewCursorSetPosition.class);
			classes.add(MewCreateCursor.class);
			classes.add(MewIsMap.class);
			classes.add(MewCreateMap.class);
			classes.add(MewMapClear.class);
			classes.add(MewMapContainsKey.class);
			classes.add(MewMapContainsValue.class);
			classes.add(MewMapPut.class);
			classes.add(MewMapGet.class);
			classes.add(MewMapRemove.class);
			classes.add(MewMapGetKeys.class);
			classes.add(MewMapGetValues.class);
			classes.add(MewMapIsMutable.class);
			classes.add(MewMapMakeImmutable.class);
			classes.add(MewMapUnpack.class);
			classes.add(MewMapClone.class);
			classes.add(MewSBCreate.class);
			classes.add(MewSBAppend.class);
			classes.add(MewSBInsert.class);
			classes.add(MewSBDelete.class);
			classes.add(MewSBReplace.class);
			classes.add(MewSBCharacterAt.class);
			classes.add(MewSBSetCharacterAt.class);
			classes.add(MewSBReverse.class);
			classes.add(MewSBClear.class);
			classes.add(MewSBLength.class);
			classes.add(MewSBToString.class);
			classes.add(MewParseJson.class);
			classes.add(MewIsStringBuilder.class);
			classes.add(MewIsInt.class);
			classes.add(MewIsFloat.class);
			classes.add(MewIsBool.class);
			classes.add(MewIsString.class);
			classes.add(MewIsNull.class);
			classes.add(MewGetType.class);
			classes.add(MewParseInteger.class);
			classes.add(MewParseFloat.class);
			classes.add(MewParseBool.class);
			classes.add(MewParseString.class);
			
			environment.onBeginInitClassList(this);
			
	//		System.out.println("Initing MewtypeClassList");
			for (Class<?> c : classes) {
	//			System.out.println("- Trying " + c.getName() + " ctor count: " + c.getConstructors().length);
	//			for (Constructor<?> ccc : c.getConstructors()) {
	//				System.out.println("I found " + ccc.getName() + ccc.toString());
	//			}
				try {
					Object[] objs = environment.getSerializationObjects();
					java.lang.reflect.Constructor<?> ctor = c.getConstructors()[0];
					
					Object cobj;
					if (ctor.getParameterCount() == 0) {
						cobj = ctor.newInstance((Object[])null);
					} else {
						cobj = ctor.newInstance(objs);
					}
					if (cobj instanceof MewNode) {
						MewNode mn = (MewNode)(cobj);
						classesMap.put(mn.getName().toLowerCase(), ctor);
	//					System.out.println("- Put " + mn.getName());
						
						// Aliases
						if (mn instanceof MewLessThan) {
							classesMap.put("<", ctor);
						} else if (mn instanceof MewLessEqualsThan) {
							classesMap.put("<=", ctor);
						} else if (mn instanceof MewGreaterThan) {
							classesMap.put(">", ctor);
						} else if (mn instanceof MewGreaterEqualsThan) {
							classesMap.put(">=", ctor);
						} else if (mn instanceof MewAnd) {
							classesMap.put("&&", ctor);
						} else if (mn instanceof MewOr) {
							classesMap.put("||", ctor);
						} else if (mn instanceof MewAndAlso) {
							classesMap.put("andalso", ctor);
						} else if (mn instanceof MewOrAlso) {
							classesMap.put("oralso", ctor);
						} else if (mn instanceof MewIf) {
							classesMap.put("?", ctor);
						}
					}
				} catch (Exception e) {
					// Eating the error because objects that couldn't be instantiated wont be on the HashMap
					environment.logMessage("Error: Could not instianate class " + c.getName() + " reflectively.");
					System.out.println(e.toString());
				}
			}
			
			environment.onEndInitClassList(this);
	//		System.out.println("Done Initing MewtypeClassList");
		}
	}
}

class MewCodeBlock {
	public MewtypeProgram value;
	public ArrayList<String> arguments;
	public boolean checkArguments;
	public MewVariableType returnType;
	public boolean checkReturnType;
	
	public MewCodeBlock(MewtypeProgram value, ArrayList<String> arguments, boolean checkArguments, MewVariableType returnType, boolean checkReturnType) {
		if (value == null) {
			this.value = new MewtypeProgram();
		} else {
			this.value = value;
		}
		
		if (arguments == null) {
			this.arguments = new ArrayList<String>();
		} else {
			this.arguments = arguments;
		}
		
		this.checkArguments = checkArguments;
		this.returnType = returnType;
		this.checkReturnType = checkReturnType;
	}
	
	int getMaxArgumentCount() {
		int count = 0;
		for (String argument : arguments) {
			if (argument.equals("...")) {
				return Integer.MAX_VALUE;
			} else if (argument.equals("_")) {
				return Integer.MAX_VALUE;
			}
			count++;
		}
		return count;
	}
	
	int getMinArgumentCount() {
		int count = 0;
		for (String argument : arguments) {
			if (argument.equals("...")) {
				// A ... can be empty!
			} else if (argument.equals("_")) {
				return 0;
			} else {
				count++;
			}
		}
		return count;
	}
	
	boolean discardArguments() {
		for (String argument : arguments) {
			if (argument.equals("_")) {
				return true;
			}
		}
		return false;
	}
	
	MewVariable run(MewEnvironment environment, ArrayList<MewVariable> args) {
		int minArgCount = getMinArgumentCount();
		int maxArgCount = getMaxArgumentCount();
		
		if (minArgCount <= args.size() && args.size() <= maxArgCount) {
			if (environment.budgetExceeded()) { 
				environment.escapeLoopFlag = true; 
				environment.escapeProgramFlag = true;
				return new MewVariable(MewVariableType.Error, new MewException("Runtime error: total work budget exhausted")); 
			}
			if (environment.codeBlockDepth >= environment.getMaxCodeBlockDepth()) {
				return new MewVariable(MewVariableType.Error, new MewException("Runtime error: code block recursion deeper than " + environment.getMaxCodeBlockDepth()));
			}
			environment.codeBlockDepth++;
			try { 
				environment.variables.push();
				return runEx(environment, args);
			} finally { 
				environment.variables.pop();
				environment.codeBlockDepth--; 
			}
//			return runEx(environment, args);
		} else {
			if (minArgCount <= args.size()) {
				return new MewVariable(MewVariableType.Error, new MewException("Too much arguments supplied for code block: " + args.size() + " supplied but a maximum of " + maxArgCount + " are needed."));
			} else {
				return new MewVariable(MewVariableType.Error, new MewException("Not enough arguments supplied for code block: " + args.size() + " supplied but a minimum of " + minArgCount + " are needed."));
			}
		}
	}
	
	MewVariable runEx(MewEnvironment environment, ArrayList<MewVariable> args) {
		if (checkArguments) {
			for (int i = 0; i < arguments.size(); i++) {
				String varName = arguments.get(i);
				if (varName.equals("...")) {
					for (int j = i; j < args.size(); j++) {
						StringBuilder nameBuilder = new StringBuilder();
						nameBuilder.append("arg");
						nameBuilder.append(j - i);
						environment.variables.put(0, nameBuilder.toString(), args.get(j));
					}
					break;
				} else {
					if (i < args.size()) {
						environment.variables.put(0, varName, args.get(i));
					} else {
						//
					}
				}
			}
		}
				
		MewVariable result = value.run(environment);
				
		if (checkReturnType) {
			if (returnType == result.type) {
				return result;
			} else {
				if (result.type == MewVariableType.Error) {
					// Return errors directly
					return result;
				}
				
				if (returnType == MewVariableType.String && result.canStringify()) {
					return new MewVariable(MewVariableType.String, result.stringifyValue());
				} else if (returnType == MewVariableType.Bool && result.isNumeric()) {
					return new MewVariable(MewVariableType.Bool, result.toBool());
				} else if (returnType == MewVariableType.Integer && result.isNumeric()) {
					return new MewVariable(MewVariableType.Integer, result.toInt());
				} else if (returnType == MewVariableType.Float && result.isNumeric()) {
					return new MewVariable(MewVariableType.Float, result.toDouble());
				}
				return new MewVariable(MewVariableType.Error, new MewException("Code block checks for return type but the wrong type is returned and cannot be coerced"));
			}
		}
		return result;
	}
	
	MewCodeBlock cloneCodeBlock() {
		MewtypeProgram program = new MewtypeProgram();
		for (MewNode step : value.steps) {
			program.steps.add(step);
		}
		return new MewCodeBlock(program, new ArrayList<String>(arguments), checkArguments, returnType, checkReturnType);
	}
}

class MewException extends Exception {
	private static final long serialVersionUID = 6650285583525677047L;
	
	public MewException() { super(); }
	public MewException(String msg) { super(msg); }
	public MewException(String msg, Throwable inner) { super(msg, inner); }
	public MewException(Throwable inner) { super(inner); }
	
	@Override
	public Throwable fillInStackTrace() {
		return this;
	}
}

abstract class MewNode {
//	public MewNode left;
//	public MewNode right;
	public ArrayList<MewNode> nodes;
	public MewNode getLeft() {
		if (nodes.size() > 0) {
			if (nodes.size() > 0) {
				return nodes.get(0);
			} else {
				return new MewLeafNode(new MewVariable(MewVariableType.Error, new MewException("Failed to get first argument [1] [" + getName() + "]")));
			}
		} else {
			return new MewLeafNode(new MewVariable(MewVariableType.Error, new MewException("Trying to get argument 1 on a node (" + getName() + ") that expects 0 arguments")));
		}
	}
	public MewNode getRight() {
		if (nodes.size() > 1) {
			if (nodes.size() > 1) {
				return nodes.get(1);
			} else {
				return new MewLeafNode(new MewVariable(MewVariableType.Error, new MewException("Failed to get second argument [2] [" + getName() + "]")));
			}
		} else {
			return new MewLeafNode(new MewVariable(MewVariableType.Error, new MewException("Trying to get argument 2 on a node (" + getName() + ") that expects less than 2 arguments")));
		}
	}
	public MewNodeCursor toCursor() { return new MewNodeCursor(nodes); }
	public abstract String getName();
	public int getMinArgumentCount() { return 0; };
	public int getMaxArgumentCount() { return 0; };
	public abstract MewVariable evaluate(MewEnvironment environment);
}

abstract class MewNode1Arg extends MewNode {
	@Override public int getMinArgumentCount() { return 1; }
	@Override public int getMaxArgumentCount() { return 1; }
}

abstract class MewNode2Arg extends MewNode {
	@Override public int getMinArgumentCount() { return 2; }
	@Override public int getMaxArgumentCount() { return 2; }
}

abstract class MewNode2VarArg extends MewNode {
	@Override public int getMinArgumentCount() { return 2; }
	@Override public int getMaxArgumentCount() { return Integer.MAX_VALUE; }
}

abstract class MewNode3Arg extends MewNode {
	@Override public int getMinArgumentCount() { return 3; }
	@Override public int getMaxArgumentCount() { return 3; }
}

class MewNodeCursor {
	private ArrayList<MewNode> nodes;
	private int position;
	
	public MewNodeCursor(ArrayList<MewNode> nodes) {
		this.nodes = nodes;
		position = 0;
	}
	
	public boolean hasNext() {
		return (position < nodes.size());
	}
	
	public MewNode next() {
		if (hasNext()) {
			MewNode node = nodes.get(position);
			position++;
			return node;
		}
		return null;
	}
	
	public MewNode peek() {
		if (hasNext()) {
			MewNode node = nodes.get(position);
			return node;
		}
		return null;
	}
	
	public int getPosition() {
		return position;
	}
	
	public void setPosition(int newPosition) {
		position = newPosition;
	}
}

class MewVariable {
	MewVariableType type;
	Object value;
	
	public Object getValue() {
		return value;
	}
	public MewVariableType getType() {
		return type;
	}
	public boolean canStringify() {
		return (type == MewVariableType.Bool || 
				type == MewVariableType.Integer || 
				type == MewVariableType.Float || 
				type == MewVariableType.String || 
				type == MewVariableType.Error || 
				type == MewVariableType.Null || 
				type == MewVariableType.Undefined || 
				type == MewVariableType.VariableReference ||
				type == MewVariableType.List ||
				type == MewVariableType.Map ||
				type == MewVariableType.StringBuilder);
	}
	public String stringifyValue() { return stringifyValue(0); }
	public String stringifyValue(int dirty) {
		if (dirty > 32) {
			return "{...}";
		}
		if (canStringify()) {
			if (type == MewVariableType.Error) {
				if (value instanceof Throwable) {
					return ((Throwable)value).toString();
				} else {
					return "Error not exposed as an Exception object";
				}
			} else if (type == MewVariableType.Null) {
				return "(null)";
			} else if (type == MewVariableType.Undefined) {
				return "(undefined)";
			} else if (type == MewVariableType.Bool && value instanceof Boolean) {
				boolean b = toBool();
				return String.valueOf(b);
			} else if (type == MewVariableType.Integer && value instanceof Integer) {
				int i = toInt();
				return String.valueOf(i);
			} else if (type == MewVariableType.Float && value instanceof Double) {
				// Handle doubles that are whole numbers
				double d = toDouble();
				if (d % 1 == 0 && d < Integer.MAX_VALUE && Integer.MIN_VALUE < d) {
					return String.valueOf((int)d);
				} else {
					return String.valueOf(d);
				}
			} else if (type == MewVariableType.Map && value instanceof MewMap) {
				MewMap m = (MewMap)value;
				return m.toStringEx(dirty + 1);
			} else if (type == MewVariableType.List && value instanceof MewList) {
				MewList l = (MewList)value;
				return l.toStringEx(dirty + 1);
			} else {
				return value.toString();
			}
		} else {
			return "Cannot stringify!";
		}
	}
	public boolean isNumeric() {
		return (type == MewVariableType.Bool || 
				type == MewVariableType.Integer || 
				type == MewVariableType.Float)
				&& (value instanceof Boolean || value instanceof Integer || value instanceof Double);
	}
	
	public int toInt() {
		// instanceof, not getClass().equals(int.class) -- a boxed Integer is never int.class
		if (value instanceof Integer) {
			return ((Integer)value).intValue();
		} else if (value instanceof Double) {
			return ((Double)value).intValue();
		} else if (value instanceof Boolean) {
			return ((Boolean)value).booleanValue() ? 1 : 0;
		} else if (value instanceof String) {
			try { return (int) Double.parseDouble((String)value); } catch (Exception e) { return 114514; }
		}
		return 114514; // Should not happen!
	}
	
	public double toDouble() {
		if (value instanceof Integer) {
			return ((Integer)value).doubleValue();
		} else if (value instanceof Double) {
			return ((Double)value).doubleValue();
		} else if (value instanceof Boolean) {
			return ((Boolean)value).booleanValue() ? 1 : 0;
		} else if (value instanceof String) {
			try { return Double.parseDouble((String)value); } catch (Exception e) { return 11451419; }
		}
		return 114514; // Should not happen!
	}
	
	public boolean toBool() {
		if (value instanceof Boolean) {
			return ((Boolean)value).booleanValue();
		} else if (value instanceof Integer) {
			return ((Integer)value).intValue() > 0;
		} else if (value instanceof Double) {
			return ((Double)value).doubleValue() > 0;
		} else {
			return false;
		}
	}
	
	public boolean truthy() {
		if (isNumeric()) {
			return toBool();
		} else if (type == MewVariableType.List) {
			if (value instanceof MewList) {
				return !((MewList)value).isEmpty();
			} else {
				return false; // Cannot unpack list!
			}
		} else if (type == MewVariableType.Map) {
			if (value instanceof MewMap) {
				return !((MewMap)value).isEmpty();
			} else {
				return false; // Cannot unpack map!
			}
		} else if (type == MewVariableType.Cursor) {
			if (value instanceof MewListCursor) {
				return ((MewListCursor)value).hasNext();
			} else {
				return false; // Cannot unpack cursor!
			}
		} else if (canStringify()) {
			if (type == MewVariableType.Null || type == MewVariableType.Undefined || type == MewVariableType.Error) {
				return false;
			}
			return !stringifyValue().trim().isEmpty();
		} else {
			return false;
		}
	}
	
	MewVariable(MewVariableType type, Object value) {
		this.type = type;
		this.value = value;
	}
	
	public boolean equalsEx(MewVariable r) {
		MewVariable l = this;
		if (l.type == MewVariableType.Null || r.type == MewVariableType.Null) {
			if (l.type == MewVariableType.Null && r.type == MewVariableType.Null) {
				return true;
			} else {
				return false;
			}
		}
		if (l.type == MewVariableType.Bool || r.type == MewVariableType.Bool) {
			return (l.toBool() == r.toBool());
		}
		if (l.type == MewVariableType.Integer || r.type == MewVariableType.Integer) {
			return (l.toInt() == r.toInt());
		}
		if (l.type == MewVariableType.Float || r.type == MewVariableType.Float) {
			return (l.toDouble() == r.toDouble());
		}
		if (l.type == MewVariableType.Error || r.type == MewVariableType.Error) {
			if (l.canStringify() || r.canStringify()) {
				if (l.canStringify()) {
					if (r.value instanceof Throwable) {
						return (l.stringifyValue().equals(((Throwable)(r.value)).toString()));
					} else {
						return (l.stringifyValue().equals(r.stringifyValue()));
					}
				} else {
					if (l.value instanceof Throwable) {
						return (r.stringifyValue().equals(((Throwable)(l.value)).toString()));
					} else {
						return (r.stringifyValue().equals(l.stringifyValue()));
					}
				}
			}
			return false;
		}
		if (l.canStringify() || r.canStringify()) {
			return (l.stringifyValue().equals(r.stringifyValue()));
		}
		if (l.type == MewVariableType.ConditionalObject || r.type == MewVariableType.ConditionalObject) {
			return false;
		}
		
		return l.equals(r);
//		return l.value.equals(r.value);
	}
	
	@Override
	public boolean equals(final Object other) {
		if (this == other) return true;
		
		if (other instanceof MewVariable) {
//			MewVariable otherVar = (MewVariable)other;
//			return equalsEx(otherVar);
			
		    MewVariable o = (MewVariable)other;
		    if (type != o.type) return false;
		    switch (type) {
		    case Null: 
		    case Undefined: 
		    	return true;
		    case Bool:    
		    	return toBool() == o.toBool();
		    case Integer: 
		    	return toInt() == o.toInt();
		    case Float:   
		    	return toDouble() == o.toDouble();
		    case String: 
		    case StringBuilder:
		    case VariableReference: 
		    case Error:
		        return stringifyValue().equals(o.stringifyValue());   // Error = message equality
		    case Code: 
		    case List: 
		    case Map: 
		    case ConditionalObject: 
		    case Cursor: 
		    case Custom:
		        return this == o;                                     // identity semantics
		    default:
		        throw new IllegalStateException("MewVariable.equals: unhandled type " + type);
		    }
		} else {
			return super.equals(other);
		}
	}
	
	@Override
	public int hashCode() {
		switch (type) {
		case Null:
			return 333;
		case Bool:
			return Boolean.hashCode(toBool());
		case Code:
			return System.identityHashCode(this);
		case ConditionalObject:
			return System.identityHashCode(this);
		case Cursor:
			return System.identityHashCode(this);
		case Custom:
			return 788;
		case Error:
			return stringifyValue().hashCode();
		case Float:
			return Double.hashCode(toDouble());
		case Integer:
			return Integer.hashCode(toInt());
		case List:
			return value.hashCode();
		case Map:
			return value.hashCode();
		case String:
			return stringifyValue().hashCode();
		case StringBuilder:
			return stringifyValue().hashCode();
		case Undefined:
			return 788;
		case VariableReference:
			return stringifyValue().hashCode();
		default:
			return System.identityHashCode(this);
		}
	}
	
	@Override
	public String toString() {
		switch (type) {
		case List:
			return stringifyValue();
		case Map:
			return stringifyValue();
		default:
			return super.toString();
		}
	}
}

enum MewVariableType {
	Null,
	Undefined,
	Custom,
	Error,
	Bool,
	Integer,
	Float,
	String,
	List,
	Map,
	VariableReference,
	ConditionalObject,
	Code,
	Cursor,
	StringBuilder
}

class MewInternalList extends MewList {
	public ArrayList<MewVariable> backing = new ArrayList<>();
	
	public MewInternalList() { super(); }
	public MewInternalList(boolean mutable) { super(mutable); }

	@Override
	public int size() {
		return backing.size();
	}

	@Override
	public MewVariable get(int index) {
		return backing.get(index);
	}
	
	@Override
	public MewVariable set(int index, MewVariable element) {
		return backing.set(index, element);
	}
	
	@Override
	public boolean add(MewVariable element) {
		return backing.add(element);
	}
	
	@Override
	public void add(int index, MewVariable element) {
		backing.add(index, element);
	}
	
	@Override
	public MewVariable remove(int index) {
		return backing.remove(index);
	}
	
	@Override
	public MewList cloneList() {
		MewInternalList l = new MewInternalList(mutable);
		l.addAll(backing);
		return l;
	}
	
	@Override
	public List<?> getBacking() {
		return backing;
	}
	@Override
	public int indexOf(Object o) {
		return backing.indexOf(o);
	}
	@Override
	public int lastIndexOf(Object o) {
		return backing.lastIndexOf(o);
	}
	@Override
	public void clear() {
		backing.clear();
	}
	@Override
	public boolean addAll(int index, Collection<? extends MewVariable> c) {
		return backing.addAll(index, c);
	}
	@Override
	public Iterator<MewVariable> iterator() {
		return backing.iterator();
	}
	@Override
	public ListIterator<MewVariable> listIterator() {
		return backing.listIterator();
	}
	@Override
	public ListIterator<MewVariable> listIterator(int index) {
		return backing.listIterator(index);
	}
	@Override
	public List<MewVariable> subList(int fromIndex, int toIndex) {
		return backing.subList(fromIndex, toIndex);
	}
	@Override
	public boolean isEmpty() {
		return backing.isEmpty();
	}
	@Override
	public boolean contains(Object o) {
		return backing.contains(o);
	}
	@Override
	public Object[] toArray() {
		return backing.toArray();
	}
	@Override
	public <T> T[] toArray(T[] a) {
		return super.toArray(a);
	}
	@Override
	public boolean remove(Object o) {
		return backing.remove(o);
	}
	@Override
	public boolean containsAll(Collection<?> c) {
		return backing.containsAll(c);
	}
	@Override
	public boolean addAll(Collection<? extends MewVariable> c) {
		return backing.addAll(c);
	}
	@Override
	public boolean removeAll(Collection<?> c) {
		return backing.removeAll(c);
	}
	@Override
	public boolean retainAll(Collection<?> c) {
		return backing.retainAll(c);
	}
	@Override
	public String toString() {
		return backing.toString();
	}
}

class MewExternalList<T> extends MewList {
	public java.util.List<T> backing;
	private final Class<T> elementType;
    private final MewElementRule rule;
    private final MewElementRule nestedAnyRule = new MewCoercion().anyRule();

    public MewExternalList(java.util.List<T> backing, boolean mutable, Class<T> elementType, MewElementRule elementRule) {
        this.backing = backing;
        this.mutable = mutable;
        this.elementType = java.util.Objects.requireNonNull(elementType, "elementType");
        this.rule = elementRule;
    }

//    /** Null-safe, checked replacement for `element.value instanceof T`. */
//    private boolean isStorable(Object value) {
//        return value != null && elementType.isInstance(value);
//    }

    /** Null-safe, checked replacement for `T e = (T) element.value`. */
    private T cast(Object value) {
        return elementType.cast(value);   // real checkcast, names the actual types on failure
    }
	
//	public MewExternalList(java.util.List<T> backing, boolean mutable) {
//		this.backing = backing;
//		this.mutable = mutable;
//	}
	
	@Override
	public int size() {
		return backing.size();
	}
	
	private MewVariable toVariable(T a) {
		if (a == null) {
			return new MewVariable(MewVariableType.Null, "Oh my god!!!!!");
		}
		if (a instanceof Integer) {
			return new MewVariable(MewVariableType.Integer, a);
		} else if (a instanceof Boolean) {
			return new MewVariable(MewVariableType.Bool, a);
		} else if (a instanceof Double) {
			return new MewVariable(MewVariableType.Float, a);
		} else if (a instanceof String) {
			return new MewVariable(MewVariableType.String, a);
		} else if (a instanceof Throwable) {
			return new MewVariable(MewVariableType.Error, a);
		} else if (a instanceof MewList) {
			return new MewVariable(MewVariableType.List, a);
		} else if (a instanceof java.util.List<?>) {
			return helper1((java.util.List<?>)a, elementType);
		} else if (a instanceof MewMap) {
			return new MewVariable(MewVariableType.Map, a);
		} else if (a instanceof java.util.Map<?, ?>) {
			return helper2((java.util.Map<?, ?>)a, elementType);
		} else if (a instanceof MewListCursor) {
			return new MewVariable(MewVariableType.Cursor, a);
		} else if (a instanceof MewCodeBlock) {
			return new MewVariable(MewVariableType.Code, a);
		} else if (a instanceof MewThen) {
			return new MewVariable(MewVariableType.ConditionalObject, a);
		} else {
			return new MewVariable(MewVariableType.Error, new MewException("MewExternalList cannot serialize objects of type " + a.getClass().getName()));
		}
	}

	@Override
	public MewVariable get(int index) {
		T a = backing.get(index);
		return toVariable(a);
	}
	
	@SuppressWarnings({"unchecked", "rawtypes"})
	private MewVariable helper1(java.util.List list, Class c) {
		return new MewVariable(MewVariableType.List, new MewExternalList<Object>((java.util.List)list, true, Object.class, nestedAnyRule));
	}
	
	@SuppressWarnings({"unchecked", "rawtypes"})
	private MewVariable helper2(java.util.Map map, Class c) {
		return new MewVariable(MewVariableType.Map, new MewExternalMap<Object, Object>((java.util.AbstractMap)map, true, Object.class, Object.class, nestedAnyRule, nestedAnyRule));
	}
	
	@Override
	public MewVariable set(int index, MewVariable element) {
		if (rule.accepts(element.value)) {
            return toVariable(backing.set(index, cast(rule.convert(element.value))));
        }
//		if (isStorable(element.value)) {
//            return toVariable(backing.set(index, cast(element.value)));
//        }
        return new MewVariable(MewVariableType.Error, new MewException(
            "MewExternalList cannot store objects of type "
            + (element.value == null ? "null" : element.value.getClass().getName())
            + " to the external backing store (set)"));
//		if (element.value instanceof T) {
//			T e = (T)element.value;
//			T t = backing.set(index, e);
//			return toVariable(t);
//		} else {
//			return new MewVariable(MewVariableType.Error, new MewException("MewExternalList cannot store objects of type " + element.value.getClass().getName() + " to the external backing store (set)"));
//		}
	}
	
	@Override
	public boolean add(MewVariable element) {
		if (rule.accepts(element.value)) {
            return backing.add(cast(rule.convert(element.value)));
        }
//		 if (isStorable(element.value)) {
//	            return backing.add(cast(element.value));
//	        }
	        throw new UnsupportedOperationException(
	            "MewExternalList cannot store objects of type "
	            + (element.value == null ? "null" : element.value.getClass().getName())
	            + " in the external backing store (add)");
//		if (element.value instanceof T) {
//			T e = (T)element.value;
//			boolean t = backing.add(e);
//			return t;
//		} else {
//			return true; // Per contract
//		}
	}
	
	@Override
	public void add(int index, MewVariable element) {
		if (rule.accepts(element.value)) {
            backing.add(index, cast(rule.convert(element.value)));
            return;
        }
//		 if (isStorable(element.value)) {
//	            backing.add(index, cast(element.value));
//	        }
	        throw new UnsupportedOperationException(
	            "MewExternalList cannot store objects of type "
	            + (element.value == null ? "null" : element.value.getClass().getName())
	            + " in the external backing store (add with index)");
	}
	
	@Override
	public MewVariable remove(int index) {
		T a = backing.remove(index);
		return toVariable(a);
	}

	@Override
	public MewList cloneList() {
		return new MewExternalList<T>(new java.util.ArrayList<>(backing), mutable, elementType, rule);
	}

	@Override
	public List<?> getBacking() {
		return backing;
	}
	
	@Override
	public void clear() {
		backing.clear();
	}

	@Override
	public boolean addAll(int index, Collection<? extends MewVariable> c) {
		ArrayList<T> a = new ArrayList<>();
		for (MewVariable cc : c) {
			if (rule.accepts(cc.value)) {
				a.add(cast(rule.convert(cc.value)));
			} else {
				// Uh what should I do
			}
		}
		return backing.addAll(index, a);
	}

	@Override
	public List<MewVariable> subList(int fromIndex, int toIndex) {
		List<T> a = backing.subList(fromIndex, toIndex);
		ArrayList<MewVariable> aa = new ArrayList<>();
		for (T tt : a) {
			aa.add(toVariable(tt));
		}
		return aa;
	}
}

abstract class MewList extends java.util.AbstractList<MewVariable> {
//	ArrayList<MewVariable> value;
	boolean mutable;
	
	MewList() { mutable = true; }
	MewList(boolean mutable) {
		this.mutable = mutable;
	}
	
	public boolean isMutable() {
		return mutable;
	}
	
	MewVariable nonmutableExceptionHelper(String verb) {
		StringBuilder sb = new StringBuilder();
		sb.append("Cannot ");
		sb.append(verb);
		sb.append(": The list is not mutable.");
		return new MewVariable(MewVariableType.Error, new Exception(sb.toString()));
	}
	
	MewListCursor toCursor() {
		return new MewListCursor(this);
	}
	
	String join(String separator) { return join(separator, 0); }
	String join(String separator, int dirty) {
		MewListCursor cursor = toCursor();
		StringBuilder sb = new StringBuilder();
		while (cursor.hasNext()) {
			MewVariable next = cursor.next();
			if (next.canStringify()) {
				sb.append(next.stringifyValue(dirty + 1));
			} else {
				sb.append("Cannot stringify!");
			}
			if (cursor.hasNext()) {
				sb.append(separator);
			}
		}
		return sb.toString();
	}
	
//	MewList cloneList() {
//		MewList list = new MewList(mutable);
//		list.value = new ArrayList<>(value);
//		return list;
//	}
	
	void makeImmutable() {
		mutable = false;
	}
	
	String toStringEx() { return toStringEx(0); }
	String toStringEx(int dirty) {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		sb.append(join(", ", dirty));
		sb.append("]");
		return sb.toString();
	}
	
	public abstract MewList cloneList();
	public abstract java.util.List<?> getBacking();
	
	@Override
	public String toString() {
		return toStringEx(0);
	}
}

class MewListCursor {
	MewList value;
	private int position;
	
	MewListCursor(MewList value) {
		this.value = value;
		position = 0;
	}
	
	boolean hasNext() {
		return (position < value.size());
	}
	
	MewVariable next() {
		if (hasNext()) {
			MewVariable result = value.get(position);
			position++;
			return result;
		} else {
			return new MewVariable(MewVariableType.Null, "Red Bungor");
		}
	}
	
	MewVariable peek() {
		if (hasNext()) {
			return value.get(position);
		} else {
			return new MewVariable(MewVariableType.Null, "Green Bungor");
		}
	}
	
	public int getPosition() {
		return position;
	}
	
	public void setPosition(int newPosition) {
		this.position = newPosition;
	}
	
	public MewListCursor cloneCursor() {
		MewListCursor cloned = new MewListCursor(value);
		cloned.setPosition(getPosition());
		return cloned;
	}
}

class MewInternalMap extends MewMap {
	public HashMap<String, MewVariable> backing = new HashMap<>()	;
	public MewInternalMap() { super(); }

	MewInternalMap(boolean mutable) {
		super(mutable);
	}

	@Override
	public MewMap cloneMap() {
		MewInternalMap map = new MewInternalMap(mutable);
		map.backing.putAll(backing);
		return map;
	}

	@Override
	public AbstractMap<?, ?> getBacking() {
		return backing;
	}

	@Override
	public Set<Entry<String, MewVariable>> entrySet() {
		return backing.entrySet();
	}
	
	@Override
	public MewVariable put(String key, MewVariable value) {
		MewVariable vv = backing.put(key, value);
		if (vv == null) {
			return new MewVariable(MewVariableType.Null, "Hehehaha");
		} else {
			return (vv);
		}
	}
	
	@Override
	public MewVariable get(Object key) {
		MewVariable vv = backing.get(key);
		if (vv == null) {
			return new MewVariable(MewVariableType.Null, "Ohohohoh");
		} else {
			return (vv);
		}
	}
	
	@Override
	public boolean containsKey(Object key) {
		return backing.containsKey(key);
	}
	
	@Override
	public boolean containsValue(Object value) {
		return backing.containsValue(value);
	}
	
	@Override
	public boolean isEmpty() {
		return backing.isEmpty();
	}
	
	@Override
	public java.util.Collection<MewVariable> values() {
		return backing.values();
	}
	
	@Override
	public java.util.Set<String> keySet() {
		return backing.keySet();
	}
	
	@Override
	public MewVariable remove(Object key) {
		MewVariable vv = backing.remove(key);
		if (vv == null) {
			return new MewVariable(MewVariableType.Null, "Uhuhuhuh");
		} else {
			return (vv);
		}
	}
	
	@Override
	public void clear() {
		backing.clear();
	}
	
	@Override
	public int size() {
		return backing.size();
	}
}

class MewExternalMap<K, V> extends MewMap {
	public java.util.Map<K, V> backing;
	public final Class<K> keyElementType;
	public final Class<V> valueElementType;
	private final MewElementRule keyRule;
	private final MewElementRule valueRule;
	private final MewElementRule nestedAnyRule = new MewCoercion().anyRule();
	
	 /**
     * Cached view. The Map contract expects entrySet() (and the derived
     * keySet()/values(), which AbstractMap caches on first use) to return a
     * stable view of the map, not a fresh copy on every call.
     */
    private final Set<Entry<String, MewVariable>> entrySetView = new ConvertingEntrySet();
	
	public MewExternalMap(java.util.Map<K, V> backing, boolean mutable, Class<K> keyType, Class<V> valueType, MewElementRule keyRule, MewElementRule valueRule) {
		this.backing = backing;
		this.mutable = mutable;
        this.keyElementType = java.util.Objects.requireNonNull(keyType, "keyElementType");
        this.valueElementType = java.util.Objects.requireNonNull(valueType, "valueElementType");
        this.keyRule = keyRule;
        this.valueRule = valueRule;
	}

	@Override
	public MewMap cloneMap() {
		return new MewExternalMap<K, V>(new HashMap<>(backing), mutable, keyElementType, valueElementType, keyRule, valueRule);
	}

	@Override
	public Map<?, ?> getBacking() {
		return backing;
	}
	
	@Override
	public MewVariable get(Object key) {
		if (keyRule.accepts(key)) {
			K ke = castKey(keyRule.convert(key));
			V vv = backing.get(ke);
			if (vv == null) {
				return new MewVariable(MewVariableType.Null, "Hohohoho");
			} else {
				return toVariable(vv);
			}
		} else {
			return new MewVariable(MewVariableType.Error, new MewException(
		            "MewExternalMap cannot get key-value pairs with keys of type "
		            + (key == null ? "null" : key.getClass().getName())
		            + " from the external backing store (get)"));
		}
	}
	
	@Override
	public boolean containsKey(Object key) {
		if (keyRule.accepts(key)) {
			K ke = castKey(keyRule.convert(key));
			return backing.containsKey(ke);
		} else {
			return backing.containsKey(key);
		}
	}
	
	@Override
	public MewVariable remove(Object key) {
		if (keyRule.accepts(key)) {
			K ke = castKey(keyRule.convert(key));
			V vv = backing.remove(ke);
			if (vv == null) {
				return new MewVariable(MewVariableType.Null, "Huhuhuhu");
			} else {
				return toVariable(vv);
			}
		} else {
			return new MewVariable(MewVariableType.Error, new MewException(
		            "MewExternalMap cannot remove key-value pairs with keys of type "
		            + (key == null ? "null" : key.getClass().getName())
		            + " from the external backing store (remove)"));
		}
	}
	
	@Override
	public void clear() {
		backing.clear();
	}
	
	@Override
	public int size() {
		return backing.size();
	}
	
	@Override
	public MewVariable put(String key, MewVariable value) {
		if (keyRule.accepts(key) && valueRule.accepts(value.value)) {
			V vv = (backing.put(castKey(keyRule.convert(key)), castValue(valueRule.convert(value.value))));
			if (vv == null) {
				return new MewVariable(MewVariableType.Null, "Hehehehe");
			} else {
				return toVariable(vv);
			}
		} else {
			if (!keyRule.accepts(key)) {
				return new MewVariable(MewVariableType.Error, new MewException(
			            "MewExternalMap cannot store key-value pairs with keys of type "
			            + (key == null ? "null" : key.getClass().getName())
			            + " to the external backing store (put)"));
			} else {
				return new MewVariable(MewVariableType.Error, new MewException(
			            "MewExternalMap cannot store key-value pairs with values of type "
			            + (value.value == null ? "null" : value.value.getClass().getName())
			            + " to the external backing store (put)"));
			}
		}
	}
	
	/** Null-safe, checked replacement for `element.value instanceof K`. */
//    private boolean isKeyStorable(Object value) {
//        return value != null && keyElementType.isInstance(value);
//    }

    /** Null-safe, checked replacement for `K e = (K) element.value`. */
    private K castKey(Object value) {
        return keyElementType.cast(value);   // real checkcast, names the actual types on failure
    }
	
	/** Null-safe, checked replacement for `element.value instanceof V`. */
//    private boolean isValueStorable(Object value) {
//        return value != null && valueElementType.isInstance(value);
//    }

    /** Null-safe, checked replacement for `V e = (V) element.value`. */
    private V castValue(Object value) {
        return valueElementType.cast(value);   // real checkcast, names the actual types on failure
    }
	
	private MewVariable toVariable(V a) {
		if (a == null) {
			return new MewVariable(MewVariableType.Null, "Nuuuuuu");
		}
		if (a instanceof Integer) {
			return new MewVariable(MewVariableType.Integer, a);
		} else if (a instanceof Boolean) {
			return new MewVariable(MewVariableType.Bool, a);
		} else if (a instanceof Double) {
			return new MewVariable(MewVariableType.Float, a);
		} else if (a instanceof String) {
			return new MewVariable(MewVariableType.String, a);
		} else if (a instanceof Throwable) {
			return new MewVariable(MewVariableType.Error, a);
		} else if (a instanceof MewList) {
			return new MewVariable(MewVariableType.List, a);
		} else if (a instanceof java.util.List<?>) {
			return helper1((java.util.List<?>)a, keyElementType, valueElementType);
		} else if (a instanceof MewMap) {
			return new MewVariable(MewVariableType.Map, a);
		} else if (a instanceof java.util.Map<?, ?>) {
			return helper2((java.util.Map<?, ?>)a, keyElementType, valueElementType);
		} else if (a instanceof MewListCursor) {
			return new MewVariable(MewVariableType.Cursor, a);
		} else if (a instanceof MewCodeBlock) {
			return new MewVariable(MewVariableType.Code, a);
		} else if (a instanceof MewThen) {
			return new MewVariable(MewVariableType.ConditionalObject, a);
		} else {
			return new MewVariable(MewVariableType.Error, new MewException("MewExternalMap cannot serialize objects of type " + a.getClass().getName()));
		}
	}
	
	@SuppressWarnings({"unchecked", "rawtypes"})
	private MewVariable helper1(java.util.List list, Class k, Class v) {
		return new MewVariable(MewVariableType.List, new MewExternalList<Object>((java.util.List)list, true, Object.class, nestedAnyRule));
	}
	
	@SuppressWarnings({"unchecked", "rawtypes"})
	private MewVariable helper2(java.util.Map map, Class c, Class v) {
		return new MewVariable(MewVariableType.Map, new MewExternalMap<Object, Object>((java.util.Map)map, true, Object.class, Object.class, nestedAnyRule, nestedAnyRule));
	}

	@Override
	public Set<Entry<String, MewVariable>> entrySet() {
		return entrySetView;
	}
	
	private final class ConvertingEntrySet extends java.util.AbstractSet<Entry<String, MewVariable>> {

        @Override public int size()      { return backing.size(); }
        @Override public void clear()    { backing.clear(); }

        @Override
        public java.util.Iterator<Entry<String, MewVariable>> iterator() {
            // Wrap the BACKING map's live iterator -- not a snapshot -- so
            // iteration reflects concurrent changes to the backing map, and
            // iterator().remove() writes through to it. (AbstractMap.remove
            // and keySet().remove are implemented via this iterator.)
        	return new ConvertingIterator<>(
                    backing.entrySet().iterator(),
                    raw -> new ConvertingEntry(raw));
        }

        @Override
        public boolean contains(Object o) {
            if (!(o instanceof Entry)) return false;
            Entry<?, ?> e = (Entry<?, ?>) o;
            if (!(e.getKey() instanceof String) || !backing.containsKey(e.getKey())) return false;
            // Compare in the FRONTEND type, per the Map.Entry contract.
            return java.util.Objects.equals(toVariable(backing.get(e.getKey())), e.getValue());
        }

        @Override
        public boolean remove(Object o) {
            if (!(o instanceof Entry)) return false;
            Entry<?, ?> e = (Entry<?, ?>) o;
            if (!(e.getKey() instanceof String) || !backing.containsKey(e.getKey())) return false;
            if (java.util.Objects.equals(toVariable(backing.get(e.getKey())), e.getValue())) {
                backing.remove(e.getKey());
                return true;
            }
            return false;
        }
    }
	
	/**
     * A frontend Map.Entry<String,Integer> wrapping a backing
     * Map.Entry<String,String>. setValue() converts and writes THROUGH to
     * the backing entry, keeping both views consistent.
     */
    private final class ConvertingEntry implements Entry<String, MewVariable> {

        private final Entry<K, V> raw;

        ConvertingEntry(Entry<K, V> raw) {
            this.raw = raw;
        }

        @Override public String getKey()     { return raw.getKey().toString(); }
        @Override public MewVariable getValue()  { return toVariable(raw.getValue()); }

        @Override
        public MewVariable setValue(MewVariable value) {
        	if (valueRule.accepts(value.value)) {
        		return toVariable(raw.setValue(castValue(valueRule.convert(value.value))));   // write-through
        	} else {
        		return new MewVariable(MewVariableType.Error, new MewException(
			            "MewExternalMap cannot store key-value pairs with values of type "
			            + (value.value == null ? "null" : value.value.getClass().getName())
			            + " to the external backing store (setValue)"));
        	}
        }

        // equals/hashCode must be based on the CONVERTED (frontend) value,
        // otherwise the entry will not equal entries from ordinary maps.
        // (AbstractMap.equals/hashCode work through these, which is what
        // makes NumericMap interoperate with plain HashMaps.)

        @Override public boolean equals(Object o) {
            if (!(o instanceof Entry)) return false;
            Entry<?, ?> e = (Entry<?, ?>) o;
            return java.util.Objects.equals(getKey(), e.getKey()) && java.util.Objects.equals(getValue(), e.getValue());
        }

        @Override public int hashCode() {
            return java.util.Objects.hashCode(getKey()) ^ java.util.Objects.hashCode(getValue());
        }

        @Override public String toString() { return getKey() + "=" + getValue(); }
    }
	
	/**
     * Live converting iterator; remove() delegates to the backing map's
     * iterator, so AbstractMap.remove(key) works without any override.
     */
	 // Non-static (Sketchware no-static-members discipline). Type parameters
	 // renamed S/T so they don't shadow MewExternalMap's own K/V.
	 private final class ConvertingIterator<S, T> implements java.util.Iterator<T> {
	
	     private final java.util.Iterator<S> source;
	     private final java.util.function.Function<? super S, ? extends T> converter;
	
	     ConvertingIterator(java.util.Iterator<S> source, java.util.function.Function<? super S, ? extends T> converter) {
	         this.source = source;
	         this.converter = converter;
	     }
	
	     @Override public boolean hasNext() { return source.hasNext(); }
	     @Override public T next()          { return converter.apply(source.next()); }
	     @Override public void remove()     { source.remove(); }   // write-through
	 }

    /*
     * Caveats (same as the HashMap version, plus one):
     *  - A backing value that is not a valid number throws NumberFormatException
     *    during iteration/conversion; validate the backing map if that can happen.
     *  - AbstractMap.get()/containsKey() are O(n) scans of entrySet(); see the
     *    optional overrides above for O(1) delegation.
     *  - Not Serializable (neither is AbstractMap itself). If you need that,
     *    implement it by reading/writing the backing map.
     */
}

abstract class MewMap extends java.util.AbstractMap<String, MewVariable> {
//	HashMap<String, MewVariable> value;
	boolean mutable;
	
	public MewMap() { mutable = true; }
	
	public MewMap(boolean mutable) {
//		value = new HashMap<>();
		this.mutable = mutable;
	}
	
//	MewMap(HashMap<String, MewVariable> value, boolean mutable) {
//		this.value = value;
//		this.mutable = mutable;
//	}
	
	public boolean isMutable() {
		return mutable;
	}
	
	void makeImmutable() {
		mutable = false;
	}
	
	MewVariable nonmutableExceptionHelper(String verb) {
		StringBuilder sb = new StringBuilder();
		sb.append("Cannot ");
		sb.append(verb);
		sb.append(": The map is not mutable.");
		return new MewVariable(MewVariableType.Error, new Exception(sb.toString()));
	}
	
	public abstract MewMap cloneMap();
	public abstract java.util.Map<?, ?> getBacking();
	
	public String toStringEx() { return toStringEx(0); }
	public String toStringEx(int dirty) {
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		java.util.Iterator<String> it = keySet().iterator();
		while (it.hasNext()) {
			String v = it.next();
			sb.append(v);
			sb.append(" = ");
			MewVariable var = get(v);
			if (var.canStringify()) {
				sb.append(var.stringifyValue(dirty));
			} else {
				sb.append("Cannot stringify!");
			}
			
			if (it.hasNext()) {
				sb.append(", ");
			}
		}
		sb.append("}");
		
		return sb.toString();
	}
	
	@Override
	public String toString() {
		return toStringEx(0);
	}
}

interface MewElementRule {

    /** May `value` be stored? Must be a pure predicate with no side effects. */
    boolean accepts(Object value);

    /**
     * Convert `value` into something the backing store can hold.
     * Only called after accepts returned true; must return a value the store accepts.
     */
    Object convert(Object value);
}

final class MewCoercion {

    /** source boxed type -> set of boxed types reachable by widening only. */
    private final Map<Class<?>, Set<Class<?>>> widening;

    /** Instance initializer stands in for the removed static block. */
    {
        Map<Class<?>, Set<Class<?>>> w = new HashMap<Class<?>, Set<Class<?>>>();
        w.put(Byte.class,      toSet(new Class<?>[]{ Short.class, Integer.class, Long.class, Float.class, Double.class }));
        w.put(Short.class,     toSet(new Class<?>[]{ Integer.class, Long.class, Float.class, Double.class }));
        w.put(Character.class, toSet(new Class<?>[]{ Integer.class, Long.class, Float.class, Double.class }));
        w.put(Integer.class,   toSet(new Class<?>[]{ Long.class, Float.class, Double.class }));
        w.put(Long.class,      toSet(new Class<?>[]{ Float.class, Double.class }));
        w.put(Float.class,     toSet(new Class<?>[]{ Double.class }));
        // Double is terminal: nothing widens out of it.
        this.widening = java.util.Collections.unmodifiableMap(w);
    }

    /** Replaces the private static set(...) helper, and works on 1.8. */
    private Set<Class<?>> toSet(Class<?>[] classes) {
        Set<Class<?>> s = new java.util.HashSet<Class<?>>();
        for (int i = 0; i < classes.length; i++) {
            s.add(classes[i]);
        }
        return java.util.Collections.unmodifiableSet(s);
    }

    /**
     * Q1: is `target` a numeric type the adapters may coerce into?
     *
     * Primitive Class objects are excluded deliberately: Class.cast rejects them, so a
     * primitive can never be a working token.
     */
    public boolean isNumericTarget(Class<?> target) {
        if (target == null) {
            return false;
        }
        if (target == Number.class) {
            return true;
        }
        return !target.isPrimitive() && Number.class.isAssignableFrom(target);
    }

    /**
     * Q2: may `value` be stored into a field of type `target`, without data loss?
     * True for an exact match, or for a widening numeric conversion.
     */
    public boolean isStorable(Class<?> target, Object value) {
        if (target == null || value == null) {
            return false;
        }
        if (target.isInstance(value)) {
            return true;                                  // already the right type
        }
        if (!isNumericTarget(target)) {
            return false;                                 // non-numeric: exact match only
        }
        if (target == Number.class) {
            return value instanceof Number || value instanceof Character;
        }
        Set<Class<?>> allowed = widening.get(value.getClass());
        return allowed != null && allowed.contains(target);
    }

    /** Q2, the conversion half. Only call after isStorable returned true. */
    public Object coerce(Class<?> target, Object value) {
        if (target != null && target.isInstance(value)) {
            return value;
        }
        if (!isStorable(target, value)) {
            throw new IllegalArgumentException("cannot store "
                    + (value == null ? "null" : value.getClass().getName())
                    + " in a " + (target == null ? "null" : target.getName()) + " field");
        }

        // Number.class is the abstract case: keep whatever Number the host supplied.
        if (target == Number.class) {
            if (value instanceof Character) {
                return Integer.valueOf(((Character) value).charValue());
            }
            return value;
        }

        long asLong;
        double asDouble;
        if (value instanceof Character) {
            asLong = ((Character) value).charValue();
            asDouble = ((Character) value).charValue();
        } else {
            Number n = (Number) value;
            asLong = n.longValue();
            asDouble = n.doubleValue();
        }

        if (target == Double.class)  { return Double.valueOf(asDouble); }
        if (target == Float.class)   { return Float.valueOf((float) asDouble); }
        if (target == Long.class)    { return Long.valueOf(asLong); }
        if (target == Integer.class) { return Integer.valueOf((int) asLong); }
        if (target == Short.class)   { return Short.valueOf((short) asLong); }
        if (target == Byte.class)    { return Byte.valueOf((byte) asLong); }
        throw new IllegalArgumentException("no conversion to " + target.getName());
    }

    /**
     * Q3, the sustainable part: a Class token cannot describe List<String> vs List<Integer>,
     * so the token alone is not enough for containers. A rule lets a host declare an element
     * rule for a container, keeping the check honest instead of silently permissive.
     *
     * A nested interface is implicitly static, so this one lives in its own file
     * (MewElementRule.java) to honour the no-static-members constraint.
     */
    public MewElementRule of(final Class<?> elementType) {
        return new MewElementRule() {
            public boolean accepts(Object value) { return isStorable(elementType, value); }
            public Object convert(Object value)  { return coerce(elementType, value); }
        };
    }

    /** Replaces the ANY constant. For genuinely heterogeneous host collections. */
    public MewElementRule anyRule() {
        return new MewElementRule() {
            public boolean accepts(Object value) { return true; }
            public Object convert(Object value)  { return value; }
        };
    }

    /** Replaces the STRING constant. */
    public MewElementRule stringRule() {
        return new MewElementRule() {
            public boolean accepts(Object value) { return value instanceof String; }
            public Object convert(Object value)  { return value; }
        };
    }

    /** Rule that rejects everything, for a read-only or sealed backing store. */
    public MewElementRule noneRule() {
        return new MewElementRule() {
            public boolean accepts(Object value) { return false; }
            public Object convert(Object value) {
                throw new UnsupportedOperationException("this backing store accepts no elements");
            }
        };
    }
}

class MewtypeProgram {
	public ArrayList<MewNode> steps = new ArrayList<MewNode>();
	
	public MewVariable run(MewEnvironment environment) {
		environment.escapeProgramFlag = false;
		
		MewNodeCursor cursor = new MewNodeCursor(steps);
		while (cursor.hasNext() && !environment.escapeProgramFlag) {
			MewNode node = cursor.next();
			
			try {
				environment.onBeginProgramStep(node);
				MewVariable result = node.evaluate(environment);
				environment.lastExpression = result;
				environment.onEndProgramStep(node);
			} catch (Exception e) {
				environment.lastExpression = new MewVariable(MewVariableType.Error, new MewException("Runtime Error: " + e.getMessage(), e));
			}
		}
		
		return environment.lastExpression;
	}
}

//class MewNodeCursor {
//	StringBuilder sb;
//	int pos = 0;
//	
//	public MewNodeCursor(String source) {
//		sb = new StringBuilder(source);
//		pos = 0;
//	}
//	
//	int getPosition() {
//		return pos;
//	}
//	void setPosition(int newpos) {
//		pos = newpos;
//	}
//	
//	boolean hasNext() {
//		return (pos < sb.length());
//	}
//	char next() {
//		char c = sb.charAt(pos);
//		pos++;
//		return c;
//	}
//	char peek() {
//		return sb.charAt(pos);
//	}
//	
//	String getSubstring(int startpos, int endpos) {
//		return sb.substring(startpos, endpos);
//	}
//}

class MewtypeProgramSource {
	StringBuilder sb;
	int pos = 0;
	
	public MewtypeProgramSource(String source) {
		sb = new StringBuilder(source);
		pos = 0;
	}
	
	int getPosition() {
		return pos;
	}
	void setPosition(int newpos) {
		pos = newpos;
	}
	
	boolean hasNext() {
		return (pos < sb.length());
	}
	char next() {
		char c = sb.charAt(pos);
		pos++;
		return c;
	}
	char peek() {
		return sb.charAt(pos);
	}
	
	String getSubstring(int startpos, int endpos) {
		return sb.substring(startpos, endpos);
	}
}

class MewStack<T> {
	private ArrayList<T> list = new ArrayList<T>();
	
	public int size() {
		return list.size();
	}
	public T push(T value) {
		list.add(value);
		return value;
	}
	public T pop() {
		if (canPop()) {
			T topmost = list.get(list.size() - 1);
			list.remove(list.size() - 1);
			return topmost;
		}
		return null;
	}
	public T peek() {
		if (canPop()) {
			return list.get(list.size() - 1);
		}
		return null;
	}
	public boolean canPop() {
		return (size() > 0);
	}
}



class MewNoopNode extends MewNode {
	@Override public String getName() { return "Noop"; }

	@Override
	public MewVariable evaluate(MewEnvironment environment) {
		return new MewVariable(MewVariableType.Null, "3 is holy");
	}
	
	public MewNoopNode() { }
}

class MewNoop2Node extends MewNode {
	@Override public String getName() { return "Noop2"; }

	@Override
	public MewVariable evaluate(MewEnvironment environment) {
		return environment.lastExpression;
	}
	
	public MewNoop2Node() { }
}

class MewLeafNode extends MewNode {
	private MewVariable value;

	@Override public String getName() { return "MewLeafNode"; }

	@Override
	public MewVariable evaluate(MewEnvironment environment) {
		if (value.type == MewVariableType.VariableReference) {
			return environment.variables.get(value.value.toString());
		}
		return value;
	}
	
	public MewVariable getValue() {
		return value;
	}
	
	public void setValue(MewVariable newValue) {
		this.value = newValue;
	}
	
	public MewLeafNode(MewVariable variable) {
		this.value = variable;
	}
}

class MewCodeBlockNode extends MewNode {
	private MewCodeBlock block;
	
	@Override public String getName() { return "MewCodeBlockNode"; }

	@Override
	public MewVariable evaluate(MewEnvironment environment) {
		ArrayList<MewVariable> args = new ArrayList<>();
		for (MewNode node : nodes) {
			args.add(node.evaluate(environment));
		}
		return block.run(environment, args);
	}
	
	public MewCodeBlock getCodeBlock() {
		return block;
	}
	
	public void setCodeBlock(MewCodeBlock newBlock) {
		block = newBlock;
	}
	
	public MewCodeBlockNode(MewCodeBlock codeBlock) {
		block = codeBlock;
	}
}

class MewAssignment extends MewNode2Arg {
	@Override public String getName() { return "="; }

	@Override
	public MewVariable evaluate(MewEnvironment environment) {
		MewNode left = getLeft();
		MewVariable value = getRight().evaluate(environment);
		String name = null;
		
		// a VariableReference leaf carries the NAME; evaluating it would give the old value
		if (left instanceof MewLeafNode && ((MewLeafNode)left).getValue().type == MewVariableType.VariableReference) {
			name = ((MewLeafNode)left).getValue().value.toString();
		} else {
			MewVariable variableName = left.evaluate(environment);
			if (variableName.canStringify()) {
				name = variableName.stringifyValue();
			}
		}
		
		if (name == null) {
			return new MewVariable(MewVariableType.Error, new MewException("Variable Name resolved to something that is not a primitive [1]"));
		}
		environment.variables.put(name, value);
		return value;
	}
	
	public MewAssignment() { }
 }

class MewGetVariable extends MewNode1Arg {
	@Override public String getName() { return "Get-Variable"; }

	@Override
	public MewVariable evaluate(MewEnvironment environment) {
		String name = null;
		MewNode left = getLeft();
		
		if (left instanceof MewLeafNode && ((MewLeafNode)left).getValue().type == MewVariableType.VariableReference) {
			name = ((MewLeafNode)left).getValue().value.toString();
		} else {
			MewVariable variableName = left.evaluate(environment);
			if (variableName.canStringify()) {
				name = variableName.stringifyValue();
			}
		}
		
		if (name == null) {
			return new MewVariable(MewVariableType.Error, new MewException("Variable Name resolved to something that is not a primitive [2]"));
		}
		
		if (environment.variables.containsKey(name)) {
			return environment.variables.get(name);
		} else {
			return new MewVariable(MewVariableType.Error, new MewException("Variable Name not found"));
		}
	}
	
	public MewGetVariable() { }
 }

class MewExec extends MewNode {
	@Override public String getName() { return "Exec"; }

	@Override
	public MewVariable evaluate(MewEnvironment environment) {
		MewNodeCursor cursor = toCursor();
		while (cursor.hasNext()) {
			MewNode next = cursor.next();
			MewVariable result = next.evaluate(environment);
			environment.lastExpression = result;
		}
		return environment.lastExpression;
	}
	
	@Override
	public int getMinArgumentCount() { return 0; }

	@Override
	public int getMaxArgumentCount() { return Integer.MAX_VALUE; }
	
	public MewExec() { }
}

class MewAns extends MewNode {
	@Override public String getName() { return "Ans"; }

	@Override
	public MewVariable evaluate(MewEnvironment environment) {
		return environment.lastExpression;
	}
	
	public MewAns() { }
}

class MewQuit extends MewNode {
	@Override public String getName() { return "Quit"; }

	@Override
	public MewVariable evaluate(MewEnvironment environment) {
		environment.escapeProgramFlag = true;
		environment.escapeLoopFlag = true;
		return environment.lastExpression;
	}
	
	public MewQuit() { }
}

class MewToastMessage extends MewNode1Arg {
	@Override public String getName() { return "Toast-Message"; }

	@Override
	public MewVariable evaluate(MewEnvironment environment) {
		MewNode left = getLeft();

		MewVariable message = left.evaluate(environment);
		
		if (message.canStringify()) {
			environment.showMessage(message.stringifyValue());
		} else {
			return new MewVariable(MewVariableType.Error, new MewException("Toast-Message: expression cannot be stringified"));
		}
		
		return message;
	}
	
	public MewToastMessage() { }
}

class MewLogMessage extends MewNode1Arg {
	@Override public String getName() { return "Log-Message"; }

	@Override
	public MewVariable evaluate(MewEnvironment environment) {
		MewNode left = getLeft();

		MewVariable message = left.evaluate(environment);
		
		if (message.canStringify()) {
			environment.logMessage(message.stringifyValue());
		} else {
			return new MewVariable(MewVariableType.Error, new MewException("Log-Message: expression cannot be stringified"));
		}
		
		return message;
	}
	
	public MewLogMessage() { }
}

class MewGetSpecialVariable extends MewNode1Arg {
	@Override public String getName() { return "Get-Special-Variable"; }

	@Override
	public MewVariable evaluate(MewEnvironment environment) {
		MewNode left = getLeft();
		
		MewVariable name = left.evaluate(environment);
		if (name.canStringify()) {
			return environment.getConstantValues(name.stringifyValue());
		} else {
			return new MewVariable(MewVariableType.Error, new MewException("Get-Special-Variable: expression cannot be stringified"));
		}
	}
	
	public MewGetSpecialVariable() { }
}

class MewGetVariables extends MewNode {
	@Override public String getName() { return "Get-Variables"; }

	@Override
	public MewVariable evaluate(MewEnvironment environment) {
		MewList l = new MewInternalList(false);
		for (String name : environment.variables.keySet()) {
			l.add(new MewVariable(MewVariableType.String, name));
		}
		return new MewVariable(MewVariableType.List, l);
	}
	
	public MewGetVariables() { }
}

class MewLogEnvironment extends MewNode {
	@Override public String getName() { return "Log-Environment"; }

	@Override
	public MewVariable evaluate(MewEnvironment environment) {
		StringBuilder sb = new StringBuilder();
		sb.append("Logging Mewtype environment");
		sb.append("\n> Platform Name: ");
		sb.append(environment.getPlatformName());
		sb.append("\n> Mewtype Version: ");
		sb.append(environment.getConstantValues("LANGUAGE_VERSION").value);
		sb.append(" build ");
		sb.append(environment.getConstantValues("BUILD_NUMBER").value);
		sb.append("\n> Variable Dump:");
		for (HashMap<String, MewVariable> stack : environment.variables.stack) {
			sb.append("\n  -> Begin Stack");
			for (java.util.Map.Entry<String, MewVariable> e : stack.entrySet()) {
				sb.append("\n      ");
				sb.append(e.getKey());
				sb.append(" = ");
				sb.append(e.getValue().value.toString());
			}
			sb.append("\n  -> End Stack");
		}
		sb.append("\n> Flags:\n  -> Break: ");
		sb.append(environment.escapeLoopFlag);
		sb.append("\n  -> Quit: ");
		sb.append(environment.escapeProgramFlag);
		sb.append("\n> Limits:\n  -> Code Block Depth: ");
		sb.append(environment.codeBlockDepth);
		sb.append(" of ");
		sb.append(environment.getMaxCodeBlockDepth());
		sb.append("\n  -> Recursive ExecuteString Depth: ");
		sb.append(environment.recursiveExecuteStringCap);
		sb.append(" of ");
		sb.append(environment.getExecuteStringCap());
		sb.append("\n  -> Loop Iteration Budget: used ");
		sb.append(environment.totalIterations);
		sb.append(" of ");
		sb.append(environment.getTotalIterationBudget());
		
		environment.logMessage(sb.toString());
		return new MewVariable(MewVariableType.String, sb.toString());
	}
	
	public MewLogEnvironment() { }
}

//abstract class MewNodeLoopCursor2 extends MewNode {
//	@Override public int getMinArgumentCount() { return 2; }
//	@Override public int getMaxArgumentCount() { return Integer.MAX_VALUE; }
//
//	@Override
//	public MewVariable evaluate(MewEnvironment environment) {
//		resetState();
//		beginStep(environment);
//		
//		MewNodeCursor cursor = toCursor();
//		while (cursor.hasNext()) {
//			MewNode next = cursor.next();
//			MewVariable result = next.evaluate(environment);
//			
//			nextStep(environment, result);
//		}
//		
//		return endStep(environment);
//	}
//	
//	public abstract void resetState();
//	public abstract void beginStep(MewEnvironment environment);
//	public abstract void nextStep(MewEnvironment environment, MewVariable result);
//	public abstract MewVariable endStep(MewEnvironment environment);
//}

class MewAddition extends MewNode2VarArg {
	@Override public String getName() { return "+"; }

	@Override
	public MewVariable evaluate(MewEnvironment environment) {
		double fResult = 0.0;
		MewNodeCursor cursor = toCursor();
		while (cursor.hasNext()) {
			MewNode next = cursor.next();
			MewVariable result = next.evaluate(environment);
			
			if (result.isNumeric()) {
				fResult += result.toDouble();
			} else {
				return new MewVariable(MewVariableType.Error, new MewException("Cannot perform addition on non-numeric argument " + (int)(cursor.getPosition() - 1)));
			}
		}
		if (fResult % 1 == 0) {
			// Whole number
			if (Integer.MAX_VALUE >= fResult && fResult >= Integer.MIN_VALUE) {
				return new MewVariable(MewVariableType.Integer, (int)fResult);
			}
		}
		return new MewVariable(MewVariableType.Float, fResult);
	}
	
	public MewAddition() { }
}

class MewSubtraction extends MewNode2VarArg {
	@Override public String getName() { return "-"; }

	@Override
	public MewVariable evaluate(MewEnvironment environment) {
		double fResult = 0.0;
		MewNodeCursor cursor = toCursor();
		MewVariable first = cursor.next().evaluate(environment);
		
		if (!first.isNumeric()) {
			return new MewVariable(MewVariableType.Error, new MewException("Cannot perform subtraction because argument 1 is non-numeric"));		
		}		
		fResult = first.toDouble();

		while (cursor.hasNext()) {
			MewNode next = cursor.next();
			MewVariable result = next.evaluate(environment);
			
			if (result.isNumeric()) {
				fResult -= result.toDouble();
			} else {
				return new MewVariable(MewVariableType.Error, new MewException("Cannot perform subtraction on non-numeric argument " + (int)(cursor.getPosition() - 1)));
			}
		}
		if (fResult % 1 == 0) {
			// Whole number
			if (Integer.MAX_VALUE >= fResult && fResult >= Integer.MIN_VALUE) {
				return new MewVariable(MewVariableType.Integer, (int)fResult);
			}
		}
		return new MewVariable(MewVariableType.Float, fResult);
	}
	
	public MewSubtraction() { }
}

class MewMultiplication extends MewNode2VarArg {
	@Override public String getName() { return "*"; }

	@Override
	public MewVariable evaluate(MewEnvironment environment) {
		double fResult = 0.0;
		MewNodeCursor cursor = toCursor();
		MewVariable first = cursor.next().evaluate(environment);
		
		if (!first.isNumeric()) {
			return new MewVariable(MewVariableType.Error, new MewException("Cannot perform multiplication because argument 1 is non-numeric"));		
		}
		fResult = first.toDouble();
		
		while (cursor.hasNext()) {
			MewNode next = cursor.next();
			MewVariable result = next.evaluate(environment);
			
			if (result.isNumeric()) {
				fResult *= result.toDouble();
			} else {
				return new MewVariable(MewVariableType.Error, new MewException("Cannot perform multiplication on non-numeric argument " + (int)(cursor.getPosition() - 1)));
			}
		}
		if (fResult % 1 == 0) {
			// Whole number
			if (Integer.MAX_VALUE >= fResult && fResult >= Integer.MIN_VALUE) {
				return new MewVariable(MewVariableType.Integer, (int)fResult);
			}
		}
		return new MewVariable(MewVariableType.Float, fResult);
	}
	
	public MewMultiplication() { }
}

class MewDivision extends MewNode2VarArg {
	@Override public String getName() { return "/"; }

	@Override
	public MewVariable evaluate(MewEnvironment environment) {
		double fResult = 0.0;
		MewNodeCursor cursor = toCursor();
		MewVariable first = cursor.next().evaluate(environment);
		if (!first.isNumeric()) {
			return new MewVariable(MewVariableType.Error, new MewException("Cannot perform division because argument 1 is non-numeric"));		
		}
		fResult = first.toDouble();
		
		while (cursor.hasNext()) {
			MewNode next = cursor.next();
			MewVariable result = next.evaluate(environment);
			
			if (result.isNumeric()) {
				if (Math.abs(result.toDouble()) < environment.getDoubleEpsilon()) {
					return new MewVariable(MewVariableType.Error, new MewException("Division by zero [1]"));
				}
			} else {
				return new MewVariable(MewVariableType.Error, new MewException("Cannot perform division on non-numeric argument " + (int)(cursor.getPosition() - 1)));
			}

			fResult /= result.toDouble();
		}
		return new MewVariable(MewVariableType.Float, fResult);
	}
	
	public MewDivision() { }
}

class MewAdditionAssign extends MewNode2VarArg {
	@Override public String getName() { return "+="; }

	@Override
	public MewVariable evaluate(MewEnvironment environment) {
		MewNode op1 = new MewAddition();
		op1.nodes = new ArrayList<MewNode>(nodes);
		
		MewNode op2 = new MewAssignment();
		op2.nodes = new ArrayList<MewNode>();
		op2.nodes.add(getLeft());
		op2.nodes.add(op1);
		return op2.evaluate(environment);
	}
	
	public MewAdditionAssign() { }
}

class MewSubtractionAssign extends MewNode2VarArg {
	@Override public String getName() { return "-="; }

	@Override
	public MewVariable evaluate(MewEnvironment environment) {
		MewNode op1 = new MewSubtraction();
		op1.nodes = new ArrayList<MewNode>(nodes);
		
		MewNode op2 = new MewAssignment();
		op2.nodes = new ArrayList<MewNode>();
		op2.nodes.add(getLeft());
		op2.nodes.add(op1);
		return op2.evaluate(environment);
	}
	
	public MewSubtractionAssign() { }
}

class MewMultiplicationAssign extends MewNode2VarArg {
	@Override public String getName() { return "*="; }

	@Override
	public MewVariable evaluate(MewEnvironment environment) {
		MewNode op1 = new MewMultiplication();
		op1.nodes = new ArrayList<MewNode>(nodes);
		
		MewNode op2 = new MewAssignment();
		op2.nodes = new ArrayList<MewNode>();
		op2.nodes.add(getLeft());
		op2.nodes.add(op1);
		return op2.evaluate(environment);
	}
	
	public MewMultiplicationAssign() { }
}

class MewDivisionAssign extends MewNode2VarArg {
	@Override public String getName() { return "/="; }

	@Override
	public MewVariable evaluate(MewEnvironment environment) {
		MewNode op1 = new MewDivision();
		op1.nodes = new ArrayList<MewNode>(nodes);
		
		MewNode op2 = new MewAssignment();
		op2.nodes = new ArrayList<MewNode>();
		op2.nodes.add(getLeft());
		op2.nodes.add(op1);
		return op2.evaluate(environment);
	}
	
	public MewDivisionAssign() { }
}

class MewConcat extends MewNode {
	@Override public String getName() { return "Concat"; }

	@Override
	public MewVariable evaluate(MewEnvironment environment) {
		MewNodeCursor cursor = toCursor();
		StringBuilder sb = new StringBuilder();
		while (cursor.hasNext()) {
			MewVariable var = cursor.next().evaluate(environment);
			if (var.type == MewVariableType.List && var.value instanceof MewList) {
				MewList list = (MewList)var.value;
				sb.append(list.toStringEx());
			} else if (var.type == MewVariableType.Map && var.value instanceof MewMap) {
				MewMap map = (MewMap)var.value;
				sb.append(map.toStringEx());
			} else if (var.canStringify()) {
				sb.append(var.stringifyValue());
			} else {
				sb.append(var.value.toString());
			}
			
			if (sb.length() > environment.getMaxStringLength()) {
				return new MewVariable(MewVariableType.Error, new MewException("Cannot Concat: string too long at size " + sb.length() + " while maximum permitted is " + environment.getMaxStringLength()));
			}
		}
		
//		// Too restrictive?
//		environment.totalStringBytes += sb.length();
//		if (environment.totalStringBytes > environment.getMaxTotalStringBytes()) { 
//			environment.escapeProgramFlag = true; 
//			environment.escapeLoopFlag = true;
//		    return new MewVariable(MewVariableType.Error, new MewException("Runtime error: string work budget exhausted")); 
//		}
		return new MewVariable(MewVariableType.String, sb.toString());
	}
	
	@Override
	public int getMinArgumentCount() { return 1; }

	@Override
	public int getMaxArgumentCount() { return Integer.MAX_VALUE; }
	
	public MewConcat() { }
}

class MewCompare extends MewNode2Arg {
	@Override public String getName() { return "Compare"; }

	@Override
	public MewVariable evaluate(MewEnvironment environment) {
		MewNode left = getLeft();
		MewNode right = getRight();
		MewVariable l = left.evaluate(environment);
		MewVariable r = right.evaluate(environment);
		
		if (l.isNumeric()) {
			if (r.isNumeric()) {
				return new MewVariable(MewVariableType.Float, Double.compare(l.toDouble(), r.toDouble()));
			} else {
				return new MewVariable(MewVariableType.Error, new MewException("Cannot compare: argument 2 (right) is not numeric"));
			}
		} else {
			return new MewVariable(MewVariableType.Error, new MewException("Cannot compare: argument 1 (left) is not numeric"));
		}
	}
	
	public MewCompare() { }
}

class MewEquals extends MewNode2Arg {
	@Override public String getName() { return "Equals"; }

	@Override
	public MewVariable evaluate(MewEnvironment environment) {
		MewNode left = getLeft();
		MewNode right = getRight();
		MewVariable l = left.evaluate(environment);
		MewVariable r = right.evaluate(environment);
		
		return new MewVariable(MewVariableType.Bool, l.equalsEx(r));
	}
	
	public MewEquals() { }
}

class MewEqualsStrict extends MewNode2Arg {
	@Override public String getName() { return "Equals-Strict"; }

	@Override
	public MewVariable evaluate(MewEnvironment environment) {
		MewNode left = getLeft();
		MewNode right = getRight();
		MewVariable l = left.evaluate(environment);
		MewVariable r = right.evaluate(environment);
		
		return new MewVariable(MewVariableType.Bool, l.equals(r));
	}
	
	public MewEqualsStrict() { }
}

class MewEqualsIdentity extends MewNode2Arg {
	@Override public String getName() { return "Equals-Identity"; }

	@Override
	public MewVariable evaluate(MewEnvironment environment) {
		MewNode left = getLeft();
		MewNode right = getRight();
		MewVariable l = left.evaluate(environment);
		MewVariable r = right.evaluate(environment);
		
		return new MewVariable(MewVariableType.Bool, System.identityHashCode(l) == System.identityHashCode(r));
	}
	
	public MewEqualsIdentity() { }
}

class MewNot extends MewNode1Arg {
	@Override public String getName() { return "Not"; }

	@Override
	public MewVariable evaluate(MewEnvironment environment) {
		MewNode left = getLeft();
		MewVariable value = left.evaluate(environment);
		
		if (value.type == MewVariableType.Bool) {
			return new MewVariable(MewVariableType.Bool, !((boolean)value.value));
		}
		if (value.type == MewVariableType.Integer) {
			return new MewVariable(MewVariableType.Integer, -1 * ((int)value.value));
		}
		if (value.type == MewVariableType.Float) {
			return new MewVariable(MewVariableType.Float, -1 * ((double)value.value));
		}
		return new MewVariable(MewVariableType.Error, new MewException("Cannot perform negation on non-numeric types"));
	}
	
	public MewNot() { }
}

class MewAnd extends MewNode2VarArg {
	@Override public String getName() { return "And"; }

	@Override
	public MewVariable evaluate(MewEnvironment environment) {
		MewNodeCursor cursor = toCursor();
		while (cursor.hasNext()) {
			MewNode node = cursor.next();
			MewVariable result = node.evaluate(environment);
			if (result.type == MewVariableType.Bool) {
				if (!result.toBool()) {
					return new MewVariable(MewVariableType.Bool, false);
				}
			} else {
				return new MewVariable(MewVariableType.Error, new MewException("Cannot perform logical and because argument " + cursor.getPosition() + " is not of type bool"));
			}
		}
		return new MewVariable(MewVariableType.Bool, true);
	}
	
	public MewAnd() { }
}

class MewAndAlso extends MewNode2VarArg {
	@Override public String getName() { return "AndEager"; }

	@Override
	public MewVariable evaluate(MewEnvironment environment) {
		boolean result = true;
		int argpos = 0;
		for (MewNode node : nodes) {
			MewVariable var = node.evaluate(environment);
			if (var.type == MewVariableType.Bool) {
				result = result && var.toBool();
			} else {
				return new MewVariable(MewVariableType.Error, new MewException("Cannot perform logical andeager on non-boolean value (argument " + argpos + ")"));
			}
			argpos++;
		}
		return new MewVariable(MewVariableType.Bool, result); 
	}
	
	public MewAndAlso() { }
}

class MewOr extends MewNode2VarArg {
	@Override public String getName() { return "Or"; }

	@Override
	public MewVariable evaluate(MewEnvironment environment) {
		MewNodeCursor cursor = toCursor();
		while (cursor.hasNext()) {
			MewNode node = cursor.next();
			MewVariable result = node.evaluate(environment);
			if (result.type == MewVariableType.Bool) {
				if (result.toBool()) {
					return new MewVariable(MewVariableType.Bool, true);
				}
			} else {
				return new MewVariable(MewVariableType.Error, new MewException("Cannot perform logical or because argument " + cursor.getPosition() + " is not of type bool"));
			}
		}
		return new MewVariable(MewVariableType.Bool, false);
	}
	
	public MewOr() { }
}

class MewOrAlso extends MewNode2VarArg {
	@Override public String getName() { return "OrEager"; }

	@Override
	public MewVariable evaluate(MewEnvironment environment) {
		boolean result = false;
		int argpos = 0;
		for (MewNode node : nodes) {
			MewVariable var = node.evaluate(environment);
			if (var.type == MewVariableType.Bool) {
				result = result || var.toBool();
			} else {
				return new MewVariable(MewVariableType.Error, new MewException("Cannot perform logical oreager on non-boolean value (argument " + argpos + ")"));
			}
			argpos++;
		}
		return new MewVariable(MewVariableType.Bool, result); 
	}
	
	public MewOrAlso() { }
}

class MewIf extends MewNode {
	@Override public String getName() { return "If"; }

	@Override
	public MewVariable evaluate(MewEnvironment environment) {
		MewNode left = getLeft();
		MewNode right = getRight();
		
		// Resolve arg2 to a Then WITHOUT evaluating anything that could have side effects.
	    MewThen then = resolveThenSafely(right, environment);

	    MewVariable condition = left.evaluate(environment);

	    if (then != null) {
	    	if (condition.truthy()) {
	    		return then.getLeft().evaluate(environment);
	    	} else {
	    		return then.getRight().evaluate(environment);
	    	}
	    }

	    if (condition.truthy()) {
	        MewVariable result = right.evaluate(environment);
	        // arg2 turned out to be a Then only at runtime (e.g. returned by a code block)
	        if (nodes.size() == 2 && result != null
	                && result.type == MewVariableType.ConditionalObject
	                && result.value instanceof MewThen) {
	            return ((MewThen)(result.value)).getLeft().evaluate(environment);
	        }
	        return result;
	    } else {
	        if (nodes.size() == 2) {
	            return new MewVariable(MewVariableType.Error, new MewException(
	                "Expected a Then(thenClause, elseClause) statement in arg2 (If (condition) (Then ...)) or (If (condition) (thenClause) (elseClause)) [2]"));
	        } else {
	            return nodes.get(2).evaluate(environment);
	        }
	    }
	}	
	
//	private boolean argumentResolvesToConditionalObject(MewVariable var, MewEnvironment environment) {
//		if (var.type == MewVariableType.VariableReference) {
//			String varName = var.value.toString();
//			if (environment.variables.containsKey(varName)) {
//				MewVariable retrievedVariable = environment.variables.get(varName);
//				return (retrievedVariable.type == MewVariableType.ConditionalObject);
//			}
//		}
//		return false;
//	}
	
	private MewThen resolveThenSafely(MewNode arg, MewEnvironment environment) {
	    if (arg instanceof MewThen) {
	        return (MewThen) arg;
	    }
	    if (arg instanceof MewLeafNode) {
	        MewVariable v = ((MewLeafNode) arg).getValue();
	        if (v != null) {
	            if (v.type == MewVariableType.VariableReference) {
	                MewVariable stored = environment.variables.get(v.value.toString());
	                if (stored != null && stored.type == MewVariableType.ConditionalObject
	                        && stored.value instanceof MewThen) {
	                    return (MewThen)(stored.value);
	                }
	            } else if (v.type == MewVariableType.ConditionalObject && v.value instanceof MewThen) {
	                return (MewThen)(v.value);
	            }
	        }
	    }
	    return null;
	}
	
	@Override
	public int getMinArgumentCount() { return 2; }

	@Override
	public int getMaxArgumentCount() { return 3; }
	
	public MewIf() { }
}

class MewThen extends MewNode {
	@Override public String getName() { return "Then"; }

	@Override
	public MewVariable evaluate(MewEnvironment environment) {
		return new MewVariable(MewVariableType.ConditionalObject, this);
	}
	
	@Override
	public int getMinArgumentCount() { return 1; }

	@Override
	public int getMaxArgumentCount() { return 2; }
	
	public MewThen() { }
	
	@Override
	public MewNode getRight() {
		if (nodes.size() == 1) {
			return new MewNoopNode();
		} else {
			return super.getRight();
		}
	}
}

class MewWhile extends MewNode2Arg {
	@Override public String getName() { return "While"; }

	@Override
	public MewVariable evaluate(MewEnvironment environment) {
		boolean loop = true;
		long count = 0;
		MewNode left = getLeft();
		MewNode right = getRight();
		MewVariable last = new MewVariable(MewVariableType.Null, "Give me 788");
		boolean envEscapeLoopFlag = environment.escapeLoopFlag;
		environment.escapeLoopFlag = false;
		while (loop && !environment.escapeLoopFlag) {
			if (count >= environment.getForeverLoopDetectionThreshold() || environment.budgetExceeded() || environment.escapeProgramFlag) {
				environment.escapeProgramFlag = envEscapeLoopFlag;
				return new MewVariable(MewVariableType.Error, new MewException("Runtime error: Maximum number of iterations hit. Current threshold is " + environment.getForeverLoopDetectionThreshold()));
			}
			MewVariable condition = left.evaluate(environment);
//			last = condition;
			environment.lastExpression = condition;
			loop = (condition.truthy());
			if (loop) {
				MewVariable body = right.evaluate(environment);
				environment.lastExpression = body;
				last = body;
				count++;
			}
		}
		environment.escapeLoopFlag = envEscapeLoopFlag;
		environment.lastExpression = last;
		return last;
	}
	
	public MewWhile() { }
}

class MewBreak extends MewNode {
	@Override public String getName() { return "Break"; }

	@Override
	public MewVariable evaluate(MewEnvironment environment) {
		environment.escapeLoopFlag = true;
		return environment.lastExpression;
	}
	
	public MewBreak() { }
}

abstract class MewNodeComparisonArity2 extends MewNode2Arg {
	@Override
	public MewVariable evaluate(MewEnvironment environment) {
		MewNode left = getLeft();
		MewNode right = getRight();
		MewVariable lr = left.evaluate(environment);
		MewVariable rr = right.evaluate(environment);
		
		return evaluateInner(environment, lr, rr);
	}
	
	public abstract MewVariable evaluateInner(MewEnvironment environment, MewVariable lr, MewVariable rr);
}

class MewLessThan extends MewNodeComparisonArity2 {
	@Override public String getName() { return "Less-Than"; }
	
	public MewLessThan() { }

	@Override
	public MewVariable evaluateInner(MewEnvironment environment, MewVariable lr, MewVariable rr) {
		if (lr.type == MewVariableType.Null || rr.type == MewVariableType.Null) {
			return new MewVariable(MewVariableType.Bool, false);
		}
		if (lr.isNumeric() && rr.isNumeric()) {
			return new MewVariable(MewVariableType.Bool, (lr.toDouble() < rr.toDouble()));
		}
		
		String sl = lr.stringifyValue();
		String sr = rr.stringifyValue();
		if (sl.length() > 0 && sr.length() > 0) {
			return new MewVariable(MewVariableType.Bool, (sl.compareTo(sr) < 0));
		} else {
			if (sl.length() > 0) {
				return new MewVariable(MewVariableType.Bool, false);
			} else if (sr.length() > 0) {
				return new MewVariable(MewVariableType.Bool, true);
			} else {
				return new MewVariable(MewVariableType.Bool, false);
			}
		}
	}
}

class MewLessEqualsThan extends MewNodeComparisonArity2 {
	@Override public String getName() { return "Less-Equals"; }
	
	public MewLessEqualsThan() { }

	@Override
	public MewVariable evaluateInner(MewEnvironment environment, MewVariable lr, MewVariable rr) {
		if (lr.type == MewVariableType.Null || rr.type == MewVariableType.Null) {
			return new MewVariable(MewVariableType.Bool, false);
		}
		if (lr.isNumeric() && rr.isNumeric()) {
			return new MewVariable(MewVariableType.Bool, (lr.toDouble() <= rr.toDouble()));
		}
		
		String sl = lr.stringifyValue();
		String sr = rr.stringifyValue();
		if (sl.length() > 0 && sr.length() > 0) {
			return new MewVariable(MewVariableType.Bool, (sl.compareTo(sr) <= 0));
		} else {
			if (sl.length() > 0) {
				return new MewVariable(MewVariableType.Bool, false);
			} else if (sr.length() > 0) {
				return new MewVariable(MewVariableType.Bool, true);
			} else {
				return new MewVariable(MewVariableType.Bool, false);
			}
		}
	}
}

class MewGreaterThan extends MewNodeComparisonArity2 {
	@Override public String getName() { return "Greater-Than"; }
	
	public MewGreaterThan() { }

	@Override
	public MewVariable evaluateInner(MewEnvironment environment, MewVariable lr, MewVariable rr) {
		if (lr.type == MewVariableType.Null || rr.type == MewVariableType.Null) {
			return new MewVariable(MewVariableType.Bool, false);
		}
		if (lr.isNumeric() && rr.isNumeric()) {
			return new MewVariable(MewVariableType.Bool, (lr.toDouble() > rr.toDouble()));
		}
		
		String sl = lr.stringifyValue();
		String sr = rr.stringifyValue();
		if (sl.length() > 0 && sr.length() > 0) {
			return new MewVariable(MewVariableType.Bool, (sl.compareTo(sr) > 0));
		} else {
			if (sl.length() > 0) {
				return new MewVariable(MewVariableType.Bool, true);
			} else if (sr.length() > 0) {
				return new MewVariable(MewVariableType.Bool, false);
			} else {
				return new MewVariable(MewVariableType.Bool, false);
			}
		}
	}
}

class MewGreaterEqualsThan extends MewNodeComparisonArity2 {
	@Override public String getName() { return "Greater-Equals"; }
	
	public MewGreaterEqualsThan() { }

	@Override
	public MewVariable evaluateInner(MewEnvironment environment, MewVariable lr, MewVariable rr) {
		if (lr.type == MewVariableType.Null || rr.type == MewVariableType.Null) {
			return new MewVariable(MewVariableType.Bool, false);
		}
		if (lr.isNumeric() && rr.isNumeric()) {
			return new MewVariable(MewVariableType.Bool, (lr.toDouble() >= rr.toDouble()));
		}
		
		String sl = lr.stringifyValue();
		String sr = rr.stringifyValue();
		if (sl.length() > 0 && sr.length() > 0) {
			return new MewVariable(MewVariableType.Bool, (sl.compareTo(sr) >= 0));
		} else {
			if (sl.length() > 0) {
				return new MewVariable(MewVariableType.Bool, true);
			} else if (sr.length() > 0) {
				return new MewVariable(MewVariableType.Bool, false);
			} else {
				return new MewVariable(MewVariableType.Bool, false);
			}
		}
	}
}

class MewModulus extends MewNode {
	@Override public String getName() { return "Mod"; }

	@Override
	public MewVariable evaluate(MewEnvironment environment) {
		MewNode left = getLeft();
		MewNode right = getRight();
		MewVariable l = left.evaluate(environment);
		MewVariable r = right.evaluate(environment);
		
		if (l.isNumeric() && r.isNumeric()) {
			if (r.toInt() == 0) {
				return new MewVariable(MewVariableType.Error, new MewException("Modulus operation by zero"));
			}
			return new MewVariable(MewVariableType.Integer, Math.abs(l.toInt() % r.toInt()));
		}
		return new MewVariable(MewVariableType.Error, new MewException("Cannot perform modulus on non-numeric types"));
	}
	
	@Override
	public int getMinArgumentCount() { return 2; }

	@Override
	public int getMaxArgumentCount() { return 2; }
	
	public MewModulus() { }
}

class MewExponent extends MewNode2Arg {
	@Override public String getName() { return "^"; }

	@Override
	public MewVariable evaluate(MewEnvironment environment) {
		MewNode left = getLeft();
		MewNode right = getRight();
		MewVariable l = left.evaluate(environment);
		MewVariable r = right.evaluate(environment);
		
		if (l.isNumeric() && r.isNumeric()) {
			double res = Math.pow(l.toDouble(), r.toDouble());
			if (res % 1 == 0) {
				// Whole number
				if (Integer.MAX_VALUE >= res && res >= Integer.MIN_VALUE) {
					return new MewVariable(MewVariableType.Integer, (int)res);
				}
			}
			return new MewVariable(MewVariableType.Float, res);
		}
		return new MewVariable(MewVariableType.Error, new MewException("Cannot perform exponential operation on non-numeric types"));
	}
	
	public MewExponent() { }
}

class MewFactorial extends MewMathOperationArity1 {
	@Override public String getName() { return "!"; }
	
	public MewFactorial() { }

	@Override
	public MewVariable evaluateInner(MewEnvironment environment, MewVariable l) {
		int il = l.toInt();
		double result = 1;
		
		if (il < 0) {
			return new MewVariable(MewVariableType.Error, new MewException("Cannot perform factorial on negative numbers"));
		}
		if (il > environment.getMaxStackDepth()) {
			return new MewVariable(MewVariableType.Error, new MewException("Cannot perform factorial: will exceed maximum stack depth"));
		}
		
		for (int i = 1; i <= il; i++) {
			result *= i;
		}
		
		if (result % 1 == 0) {
			// Whole number
			if (Integer.MAX_VALUE >= result && result >= Integer.MIN_VALUE) {
				return new MewVariable(MewVariableType.Integer, (int)result);
			}
		}
		return new MewVariable(MewVariableType.Float, result);
	}
}

class MewAbs extends MewMathOperationArity1 {
	@Override public String getName() { return "Abs"; }
	
	public MewAbs() { }

	@Override
	public MewVariable evaluateInner(MewEnvironment environment, MewVariable l) {
		if (l.type == MewVariableType.Integer) {
			return new MewVariable(MewVariableType.Integer, Math.abs(l.toInt()));
		} else if (l.type == MewVariableType.Float) {
			return new MewVariable(MewVariableType.Float, Math.abs(l.toDouble()));		
		} else if (l.type == MewVariableType.Bool) {
			return l;
		} else {
			return new MewVariable(MewVariableType.Integer, Math.abs(l.toInt()));
		}
	}
}

class MewMax extends MewNode {
	@Override public String getName() { return "Max"; }

	@Override
	public MewVariable evaluate(MewEnvironment environment) {
		int iResult = Integer.MIN_VALUE;
		double fResult = Double.NEGATIVE_INFINITY;
		int returnType = 0;
		MewNodeCursor cursor = toCursor();
		while (cursor.hasNext()) {
			MewNode next = cursor.next();
			MewVariable result = next.evaluate(environment);
			if (result.isNumeric()) {
				if (result.type == MewVariableType.Float && returnType < 2) {
					returnType = 2;
				}
				if (result.type == MewVariableType.Integer && returnType < 1) {
					returnType = 1;
				}
				
				fResult = Math.max(fResult, result.toDouble());
				if (returnType < 2) {
					iResult = Math.max(iResult, result.toInt());
				}
			} else {
				return new MewVariable(MewVariableType.Error, new MewException("Cannot perform Max on non-numeric argument " + (int)(cursor.getPosition() - 1)));
			}
		}
		
		if (returnType == 2) {
			return new MewVariable(MewVariableType.Float, fResult);
		} else {
			return new MewVariable(MewVariableType.Integer, iResult);
		}
	}
	
	@Override
	public int getMinArgumentCount() { return 1; }

	@Override
	public int getMaxArgumentCount() { return Integer.MAX_VALUE; }
	
	public MewMax() { }
}

class MewMin extends MewNode {
	@Override public String getName() { return "Min"; }

	@Override
	public MewVariable evaluate(MewEnvironment environment) {
		int iResult = Integer.MAX_VALUE;
		double fResult = Double.POSITIVE_INFINITY;
		int returnType = 0;
		MewNodeCursor cursor = toCursor();
		while (cursor.hasNext()) {
			MewNode next = cursor.next();
			MewVariable result = next.evaluate(environment);
			if (result.isNumeric()) {
				if (result.type == MewVariableType.Float && returnType < 2) {
					returnType = 2;
				}
				if (result.type == MewVariableType.Integer && returnType < 1) {
					returnType = 1;
				}
				
				fResult = Math.min(fResult, result.toDouble());
				if (returnType < 2) {
					iResult = Math.min(iResult, result.toInt());
				}
			} else {
				return new MewVariable(MewVariableType.Error, new MewException("Cannot perform Min on non-numeric argument " + (int)(cursor.getPosition() - 1)));
			}
			
		}
		
		if (returnType == 2) {
			return new MewVariable(MewVariableType.Float, fResult);
		} else {
			return new MewVariable(MewVariableType.Integer, iResult);
		}
	}
	
	@Override
	public int getMinArgumentCount() { return 1; }

	@Override
	public int getMaxArgumentCount() { return Integer.MAX_VALUE; }
	
	public MewMin() { }
}

class MewClamp extends MewNode3Arg {
	@Override public String getName() { return "Clamp"; }

	@Override
	public MewVariable evaluate(MewEnvironment environment) {
		MewVariable lo = getLeft().evaluate(environment);
		MewVariable value = getRight().evaluate(environment);
		MewVariable hi = nodes.get(2).evaluate(environment);
		
		if (!lo.isNumeric()) {
			return new MewVariable(MewVariableType.Error, new MewException("Cannot Clamp: argument 1 (the low value) is a non-numeric value"));
		}
		if (!value.isNumeric()) {
			return new MewVariable(MewVariableType.Error, new MewException("Cannot Clamp: argument 2 (the value to clamp) is a non-numeric value"));
		}
		if (!hi.isNumeric()) {
			return new MewVariable(MewVariableType.Error, new MewException("Cannot Clamp: argument 3 (the hi value) is a non-numeric value"));
		}
		
		if (lo.type == MewVariableType.Float || value.type == MewVariableType.Float || hi.type == MewVariableType.Float) {
			Double int2 = lo.toDouble();
			Double int3 = value.toDouble();
			Double int4 = hi.toDouble();
			Double result = ((int3 < int2) ? int2 : (int3 > int4 ? int4 : int3));
			
			return new MewVariable(MewVariableType.Float, result);
		} else {
			Integer int2 = lo.toInt();
			Integer int3 = value.toInt();
			Integer int4 = hi.toInt();
			Integer result = ((int3 < int2) ? int2 : (int3 > int4 ? int4 : int3));
			
			return new MewVariable(MewVariableType.Integer, result);
		}
	}
	
	public MewClamp() { }
}

class MewFloor extends MewMathOperationArity1 {
	@Override public String getName() { return "Floor"; }
	
	public MewFloor() { }

	@Override
	public MewVariable evaluateInner(MewEnvironment environment, MewVariable value) {
		return new MewVariable(MewVariableType.Float, Math.floor(value.toDouble()));
	}
}

class MewLogarithm extends MewMathOperationArity1 {
	@Override public String getName() { return "Log"; }
	
	public MewLogarithm() { }

	@Override
	public MewVariable evaluateInner(MewEnvironment environment, MewVariable value) {
		return new MewVariable(MewVariableType.Float, Math.log(value.toDouble()));
	}
}

class MewLogarithm10 extends MewMathOperationArity1 {
	@Override public String getName() { return "Log-10"; }
	
	public MewLogarithm10() { }

	@Override
	public MewVariable evaluateInner(MewEnvironment environment, MewVariable value) {
		return new MewVariable(MewVariableType.Float, Math.log10(value.toDouble()));
	}
}

class MewSqrt extends MewMathOperationArity1 {
	@Override public String getName() { return "Sqrt"; }
	
	public MewSqrt() { }

	@Override
	public MewVariable evaluateInner(MewEnvironment environment, MewVariable value) {
		return new MewVariable(MewVariableType.Float, Math.sqrt(value.toDouble()));
	}
}

class MewMathSin extends MewMathOperationArity1 {
	@Override public String getName() { return "Math-Sin"; }
	
	public MewMathSin() { }

	@Override
	public MewVariable evaluateInner(MewEnvironment environment, MewVariable value) {
		return new MewVariable(MewVariableType.Float, Math.sin(value.toDouble()));
	}
}

class MewMathSinh extends MewMathOperationArity1 {
	@Override public String getName() { return "Math-Sinh"; }
	
	public MewMathSinh() { }

	@Override
	public MewVariable evaluateInner(MewEnvironment environment, MewVariable value) {
		return new MewVariable(MewVariableType.Float, Math.sinh(value.toDouble()));
	}
}

class MewMathCos extends MewMathOperationArity1 {
	@Override public String getName() { return "Math-Cos"; }
	
	public MewMathCos() { }

	@Override
	public MewVariable evaluateInner(MewEnvironment environment, MewVariable value) {
		return new MewVariable(MewVariableType.Float, Math.cos(value.toDouble()));
	}
}

class MewMathCosh extends MewMathOperationArity1 {
	@Override public String getName() { return "Math-Cosh"; }
	
	public MewMathCosh() { }

	@Override
	public MewVariable evaluateInner(MewEnvironment environment, MewVariable value) {
		return new MewVariable(MewVariableType.Float, Math.cosh(value.toDouble()));
	}
}

class MewMathTan extends MewMathOperationArity1 {
	@Override public String getName() { return "Math-Tan"; }
	
	public MewMathTan() { }

	@Override
	public MewVariable evaluateInner(MewEnvironment environment, MewVariable value) {
		return new MewVariable(MewVariableType.Float, Math.tan(value.toDouble()));
	}
}

class MewMathTanh extends MewMathOperationArity1 {
	@Override public String getName() { return "Math-Tanh"; }
	
	public MewMathTanh() { }

	@Override
	public MewVariable evaluateInner(MewEnvironment environment, MewVariable value) {
		return new MewVariable(MewVariableType.Float, Math.tanh(value.toDouble()));
	}
}

class MewMathRound extends MewMathOperationArity1 {
	@Override public String getName() { return "Math-Round"; }
	
	public MewMathRound() { }

	@Override
	public MewVariable evaluateInner(MewEnvironment environment, MewVariable value) {
		long l = Math.round(value.toDouble());
		if (l > Integer.MAX_VALUE || l < Integer.MIN_VALUE) {
			return new MewVariable(MewVariableType.Float, (double)l);
		} else {
			return new MewVariable(MewVariableType.Integer, (int)l);
		}
	}
}

class MewMathSign extends MewMathOperationArity1 {
	@Override public String getName() { return "Math-Sign"; }
	
	public MewMathSign() { }

	@Override
	public MewVariable evaluateInner(MewEnvironment environment, MewVariable value) {
		return new MewVariable(MewVariableType.Integer, (int)Math.signum(value.toDouble()));
	}
}

class MewTruncate extends MewMathOperationArity1 {
	@Override public String getName() { return "Truncate"; }
	
	public MewTruncate() { }

	@Override
	public MewVariable evaluateInner(MewEnvironment environment, MewVariable value) {
		double d = value.toDouble();
		int res = (int)Math.floor(d);
		if ((d % 1 == 0) || d > 0) {
			return new MewVariable(MewVariableType.Integer, res);
		} else {
			return new MewVariable(MewVariableType.Integer, 1 + res);
		}
	}
}

class MewRandom extends MewNode2Arg {
	@Override public String getName() { return "Random-Int"; }

	@Override
	public MewVariable evaluate(MewEnvironment environment) {
		MewNode left = getLeft();
		MewNode right = getRight();
		MewVariable l = left.evaluate(environment);
		MewVariable r = right.evaluate(environment);
		
		if (l.isNumeric() && r.isNumeric()) {
			int il = l.toInt();
			int ir = r.toInt();
			
			if (il == ir) {
				return new MewVariable(MewVariableType.Integer, il);
			}
			if (il > ir) { 
				int t = il; il = ir; ir = t; 
			}
			
			// upper bound was already inclusive by accident; make it explicit and crash-proof
			int range = (ir - il);
			if (range <= 0) { 
				range = Integer.MAX_VALUE; 
			}
			if (range < Integer.MAX_VALUE) {
				range++;
			}
			
			return new MewVariable(MewVariableType.Integer, (int)Math.floor(environment.getRandom().nextDouble() * range) + il);
		}
		return new MewVariable(MewVariableType.Error, new MewException("Cannot generate random number from non-numeric bounds"));
	}
	
	public MewRandom() { }
}

class MewRandomFloat extends MewNode {
	@Override public String getName() { return "Random-Float"; }

	@Override
	public MewVariable evaluate(MewEnvironment environment) {
		return new MewVariable(MewVariableType.Float, environment.getRandom().nextDouble());
	}
	
	public MewRandomFloat() { }
}

abstract class MewMathOperationArity1 extends MewNode1Arg {
	@Override
	public MewVariable evaluate(MewEnvironment environment) {
		MewVariable value = getLeft().evaluate(environment);
		
		if (value.isNumeric()) {
			return evaluateInner(environment, value);
		} else {
			return new MewVariable(MewVariableType.Error, new MewException(getName() + ": Cannot operate on a non-numeric value"));
		}
	}	
	
	public abstract MewVariable evaluateInner(MewEnvironment environment, MewVariable value);
}

abstract class MewStringOperationArity1 extends MewNode1Arg {
	@Override
	public MewVariable evaluate(MewEnvironment environment) {
		MewNode left = getLeft();
		MewVariable str = left.evaluate(environment);
		
		if (str.canStringify()) {
			return evaluateInner(environment, str.stringifyValue());
		} else {
			return new MewVariable(MewVariableType.Error, new MewException("Cannot " + getName() + ": arg1 string cannot be stringified"));
		}
	}	
	
	public abstract MewVariable evaluateInner(MewEnvironment environment, String str);
}

abstract class MewStringOperationArity2 extends MewNode2Arg {
	private boolean checkNumeric;
	
	public MewStringOperationArity2() { checkNumeric = false; }
	public MewStringOperationArity2(boolean checkNumeric) { this.checkNumeric = checkNumeric; }
	
	@Override
	public MewVariable evaluate(MewEnvironment environment) {
		MewNode left = getLeft();
		MewNode right = getRight();
		MewVariable l = left.evaluate(environment);
		MewVariable r = right.evaluate(environment);
		
		if (l.canStringify()) {
			String sl = l.stringifyValue();
			if (checkNumeric) {
				if (r.isNumeric()) {
					return evaluateInner(environment, sl, r);
				} else {
					return new MewVariable(MewVariableType.Error, new MewException("Cannot " + getName() + ": Argument 2 not numeric"));
				}
			} else {
				return evaluateInner(environment, sl, r);
			}

		} else {
			return new MewVariable(MewVariableType.Error, new MewException("Cannot " + getName() + ": arg1 cannot be stringified"));
		}
	}	
	
	public abstract MewVariable evaluateInner(MewEnvironment environment, String sl, MewVariable r);
}

abstract class MewStringOperationArity3 extends MewNode3Arg {
	
	public MewStringOperationArity3() { }
	
	@Override
	public MewVariable evaluate(MewEnvironment environment) {
		MewNode left = getLeft();
		MewNode right = getRight();
		MewNode third = nodes.get(2);
		MewVariable l = left.evaluate(environment);
		MewVariable r = right.evaluate(environment);
		MewVariable t = third.evaluate(environment);
		
		if (l.canStringify()) {
			String sl = l.stringifyValue();
			return evaluateInner(environment, sl, r, t);
		} else {
			return new MewVariable(MewVariableType.Error, new MewException("Cannot " + getName() + ": arg1 cannot be stringified"));
		}
	}	
	
	public abstract MewVariable evaluateInner(MewEnvironment environment, String sl, MewVariable r1, MewVariable r2);
}

class MewCharacterAt extends MewStringOperationArity2 {
	@Override public String getName() { return "Character-At"; }
	
	public MewCharacterAt() { super(true); }

	@Override
	public MewVariable evaluateInner(MewEnvironment environment, String sl, MewVariable r) {
		if (sl.length() < 1) {
			return new MewVariable(MewVariableType.Error, new MewException("Cannot find character at string: String has length 0"));
		}
		
		// Boxing char into string because Mewtype doesn't have a char type yet
		int ir = r.toInt();
		if (ir < 0) {
			return new MewVariable(MewVariableType.String, sl.charAt(0));
		} else if (ir >= sl.length()) {
			return new MewVariable(MewVariableType.String, sl.charAt(sl.length() - 1));
		} else {
			return new MewVariable(MewVariableType.String, sl.charAt(ir));
		}
	}
}

class MewFindString extends MewStringOperationArity2 {
	@Override public String getName() { return "Find-String"; }
	
	public MewFindString() { }

	@Override
	public MewVariable evaluateInner(MewEnvironment environment, String sl, MewVariable r) {
		if (r.canStringify()) {
			return new MewVariable(MewVariableType.Integer, sl.indexOf(r.stringifyValue()));
		} else {
			return new MewVariable(MewVariableType.Error, new MewException("Cannot find in string: substring cannot be stringified"));
		}
	}
}

class MewSplitString extends MewNode {
	@Override public String getName() { return "String-Split"; }

	@Override
	public MewVariable evaluate(MewEnvironment environment) {
		MewNode left = getLeft();
		MewNode right = getRight();
		MewVariable l = left.evaluate(environment);
		MewVariable r = right.evaluate(environment);
		
		if (l.canStringify()) {
			String sl = l.stringifyValue();
			if (r.canStringify()) {
				String sr = r.stringifyValue();
				boolean regex = false;
				
				if (nodes.size() > 2) {
					MewVariable third = nodes.get(2).evaluate(environment);
					if (third.isNumeric()) {
						regex = third.truthy();
					} else {
						return new MewVariable(MewVariableType.Error, new MewException("Cannot split string: arg3 is-regex switch is not a number/boolean"));
					}
				}
				
				String[] ss;
				if (regex) {
					ss = sl.split(sr);
				} else {
					ss = sl.split(Pattern.quote(sr));
				}
				
				MewList list = new MewInternalList(false);
				for (String s : ss) {
					list.add(new MewVariable(MewVariableType.String, s));
				}
				return new MewVariable(MewVariableType.List, list);
			} else {
				return new MewVariable(MewVariableType.Error, new MewException("Cannot split string: separator cannot be stringified"));
			}
		} else {
			return new MewVariable(MewVariableType.Error, new MewException("Cannot split string: arg1 string cannot be stringified"));
		}
	}
	
	@Override
	public int getMinArgumentCount() { return 2; }

	@Override
	public int getMaxArgumentCount() { return 3; }
	
	public MewSplitString() { }
}

class MewStringLeft extends MewStringOperationArity2 {
	@Override public String getName() { return "String-Left"; }
	
	public MewStringLeft() { super(true); }

	@Override
	public MewVariable evaluateInner(MewEnvironment environment, String sl, MewVariable r) {
		int ir = r.toInt();
		if (ir >= sl.length()) {
			return new MewVariable(MewVariableType.String, sl);
		} else if (ir <= 0) {
			return new MewVariable(MewVariableType.String, "");
		} else {
			return new MewVariable(MewVariableType.String, sl.substring(0, ir));
		}
	}
}

class MewStringRight extends MewStringOperationArity2 {
	@Override public String getName() { return "String-Right"; }
	
	public MewStringRight() { super(true); }

	@Override
	public MewVariable evaluateInner(MewEnvironment environment, String sl, MewVariable r) {
		int ir = r.toInt();
		if (ir <= 0) {
			return new MewVariable(MewVariableType.String, "");
		} else if (ir >= sl.length()) {
			return new MewVariable(MewVariableType.String, sl);
		} else {
			return new MewVariable(MewVariableType.String, sl.substring(sl.length() - ir));
		}
	}
}

class MewStringStartsWith extends MewStringOperationArity2 {
	@Override public String getName() { return "String-Starts-With"; }
	
	public MewStringStartsWith() { super(false); }

	@Override
	public MewVariable evaluateInner(MewEnvironment environment, String sl, MewVariable r) {
		return new MewVariable(MewVariableType.Bool, sl.startsWith(r.stringifyValue()));
	}
}

class MewStringEndsWith extends MewStringOperationArity2 {
	@Override public String getName() { return "String-Ends-With"; }
	
	public MewStringEndsWith() { super(false); }

	@Override
	public MewVariable evaluateInner(MewEnvironment environment, String sl, MewVariable r) {
		return new MewVariable(MewVariableType.Bool, sl.endsWith(r.stringifyValue()));
	}
}

class MewStringContains extends MewStringOperationArity2 {
	@Override public String getName() { return "String-Contains"; }
	
	public MewStringContains() { super(false); }

	@Override
	public MewVariable evaluateInner(MewEnvironment environment, String sl, MewVariable r) {
		return new MewVariable(MewVariableType.Bool, sl.contains(r.stringifyValue()));
	}
}

class MewStringRepeat extends MewStringOperationArity2 {
	@Override public String getName() { return "String-Repeat"; }
	
	public MewStringRepeat() { super(true); }

	@Override
	public MewVariable evaluateInner(MewEnvironment environment, String sl, MewVariable r) {
		int count = r.toInt();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < count; i++) {
			sb.append(sl);
		}
		return new MewVariable(MewVariableType.String, sb.toString());
	}
}

class MewStringPadLeft extends MewStringOperationArity3 {
	@Override public String getName() { return "String-Pad-Left"; }
	
	public MewStringPadLeft() { }

	@Override
	public MewVariable evaluateInner(MewEnvironment environment, String sl, MewVariable r1, MewVariable r2) {
		if (r1.isNumeric()) {
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < r1.toInt(); i++) {
				sb.append(r2.stringifyValue());
			}
			sb.append(sl);
			return new MewVariable(MewVariableType.String, sb.toString());
		} else {
			return new MewVariable(MewVariableType.Error, new MewException("Cannot String-Pad-Left: argument 2 (count) is not numeric"));
		}
	}
}

class MewStringPadRight extends MewStringOperationArity3 {
	@Override public String getName() { return "String-Pad-Right"; }
	
	public MewStringPadRight() { }

	@Override
	public MewVariable evaluateInner(MewEnvironment environment, String sl, MewVariable r1, MewVariable r2) {
		if (r1.isNumeric()) {
			StringBuilder sb = new StringBuilder();
			sb.append(sl);
			for (int i = 0; i < r1.toInt(); i++) {
				sb.append(r2.stringifyValue());
			}
			return new MewVariable(MewVariableType.String, sb.toString());
		} else {
			return new MewVariable(MewVariableType.Error, new MewException("Cannot String-Pad-Right: argument 2 (count) is not numeric"));
		}
	}
}

class MewStringLength extends MewStringOperationArity1 {
	@Override public String getName() { return "String-Length"; }
	
	public MewStringLength() { }

	@Override
	public MewVariable evaluateInner(MewEnvironment environment, String str) {
		return new MewVariable(MewVariableType.Integer, str.length());
	}
}

class MewStringUppercase extends MewStringOperationArity1 {
	@Override public String getName() { return "String-Uppercase"; }
	
	public MewStringUppercase() { }

	@Override
	public MewVariable evaluateInner(MewEnvironment environment, String str) {
		return new MewVariable(MewVariableType.String, str.toUpperCase());
	}
}

class MewStringLowercase extends MewStringOperationArity1 {
	@Override public String getName() { return "String-Lowercase"; }
	
	public MewStringLowercase() { }

	@Override
	public MewVariable evaluateInner(MewEnvironment environment, String str) {
		return new MewVariable(MewVariableType.String, str.toLowerCase());
	}
}

class MewStringTrim extends MewStringOperationArity1 {
	@Override public String getName() { return "String-Trim"; }
	
	public MewStringTrim() { }

	@Override
	public MewVariable evaluateInner(MewEnvironment environment, String str) {
		return new MewVariable(MewVariableType.String, str.trim());
	}
}

class MewStringReverse extends MewStringOperationArity1 {
	@Override public String getName() { return "String-Reverse"; }
	
	public MewStringReverse() { }

	@Override
	public MewVariable evaluateInner(MewEnvironment environment, String str) {
		StringBuilder sb = new StringBuilder(str);
		String s = sb.reverse().toString();
		return new MewVariable(MewVariableType.String, s);
	}
}

class MewSubstring extends MewNode3Arg {
	@Override public String getName() { return "Substring"; }

	@Override
	public MewVariable evaluate(MewEnvironment environment) {
		MewNode left = getLeft();
		MewVariable str = left.evaluate(environment);
		
		if (str.canStringify()) {
			MewNode right = getRight();
			MewVariable start = right.evaluate(environment);
			MewNode third = nodes.get(2);
			MewVariable end = third.evaluate(environment);
			
			if (start.isNumeric()) {
				if (end.isNumeric()) {
					String s = str.stringifyValue();
					int iStart = start.toInt();
					if (iStart < 0) { iStart = 0; }
					int iEnd = end.toInt();
					if (iEnd >= s.length()) { iEnd = s.length(); }
					if (iEnd < iStart) { int temp = iEnd; iEnd = iStart; iStart = temp; } // Swap start and end if they are inverted
					return new MewVariable(MewVariableType.String, s.substring(iStart, iEnd));
				} else {
					return new MewVariable(MewVariableType.Error, new MewException("Cannot Substring: arg3 end index is not a number"));
				}
			} else {
				return new MewVariable(MewVariableType.Error, new MewException("Cannot Substring: arg2 start index is not a number"));
			}
		} else {
			return new MewVariable(MewVariableType.Error, new MewException("Cannot Substring: arg1 string cannot be stringified"));
		}
	}
	
	public MewSubstring() { }
}

class MewStringReplace extends MewNode3Arg {
	@Override public String getName() { return "String-Replace"; }

	@Override
	public MewVariable evaluate(MewEnvironment environment) {
		MewNode left = getLeft();
		MewVariable str = left.evaluate(environment);
		
		if (str.canStringify()) {
			MewNode right = getRight();
			MewVariable searchFor = right.evaluate(environment);
			MewNode third = nodes.get(2);
			MewVariable replaceTo = third.evaluate(environment);
			
			if (searchFor.canStringify()) {
				if (replaceTo.canStringify()) {
					return new MewVariable(MewVariableType.String, str.stringifyValue().replace(searchFor.stringifyValue(), replaceTo.stringifyValue()));
				} else {
					return new MewVariable(MewVariableType.Error, new MewException("Cannot String-Replace: arg3 replacement value cannot be stringified"));
				}
			} else {
				return new MewVariable(MewVariableType.Error, new MewException("Cannot String-Replace: arg2 search target cannot be stringified"));
			}
		} else {
			return new MewVariable(MewVariableType.Error, new MewException("Cannot String-Replace: arg1 string cannot be stringified"));
		}
	}
	
	public MewStringReplace() { }
}

class MewExecuteString extends MewNode {
	@Override public String getName() { return "Execute-String"; }

	@Override
	public MewVariable evaluate(MewEnvironment environment) {
		if (environment.recursiveExecuteStringCap > environment.getExecuteStringCap()) {
			return new MewVariable(MewVariableType.Error, new MewException("Cannot Execute-String: Recursive Execute-String cap has been achieved"));
		}
		
		ArrayList<String> s = new ArrayList<String>();
		environment.recursiveExecuteStringCap++;
		MewtypeInterpreter interpreter = new MewtypeInterpreter(environment);
		MewVariable last = new MewNoopNode().evaluate(environment);
		int argNum = 0;
		for (MewNode node : nodes) {
			MewVariable str = node.evaluate(environment);
			if (str.canStringify()) {
				s.add(str.stringifyValue());
			} else {
				last = new MewVariable(MewVariableType.Error, new MewException("Cannot Execute-String: argument " + argNum + " cannot be converted to a string"));
				environment.recursiveExecuteStringCap--;
				return last;
			}
			argNum++;
		}
		
		for (String ss : s) {
			try {
				MewtypeProgram program = interpreter.interpret(ss, environment);
				last = program.run(environment);
			} catch (Exception e) {
				last = new MewVariable(MewVariableType.Error, new MewException("Execute-String runtime exception: " + e.getMessage(), e));
				environment.recursiveExecuteStringCap--;
				environment.lastExpression = last;
			}
		}
		
		environment.recursiveExecuteStringCap--;
		return last;
	}	
	
	@Override
	public int getMinArgumentCount() { return 1; }

	@Override
	public int getMaxArgumentCount() { return Integer.MAX_VALUE; }
	
	public MewExecuteString() { }
}

class MewIsStar extends MewNode1Arg {
	private MewVariableType type;
	private String typeName;
	
	@Override public String getName() { return "Is-" + typeName; }

	@Override
	public MewVariable evaluate(MewEnvironment environment) {
		MewNode left = getLeft();
		MewVariable str = left.evaluate(environment);
		
		return new MewVariable(MewVariableType.Bool, (str.type == type));
	}
	
	public MewIsStar(MewVariableType type, String typeName) { 
		this.type = type;
		this.typeName = typeName;
	}
}

class MewIsError extends MewIsStar {
	public MewIsError() { super(MewVariableType.Error, "Error"); }
}

class MewCreateError extends MewNode {
	@Override public String getName() { return "Create-Error"; }

	@Override
	public MewVariable evaluate(MewEnvironment environment) {
		String msg = "No error message is supplied when this error is created";
		Throwable inner = null;
		
		if (nodes.size() > 0) {
			MewNode left = getLeft();
			MewVariable str = left.evaluate(environment);
			
			if (str.type == MewVariableType.Error && str.value instanceof Throwable) {
				inner = (Throwable)str.value;
			} else if (str.canStringify()) {
				msg = str.stringifyValue();
			} else {
				return new MewVariable(MewVariableType.Error, new MewException("Cannot Create-Error: Argument 1 could not be stringified as an error message or exposed as an error value to parse as an inner error"));
			}
		}
		if (nodes.size() > 1) {
			MewNode left = nodes.get(1);
			MewVariable str = left.evaluate(environment);
			
			if (str.type == MewVariableType.Error && str.value instanceof Throwable) {
				inner = (Throwable)str.value;
			} else {
				return new MewVariable(MewVariableType.Error, new MewException("Cannot Create-Error: Argument 2 could not be exposed as an error value to parse as an inner error"));
			}
		}
		
		if (inner == null) {
			return new MewVariable(MewVariableType.Error, new MewException(msg));
		} else {
			return new MewVariable(MewVariableType.Error, new MewException(msg, inner));
		}
	}	
	
	@Override
	public int getMinArgumentCount() { return 0; }

	@Override
	public int getMaxArgumentCount() { return 2; }
	
	public MewCreateError() { }
}

abstract class MewErrorOperationArity1 extends MewNode1Arg {
	@Override
	public MewVariable evaluate(MewEnvironment environment) {
		MewNode left = getLeft();
		MewVariable str = left.evaluate(environment);
		
		if (str.type == MewVariableType.Error) {
			if (str.value instanceof Throwable) {
				Throwable ex = (Throwable)str.value;
				return evaluateInner(environment, ex);
			} else {
				return new MewVariable(MewVariableType.Error, new MewException("Cannot " + getName() + ": error not exposed properly"));
			}
		}
		return new MewVariable(MewVariableType.Error, new MewException("Cannot " + getName() + ": argument is not an error"));
	}	
	
	public abstract MewVariable evaluateInner(MewEnvironment environment, Throwable left);
}

class MewGetErrorMessage extends MewErrorOperationArity1 {
	@Override public String getName() { return "Get-Error-Message"; }

	@Override
	public MewVariable evaluateInner(MewEnvironment environment, Throwable ex) {
		return new MewVariable(MewVariableType.String, ex.toString());
	}
	
	public MewGetErrorMessage() { }
}

class MewHasInnerError extends MewErrorOperationArity1 {
	@Override public String getName() { return "Has-Inner-Error"; }

	@Override
	public MewVariable evaluateInner(MewEnvironment environment, Throwable ex) {
		return new MewVariable(MewVariableType.Bool, (ex.getCause() != null));
	}
	
	public MewHasInnerError() { }
}

class MewGetInnerError extends MewErrorOperationArity1 {
	@Override public String getName() { return "Get-Inner-Error"; }

	@Override
	public MewVariable evaluateInner(MewEnvironment environment, Throwable ex) {
		if (ex.getCause() != null ) {
			return new MewVariable(MewVariableType.Error, ex.getCause());
		} else {
			return new MewVariable(MewVariableType.Error, new MewException("Cannot Get-Inner-Error: error has no inner error"));
		}
	}
	
	public MewGetInnerError() { }
}

class MewDebugInspectNull extends MewNode1Arg {
	@Override public String getName() { return "Debug-Inspect-Null"; }

	@Override
	public MewVariable evaluate(MewEnvironment environment) {
		MewNode left = getLeft();
		MewVariable str = left.evaluate(environment);
		
		if (str.type == MewVariableType.Null) {
			return new MewVariable(MewVariableType.String, str.value.toString());
		}
		return new MewVariable(MewVariableType.Error, new MewException("Cannot Debug-Inspect-Null: argument is not null"));
	}
	
	public MewDebugInspectNull() { }
}

class MewExecute extends MewNode {
	@Override public String getName() { return "Execute"; }

	@Override
	public MewVariable evaluate(MewEnvironment environment) {
		if (environment.recursiveExecuteStringCap > environment.getExecuteStringCap()) {
			return new MewVariable(MewVariableType.Error, new MewException("Cannot Execute: Recursive Execute-String cap has been achieved"));
		}
		MewVariable left = getLeft().evaluate(environment);
		
		if (left.type == MewVariableType.Code) {
			if (left.value instanceof MewCodeBlock) {
				MewCodeBlock codeBlock = (MewCodeBlock)(left.value);
				MewCodeBlockNode codeBlockNode = new MewCodeBlockNode(codeBlock);
				if (nodes.size() == 1) {
					codeBlockNode.nodes = new ArrayList<MewNode>();
				} else {
					codeBlockNode.nodes = new ArrayList<MewNode>(nodes.subList(1, nodes.size()));
				}
				
				environment.recursiveExecuteStringCap++;
				try {
					MewVariable result = codeBlockNode.evaluate(environment);
					environment.recursiveExecuteStringCap--;
					return result;
				} catch (Exception e) {
					environment.recursiveExecuteStringCap--;
					throw e;
				}
			} else {
				return new MewVariable(MewVariableType.Error, new MewException("Cannot evaluate code block: code block not exposed in the correct format"));
			}
		}
		return new MewVariable(MewVariableType.Error, new MewException("Cannot execute argument 0 that is not a code block"));
	}
	
	@Override
	public int getMinArgumentCount() { return 1; }

	@Override
	public int getMaxArgumentCount() { return Integer.MAX_VALUE; }
	
	public MewExecute() { }
	
}

class MewExecuteSaved extends MewNode {
	@Override public String getName() { return "Execute-Saved"; }

	@Override
	public MewVariable evaluate(MewEnvironment environment) {
		if (environment.recursiveExecuteStringCap > environment.getExecuteStringCap()) {
			return new MewVariable(MewVariableType.Error, new MewException("Cannot Execute-Saved: Recursive Execute-String cap has been achieved"));
		}
		MewVariable left = getLeft().evaluate(environment);
		
		if (left.type == MewVariableType.Code) {
			if (left.value instanceof MewCodeBlock) {
				MewCodeBlock codeBlock = (MewCodeBlock)(left.value);
				MewCodeBlockNode codeBlockNode = new MewCodeBlockNode(codeBlock);
				if (nodes.size() == 1) {
					codeBlockNode.nodes = new ArrayList<MewNode>();
				} else {
					codeBlockNode.nodes = new ArrayList<MewNode>(nodes.subList(1, nodes.size()));
				}
				
				environment.recursiveExecuteStringCap++;
				boolean topMostState = environment.variables.writeOnlyToTopmost;
				environment.variables.writeOnlyToTopmost = true;
				try {
					MewVariable result = codeBlockNode.evaluate(environment);
					
					environment.recursiveExecuteStringCap--;
					environment.variables.writeOnlyToTopmost = topMostState;
				return result;
				} catch (Exception e) {
					environment.recursiveExecuteStringCap--;
					environment.variables.writeOnlyToTopmost = topMostState;
					throw e;
				}
			} else {
				return new MewVariable(MewVariableType.Error, new MewException("Cannot evaluate (saved) code block: code block not exposed in the correct format"));
			}
		}
		return new MewVariable(MewVariableType.Error, new MewException("Cannot execute (saved) argument 0 that is not a code block"));
	}
	
	@Override
	public int getMinArgumentCount() { return 1; }

	@Override
	public int getMaxArgumentCount() { return Integer.MAX_VALUE; }
	
	public MewExecuteSaved() { }
	
}

class MewInterpret extends MewNode {
	@Override public String getName() { return "Interpret"; }

	@Override
	public MewVariable evaluate(MewEnvironment environment) {
		MewNode left = getLeft();
		MewVariable str = left.evaluate(environment);
		
		if (str.canStringify()) {
			String s = str.stringifyValue();
			if (s.startsWith("{")) {
				// Strip starting {
				s = s.substring(1);
			}
			
			// Any ideas on how to call interpret() without making a new MewtypeClassList object
			MewtypeInterpreter interpreter = new MewtypeInterpreter(environment);
			return new MewVariable(MewVariableType.Code, interpreter.consumeCodeBlock(new MewtypeProgramSource(s), environment));
		}
		
		return new MewVariable(MewVariableType.Error, new MewException("Cannot interpret: code block not supplied"));
	}	
	
	@Override
	public int getMinArgumentCount() { return 1; }

	@Override
	public int getMaxArgumentCount() { return 1; }
	
	public MewInterpret() { }
}

abstract class MewCodeOperationArity1 extends MewNode1Arg {
	@Override
	public MewVariable evaluate(MewEnvironment environment) {
		MewNode left = getLeft();
		MewVariable str = left.evaluate(environment);
		
		if (str.type == MewVariableType.Code) {
			if (str.value instanceof MewCodeBlock) {
				MewCodeBlock codeBlock = (MewCodeBlock)str.value;
				return evaluateInner(environment, codeBlock);
			}
			return new MewVariable(MewVariableType.Error, new MewException("Cannot " + getName() + ": code not exposed properly"));
		}
		return new MewVariable(MewVariableType.Error, new MewException("Cannot " + getName() + ": argument is not of type code"));
	}	
	
	public abstract MewVariable evaluateInner(MewEnvironment environment, MewCodeBlock left);
}

class MewIsCode extends MewIsStar {
	public MewIsCode() { super(MewVariableType.Code, "Code"); }
}

class MewGetMinArgumentCount extends MewCodeOperationArity1 {
	@Override public String getName() { return "Get-Min-Argument-Count"; }

	@Override
	public MewVariable evaluateInner(MewEnvironment environment, MewCodeBlock codeBlock) {
		return new MewVariable(MewVariableType.Integer, codeBlock.getMinArgumentCount());
	}
	
	public MewGetMinArgumentCount() { }
}

class MewGetMaxArgumentCount extends MewCodeOperationArity1 {
	@Override public String getName() { return "Get-Max-Argument-Count"; }

	@Override
	public MewVariable evaluateInner(MewEnvironment environment, MewCodeBlock codeBlock) {
		return new MewVariable(MewVariableType.Integer, codeBlock.getMaxArgumentCount());
	}
	
	public MewGetMaxArgumentCount() { }
}

class MewGetReturnType extends MewCodeOperationArity1 {
	@Override public String getName() { return "Get-Return-Type"; }

	@Override
	public MewVariable evaluateInner(MewEnvironment environment, MewCodeBlock codeBlock) {
		return new MewVariable(MewVariableType.String, codeBlock.returnType.name());
	}
	
	public MewGetReturnType() { }
}

abstract class MewListOperationArity1 extends MewNode1Arg {
	private boolean checkMutable;
	
	public MewListOperationArity1(boolean checkMutable) {
		this.checkMutable = checkMutable;
	}

	@Override
	public MewVariable evaluate(MewEnvironment environment) {
		MewNode left = getLeft();
		MewVariable l = left.evaluate(environment);
		
		if (l.type == MewVariableType.List) {
			if (l.value instanceof MewList) {
				MewList a = (MewList)l.value;
				
				if (checkMutable && !a.isMutable()) {
					return a.nonmutableExceptionHelper(getName());
				}
				
				return evaluateInner(environment, a);
			}
			return new MewVariable(MewVariableType.Error, new MewException("Cannot " + getName() + ": first argument list not exposed properly"));
		}
		return new MewVariable(MewVariableType.Error, new MewException("Cannot " + getName() + ": argument is not of type list"));
	}	
	
	public abstract MewVariable evaluateInner(MewEnvironment environment, MewList left);
}

abstract class MewListOperationArity2 extends MewNode2Arg {
	private boolean checkMutable;
	
	public MewListOperationArity2(boolean checkMutable) {
		this.checkMutable = checkMutable;
	}

	@Override
	public MewVariable evaluate(MewEnvironment environment) {
		MewNode left = getLeft();
		MewVariable l = left.evaluate(environment);
		
		if (l.type == MewVariableType.List) {
			if (l.value instanceof MewList) {
				MewList a = (MewList)l.value;
				
				if (checkMutable && !a.isMutable()) {
					return a.nonmutableExceptionHelper(getName());
				}
				
				MewNode right = getRight();
				MewVariable r = right.evaluate(environment);
				
				return evaluateInner(environment, a, r);
			}
			return new MewVariable(MewVariableType.Error, new MewException("Cannot " + getName() + ": first argument list not exposed properly"));
		}
		return new MewVariable(MewVariableType.Error, new MewException("Cannot " + getName() + ": argument is not of type list"));
	}	
	
	public abstract MewVariable evaluateInner(MewEnvironment environment, MewList left, MewVariable right);
}

class MewCreateList extends MewNode {
	@Override public String getName() { return "Create-List"; }

	@Override
	public MewVariable evaluate(MewEnvironment environment) {
		MewList a = new MewInternalList(true);
		MewNodeCursor cursor = toCursor();
		while (cursor.hasNext()) {
			MewVariable node = cursor.next().evaluate(environment);
			a.add(node);
		}
		return new MewVariable(MewVariableType.List, a);
	}	
	
	@Override
	public int getMinArgumentCount() { return 0; }

	@Override
	public int getMaxArgumentCount() { return Integer.MAX_VALUE; }
	
	public MewCreateList() { }
}

class MewListAppend extends MewNode {
	@Override public String getName() { return "List-Append"; }

	@Override
	public MewVariable evaluate(MewEnvironment environment) {
		MewNode left = getLeft();
		MewVariable l = left.evaluate(environment);
		
		if (l.type == MewVariableType.List) {
			if (l.value instanceof MewList) {
				MewList a = (MewList)l.value;
				
				if (!a.isMutable() ) {
					return a.nonmutableExceptionHelper(getName());
				}
				
				MewNodeCursor cursor = toCursor();
				cursor.next();
				while (cursor.hasNext()) {
					MewNode right = cursor.next();
					MewVariable r = right.evaluate(environment);
					
					if (r.type == MewVariableType.List) {
						if (r.value instanceof MewList) {
							if (a.size() >= environment.getMaxListSize()) { return new MewVariable(MewVariableType.Error, new MewException("Cannot List-Append: list size limit reached")); }
							MewList b = (MewList)(r.value);
							a.addAll(b);
						} else {
							return new MewVariable(MewVariableType.Error, new MewException("Cannot List-Append: second argument list not exposed properly"));
						}
					} else {
						if (a.size() >= environment.getMaxListSize()) { return new MewVariable(MewVariableType.Error, new MewException("Cannot List-Append: list size limit reached")); }
						a.add(r);
					}
				}
				
				return new MewVariable(MewVariableType.Bool, true);
			}
			return new MewVariable(MewVariableType.Error, new MewException("Cannot List-Append: first argument list not exposed properly"));
		}
		return new MewVariable(MewVariableType.Error, new MewException("Cannot List-Append: argument is not of type list"));
	}	
	
	@Override
	public int getMinArgumentCount() { return 2; }

	@Override
	public int getMaxArgumentCount() { return Integer.MAX_VALUE; }
	
	public MewListAppend() { }
}

class MewListAppendAt extends MewNode3Arg {
	@Override public String getName() { return "List-Append-At"; }

	@Override
	public MewVariable evaluate(MewEnvironment environment) {
		MewNode left = getLeft();
		MewVariable l = left.evaluate(environment);
		
		if (l.type == MewVariableType.List) {
			if (l.value instanceof MewList) {
				MewList a = (MewList)l.value;
				
				if (!a.isMutable()) {
					return a.nonmutableExceptionHelper(getName());
				}
				
				MewNode right = getRight();
				MewVariable r = right.evaluate(environment);
				MewNode third = nodes.get(2);
				MewVariable pos = third.evaluate(environment);
				
				if (pos.type == MewVariableType.Integer || pos.type == MewVariableType.Bool) {
					int iPos = pos.toInt();
					if (iPos > a.size() || iPos < 0) {
						return new MewVariable(MewVariableType.Error, new MewException("Cannot List-Append-At: index out of range, should be 0..." + a.size()));
					} else if (iPos == a.size()) {
						if (a.size() >= environment.getMaxListSize()) { return new MewVariable(MewVariableType.Error, new MewException("Cannot List-Append-At: list size limit reached")); }
						a.add(r);
					} else {
						if (a.size() >= environment.getMaxListSize()) { return new MewVariable(MewVariableType.Error, new MewException("Cannot List-Append-At: list size limit reached")); }
						a.add(iPos, r);
					}
				} else {
					return new MewVariable(MewVariableType.Error, new MewException("Cannot List-Append-At: third argument (index) not an integer or a boolean"));
				}
				
				return new MewVariable(MewVariableType.Bool, true);
			}
			return new MewVariable(MewVariableType.Error, new MewException("Cannot List-Append-At: first argument list not exposed properly"));
		}
		return new MewVariable(MewVariableType.Error, new MewException("Cannot List-Append-At: argument is not of type list"));
	}
	
	public MewListAppendAt() { }
}

class MewListRemove extends MewListOperationArity2 {
	@Override public String getName() { return "List-Remove"; }
	
	public MewListRemove() { super(true); }

	@Override
	public MewVariable evaluateInner(MewEnvironment environment, MewList left, MewVariable right) {
		return new MewVariable(MewVariableType.Bool, left.remove(right));
	}
}

class MewListRemoveAt extends MewListOperationArity2 {
	@Override public String getName() { return "List-Remove-At"; }
	
	public MewListRemoveAt() { super(true); }

	@Override
	public MewVariable evaluateInner(MewEnvironment environment, MewList a, MewVariable pos) {
		if (pos.type == MewVariableType.Integer || pos.type == MewVariableType.Bool) {
			int iPos = pos.toInt();
			if (iPos >= a.size() || iPos < 0) {
				return new MewVariable(MewVariableType.Error, new MewException("Cannot List-Remove-At: index out of range, should be 0..." + a.size()));
			} else {
				MewVariable result = a.remove(iPos);
				return result;
			}
		} else {
			return new MewVariable(MewVariableType.Error, new MewException("Cannot List-Remove-At: second argument (index) not an integer or a boolean"));
		}
	}
}

class MewListRemoveLast extends MewListOperationArity1 {
	@Override public String getName() { return "List-Remove-Last"; }
	
	public MewListRemoveLast() { super(true); }

	@Override
	public MewVariable evaluateInner(MewEnvironment environment, MewList a) {
		if (a.size() > 0) {
			return a.remove(a.size() - 1);
		} else {
			return new MewVariable(MewVariableType.Error, new MewException("Cannot List-Remove-Last: list is empty"));
		}
	}
}

class MewListGet extends MewListOperationArity2 {
	@Override public String getName() { return "List-Get"; }
	
	public MewListGet() { super(false); }

	@Override
	public MewVariable evaluateInner(MewEnvironment environment, MewList a, MewVariable r) {
		if (r.type == MewVariableType.Bool || r.type == MewVariableType.Integer) {
			int ir = r.toInt();
			if (0 <= ir && ir < a.size()) {
				MewVariable result = a.get(r.toInt());
				return result;
			} else {
				return new MewVariable(MewVariableType.Error, new MewException("Cannot List-Get: index out of range, should be 0..." + a.size()));
			}
		} else {
			return new MewVariable(MewVariableType.Error, new MewException("Cannot List-Get: second argument (index) expects an integer or a boolean but got " + r.type.name()));
		}
	}
}

class MewListGetLast extends MewListOperationArity1 {
	@Override public String getName() { return "List-Get-Last"; }
	
	public MewListGetLast() { super(false); }

	@Override
	public MewVariable evaluateInner(MewEnvironment environment, MewList a) {
		if (a.size() > 0) {
			return a.get(a.size() - 1);
		} else {
			return new MewVariable(MewVariableType.Error, new MewException("Cannot List-Get-Last: list is empty"));
		}
	}
}

class MewListClear extends MewListOperationArity1 {
	@Override public String getName() { return "List-Clear"; }
	
	public MewListClear() { super(true); }

	@Override
	public MewVariable evaluateInner(MewEnvironment environment, MewList a) {
		a.clear();
		return new MewVariable(MewVariableType.Bool, true);
	}
}

class MewListSublist extends MewNode {
	@Override public String getName() { return "List-Sublist"; }

	@Override
	public MewVariable evaluate(MewEnvironment environment) {
		MewNode left = getLeft();
		MewVariable l = left.evaluate(environment);
		
		if (l.type == MewVariableType.List) {
			if (l.value instanceof MewList) {
				MewList a = (MewList)l.value;
				
				MewNode right = getRight();
				MewVariable start = right.evaluate(environment);
				MewNode third = nodes.get(2);
				MewVariable end = third.evaluate(environment);
				if (start.type == MewVariableType.Integer || start.type == MewVariableType.Bool) {
					if (end.type == MewVariableType.Integer || end.type == MewVariableType.Bool) {
						int iStart = start.toInt();
						int iEnd = end.toInt();
						int iStep = 1;
						
						if (iStart > iEnd) {
							int temp = iStart;
							iStart = iEnd;
							iEnd = temp;
						}
						
						if (nodes.size() == 4) {
							MewNode fourth = nodes.get(3);
							MewVariable step = fourth.evaluate(environment);
							
							if (step.type == MewVariableType.Integer || step.type == MewVariableType.Bool) {
								iStep = step.toInt();
								
								if (iStep < 1) {
									return new MewVariable(MewVariableType.Error, new MewException("Cannot List-Sublist: argument 4 (step size) should be larger or equals to 1"));
								}
							} else {
								return new MewVariable(MewVariableType.Error, new MewException("Cannot List-Sublist: argument 4 (step size) is not an integer or a boolean"));
							}
						}
						
						if (0 <= iStart && iStart < a.size()) {
							if (0 <= iEnd && iEnd <= a.size()) {
								MewListCursor cursor = a.toCursor();
								MewList list = new MewInternalList(true);
								cursor.setPosition(iStart);
								while (cursor.hasNext() && cursor.getPosition() < iEnd) {
									list.add(cursor.next());
									
									int j = iStep - 1;
									while (j > 0 && cursor.hasNext()) {
										cursor.next();
										j--;
									}
								}
								return new MewVariable(MewVariableType.List, list);
							} else {
								return new MewVariable(MewVariableType.Error, new MewException("Cannot List-Sublist: end value should be inside range 0..." + a.size()));
							}
						} else {
							return new MewVariable(MewVariableType.Error, new MewException("Cannot List-Sublist: start value should be inside range 0..." + a.size()));
						}
					} else {
						return new MewVariable(MewVariableType.Error, new MewException("Cannot List-Sublist: argument 3 (end index) is not an integer or a boolean"));
					}
				} else {
					return new MewVariable(MewVariableType.Error, new MewException("Cannot List-Sublist: argument 2 (start index) is not an integer or a boolean"));
				}
			}
			return new MewVariable(MewVariableType.Error, new MewException("Cannot List-Sublist: first argument list not exposed properly"));
		}
		return new MewVariable(MewVariableType.Error, new MewException("Cannot List-Sublist: argument is not of type list"));
	}	
	
	@Override
	public int getMinArgumentCount() { return 3; }

	@Override
	public int getMaxArgumentCount() { return 4; }
	
	public MewListSublist() { }
}

class MewListIndexOf extends MewListOperationArity2 {
	@Override public String getName() { return "List-Index-Of"; }
	
	public MewListIndexOf() { super(false); }

	@Override
	public MewVariable evaluateInner(MewEnvironment environment, MewList a, MewVariable r) {
		return new MewVariable(MewVariableType.Integer, a.indexOf(r));
	}
}

class MewListContains extends MewListOperationArity2 {
	@Override public String getName() { return "List-Contains"; }
	
	public MewListContains() { super(false); }

	@Override
	public MewVariable evaluateInner(MewEnvironment environment, MewList a, MewVariable r) {
		return new MewVariable(MewVariableType.Bool, (a.contains(r)));
	}
}


class MewListLength extends MewListOperationArity1 {
	@Override public String getName() { return "List-Length"; }
	
	public MewListLength() { super(false); }

	@Override
	public MewVariable evaluateInner(MewEnvironment environment, MewList a) {
		return new MewVariable(MewVariableType.Integer, a.size());
	}
}

class MewSortAbortException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    final MewVariable error;
    MewSortAbortException(MewVariable error) { this.error = error; }
}

class MewListSort extends MewNode {
	@Override public String getName() { return "List-Sort"; }

	@Override
	public MewVariable evaluate(MewEnvironment environment) {
		MewNode left = getLeft();
		MewVariable l = left.evaluate(environment);
		
		if (l.type == MewVariableType.List) {
			if (l.value instanceof MewList) {
				MewList a = (MewList)l.value;
				MewList aa = a.cloneList();
				
				if (nodes.size() == 2) {
					MewVariable second = nodes.get(1).evaluate(environment);
					if (second.type == MewVariableType.Code) {
						if (second.value instanceof MewCodeBlock) {
							final MewCodeBlock comparator = (MewCodeBlock)(second.value);
							
							try {
								aa.sort((first, secondd) -> {
									ArrayList<MewVariable> args = new ArrayList<>();
									args.add(first);
									args.add(secondd);
									MewVariable result = comparator.run(environment, args);
									if (result.type == MewVariableType.Error) {
										throw new MewSortAbortException(result);
									}
									if (result.type == MewVariableType.Bool) {
										if (result.toBool()) {
											return 1;
										} else {
											return -1;
										}
									} else if (result.isNumeric()) {
										return (int) Math.signum(result.toDouble());
									} else {
										if (result.truthy()) {
											return 1;
										} else {
											return 0;
										}
									}
								});
							} catch (MewSortAbortException abort) {
							    return abort.error;
							}
							
							return new MewVariable(MewVariableType.List, aa);
						} else {
							return new MewVariable(MewVariableType.Error, new MewException("Cannot List-Sort: second argument comparator not exposed properly"));
						}
					} else {
						return new MewVariable(MewVariableType.Error, new MewException("Cannot List-Sort: second  argument comparator is not of type code"));
					}
				}
				
				aa.sort((first, second) -> { 
				    boolean fn = first.isNumeric(), sn = second.isNumeric();
				    if (fn && sn) { return Double.compare(first.toDouble(), second.toDouble()); }
				    if (fn != sn) { return fn ? -1 : 1; } // numbers before non-numbers
				    return first.stringifyValue().compareTo(second.stringifyValue()); 
				});
				return new MewVariable(MewVariableType.List, aa);
			}
			return new MewVariable(MewVariableType.Error, new MewException("Cannot List-Sort: first argument list not exposed properly"));
		}
		return new MewVariable(MewVariableType.Error, new MewException("Cannot List-Sort: argument is not of type list"));
	}	
	
	@Override
	public int getMinArgumentCount() { return 1; }

	@Override
	public int getMaxArgumentCount() { return 2; }
	
	public MewListSort() { }
}

class MewListReverse extends MewListOperationArity1 {
	@Override public String getName() { return "List-Reverse"; }
	
	public MewListReverse() { super(false); }

	@Override
	public MewVariable evaluateInner(MewEnvironment environment, MewList a) {
		MewList aa = a.cloneList();
		java.util.Collections.reverse(aa);
		
		return new MewVariable(MewVariableType.List, aa);
	}
}

class MewListClone extends MewListOperationArity1 {
	@Override public String getName() { return "List-Clone"; }
	
	public MewListClone() { super(false); }

	@Override
	public MewVariable evaluateInner(MewEnvironment environment, MewList a) {
		return new MewVariable(MewVariableType.List, a.cloneList());
	}
}

class MewListJoin extends MewNode {
	@Override public String getName() { return "List-Join"; }

	@Override
	public MewVariable evaluate(MewEnvironment environment) {
		MewNode left = getLeft();
		MewVariable l = left.evaluate(environment);
		
		if (l.type == MewVariableType.List) {
			if (l.value instanceof MewList) {
				MewList a = (MewList)l.value;
				String sep = ",";
				
				if (nodes.size() == 2) {
					MewVariable second = nodes.get(1).evaluate(environment);
					if (second.canStringify()) {
						sep = second.stringifyValue();
					} else {
						return new MewVariable(MewVariableType.Error, new MewException("Cannot List-Join: argument 2 (separator) cannot be stringified"));
					}
				}
				
				return new MewVariable(MewVariableType.String, a.join(sep));
			}
			return new MewVariable(MewVariableType.Error, new MewException("Cannot List-Join: first argument list not exposed properly"));
		}
		return new MewVariable(MewVariableType.Error, new MewException("Cannot List-Join: argument is not of type list"));
	}	
	
	@Override
	public int getMinArgumentCount() { return 1; }

	@Override
	public int getMaxArgumentCount() { return 2; }
	
	public MewListJoin() { }
}


class MewForEach extends MewNode2Arg {
	@Override public String getName() { return "For-Each"; }

	@Override
	public MewVariable evaluate(MewEnvironment environment) {
		MewNode left = getLeft();
		MewVariable l = left.evaluate(environment);
		
		if (l.type == MewVariableType.List) {
			if (l.value instanceof MewList) {
				MewList a = (MewList)l.value;
				MewListCursor cursor = a.toCursor();
				
				MewVariable second = nodes.get(1).evaluate(environment);
				if (second.type == MewVariableType.Code) {
					if (second.value instanceof MewCodeBlock) {
						final MewCodeBlock comparator = (MewCodeBlock)(second.value);
						
						boolean envEscapeLoopFlag = environment.escapeLoopFlag;
						environment.escapeLoopFlag = false;
						long executionCap = 0;
						while (cursor.hasNext() && !environment.escapeLoopFlag) {
							MewVariable current = cursor.next();
							ArrayList<MewVariable> args = new ArrayList<>();
							args.add(current);
							args.add(new MewVariable(MewVariableType.Integer, cursor.getPosition() - 1));
							MewVariable result = comparator.run(environment, args);
							environment.lastExpression = result;
							executionCap++;
							
							if (executionCap > environment.getForeverLoopDetectionThreshold()) {
								return new MewVariable(MewVariableType.Error, new MewException("Runtime error: Maximum number of iterations hit. Current threshold is " + environment.getForeverLoopDetectionThreshold()));
							}
						}
						environment.escapeLoopFlag = envEscapeLoopFlag;
						
						return environment.lastExpression;
					} else {
						return new MewVariable(MewVariableType.Error, new MewException("Cannot For-Each: second argument comparator not exposed properly"));
					}
				} else {
					return new MewVariable(MewVariableType.Error, new MewException("Cannot For-Each: second  argument comparator is not of type code"));
				}
			}
			return new MewVariable(MewVariableType.Error, new MewException("Cannot For-Each: first argument list not exposed properly"));
		} else if (l.type == MewVariableType.Map) {
			if (l.value instanceof MewMap) {
				MewMap a  = (MewMap)l.value;
				
				MewVariable second = nodes.get(1).evaluate(environment);
				if (second.type == MewVariableType.Code) {
					if (second.value instanceof MewCodeBlock) {
						final MewCodeBlock comparator = (MewCodeBlock)(second.value);
						
						boolean envEscapeLoopFlag = environment.escapeLoopFlag;
						environment.escapeLoopFlag = false;
						long executionCap = 0;
						for (String key : a.keySet()) {
							ArrayList<MewVariable> args = new ArrayList<>();
							args.add(new MewVariable(MewVariableType.String, key));
							args.add(a.get(key));
							MewVariable result = comparator.run(environment, args);
							environment.lastExpression = result;
							
							executionCap++;
							if (environment.escapeLoopFlag) {
								break;
							}
							if (executionCap > environment.getForeverLoopDetectionThreshold()) {
								return new MewVariable(MewVariableType.Error, new MewException("Runtime error: Maximum number of iterations hit. Current threshold is " + environment.getForeverLoopDetectionThreshold()));
							}
						}
						environment.escapeLoopFlag = envEscapeLoopFlag;
						
						return environment.lastExpression;
					} else {
						return new MewVariable(MewVariableType.Error, new MewException("Cannot For-Each: second argument comparator not exposed properly"));
					}
				} else {
					return new MewVariable(MewVariableType.Error, new MewException("Cannot For-Each: second  argument comparator is not of type code"));
				}
			}
		} else if (l.type == MewVariableType.Bool || l.type == MewVariableType.Integer || l.type == MewVariableType.Float) {
			int target;
			if (l.type == MewVariableType.Float) {
				target = (int) Math.floor(l.toDouble());
			} else {
				target = l.toInt();
			}
			
			MewVariable second = nodes.get(1).evaluate(environment);
			if (second.type == MewVariableType.Code) {
				if (second.value instanceof MewCodeBlock) {
					final MewCodeBlock comparator = (MewCodeBlock)(second.value);
					
					int i = 0;
					boolean envEscapeLoopFlag = environment.escapeLoopFlag;
					environment.escapeLoopFlag = false;
					long executionCap = 0;
					while (target > i) {
						ArrayList<MewVariable> args = new ArrayList<>();
						args.add(new MewVariable(MewVariableType.Integer, i + 1));
						args.add(new MewVariable(MewVariableType.Integer, i));
						MewVariable result = comparator.run(environment, args);
						environment.lastExpression = result;
						
						if (environment.escapeLoopFlag) { break; }
						i++;
						executionCap++;
						if (executionCap > environment.getForeverLoopDetectionThreshold()) {
							return new MewVariable(MewVariableType.Error, new MewException("Runtime error: Maximum number of iterations hit. Current threshold is " + environment.getForeverLoopDetectionThreshold()));
						}
					}
					
					environment.escapeLoopFlag = envEscapeLoopFlag;
					return environment.lastExpression;
				} else {
					return new MewVariable(MewVariableType.Error, new MewException("Cannot For-Each: second argument comparator not exposed properly"));
				}
			} else {
				return new MewVariable(MewVariableType.Error, new MewException("Cannot For-Each: second  argument comparator is not of type code"));
			}
		} else if (l.canStringify()) {
			String target = l.stringifyValue();
			MewtypeProgramSource cursor = new MewtypeProgramSource(target);
			
			MewVariable second = nodes.get(1).evaluate(environment);
			if (second.type == MewVariableType.Code) {
				if (second.value instanceof MewCodeBlock) {
					final MewCodeBlock comparator = (MewCodeBlock)(second.value);
					
					boolean envEscapeLoopFlag = environment.escapeLoopFlag;
					environment.escapeLoopFlag = false;
					long executionCap = 0;
					while (cursor.hasNext() && !environment.escapeLoopFlag) {
						char next = cursor.next();
						ArrayList<MewVariable> args = new ArrayList<>();
						args.add(new MewVariable(MewVariableType.String, String.valueOf(next)));
						args.add(new MewVariable(MewVariableType.Integer, cursor.getPosition() - 1));
						MewVariable result = comparator.run(environment, args);
						environment.lastExpression = result;
						
						executionCap++;
						if (executionCap > environment.getForeverLoopDetectionThreshold()) {
							return new MewVariable(MewVariableType.Error, new MewException("Runtime error: Maximum number of iterations hit. Current threshold is " + environment.getForeverLoopDetectionThreshold()));
						}
					}
					
					environment.escapeLoopFlag = envEscapeLoopFlag;
					return environment.lastExpression;
				} else {
					return new MewVariable(MewVariableType.Error, new MewException("Cannot For-Each: second argument comparator not exposed properly"));
				}
			} else {
				return new MewVariable(MewVariableType.Error, new MewException("Cannot For-Each: second  argument comparator is not of type code"));
			}
		}
		return new MewVariable(MewVariableType.Error, new MewException("Cannot For-Each: argument could not be iterated"));
	}
	
	public MewForEach() { }
}

class MewListIsMutable extends MewListOperationArity1 {
	@Override public String getName() { return "List-Is-Mutable"; }
	
	public MewListIsMutable() { super(false); }

	@Override
	public MewVariable evaluateInner(MewEnvironment environment, MewList a) {
		return new MewVariable(MewVariableType.Bool, a.isMutable());
	}
}

class MewListMakeImmutable extends MewListOperationArity1 {
	@Override public String getName() { return "List-Make-Immutable"; }
	
	public MewListMakeImmutable() { super(false); }

	@Override
	public MewVariable evaluateInner(MewEnvironment environment, MewList a) {
		a.makeImmutable();
		return new MewVariable(MewVariableType.Bool, true);
	}
}

class MewIsList extends MewIsStar {
	public MewIsList() { super(MewVariableType.List, "List"); }
}

abstract class MewCursorOperationArity1 extends MewNode1Arg {	
	@Override
	public int getMinArgumentCount() { return 1; }

	@Override
	public int getMaxArgumentCount() { return 1; }

	@Override
	public MewVariable evaluate(MewEnvironment environment) {
		MewNode left = getLeft();
		MewVariable l = left.evaluate(environment);
		
		if (l.type == MewVariableType.Cursor) {
			if (l.value instanceof MewListCursor) {
				MewListCursor a = (MewListCursor)l.value;
				
				return evaluateInner(environment, a);
			}
			return new MewVariable(MewVariableType.Error, new MewException("Cannot " + getName() +": first argument cursor not exposed properly"));
		}
		return new MewVariable(MewVariableType.Error, new MewException("Cannot " + getName() + ": argument is not of type cursor"));
	}	
	
	public abstract MewVariable evaluateInner(MewEnvironment environment, MewListCursor a);
}

class MewIsCursor extends MewIsStar {
	public MewIsCursor() { super(MewVariableType.Cursor, "Cursor"); }
}

class MewCursorHasNext extends MewCursorOperationArity1 {
	@Override public String getName() { return "Cursor-Has-Next"; }
	
	public MewCursorHasNext() { }

	@Override
	public MewVariable evaluateInner(MewEnvironment environment, MewListCursor a) {
		return new MewVariable(MewVariableType.Bool, a.hasNext());
	}
}

class MewCursorGetNext extends MewCursorOperationArity1 {
	@Override public String getName() { return "Cursor-Get-Next"; }
	
	public MewCursorGetNext() { }

	@Override
	public MewVariable evaluateInner(MewEnvironment environment, MewListCursor a) {
		return a.next();
	}
}

class MewCursorPeekNext extends MewCursorOperationArity1 {
	@Override public String getName() { return "Cursor-Peek-Next"; }
	
	public MewCursorPeekNext() { }

	@Override
	public MewVariable evaluateInner(MewEnvironment environment, MewListCursor a) {
		return a.peek();
	}
}

class MewCursorGetPosition extends MewCursorOperationArity1 {
	@Override public String getName() { return "Cursor-Get-Position"; }
	
	public MewCursorGetPosition() { }

	@Override
	public MewVariable evaluateInner(MewEnvironment environment, MewListCursor a) {
		return new MewVariable(MewVariableType.Integer, a.getPosition());
	}
}

class MewCursorSetPosition extends MewNode2Arg {
	@Override public String getName() { return "Cursor-Set-Position"; }

	@Override
	public MewVariable evaluate(MewEnvironment environment) {
		MewNode left = getLeft();
		MewVariable l = left.evaluate(environment);
		
		if (l.type == MewVariableType.Cursor) {
			if (l.value instanceof MewListCursor) {
				MewListCursor a = (MewListCursor)l.value;
				
				MewNode right = getRight();
				MewVariable r = right.evaluate(environment);
				
				if (r.type == MewVariableType.Bool || r.type == MewVariableType.Integer) {
					int ir = r.toInt();
					if (ir >= 0) {
						a.setPosition(ir);
					} else {
						return new MewVariable(MewVariableType.Error, new MewException("Cannot Cursor-Set-Position: argument 2 (new position) is a negative number"));
					}
				} else {
					return new MewVariable(MewVariableType.Error, new MewException("Cannot Cursor-Set-Position: argument 2 (new position) is not of type integer or boolean"));
				}
				
				return new MewVariable(MewVariableType.Bool, true);
			}
			return new MewVariable(MewVariableType.Error, new MewException("Cannot Cursor-Set-Position: first argument cursor not exposed properly"));
		}
		return new MewVariable(MewVariableType.Error, new MewException("Cannot Cursor-Set-Position: argument is not of type cursor"));
	}
	
	public MewCursorSetPosition() { }
}

class MewCreateCursor extends MewListOperationArity1 {
	@Override public String getName() { return "Create-Cursor"; }
	
	public MewCreateCursor() { super(false); }

	@Override
	public MewVariable evaluateInner(MewEnvironment environment, MewList a) {
		return new MewVariable(MewVariableType.Cursor, new MewListCursor(a));
	}
}

abstract class MewMapOperationArity1 extends MewNode1Arg {
	private boolean checkMutable;
	
	public MewMapOperationArity1() { checkMutable = false; }
	public MewMapOperationArity1(boolean checkMutable) {
		this.checkMutable = checkMutable;
	}

	@Override
	public MewVariable evaluate(MewEnvironment environment) {
		MewNode left = getLeft();
		MewVariable l = left.evaluate(environment);
		
		if (l.type == MewVariableType.Map) {
			if (l.value instanceof MewMap) {
				MewMap a = (MewMap)l.value;
				
				if (checkMutable && !a.isMutable()) {
					return a.nonmutableExceptionHelper(getName());
				}
				
				return evaluateInner(environment, a);
			}
			return new MewVariable(MewVariableType.Error, new MewException("Cannot " + getName() + ": first argument map not exposed properly"));
		}
		return new MewVariable(MewVariableType.Error, new MewException("Cannot " + getName() + ": argument is not of type map"));
	}	
	
	public abstract MewVariable evaluateInner(MewEnvironment environment, MewMap left);
}

abstract class MewMapOperationArity2 extends MewNode2Arg {
	private boolean checkMutable;
	
	public MewMapOperationArity2() { checkMutable = false; }
	public MewMapOperationArity2(boolean checkMutable) {
		this.checkMutable = checkMutable;
	}

	@Override
	public MewVariable evaluate(MewEnvironment environment) {
		MewNode left = getLeft();
		MewVariable l = left.evaluate(environment);
		
		if (l.type == MewVariableType.Map) {
			if (l.value instanceof MewMap) {
				MewMap a = (MewMap)l.value;
				
				if (checkMutable && !a.isMutable()) {
					return a.nonmutableExceptionHelper(getName());
				}
				
				MewNode right = getRight();
				MewVariable r = right.evaluate(environment);
				
				return evaluateInner(environment, a, r);
			}
			return new MewVariable(MewVariableType.Error, new MewException("Cannot " + getName() + ": first argument map not exposed properly"));
		}
		return new MewVariable(MewVariableType.Error, new MewException("Cannot " + getName() + ": argument is not of type map"));
	}	
	
	public abstract MewVariable evaluateInner(MewEnvironment environment, MewMap left, MewVariable right);
}

class MewIsMap extends MewIsStar {
	public MewIsMap() { super(MewVariableType.Map, "Map"); }
}

class MewCreateMap extends MewNode {
	@Override public String getName() { return "Create-Map"; }

	@Override
	public MewVariable evaluate(MewEnvironment environment) {
		return new MewVariable(MewVariableType.Map, new MewInternalMap(true));
	}
	
	public MewCreateMap() { }
}

class MewMapClear extends MewMapOperationArity1 {
	@Override public String getName() { return "Map-Clear"; }
	
	public MewMapClear() { super(true); }

	@Override
	public MewVariable evaluateInner(MewEnvironment environment, MewMap a) {
		a.clear();
		return new MewVariable(MewVariableType.Bool, true);
	}
}

class MewMapContainsKey extends MewMapOperationArity2 {
	@Override public String getName() { return "Map-Contains-Key"; }
	
	public MewMapContainsKey() { }

	@Override
	public MewVariable evaluateInner(MewEnvironment environment, MewMap a, MewVariable r) {
		return new MewVariable(MewVariableType.Bool, (boolean)a.containsKey(r.stringifyValue()));
	}
}

class MewMapContainsValue extends MewMapOperationArity2 {
	@Override public String getName() { return "Map-Contains-Value"; }
	
	public MewMapContainsValue() { }

	@Override
	public MewVariable evaluateInner(MewEnvironment environment, MewMap a, MewVariable r) {
		return new MewVariable(MewVariableType.Bool, a.containsValue(r));
	}
}

class MewMapPut extends MewNode3Arg {
	@Override public String getName() { return "Map-Put"; }

	@Override
	public MewVariable evaluate(MewEnvironment environment) {
		MewNode left = getLeft();
		MewVariable l = left.evaluate(environment);
		
		if (l.type == MewVariableType.Map) {
			if (l.value instanceof MewMap) {
				MewMap a = (MewMap)l.value;
				
				if (!a.isMutable()) {
					return a.nonmutableExceptionHelper(getName());
				}
				
				MewVariable name = getRight().evaluate(environment);
				if (name.canStringify()) {
					String sName = name.stringifyValue();
					
					MewVariable object = nodes.get(2).evaluate(environment);
					MewVariable prev = a.put(sName, object);
					if (prev == null) {
						return new MewVariable(MewVariableType.Null, "Yeetoo Birdy");
					} else {
						return prev;
					}
				} else {
					return new MewVariable(MewVariableType.Error, new MewException("Cannot Map-Put: argument 2 (name) cannot be stringified"));
				}
			}
			return new MewVariable(MewVariableType.Error, new MewException("Cannot Map-Put: first argument map not exposed properly"));
		}
		return new MewVariable(MewVariableType.Error, new MewException("Cannot Map-Put: argument is not of type map"));
	}
	
	public MewMapPut() { }
}

class MewMapGet extends MewNode {
	@Override public String getName() { return "Map-Get"; }

	@Override
	public MewVariable evaluate(MewEnvironment environment) {
		MewNode left = getLeft();
		MewVariable l = left.evaluate(environment);
		
		if (l.type == MewVariableType.Map) {
			if (l.value instanceof MewMap) {
				MewMap a = (MewMap)l.value;
				
				MewVariable name = getRight().evaluate(environment);
				if (name.canStringify()) {
					String sName = name.stringifyValue();
					
					boolean c = a.containsKey(sName);
					MewVariable prev = a.get(sName);
					if (!(c)) {
						MewVariable def = new MewVariable(MewVariableType.Error, new MewException("Cannot Map-Get: The key " + sName + " does not exist in the map"));
						if (nodes.size() == 3) {
							def = nodes.get(2).evaluate(environment);
						}
						return def;
					} else {
						return prev;
					}
				} else {
					return new MewVariable(MewVariableType.Error, new MewException("Cannot Map-Get: argument 2 (name) cannot be stringified"));
				}
			}
			return new MewVariable(MewVariableType.Error, new MewException("Cannot Map-Get: first argument map not exposed properly"));
		}
		return new MewVariable(MewVariableType.Error, new MewException("Cannot Map-Get: argument is not of type map"));
	}	
	
	@Override
	public int getMinArgumentCount() { return 2; }

	@Override
	public int getMaxArgumentCount() { return 3; }
	
	public MewMapGet() { }
}

class MewMapRemove extends MewMapOperationArity2 {
	@Override public String getName() { return "Map-Remove"; }
	
	public MewMapRemove() { super(true); }

	@Override
	public MewVariable evaluateInner(MewEnvironment environment, MewMap a, MewVariable name) {
		if (name.canStringify()) {
			String sName = name.stringifyValue();
			
			boolean c = a.containsKey(sName);
			MewVariable prev = a.remove(sName);
			if (!(c)) {
				return new MewVariable(MewVariableType.Error, new MewException("Cannot Map-Remove: The key " + sName + " does not exist in the map or you are removing a null value from the map"));
			} else {
				return prev;
			}
		} else {
			return new MewVariable(MewVariableType.Error, new MewException("Cannot Map-Remove: argument 2 (name) cannot be stringified"));
		}
	}
}

class MewMapUnpack extends MewMapOperationArity1 {
	@Override public String getName() { return "Map-Unpack"; }

	@Override
	public MewVariable evaluateInner(MewEnvironment environment, MewMap left) {
		for (String s : left.keySet()) {
			String varName = s;
			while (environment.variables.containsKey(varName)) {
				varName = "_" + varName;
			}
			environment.variables.put(varName, left.get(s));
		}
		return new MewVariable(MewVariableType.Null, "Hatsune Miku is Hiding in Your Wifi");
	}
	
	public MewMapUnpack() { }
}

class MewMapGetKeys extends MewMapOperationArity1 {
	@Override public String getName() { return "Map-Get-Keys"; }
	
	public MewMapGetKeys() { }

	@Override
	public MewVariable evaluateInner(MewEnvironment environment, MewMap a) {
		MewList list = new MewInternalList(false);
		
		for (String s : a.keySet()) {
			list.add(new MewVariable(MewVariableType.String, s));
		}
		
		return new MewVariable(MewVariableType.List, list);
	}
}

class MewMapGetValues extends MewMapOperationArity1 {
	@Override public String getName() { return "Map-Get-Values"; }
	
	public MewMapGetValues() { }

	@Override
	public MewVariable evaluateInner(MewEnvironment environment, MewMap a) {
		MewList list = new MewInternalList(false);
		
		for (MewVariable s : a.values()) {
			list.add(s);
		}
		
		return new MewVariable(MewVariableType.List, list);
	}
}

class MewMapIsMutable extends MewMapOperationArity1 {
	@Override public String getName() { return "Map-Is-Mutable"; }
	
	public MewMapIsMutable() { }

	@Override
	public MewVariable evaluateInner(MewEnvironment environment, MewMap a) {
		return new MewVariable(MewVariableType.Bool, a.isMutable());
	}
}

class MewMapMakeImmutable extends MewMapOperationArity1 {
	@Override public String getName() { return "Map-Make-Immutable"; }
	
	public MewMapMakeImmutable() { }

	@Override
	public MewVariable evaluateInner(MewEnvironment environment, MewMap a) {
		a.makeImmutable();
		return new MewVariable(MewVariableType.Bool, true);
	}
}

class MewMapClone extends MewMapOperationArity1 {
	@Override public String getName() { return "Map-Clone"; }
	
	public MewMapClone() { }

	@Override
	public MewVariable evaluateInner(MewEnvironment environment, MewMap a) {
		return new MewVariable(MewVariableType.Map, a.cloneMap());
	}
}

abstract class MewStringBuilderOperationArity1 extends MewNode1Arg {
	public MewStringBuilderOperationArity1() { }

	@Override
	public MewVariable evaluate(MewEnvironment environment) {
		MewNode left = getLeft();
		MewVariable l = left.evaluate(environment);
		
		if (l.type == MewVariableType.StringBuilder) {
			if (l.value instanceof StringBuilder) {
				StringBuilder a = (StringBuilder)l.value;
				
				return evaluateInner(environment, a);
			}
			return new MewVariable(MewVariableType.Error, new MewException("Cannot " + getName() + ": first argument StringBuilder not exposed properly"));
		}
		return new MewVariable(MewVariableType.Error, new MewException("Cannot " + getName() + ": argument is not of type StringBuilder"));
	}	
	
	public abstract MewVariable evaluateInner(MewEnvironment environment, StringBuilder left);
}

abstract class MewStringBuilderOperationArity2 extends MewNode2Arg {
	public MewStringBuilderOperationArity2() { }

	@Override
	public MewVariable evaluate(MewEnvironment environment) {
		MewNode left = getLeft();
		MewVariable l = left.evaluate(environment);
		
		if (l.type == MewVariableType.StringBuilder) {
			if (l.value instanceof StringBuilder) {
				StringBuilder a = (StringBuilder)l.value;
				
				MewNode right = getRight();
				MewVariable r = right.evaluate(environment);
				
				return evaluateInner(environment, a, r);
			}
			return new MewVariable(MewVariableType.Error, new MewException("Cannot " + getName() + ": first argument StringBuilder not exposed properly"));
		}
		return new MewVariable(MewVariableType.Error, new MewException("Cannot " + getName() + ": argument is not of type StringBuilder"));
	}	
	
	public abstract MewVariable evaluateInner(MewEnvironment environment, StringBuilder left, MewVariable right);
}

abstract class MewStringBuilderOperationArity3 extends MewNode3Arg {
	public MewStringBuilderOperationArity3() { }

	@Override
	public MewVariable evaluate(MewEnvironment environment) {
		MewNode left = getLeft();
		MewVariable l = left.evaluate(environment);
		
		if (l.type == MewVariableType.StringBuilder) {
			if (l.value instanceof StringBuilder) {
				StringBuilder a = (StringBuilder)l.value;
				
				MewNode right = getRight();
				MewVariable r = right.evaluate(environment);
				MewNode third = nodes.get(2);
				MewVariable t = third.evaluate(environment);
				
				return evaluateInner(environment, a, r, t);
			}
			return new MewVariable(MewVariableType.Error, new MewException("Cannot " + getName() + ": first argument StringBuilder not exposed properly"));
		}
		return new MewVariable(MewVariableType.Error, new MewException("Cannot " + getName() + ": argument is not of type StringBuilder"));
	}	
	
	public abstract MewVariable evaluateInner(MewEnvironment environment, StringBuilder left, MewVariable middle, MewVariable right);
}

class MewSBCreate extends MewNode {

	@Override public String getName() { return "Create-String-Builder"; }

	@Override
	public MewVariable evaluate(MewEnvironment environment) {
		StringBuilder sb = new StringBuilder();
		if (nodes.size() == 1) {
			MewVariable def = nodes.get(0).evaluate(environment);
			if (def.canStringify()) {
				sb.append(def.stringifyValue());
			} else {
				return new MewVariable(MewVariableType.Error, new MewException("Cannot Create-String-Builder: the provided default value (argument 1) cannot be converted into a string"));
			}
		}
		return new MewVariable(MewVariableType.StringBuilder, sb);
	}
	
	@Override public int getMinArgumentCount() { return 0; }
	@Override public int getMaxArgumentCount() { return 1; }
	
	public MewSBCreate() { }
}

class MewSBAppend extends MewStringBuilderOperationArity2 {
	@Override
	public String getName() { return "SB-Append"; }

	@Override
	public MewVariable evaluateInner(MewEnvironment environment, StringBuilder left, MewVariable right) {
		if (right.canStringify()) {
			left.append(right.stringifyValue());
			
			if (nodes.size() == 3) {
				MewVariable nextLine = nodes.get(2).evaluate(environment);
				if (nextLine.truthy()) {
					left.append('\n');
				}
			}
			
			return new MewVariable(MewVariableType.StringBuilder, left);
		} else {
			return new MewVariable(MewVariableType.Error, new MewException("Cannot SB-Append: the provided string to append (argument 2) cannot be converted into a string"));
		}
	}
	
	@Override public int getMaxArgumentCount() { return 3; }
	
	public MewSBAppend() { }
}

class MewSBInsert extends MewStringBuilderOperationArity3 {
	@Override
	public String getName() { return "SB-Insert"; }
	
	@Override
	public MewVariable evaluateInner(MewEnvironment environment, StringBuilder left, MewVariable middle,
			MewVariable right) {
		if (middle.isNumeric()) {
			int offset = middle.toInt();
			if (offset >= 0 && offset <= left.length()) {
				if (right.canStringify()) {
					left.insert(offset, right.stringifyValue());
					return new MewVariable(MewVariableType.StringBuilder, left);
				} else {
					return new MewVariable(MewVariableType.Error, new MewException("Cannot SB-Insert: the provided string to insert (argument 3) cannot be converted into a string"));
				}
			} else {
				return new MewVariable(MewVariableType.Error, new MewException("Cannot SB-Insert: the provided offset (argument 2) (" + offset + ") is out of bounds, should be 0..." + left.length()));
			}
		} else {
			return new MewVariable(MewVariableType.Error, new MewException("Cannot SB-Insert: the provided offset (argument 2) cannot be converted into a number"));
		}
	}
	
	public MewSBInsert() { }
}

class MewSBDelete extends MewStringBuilderOperationArity3 {
	@Override
	public String getName() { return "SB-Delete"; }
	
	@Override
	public MewVariable evaluateInner(MewEnvironment environment, StringBuilder left, MewVariable middle,
			MewVariable right) {
		if (middle.isNumeric()) {
			int begin = middle.toInt();
			if (begin >= 0 && begin < left.length()) {
				if (right.isNumeric()) {
					int end = right.toInt();
					if (begin >= end) {
						return new MewVariable(MewVariableType.Error, new MewException("Cannot SB-Delete: the provided begin index (argument 2) (" + begin + ") is larger than the end index"));
					}
					if (end >= 0) {
						left.delete(begin, end);
						return new MewVariable(MewVariableType.StringBuilder, left);
					} else {
						return new MewVariable(MewVariableType.Error, new MewException("Cannot SB-Delete: the provided end index (argument 3) (" + end + ") is out of bounds, should be " + begin + "..." + left.length()));
					}
				} else {
					return new MewVariable(MewVariableType.Error, new MewException("Cannot SB-Delete: the provided end index (argument 3) cannot be converted into a number"));
				}
			} else {
				return new MewVariable(MewVariableType.Error, new MewException("Cannot SB-Delete: the provided begin index (argument 2) (" + begin + ") is out of bounds, should be 0..." + left.length()));
			}
		} else {
			return new MewVariable(MewVariableType.Error, new MewException("Cannot SB-Delete: the provided begin index (argument 2) cannot be converted into a number"));
		}
	}
	
	public MewSBDelete() { }
}

class MewSBReplace extends MewNode {
	@Override public String getName() { return "SB-Replace"; }
	
	@Override
	public MewVariable evaluate(MewEnvironment environment) {
		MewVariable left = getLeft().evaluate(environment);
		MewVariable middle = getRight().evaluate(environment);
		MewVariable right = nodes.get(2).evaluate(environment);
		MewVariable extra = nodes.get(3).evaluate(environment);
		
		if (left.type == MewVariableType.StringBuilder) {
			if (left.value instanceof StringBuilder) {
				StringBuilder sb = (StringBuilder)left.value;
				
				if (middle.isNumeric()) {
					int begin = middle.toInt();
					if (begin >= 0 && begin < sb.length()) {
						if (right.isNumeric()) {
							int end = right.toInt();
							if (begin >= end) {
								return new MewVariable(MewVariableType.Error, new MewException("Cannot SB-Replace: the provided begin index (argument 2) (" + begin + ") is larger than the end index"));
							}
							if (end >= 0) {
								if (extra.canStringify()) {
									sb.replace(begin, end, extra.stringifyValue());
									return new MewVariable(MewVariableType.StringBuilder, sb);
								} else {
									return new MewVariable(MewVariableType.Error, new MewException("Cannot SB-Replace: the provided begin replacement value (argument 4) cannot be converted into a string"));
								}
							} else {
								return new MewVariable(MewVariableType.Error, new MewException("Cannot SB-Replace: the provided end index (argument 3) (" + end + ") is out of bounds, should be " + begin + "..." + sb.length()));
							}
						} else {
							return new MewVariable(MewVariableType.Error, new MewException("Cannot SB-Replace: the provided end index (argument 3) cannot be converted into a number"));
						}
					} else {
						return new MewVariable(MewVariableType.Error, new MewException("Cannot SB-Replace: the provided begin index (argument 2) (" + begin + ") is out of bounds, should be 0..." + sb.length()));
					}
				} else {
					return new MewVariable(MewVariableType.Error, new MewException("Cannot SB-Replace: the provided begin index (argument 2) cannot be converted into a number"));
				}
			} else {
				return new MewVariable(MewVariableType.Error, new MewException("Cannot " + getName() + ": first argument StringBuilder not exposed properly"));
			}
		} else {
			return new MewVariable(MewVariableType.Error, new MewException("Cannot " + getName() + ": argument is not of type StringBuilder"));
		}
	}
	
	public MewSBReplace() { }
	@Override public int getMinArgumentCount() { return 4; }
	@Override public int getMaxArgumentCount() { return 4; }
}

class MewSBCharacterAt extends MewStringBuilderOperationArity2 {
	@Override public String getName() { return "SB-Character-At"; }
	public MewSBCharacterAt() { }

	@Override
	public MewVariable evaluateInner(MewEnvironment environment, StringBuilder left, MewVariable middle) {
		if (middle.isNumeric()) {
			int offset = middle.toInt();
			if (offset >= 0 && offset < left.length()) {
				return new MewVariable(MewVariableType.String, String.valueOf(left.charAt(offset)));
			} else {
				return new MewVariable(MewVariableType.Error, new MewException("Cannot SB-Character-At: the provided offset (argument 2) (" + offset + ") is out of bounds, should be 0..." + left.length()));
			}
		} else {
			return new MewVariable(MewVariableType.Error, new MewException("Cannot SB-Character-At: the provided offset (argument 2) cannot be converted into a number"));
		}	
	}
}

class MewSBSetCharacterAt extends MewStringBuilderOperationArity3 {
	@Override public String getName() { return "SB-Set-Character-At"; }
	public MewSBSetCharacterAt() { }
	
	@Override
	public MewVariable evaluateInner(MewEnvironment environment, StringBuilder left, MewVariable middle,
			MewVariable right) {
		if (middle.isNumeric()) {
			int offset = middle.toInt();
			if (offset >= 0 && offset < left.length()) {
				if (right.canStringify()) {
					String sr = right.stringifyValue();
					if (sr.length() == 1) {
						char cr = sr.charAt(0);
						left.setCharAt(offset, cr);
						return new MewVariable(MewVariableType.StringBuilder, left);
					} else {
						return new MewVariable(MewVariableType.Error, new MewException("Cannot SB-Set-Character-At: the provided character to insert (argument 3) (" + sr + ") should have length one, not " + sr.length()));
					}
				} else {
					return new MewVariable(MewVariableType.Error, new MewException("Cannot SB-Set-Character-At: The provided character to insert (argument 3) cannot be converted into a stiring"));
				}
			} else {
				return new MewVariable(MewVariableType.Error, new MewException("Cannot SB-Set-Character-At: the provided offset (argument 2) (" + offset + ") is out of bounds, should be 0..." + left.length()));
			}
		} else {
			return new MewVariable(MewVariableType.Error, new MewException("Cannot SB-Set-Character-At: the provided offset (argument 2) cannot be converted into a number"));
		}	
	}
}

class MewSBReverse extends MewStringBuilderOperationArity1 {
	@Override public String getName() { return "SB-Reverse"; }

	@Override
	public MewVariable evaluateInner(MewEnvironment environment, StringBuilder left) {
		return new MewVariable(MewVariableType.StringBuilder, left.reverse());
	}
	public MewSBReverse() { }
}

class MewSBClear extends MewStringBuilderOperationArity1 {
	@Override public String getName() { return "SB-Clear"; }

	@Override
	public MewVariable evaluateInner(MewEnvironment environment, StringBuilder left) {
		left.setLength(0);
		return new MewVariable(MewVariableType.StringBuilder, left);
	}
	public MewSBClear() { }
}

class MewSBLength extends MewStringBuilderOperationArity1 {
	@Override public String getName() { return "SB-Length"; }

	@Override
	public MewVariable evaluateInner(MewEnvironment environment, StringBuilder left) {
		return new MewVariable(MewVariableType.Integer, left.length());
	}
	public MewSBLength() { }
}

class MewSBToString extends MewStringBuilderOperationArity1 {
	@Override public String getName() { return "SB-To-String"; }

	@Override
	public MewVariable evaluateInner(MewEnvironment environment, StringBuilder left) {
		return new MewVariable(MewVariableType.String, left.toString());
	}
	public MewSBToString() { }
}

class MewParseJson extends MewStringOperationArity1 {
	@Override public String getName() { return "Parse-Json"; }

	@Override
	public MewVariable evaluateInner(MewEnvironment environment, String str) {
		return environment.ritsu.parse(str);
	}

	public MewParseJson() { }
}

class MewGetType extends MewNode1Arg {
	@Override public String getName() { return "Get-Type"; }

	@Override
	public MewVariable evaluate(MewEnvironment environment) {
		MewVariable value = getLeft().evaluate(environment);
		return new MewVariable(MewVariableType.String, value.type.name());
	}
	
	public MewGetType() { }
}

class MewIsStringBuilder extends MewIsStar {
	public MewIsStringBuilder() { super(MewVariableType.StringBuilder, "String-Builder"); }
}

class MewIsInt extends MewIsStar {
	public MewIsInt() { super(MewVariableType.Integer, "Integer"); }
}

class MewIsFloat extends MewIsStar {
	public MewIsFloat() { super(MewVariableType.Float, "Float"); }
}

class MewIsBool extends MewIsStar {
	public MewIsBool() { super(MewVariableType.Bool, "Bool"); }
}

class MewIsString extends MewIsStar {
	public MewIsString() { super(MewVariableType.String, "String"); }
}

class MewIsNull extends MewIsStar {
	public MewIsNull() { super(MewVariableType.Null, "Null"); }
}

class MewParseInteger extends MewNode {
	@Override public String getName() { return "Parse-Integer"; }

	@Override
	public MewVariable evaluate(MewEnvironment environment) {
		MewVariable value = getLeft().evaluate(environment);
		if (value.canStringify()) {
			int radix = 10;
			if (nodes.size() == 2) {
				MewVariable second = getRight().evaluate(environment);
				if (second.type == MewVariableType.Bool || second.type == MewVariableType.Integer) {
					radix = second.toInt();
					
					if (radix < Character.MIN_RADIX) {
						return new MewVariable(MewVariableType.Error, new MewException("Cannot Parse-Integer: The minimum radix is " + Character.MIN_RADIX + " but argument 2 (the radix) supplied " + radix));
					}
					if (radix > Character.MAX_RADIX) {
						return new MewVariable(MewVariableType.Error, new MewException("Cannot Parse-Integer: The maximum radix is " + Character.MAX_RADIX + " but argument 2 (the radix) supplied " + radix));
					}
				} else {
					return new MewVariable(MewVariableType.Error, new MewException("Cannot Parse-Integer: argument 2 (the radix) is not of type integer or boolean"));
				}
			}
			try {
				int i = Integer.parseInt(value.stringifyValue(), radix);
				return new MewVariable(MewVariableType.Integer, i);
			} catch (Exception e) {
				return new MewVariable(MewVariableType.Error, new MewException("Cannot Parse-Integer: parsing failed: " + e.getMessage(), e));
			}
		} else {
			return new MewVariable(MewVariableType.Error, new MewException("Cannot Parse-Integer: argument 1 (the value to parse) could not be stringified"));
		}
	}
	
	@Override
	public int getMinArgumentCount() { return 1; }

	@Override
	public int getMaxArgumentCount() { return 2; }
	
	public MewParseInteger() { }
}

class MewParseFloat extends MewNode1Arg {
	@Override public String getName() { return "Parse-Float"; }

	@Override
	public MewVariable evaluate(MewEnvironment environment) {
		MewVariable value = getLeft().evaluate(environment);
		if (value.canStringify()) {
			try {
				Double i = Double.parseDouble(value.stringifyValue());
				return new MewVariable(MewVariableType.Float, i);
			} catch (Exception e) {
				return new MewVariable(MewVariableType.Error, new MewException("Cannot Parse-Float: parsing failed: " + e.getMessage(), e));
			}
		} else {
			return new MewVariable(MewVariableType.Error, new MewException("Cannot Parse-Float: argument 1 (the value to parse) could not be stringified"));
		}
	}
	
	public MewParseFloat() { }
}

class MewParseBool extends MewNode1Arg {
	@Override public String getName() { return "Parse-Bool"; }

	@Override
	public MewVariable evaluate(MewEnvironment environment) {
		MewVariable value = getLeft().evaluate(environment);
		if (value.canStringify()) {
			try {
				boolean i = Boolean.parseBoolean(value.stringifyValue());
				return new MewVariable(MewVariableType.Bool, i);
			} catch (Exception e) {
				return new MewVariable(MewVariableType.Error, new MewException("Cannot Parse-Bool: parsing failed: " + e.getMessage(), e));
			}
		} else {
			return new MewVariable(MewVariableType.Error, new MewException("Cannot Parse-Bool: argument 1 (the value to parse) could not be stringified"));
		}
	}
	
	public MewParseBool() { }
}

class MewParseString extends MewNode1Arg {
	@Override public String getName() { return "Parse-String"; }

	@Override
	public MewVariable evaluate(MewEnvironment environment) {
		MewVariable value = getLeft().evaluate(environment);
		if (value.canStringify()) {
			return new MewVariable(MewVariableType.String, value.stringifyValue());
		} else {
			return new MewVariable(MewVariableType.Error, new MewException("Cannot Parse-String: argument 1 (the value to parse) could not be stringified"));
		}
	}
	
	public MewParseString() { }
}

/**
MewBridge â€” host-side factory for MewExternalList / MewExternalMap injection.

Placement: append this class to the END of MewtypeInterpreter.java (before nothing â€”
it is a top-level class), or keep it as a separate file in the SAME package. All the
types it touches (MewCoercion, MewElementRule, MewExternalList, MewExternalMap,
MewVariable) are package-private, so a different package will not compile.

Conventions: no static members (matches the codebase's Sketchware constraint);
coercion rules are cached per element class (they are stateless closures).

Before (six constructor arguments + manual MewVariable wrap, per collection):

	m.put("HOST_MAP", new MewVariable(MewVariableType.Map,
		new MewExternalMap<String, Integer>(host, true, String.class, Integer.class,
			new MewCoercion().stringRule(), new MewCoercion().of(Integer.class))));
	m.put("HOST_NULLS", new MewVariable(MewVariableType.List,
		new MewExternalList<Integer>(hostL, false, Integer.class,
			new MewCoercion().of(Integer.class))));

After:

	MewBridge bridge = new MewBridge();          // keep one instance per host
	m.put("HOST_MAP",   bridge.map(host, true, Integer.class));
	m.put("HOST_NULLS", bridge.list(hostL, false, Integer.class));
	// or, for the common element types:
	m.put("HOST_NAMES", bridge.listOfStrings(names, false));
	m.put("HOST_SCORES", bridge.mapOfInts(scores, true));

Notes:
- `mutable` is passed straight through: false = scripts may read but not store.
- Map keys are String by convention (MewMap extends AbstractMap<String, MewVariable>);
  the generic overloads allow other key classes, but Mewtype looks keys up by their
  stringified name, so non-String keys only work if the rule converts from strings.
- listAny/mapAny use anyRule() for genuinely heterogeneous backings â€” this trades
  away the honest element-type checking the rule system exists for. Prefer the
  typed factories whenever the backing is homogeneous.
- inject() binds into the environment's TOPMOST variable frame. Call it at top
  level; injected mid-block (b29+), the binding disappears when the block's frame
  pops. For always-available globals, return them from injectSpecialVariables().
*/
final class MewBridge {
private final MewCoercion coercion = new MewCoercion();
private final java.util.HashMap<Class<?>, MewElementRule> ruleCache = new java.util.HashMap<>();

/** Coercion rule for elements of a specific class; cached (rules are stateless). */
public MewElementRule rule(final Class<?> elementType) {
	MewElementRule cached = ruleCache.get(elementType);
	if (cached == null) {
		cached = coercion.of(elementType);
		ruleCache.put(elementType, cached);
	}
	return cached;
}

/** Escape hatches, for composing with the explicit-rule overloads below. */
public MewElementRule anyRule()  { return coercion.anyRule(); }
public MewElementRule stringRule() { return coercion.stringRule(); }
public MewElementRule noneRule() { return coercion.noneRule(); }

// ---------------- raw wrappers (when you need the MewList/MewMap object) ----------------

public <T> MewExternalList<T> externalList(java.util.List<T> backing, boolean mutable, Class<T> elementType) {
	return new MewExternalList<T>(backing, mutable, elementType, rule(elementType));
}

public <T> MewExternalList<T> externalList(java.util.List<T> backing, boolean mutable, Class<T> elementType, MewElementRule elementRule) {
	return new MewExternalList<T>(backing, mutable, elementType, elementRule);
}

public MewExternalList<Object> externalListAny(java.util.List<?> backing, boolean mutable) {
	@SuppressWarnings("unchecked")
	java.util.List<Object> b = (java.util.List<Object>) backing;
	return new MewExternalList<Object>(b, mutable, Object.class, coercion.anyRule());
}

public <V> MewExternalMap<String, V> externalMap(java.util.AbstractMap<String, V> backing, boolean mutable, Class<V> valueType) {
	return new MewExternalMap<String, V>(backing, mutable, String.class, valueType, coercion.stringRule(), rule(valueType));
}

public <V> MewExternalMap<String, V> externalMap(java.util.AbstractMap<String, V> backing, boolean mutable, Class<V> valueType, MewElementRule valueRule) {
	return new MewExternalMap<String, V>(backing, mutable, String.class, valueType, coercion.stringRule(), valueRule);
}

public MewExternalMap<String, Object> externalMapAny(java.util.AbstractMap<String, ?> backing, boolean mutable) {
	@SuppressWarnings("unchecked")
	java.util.AbstractMap<String, Object> b = (java.util.AbstractMap<String, Object>) backing;
	return new MewExternalMap<String, Object>(b, mutable, String.class, Object.class, coercion.stringRule(), coercion.anyRule());
}

// ---------------- ready-to-inject MewVariables ----------------

public <T> MewVariable list(java.util.List<T> backing, boolean mutable, Class<T> elementType) {
	return new MewVariable(MewVariableType.List, externalList(backing, mutable, elementType));
}

public <T> MewVariable list(java.util.List<T> backing, boolean mutable, Class<T> elementType, MewElementRule elementRule) {
	return new MewVariable(MewVariableType.List, externalList(backing, mutable, elementType, elementRule));
}

public MewVariable listAny(java.util.List<?> backing, boolean mutable) {
	return new MewVariable(MewVariableType.List, externalListAny(backing, mutable));
}

public MewVariable listOfStrings(java.util.List<String> backing, boolean mutable)  { return list(backing, mutable, String.class); }
public MewVariable listOfInts(java.util.List<Integer> backing, boolean mutable)    { return list(backing, mutable, Integer.class); }
public MewVariable listOfDoubles(java.util.List<Double> backing, boolean mutable)  { return list(backing, mutable, Double.class); }
public MewVariable listOfBools(java.util.List<Boolean> backing, boolean mutable)   { return list(backing, mutable, Boolean.class); }

public <V> MewVariable map(java.util.AbstractMap<String, V> backing, boolean mutable, Class<V> valueType) {
	return new MewVariable(MewVariableType.Map, externalMap(backing, mutable, valueType));
}

public <V> MewVariable map(java.util.AbstractMap<String, V> backing, boolean mutable, Class<V> valueType, MewElementRule valueRule) {
	return new MewVariable(MewVariableType.Map, externalMap(backing, mutable, valueType, valueRule));
}

public MewVariable mapAny(java.util.AbstractMap<String, ?> backing, boolean mutable) {
	return new MewVariable(MewVariableType.Map, externalMapAny(backing, mutable));
}

public MewVariable mapOfStrings(java.util.AbstractMap<String, String> backing, boolean mutable)  { return map(backing, mutable, String.class); }
public MewVariable mapOfInts(java.util.AbstractMap<String, Integer> backing, boolean mutable)    { return map(backing, mutable, Integer.class); }
public MewVariable mapOfDoubles(java.util.AbstractMap<String, Double> backing, boolean mutable)  { return map(backing, mutable, Double.class); }
public MewVariable mapOfBools(java.util.AbstractMap<String, Boolean> backing, boolean mutable)   { return map(backing, mutable, Boolean.class); }

// ---------------- injection shortcut ----------------

/** Bind into the environment's topmost variable frame (call at top level; see header notes). */
public void inject(MewEnvironment environment, String name, MewVariable value) {
	environment.variables.put(name, value);
}
}

/**
 * Simple, fast, small JSON parser for Java 1.8
 * 
 * No class-level members - fully instance based.
 * Compatible with Java 1.8
 * 
 * Returns:
 *  - JSON object -> Map<String, Object>
 *  - JSON array  -> List<Object>
 *  - JSON string -> String
 *  - JSON number -> Integer, Long or Double
 *  - JSON true/false -> Boolean
 *  - JSON null -> null
 */
class RitsuJsonParser {

    private String input;
    private int length;
    private int position;

    public RitsuJsonParser() {
        this.input = "";
        this.length = 0;
        this.position = 0;
    }

    /**
     * Parse a JSON text.
     */
    public MewVariable parse(String json) {
        if (json == null) {
            throw new JsonParseException("Input is null");
        }
        this.input = json;
        this.length = json.length();
        this.position = 0;

        this.skipWhitespace();
        if (this.position >= this.length) {
            throw this.error("Empty input");
        }

        MewVariable result = this.parseValue();

        this.skipWhitespace();
        if (this.position != this.length) {
            throw this.error("Trailing characters after JSON value");
        }
        return result;
    }

    private MewVariable parseValue() {
        this.skipWhitespace();
        if (this.position >= this.length) {
            throw this.error("Unexpected end of input");
        }
        char c = this.input.charAt(this.position);
        if (c == '{') {
            return new MewVariable(MewVariableType.Map, this.parseObject());
        }
        if (c == '[') {
            return new MewVariable(MewVariableType.List, this.parseArray());
        }
        if (c == '"') {
            return new MewVariable(MewVariableType.String, this.parseString());
        }
        if (c == 't' || c == 'f' || c == 'n') {
            return this.parseLiteral();
        }
        if (c == '-' || (c >= '0' && c <= '9')) {
            return this.parseNumber();
        }
        throw this.error("Unexpected character '" + c + "'");
    }

    private MewInternalMap parseObject() {
        this.expect('{');
        this.skipWhitespace();

        MewInternalMap map = new MewInternalMap();

        // empty object {}
        if (this.position < this.length && this.input.charAt(this.position) == '}') {
            this.position++;
            return map;
        }

        while (true) {
            this.skipWhitespace();
            if (this.position >= this.length) {
                throw this.error("Unterminated object");
            }
            // key must be string
            if (this.input.charAt(this.position) != '"') {
                throw this.error("Expected string key");
            }
            String key = this.parseString();

            this.skipWhitespace();
            this.expect(':');

            MewVariable value = this.parseValue();
            map.put(key, value);

            this.skipWhitespace();
            if (this.position >= this.length) {
                throw this.error("Unterminated object");
            }
            char ch = this.input.charAt(this.position++);
            if (ch == '}') {
                break;
            }
            if (ch != ',') {
                throw this.error("Expected ',' or '}' in object");
            }
        }
        return map;
    }

    private MewInternalList parseArray() {
        this.expect('[');
        this.skipWhitespace();

        MewInternalList list = new MewInternalList();

        // empty array []
        if (this.position < this.length && this.input.charAt(this.position) == ']') {
            this.position++;
            return list;
        }

        while (true) {
            MewVariable value = this.parseValue();
            list.add(value);

            this.skipWhitespace();
            if (this.position >= this.length) {
                throw this.error("Unterminated array");
            }
            char ch = this.input.charAt(this.position++);
            if (ch == ']') {
                break;
            }
            if (ch != ',') {
                throw this.error("Expected ',' or ']' in array");
            }
            this.skipWhitespace();
        }
        return list;
    }

    private String parseString() {
        this.expect('"');
        StringBuilder sb = new StringBuilder();

        while (this.position < this.length) {
            char c = this.input.charAt(this.position++);
            if (c == '"') {
                return sb.toString();
            }
            if (c == '\\') {
                if (this.position >= this.length) {
                    throw this.error("Unterminated escape sequence");
                }
                char esc = this.input.charAt(this.position++);
                if (esc == '"') {
                    sb.append('"');
                } else if (esc == '\\') {
                    sb.append('\\');
                } else if (esc == '/') {
                    sb.append('/');
                } else if (esc == 'b') {
                    sb.append('\b');
                } else if (esc == 'f') {
                    sb.append('\f');
                } else if (esc == 'n') {
                    sb.append('\n');
                } else if (esc == 'r') {
                    sb.append('\r');
                } else if (esc == 't') {
                    sb.append('\t');
                } else if (esc == 'u') {
                    if (this.position + 4 > this.length) {
                        throw this.error("Invalid unicode escape");
                    }
                    String hex = this.input.substring(this.position, this.position + 4);
                    int code;
                    try {
                        code = Integer.parseInt(hex, 16);
                    } catch (NumberFormatException e) {
                        throw this.error("Invalid unicode escape: \\u" + hex);
                    }
                    sb.append((char) code);
                    this.position += 4;
                } else {
                    throw this.error("Invalid escape character: \\" + esc);
                }
            } else {
                // JSON disallows unescaped control chars
                if (c < 0x20) {
                    throw this.error("Unescaped control character in string");
                }
                sb.append(c);
            }
        }
        throw this.error("Unterminated string");
    }

    private MewVariable parseNumber() {
        int start = this.position;

        if (this.position < this.length && this.input.charAt(this.position) == '-') {
            this.position++;
        }

        if (this.position >= this.length) {
            throw this.error("Invalid number");
        }

        char first = this.input.charAt(this.position);
        if (first == '0') {
            this.position++;
        } else if (first >= '1' && first <= '9') {
            this.position++;
            while (this.position < this.length && this.isDigit(this.input.charAt(this.position))) {
                this.position++;
            }
        } else {
            throw this.error("Invalid number");
        }

        boolean isDouble = false;

        // fraction
        if (this.position < this.length && this.input.charAt(this.position) == '.') {
            isDouble = true;
            this.position++;
            if (this.position >= this.length || !this.isDigit(this.input.charAt(this.position))) {
                throw this.error("Invalid number, missing digits after decimal point");
            }
            while (this.position < this.length && this.isDigit(this.input.charAt(this.position))) {
                this.position++;
            }
        }

        // exponent
        if (this.position < this.length) {
            char e = this.input.charAt(this.position);
            if (e == 'e' || e == 'E') {
                isDouble = true;
                this.position++;
                if (this.position < this.length) {
                    char sign = this.input.charAt(this.position);
                    if (sign == '+' || sign == '-') {
                        this.position++;
                    }
                }
                if (this.position >= this.length || !this.isDigit(this.input.charAt(this.position))) {
                    throw this.error("Invalid number, missing exponent digits");
                }
                while (this.position < this.length && this.isDigit(this.input.charAt(this.position))) {
                    this.position++;
                }
            }
        }

        String numStr = this.input.substring(start, this.position);
        try {
            if (isDouble) {
                double d = Double.parseDouble(numStr);
                return new MewVariable(MewVariableType.Float, d);
            } else {
                long l = Long.parseLong(numStr);
                if (l >= Integer.MIN_VALUE && l <= Integer.MAX_VALUE) {
                    return new MewVariable(MewVariableType.Integer, (int) l);
                }
                return new MewVariable(MewVariableType.Float, (double)l);
            }
        } catch (NumberFormatException ex) {
            throw this.error("Invalid number format: " + numStr);
        }
    }

    private MewVariable parseLiteral() {
        if (this.input.startsWith("true", this.position)) {
            this.position += 4;
            return new MewVariable(MewVariableType.Bool, Boolean.TRUE);
        }
        if (this.input.startsWith("false", this.position)) {
            this.position += 5;
            return new MewVariable(MewVariableType.Bool, Boolean.FALSE);
        }
        if (this.input.startsWith("null", this.position)) {
            this.position += 4;
            return new MewVariable(MewVariableType.Null, "Ritsu-chan!!!!!");
        }
        throw this.error("Invalid literal");
    }

    private void skipWhitespace() {
        while (this.position < this.length) {
            char c = this.input.charAt(this.position);
            if (c == ' ' || c == '\t' || c == '\n' || c == '\r') {
                this.position++;
            } else {
                break;
            }
        }
    }

    private void expect(char expected) {
        if (this.position >= this.length) {
            throw this.error("Expected '" + expected + "' but reached end of input");
        }
        char c = this.input.charAt(this.position++);
        if (c != expected) {
            throw this.error("Expected '" + expected + "' but got '" + c + "'");
        }
    }

    private boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    private JsonParseException error(String message) {
        int errPos = this.position;
        int start = errPos - 20;
        if (start < 0) {
            start = 0;
        }
        int end = errPos + 20;
        if (end > this.length) {
            end = this.length;
        }
        String context = "";
        if (this.length > 0) {
            context = this.input.substring(start, end).replace("\n", "\\n");
        }
        return new JsonParseException(message + " at position " + errPos + " near '" + context + "'");
    }
}

class JsonParseException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	public JsonParseException() { super(); }
	public JsonParseException(String msg) { super(msg); }
}