//huehue
package syntaxnazi;

import java.io.*;
import java.util.*;

/**
 *
 * @author Munic
 */
public class SyntaxNazi {
    static ArrayList<String> cases = new ArrayList<String>();    
    static ArrayList<String> vars = new ArrayList<String>();   
    static ArrayList<String> answers = new ArrayList<String>();
    static List<String> dataType = Arrays.asList("int", "float", "double",  "char");
    static List<String> retType = Arrays.asList("int", "float", "double",  "char", "void");
    static String[] tokens;
    static String[] tempTokens;
    static String str = "";
    static String[] testCase;
    static int stage = 1;
    static int state = 0;
    static int f = 0;
    static int ffinal = 0;
    static String ret = "";
    
    public static void main(String[] args) throws IOException {   
        Scanner name = new Scanner(System.in);
        String inname = new String();
        String outname = new String();
        
        System.out.println("Please enter input file name: ");
        inname = name.nextLine();
        System.out.println("Please enter output file name: ");
        outname = name.nextLine();
        
        System.out.println("Open " + outname + " to view results");
        
        File file = new File(outname);
        file.createNewFile();
        
        FileWriter writer = new FileWriter(file, true);
        BufferedWriter out = new BufferedWriter(writer);
        
        String s = new String();
        String str = new String();
        
       
        try{
            FileInputStream fstream = new FileInputStream(inname);// Open the file that is the first command line parameter
            DataInputStream in = new DataInputStream(fstream);//Get the object of DataInputStream
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine = "";

            while ((strLine = br.readLine()) != null) {//Read File Line By Line
                s = s + strLine + " ";  
                
            }          
        in.close();//Close the input stream
        }catch (Exception e){//Catch exception if any
                System.err.println("Error: " + e.getMessage());
        }//end of try-catch statement
        
        properSpace(s);//calls the function to put proper spaces in the string
        
        for(int i = 0; i <answers.size(); i++){
            String answer = answers.get(i);
            out.write(answer.toString()); 
            out.newLine();
        }
    
        out.flush();
        out.close();
}
//this function adds proper space before a semicolon
public static void properSpace(String s){
    int init = 0, semi = 0;
    for(int i = 0; i < s.length(); i++){
        char c = s.charAt(i);
        if(c == '='){
            str = str + s.substring(init, i )+ " = ";
            init = i + 1;
        }
        else if (c == ','){
            str = str + s.substring(init, i )+ " , ";
            init = i+1;
        }
        else if (c == '['){
            str = str + s.substring(init, i )+ " [ ";
            init = i+1;
        }
        else if (c == ']'){
            str = str + s.substring(init, i )+ " ] ";
            init = i+1;
        }
        else if(c == ';'){
            str = str + s.substring(init, i) + " ";
            init = i;
            semi = i;
        } 
        else if(c == '{'){
            str = str + s.substring(init, i) + " { ";
            init = i + 1;
        }
        else if(c == '}'){
            str = str + s.substring(init, i) + " } ";
            init = i + 2;
        }
        else if(c == '('){
            str = str + s.substring(init, i) + " ( ";
            init = i + 1;
        }
        else if(c == ')'){
            str = str + s.substring(init, i) + " ) ";
            init = i + 1;
        }
        else if(c == 39){        
            str = str + s.substring(init, i) + " ' ";
            init = i + 1;
        }
    }
    if(init < s.length() && s.charAt(s.length()-2)!='{'){
        
        str = str + s.substring(semi, s.length()-1);
    }
    if(s.charAt(s.length()-1)==';'){
        str = str + ";";
    }
    if(!(str.charAt(str.length()-1)==';' || str.charAt(str.length()-1)=='{' || str.charAt(str.length()-2)=='}')){

        str = str + " /";
    }
//    if(s.charAt(s.length()-1)=='}'){
//        str = str + "}";
//    }
//    System.out.println(str);
    split(str);
}
//this function splits per white space
public static void split (String s){
        tokens = str.split("\\s+");
//        for(int j = 0; j < tokens.length; j++){
//            System.out.print(tokens[j]+"/");
//        }
        slice();
}
//this function will divide the string by test case
public static void slice(){
    int init = 0;
    String s = "";
    for(int i = 0; i < tokens.length; i++){
        if(tokens[i].equals("(")){
            
            stage = 2;
        }
        else if(tokens[i].equals("{") && stage == 2){
            
            stage = 3;
            f = 1;
            ffinal = 1;
        }
        else if(tokens[i].equals("}") && stage == 3){            
            f = 3;
        }
        for(int j = init; j < i; j++){
            init = i;
            s = s + tokens[j] + " ";
            if(tokens[i].equals(";")){
                s = s + ";";
                split2(s);   
                s = "";
                init = init + 1; 
                if(stage!=3){
                stage  = 1;
                }
            }
            else if(tokens[i].equals(";") && init != i-1){//check if valid test case
                
            
                split2(s);
                s = "";
                init = i;                
                stage  = 1;
            }
            else if(tokens[i].equals("{") && init != i-1 && stage == 3){//check if valid test case
                s = s +"{";
                split2(s);
                s = "";
                init = i+1;                
                stage  = 3;
            }
            else if((dataType.contains(tokens[i])) && (!tokens[i-1].equals(";")) && (!tokens[i-1].equals("(")) && stage!=2  ){ 
                
                split2(s);                 
                s = "";
                init = i;                
                stage  = 1;
            }
            else if(tokens[i].equals("/")){
                
                split2(s); 
                s = "";
                init = i;                
                stage  = 1;
            }       
            else if(tokens[i].equals("}")){
                stage = 3 ;
                f = 3;
                s = s + "}";
                split2(s); 
                s = ""; 
                init = init + 1;
            }
        }
        state = 0;

        }
    if(tokens[tokens.length-1].equals("}") ){
        stage = 3 ;
        f = 3;
        split2("}"); 
        s = "";        
    }
    
}
//this tokenizes the test case string
public static void split2(String s){
//    System.out.println(s);
    testCase = s.split("\\s+"); 
    if(f == 1 && stage == 3){
        checkFunction(testCase);
    }
    else if ((f==0 || f==3 || f ==2 )&& stage == 3){
        checkState(testCase);//calls checkState
    }
    else if (f==0 && stage != 3){
        checkState(testCase);//calls checkState
    }
}
//checks the state of the string
public static int checkState(String[] testCase){
    getVars(testCase);
    String s = new String();
    for(int i = 0; i < testCase.length; i++){
        s = testCase[i];
       
        switch(state){
            case (0):{
                if(checkDataType(s)){
                    state = 1;
                }
                else if(s.equals("return")&& (!ret.equals("void"))){
                    state = 16;
                }
                else if(stage == 2 && s.equals(")")){
                    state = 4;
                }
                else if(s.equals("}") && stage == 3 && f == 3){
                    state = 6;
                }
                else {
                    f = 4 ;
                    state = 15;
                }
                break;
            }
            case (1):{
                if(checkVar(s) && (stage == 2|| stage == 3)){
                    state = 2;
                }
                else if(checkVar(s)){
                    state = 2;
                }
                else if(stage == 2 && s.equals(")")){
                    state = 4;
                }
                else if(stage ==2 && s.equals(",")){
                    state = 0;
                }
                else state = 15;
                break;
            }
            case (2):{
                if(s.equals("=")){
                    state = 3;
                }
                else if(s.equals(";")){
                    state = 6;
                }
                else if(s.equals(",") && stage == 2){
                    state = 0;
                }
                else if(s.equals(",")){
                    state = 5;
                }
                else if(s.equals("[")){
                    state = 7;
                }
                else if(s.equals("(")){
                    state = 0;
                    stage = 2;
                }
                else if(s.equals(")")){                    
                    stage = 2;
                    state = 4;
                }
                else state = 15;
                break;
            }
            case (3):{
                if(checkNumOrVar(s)){
                    state = 4;
                }
                else state = 15;
                break;
            }
            case (4):{
                if(s.equals(",")){
                    state = 5;
                }
                else if(s.equals(";")){
                    state = 6;
                }
                else if(s.equals("{") && stage == 2){
                    state = 6;
                    stage = 3;
                }
                else state = 15;
                break;
            }
            case (5):{
                if(checkVar(s)){
                    state = 2;
                }
                
                else state = 15;
                break;
            }
            case (6):{
                break;
            }
            case(7):{
                if(checkNum(s)){
                   state = 8;
                }
                else if(s.equals("]")){
                    state = 9;
                }
                else state = 15;
                break;
            }
            case(8):{
                 if(s.equals("]")){
                    state = 9;
                }                 
                else state = 15;
                 break;
            }
            case(9):{
                 if(s.equals("=")){
                    state = 10;
                }
                 else if(s.equals(";")){
                     state = 6;
                 }
                 else state = 15;
                 break;
            }
            case(10):{
                if(s.equals("{")){
                    state = 11;
                }
                else state = 14;
                 break;
            }
            case(11):{
                if(s.equals("'")){
                    state = 14;
                }
                else if(checkNum(s)){
                    state = 12;
                }
                else state = 15;
                 break;
            }
            case(12):{
                 if(s.equals("}")){
                    state = 13;
                }
                 else if(s.equals(",")){
                     state = 11;
                 }
                else state = 15;
                 break;
            }
            case(13):{
                 if(s.equals(";")){
                    state = 6;
                }
                 else if(s.equals(",")){
                     state = 1;
                 }
                 else if(checkNumOrVar(s)){
                     state = 13;
                 }
                else {
                     state = 15;
                 }
                 break;
            }
            case(14):{
                if(s.equals("}")){
                    state = 13;
                }
                else if(s.equals(",")){
                    state = 11;
                }                
                else if(checkNumOrChar(s)){
                    state = 11;
                }
                else state = 15;
                break;
            }
            case(15):{
                break;
            }
            case(16):{
                if(checkRet(s)){
                    state = 13;
                }
                else
                    state = 15;
                
                break;
            }
            default:{
                
            }   
        }        
//            System.out.println(state);
    }
    if(f!=1){
    vars.clear();
    }
    if(stage == 3 && state != 6){
        ffinal = 0;
    }
    if ((f == 3 && stage ==3) || stage!=3){
        evaluateAns(state);
    }
    
    if(state != 6 && stage == 3){
        ffinal = 0;
    }
//    System.out.println(state);
    return state;
}
//this function checks the first line if function definition
public static boolean checkFunction(String[] TestCase){
    ffinal = 1;
    getVars(testCase);
    int state = 0;
    String s = new String();
    for(int i = 0; i < testCase.length; i++){
        s = testCase[i];
        switch(state){
            case(0):{
                if(retType.contains(s)){
                    ret = s;
                    state  =1;
                }     
                else state = 5;
                break;
            }
            case(1):{
                if(checkVar(s)){
                    state = 2;
                }
                else state = 5;
                break;
            }
            case(2):{
                if(s.equals("(")){
                    state = 3;
                }
                else if(s.equals(",")){
                    state = 3;
                }
                else if(s.equals(")")){
                    state = 4;
                }
                else state = 5;
                break;
            }
            case(3):{
                if(dataType.contains(s)){
                    state = 1;
                }
                else state = 5;
                break;
            }
            case(4):{
                if(s.equals("{")){
                    state = 6;
                }
                else state = 5;
                break;
            }
            case(5):{
                break;
            }
            case(6):{
                break;
            }
        }
        
//            System.out.println("state " +state);
    }
    if( f != 1){
    vars.clear();
    }
    f = 2;
    if(state == 6){
        ffinal = 1;
        return true;
    }
    else if(state!=6){
        ffinal = 0;
        return false;
    }
    return false;
}
//this function gets the variable declared
public static void getVars(String[] testCase){
    if(testCase.length!=1 && (!testCase[1].equals(";"))){
        vars.add(testCase[1]);
    }
    for(int i = 2; i < testCase.length; i++){
        if(testCase[i].equals("=") && i < 2){
            vars.add(testCase[i]);
        }
        else if(testCase[i].equals(",") && f != 1){
            vars.add(testCase[i+1]);        
        }
        else if(testCase[i].equals(",") && f == 1){
            vars.add(testCase[i-1]);        
        }
        else if(testCase[i].equals(")")){
            vars.add(testCase[i-1]);
        }
    }
//    for(int i =0 ;i <vars.size(); i++){
//        System.out.print(vars.get(i)+" /");
//    }
}
//this function evaluates if the string is a valid dataType
public static boolean checkDataType(String s){
    if(dataType.contains(s)){
        return true;
    }
    else if(retType.contains(s)){
        stage = 2;
        return true;
    }
    return false;
}
//this function evaluates if the string is a valid variable name
public static boolean checkVar(String s){
    int count = 0;
    List<Character> data = Arrays.asList('_', '@');
    if(dataType.contains(s) || retType.contains(s)){
        return false;
    }
    for (int i = 0; i<s.length(); i++){
        char c = s.charAt(i);
        if(!((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'B') || (data.contains(c) == true) || ((c >= '0' && c <= '9') && i != 0))){
            return false;        
        }
    }
    for(int i = 0; i < vars.size(); i++){
        if(s.equals(vars.get(i))){
            count++;
        }
    }
    if(count > 1){
        return false;
    }
    
    return true;
}
//this function evaluates correct number or variable initiation
public static boolean checkNumOrVar(String s){
    char c;
    for(int i = 0; i < s.length(); i++){
        c = s.charAt(i);
        if(!((c >= '0' && c <= '9') || vars.contains(s) == true)){
            return false;
        }
    }
    return true;
}
//this function checks if vadlid numberor character
public static boolean checkNumOrChar(String s){
    if(s.length() == 1){
        if((s.charAt(0) >= 'a' && s.charAt(0) >= 'z') || (s.charAt(0) >= 'A' && s.charAt(0) >= 'Z')){
            return true;
        }
    }
    else if (s.length() > 1){
        for(int i = 0; i < s.length(); i++){
            if(!(s.charAt(i)>='0' && s.charAt(i)<='9')){
                return false;
            }
        }
    }
    return true;
}
//this function checks if valid number
public static boolean checkNum(String s){
    for(int i = 0; i < s.length();i++){
        if(!(s.charAt(i)>='0' && s.charAt(i)<='9'))
            return false;
    }
    return true;
}
//this function checks return type
public static boolean checkRet(String s){
    if(s.equals(";") && ret.equals("void")){
        return true;
    }
    else if(!(s.equals("void"))){
        if(checkNumOrVar(s)){
            return true;
        }
        ffinal = 0;
        return false;
    }
    ffinal = 0;
    return false;
}
//this function evaluates the answer
public static void evaluateAns(int state){
        if(stage == 1 ){
            if(state == 6){
                answers.add("Valid Variable declaration.");
            }
            else if(state<=15){
                answers.add("Invalid Variable declaration.");
            }
            
    stage = 0;
    state = 0;
        }
        else if(stage == 2){
            if(state == 6){
                answers.add("Valid function declaration.");
            }
            else{
                answers.add("Invalid function declaration.");
            }
            
    stage = 0;
    state = 0;
        }
    
        else if(stage == 3){
        if(state == 6){
            answers.add("Valid function definition.");
        }
        else if( ffinal == 0 && state != 6){
            answers.add("Invalid function definition.");
        }
        
    stage = 0;
    state = 0;
    }
        
    stage = 0;
    state = 0;
//    f = 0;
//    ffinal = 0;
}

public static void checkFunc(String[] testCase){
    for(int i = 0; i <testCase.length; i++){
        System.out.print(testCase[i]);
    }
    String s = testCase[0];    
    int count = 0;
    if(!(s.equals("void"))){
        for(int i = 0; i<testCase.length; i++){
            if(testCase[i].equals("return")){
                count = 1;
            }
        }
    }
    System.out.println(count);
    if(((s.equals("void")) == false) && count == 0){
        System.out.println("what");
        state = 15;
    }
}
}
