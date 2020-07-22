package app;

import java.io.*;
import java.util.*;
import java.util.regex.*;

import structures.Stack;

public class Expression{

	public static String delims = " \t*+-/()[]";
			
    /**
     * Populates the vars list with simple variables, and arrays lists with arrays
     * in the expr. For every variable (simple or array), a SINGLE instance is created 
     * and stored, even if it appears more than once in the expr.
     * At this time, values for all variables and all array items are set to
     * zero - they will be loaded from a file in the loadVariableValues method.
     * 
     * @param expr The expr
     * @param vars The variables array list - already created by the caller
     * @param arrays The arrays array list - already created by the caller
     */
    public static void
    makeVariableLists(String expr, ArrayList<Variable> vars,ArrayList<Array> arrays) {
    	
        String str1 = "";


        for(int i = 0; i < expr.length(); i++){
                if(Character.isLetter(expr.charAt(i))){
                                str1 += expr.charAt(i);
                } else {
                        if(expr.charAt(i) == '['){
                                Array arrSym = new Array(str1);
                                if(!arrays.contains(arrSym))
                                        arrays.add(arrSym);
                        }

                        else{
                                Variable scalarSym = new Variable(str1);
                                if( !vars.contains(scalarSym) && !str1.equals("")){
                                        vars.add(scalarSym);
                                }

                        }
                        str1 = "";
                }


        }

        if(!str1.equals("")){
                Variable scalarSym = new Variable(str1);
                        if( !vars.contains(scalarSym)){
                                vars.add(scalarSym);
                        }

        }



    }
    /**
     * Loads values for variables and arrays in the expr
     * 
     * @param sc Scanner for values input
     * @throws IOException If there is a problem with the input 
     * @param vars The variables array list, previously populated by makeVariableLists
     * @param arrays The arrays array list - previously populated by makeVariableLists
     */
    public static void 
    loadVariableValues(Scanner sc, ArrayList<Variable> vars, ArrayList<Array> arrays) 
    throws IOException {
        while (sc.hasNextLine()) {
            StringTokenizer st = new StringTokenizer(sc.nextLine().trim());
            int numTokens = st.countTokens();
            String tok = st.nextToken();
            Variable var = new Variable(tok);
            Array arr = new Array(tok);
            int vari = vars.indexOf(var);
            int arri = arrays.indexOf(arr);
            if (vari == -1 && arri == -1) {
            	continue;
            }
            int num = Integer.parseInt(st.nextToken());
            if (numTokens == 2) { // scalar symbol
                vars.get(vari).value = num;
            } else { // array symbol
            	arr = arrays.get(arri);
            	arr.values = new int[num];
                // following are (index,val) pairs
                while (st.hasMoreTokens()) {
                    tok = st.nextToken();
                    StringTokenizer stt = new StringTokenizer(tok," (,)");
                    int index = Integer.parseInt(stt.nextToken());
                    int val = Integer.parseInt(stt.nextToken());
                    arr.values[index] = val;              
                }
            }
        }
    }
    
    /**
     * Evaluates the expr.
     * 
     * @param vars The variables array list, with values for all variables in the expr
     * @param arrays The arrays array list, with values for all array items
     * @return Result of evaluation
     */ 
   
    
    private static String varValue (String expr1, ArrayList<Variable> vari) {
    	for (int i = 0 ; i < vari.size(); i++) {
    expr1 = expr1.replace(vari.get(i).name, "" + vari.get(i).value);
    		}
    			return expr1;
    }
    
    private static Array arrayValue(String name, ArrayList<Array> arrays) {
		for (int i = 0; i < arrays.size(); i++) {
			if (arrays.get(i).name.equals(name)){
				return arrays.get(i);
			}
		}
		return null;
	}
        
    private static String evaluate (String expression, ArrayList<Array> hi) {
    	if (expression == null || expression.length() == 0) {
    		return null;
    		}
    		if (expression.indexOf('[') == -1) {
    			
    			if (expression.indexOf('(') == -1) {
    			expression = expression.replace(" ", "");
    			
    			expression = " " + expression;
    				
    				boolean isNeg = false;
    				float num;
    				float num2;
    				int index;
    				int index2;

    				for (int i = 0; i < expression.length(); i++) {
    						if (i == 1 && expression.charAt(i) == '-'){
    						isNeg = true;
    						} else if (expression.charAt(i) == '*' || expression.charAt(i) == '/') {
    						index = index2 = i;
    				while (index > 0 && (Character.isDigit(expression.charAt(index - 1)) || expression.charAt(index - 1) == '.')) {
    							index--;
    						}
    				while (index2 < expression.length() - 1 && (Character.isDigit(expression.charAt(index2 + 1)) || expression.charAt(index2 + 1) == '.')) {
    							index2++;
    						}
    						try {
    							num = Float.parseFloat(expression.substring(index, i));
    						} catch (Exception e){
    							num = 0;
    						}
    						boolean secondNumZero = true;
    						try {
    							num2 = Float.parseFloat(expression.substring(i + 1, index2 + 1));
    						} catch (Exception e) {
    							num2 = 0;
    							secondNumZero = false;
    						}
    						String result = "";
    					if (isNeg)
    							num = -1 * num;
    					if (expression.charAt(i) == '*') {
    							result = "" + num * num2;
    						} 
    					else {
    					
    					if (num2 == 0 && secondNumZero)
    								throw new IllegalArgumentException("null");
    						else if (num2 == 0 && !secondNumZero)
    								num2 = 1;
    							result = "" + num / num2;
    						}
    				int qwerty = 0;
    							if (isNeg)
    							qwerty = 2;
    						expression = expression.substring(qwerty, index) + result + expression.substring(index2+ 1);
    						
    						i = 0;
    						isNeg = false;
    					}
    				}
    				isNeg = false;
    					for (int i = 0; i < expression.length(); i++) {
    				
    				if (i == 1 && expression.charAt(i) == '-') {
    						isNeg = true;
    					} 
    						else if (expression.charAt(i) == '+' || expression.charAt(i) == '-') {
    						index = index2 = i;
    						
    					while (index > 0 && (Character.isDigit(expression.charAt(index - 1)) || expression.charAt(index - 1) == '.')) {
    							index--;
    						}
    						
    						while (index2 < expression.length() - 1 && (Character.isDigit(expression.charAt(index2 + 1)) || expression.charAt(index2 + 1) == '.')) {
    							index2++;
    						}
    						
    							try {
    							num = Float.parseFloat(expression.substring(index, i));
    								} catch (Exception e) {
    							num = 0;
    						}
    						
    							try {
    							num2 = Float.parseFloat(expression.substring(i + 1, index2 + 1));
    								} catch (Exception e) {
    							num2 = 0;
    						}
    						
    					
    					String result = "";
    							if (isNeg){
    							num = -1 * num;  }
    						
    							if ( expression.charAt(i) == '+') {
    							result = "" + (num + num2); }

    						else {
    							result = "" + (num - num2); }
    						
    							int stadex = 0;
    						if (isNeg)
    							stadex = 2;
    				expression = expression.substring(stadex, index) + result + expression.substring(index2+1);
    						
    						i = 0;
    						isNeg = false;
    					}
    				}
    				expression = expression.replace(" ", "");
    				return expression;
    				} else {
    					for (int index = 0; index < expression.length(); index++){
    						if (expression.charAt(index) == '(' || expression.charAt(index) == '['){
    						
    								int bIndex = index;
    								int pp = 1;
    								index++;
    								int eIndex = index;
    						
    						for (; eIndex < expression.length(); eIndex++)  {
    							if (expression.charAt(eIndex) == '(')
    								pp++;
    							
    							if (expression.charAt(eIndex) == ')')
    								pp--;
    							
    							if (pp == 0 && expression.charAt(eIndex) == ')')
    								break;
    						}
    					
    					String ptr1 = expression.substring(0, bIndex);
    					String ptr2 = evaluate(expression.substring(index, eIndex), hi);
    					String ptr3 = expression.substring(eIndex + 1);
    					
    				return evaluate(ptr1 + ptr2 + ptr3, hi);
    					
    					}
    				}
    			}
    		} 
    				else {
    					
    					for (int index = 0; index < expression.length(); index++){
    					
    						if (Character.isLetter(expression.charAt(index)))   {
    						
    						String currentSym = "";
    						boolean pop = false;
    						int beginIndex = index;
    						while (index < expression.length() && Character.isLetter(expression.charAt(index))){
    						currentSym += expression.charAt(index);
    						index++;
    					
    					}
    						if (index < expression.length() && expression.charAt(index) == '[')
    						pop = true;
    					
    						if (pop == true) {
    						
    						int push = 1;
    						index++;
    						int endIndex = index;
    						
    							for (; endIndex < expression.length(); endIndex++)   {
    								if (expression.charAt(endIndex) == '['){
    								push++;
    						}
    							if ( expression.charAt(endIndex) == ']')  {
    								push--;
    						}

    							if (push == 0 && expression.charAt(endIndex) == ']')    {
    								break;
    						}
    					}
    					String bArray = expression.substring (0, beginIndex);
    					String eArray = expression.substring (endIndex+1);
    					String end = evaluate (expression.substring(index, endIndex), hi);
    					String evalulateArray = "" + arrayValue(currentSym, hi).values[(int)Float.parseFloat(end)];
    					return evaluate (bArray + evalulateArray + eArray, hi); 
    					}
    				}
    			}
    		}
    		return "";
    	}
    	

    
    
    
    public static float evaluate(String expr, ArrayList<Variable> vars, ArrayList<Array> arrays) {
    	
    			String expression = expr;
    			expression =varValue(expr, vars);
    			expression = evaluate(expression, arrays);
    				try {
    				return Float.parseFloat(expression);
    				} 	catch (Exception e) {
    				return 0;
    			}
    		}
}





