/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Project1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author David Klecker
 */
public class OnLineQuiz {
    
    private PreparedStatement pstmtInsert;
    private PreparedStatement pstmtCreate;
    private PreparedStatement pstmtQuery;
    private PreparedStatement pstmtQueryQuiz;
    private PreparedStatement pstmtSubmitQuiz;
    
    private ArrayList<QuizCls> pQuizClsList;
    private String insertString;
    private String queryString;
    private String queryQuizString;
    private String submitString;
    private String fileString;
    private String chapterNo;
    private String questionNo;
    private String title;
    private String question;
    private String[] choices;
    private String answerKey;
    private String hint;
    private boolean Correct;
    private String AnswersGiven;
    
    private String[] Letters = {"A", "B", "C", "D", "E"};
    
    private String[] KeyWords = {"abstract", "continue" , "for", "new", "switch", "assert", "default", "goto", "package", "synchronized", 
                                "boolean", "do", "if", "private", "this", "break", "double", "implements", "protected", "throw",
                                "byte", "else", "import", "public", "throws", "case", "enum", "instanceof", "return", "transient",
                                "catch", "extends", "int", "short", "try", "char", "final", "interface", "static", "void",
                                "class", "finally", "long", "strictfp", "volatile", "const", "float", "native", "super", "while"};
    
    private String KeyWordsRegExp = "(\\babstract\\b)|(\\bcontinue\\b)|(\\bfor\\b)|(\\bnew\\b)|(\\bswitch\\b)|(\\bassert\\b)|(\\bdefault\\b)|(\\bgoto\\b)|(\\bpackage\\b)|(\\bsynchronized\\b)|(\\bboolean\\b)|(\\bdo\\b)|(\\bif\\b)|(\\bprivate\\b)|(\\bthis\\b)|(\\bbreak\\b)|(\\bdouble\\b)|(\\bimplements\\b)|(\\bprotected\\b)|(\\bthrow\\b) |(\\bbyte\\b)|(\\belse\\b)|(\\bimport\\b)|(\\bpublic\\b)|(\\bthrows\\b)|(\\bcase\\b)|(\\benum\\b)|(\\binstanceof\\b)|(\\breturn\\b)|(\\btransient\\b)|(\\bcatch\\b)|(\\bextends\\b)|(\\bint\\b)|(\\bshort\\b)|(\\btry\\b)|(\\bchar\\b)|(\\bfinal\\b)|(\\binterface\\b)|(\\bstatic\\b)|(\\bvoid\\b) |(\\bclass\\b)|(\\bfinally\\b)|(\\blong\\b)|(\\bstrictfp\\b)|(\\bvolatile\\b)|(\\bconst\\b)|(\\bfloat\\b)|(\\bnative\\b)|(\\bsuper\\b)|(\\bwhile\\b)";
    
    HashMap<String,String> KeyWordHashMap = new HashMap<String,String>(); 

    public boolean getCorrect() {return Correct;}
    public void setCorrect(boolean val) {Correct = val;}
    public String getChapterNo() {return chapterNo;}
    public void setChapterNo(String chapterNo) { this.chapterNo = chapterNo;}
    public String getQuestionNo() { return questionNo; }
    public void setQuestionNo(String questionNo) { this.questionNo = questionNo;}
    public String getTitle() { return title;}
    public void setTitle(String title) { this.title = title;}
    public String getQuestion() { return question;}
    public void setQuestion(String question) { this.question = question;}       
    public String getFileString(){return fileString;}
    public void setFileString(String val) {fileString = val;}
    public String getAnswerKey() {return answerKey;}
    public void setAnswerKey(String val) { this.answerKey = val;}
    public String getHint() {return hint;}
    public void setHint(String val) { this.hint = val;}
    public String[] getChoices() {return choices;}
    public void setChoices(String[] val) { this.choices = val;}
    public String getAnswersGiven() {return AnswersGiven;}
    public void setAnswerGiven(String val){AnswersGiven = val;}
    
    public String getLetter(int index){ 
        if(index < Letters.length){
            return Letters[index];
        }
        return "";
    }
    public OnLineQuiz() throws SQLException{
        
        insertString    = "";
        queryString     = "";
        queryQuizString = "";
        submitString    = "";
        fileString      = "";
        chapterNo       = "";
        questionNo      = "";
        title           = "";
        question        = "";
        choices         = new String[5];
        answerKey       = "";
        hint            = ""; 
        
        initializeJdbc();
}

    private void initializeJdbc() throws SQLException {
        
            Connection conn = null;
            try {

            // Load the JDBC driver
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("Driver loaded");

            // Establish a connection
            conn = DriverManager.getConnection("jdbc:mysql://35.185.94.191/klecker" , "klecker", "tiger");
            //Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/javabook" , "root", "m0nkwork");
            System.out.println("Database connected");
            
            insertString    = "insert into intro11equiz (chapterNo, questionNo, question, choiceA, choiceB, choiceC, choiceD, choiceE, answerKey, hint) values (?,?,?,?,?,?,?,?,?,?);";
            queryString     = "select * from intro11equiz ;";
            queryQuizString = "select chapterNo, questionNo, question, choiceA, choiceB, choiceC, choiceD, choiceE, answerKey, hint from intro11equiz where chapterNo = ? and questionNo = ?;";
            submitString    = "insert into intro11e (chapterNo, questionNo, isCorrect, time, hostname, answerA, answerB, answerC, answerD, answerE, username) values (?, ?,?,?,?,?,?,?,?,?,?)";
            

            pstmtInsert     = conn.prepareStatement(insertString);
            pstmtQuery      = conn.prepareStatement(queryString);
            pstmtQueryQuiz  = conn.prepareStatement(queryQuizString);
            pstmtSubmitQuiz = conn.prepareStatement(submitString);

            ResultSet set = pstmtQuery.executeQuery();
            if(!set.next()){

                BuildTablesForDatabase(conn);
            }

        }
        catch (Exception ex) {
                
            if(ex.getMessage().contentEquals("Table \'klecker.intro11equiz\' doesn\'t exist") && conn != null){
                BuildTablesForDatabase(conn);
            }
            else
                ex.printStackTrace();
        }
    }     
    
    public void CollectDataFromFlatFiles(String datafile) throws FileNotFoundException, IOException{
            
        File f = new File(datafile);
        File[] fList = f.listFiles(); 
        int QuestionNumber = 1;
        for (File file : fList){
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line = "";
            QuizCls pNewQuiz = new QuizCls();
            boolean CodeWriteUp = false;
            
            while ((line = br.readLine()) != null) {

                ArrayList<String> tokenList = new ArrayList<>();
                StringTokenizer tokenedLine = new StringTokenizer(line);
                while (tokenedLine.hasMoreTokens()) {
                    tokenList.add(tokenedLine.nextToken());
                }
                if(tokenList.size() > 0){
                    CodeWriteUp         = false;
                    String firstWord    = tokenList.get(0);

                    if(firstWord.contentEquals("Chapter")){
                        pNewQuiz.setChapterNo(tokenList.get(1));
                        QuestionNumber = 1;
                    }
                    else if(firstWord.contentEquals("Section")){
                        String Section = tokenList.get(1);
                        if(Section.contains(".")){
                            pNewQuiz.setSectionNo(Section.split("\\.")[1]);
                        }
                    }
                    else if(firstWord.contentEquals("#")){
                        
                        String HTMLStr = CreateHTMLForQuestion(pNewQuiz.getQuestion(), pNewQuiz.getCodeLines());
                        pNewQuiz.setQuestion(HTMLStr);
                        pNewQuiz.setQuestionNo(Integer.toString(QuestionNumber));
                        QuestionNumber++;
                        pQuizClsList.add(pNewQuiz);
                        String Chapter = pNewQuiz.getChapterNo();
                        String Section = pNewQuiz.getSectionNo();
                        pNewQuiz = new QuizCls(Chapter, Section);
                    }
                    else if(firstWord.matches("[0-9]+.")){

                        String questionStr = "";
                        for(int i=1; i<tokenList.size(); i++){
                            questionStr += tokenList.get(i) + " ";
                        }
                        pNewQuiz.setQuestion(questionStr);
                    }
                    else if(firstWord.matches("[a-f].")){
                        String answerStr = "";
                        for(int i=1; i<tokenList.size(); i++){
                            answerStr += tokenList.get(i) + " ";
                        }
                        pNewQuiz.AddAnswerToList(answerStr);
                    }
                    else if(firstWord.matches("[Kk]ey[:]*[\\ ]*[A-Fa-f]+")){
                         String answers = firstWord.split(":")[1];
                         for(int i=0;i<answers.length(); i++){
                             pNewQuiz.AddKeyToList(answers.charAt(i));
                         }
                         
                         String Hint = "";
                         for(int i=1; i<tokenList.size(); i++){
                             Hint += tokenList.get(i) + " ";
                         }
                         pNewQuiz.setHint(Hint);
                    }
                    else{
                        CodeWriteUp = true;
                        pNewQuiz.AddQuestionContent(line);
                    }
                }
                if(CodeWriteUp && tokenList.size() == 0){
                    pNewQuiz.AddQuestionContent("<p>");
                }
            } 
            
            pNewQuiz.setQuestionNo(Integer.toString(QuestionNumber));
            pQuizClsList.add(pNewQuiz);
            String Chapter = pNewQuiz.getChapterNo();
            String Section = pNewQuiz.getSectionNo();
            pNewQuiz = new QuizCls(Chapter, Section);
        }
    }
        
    public void BuildTablesForDatabase(Connection conn) throws SQLException{
            String createDBForQuiz = "create table if not exists intro11equiz ("+
                "chapterNo int(11),"+
                "questionNo int(11), "+
                "question text, "+
                "choiceA varchar(1000), "+
                "choiceB varchar(1000), "+
                "choiceC varchar(1000), "+
                "choiceD varchar(1000), "+
                "choiceE varchar(1000), "+
                "answerKey varchar(5), "+
                "hint text"+
                ");";

            String createDBForResults = "create table if not exists intro11e ("+
                "chapterNo int(11), "+
                "questionNo int(11), "+
                "isCorrect bit(1) default 0,"+
                "time timestamp default current_timestamp,"+
                "hostname varchar(100),"+
                "answerA bit(1) default 0,"+
                "answerB bit(1) default 0,"+
                "answerC bit(1) default 0,"+
                "answerD bit(1) default 0,"+
                "answerE bit(1) default 0,"+
                "username varchar(100)"+
                ");";

            // Create a Statement
            
            pstmtCreate = conn.prepareStatement(createDBForQuiz);
            pstmtCreate.executeUpdate();
            pstmtCreate = conn.prepareStatement(createDBForResults);
            pstmtCreate.executeUpdate();    
    }
    
    /**
     * Takes each object from QuizClsList and beings to insert each record into the database. 
     * @throws SQLException 
     */
    public void AddDataToDatabase() throws SQLException{
        int k = 1;
        
        for(int i=0; i<pQuizClsList.size();i++){
            k = 1;
            QuizCls pQuizCls = pQuizClsList.get(i);

            pstmtInsert.setString(k++, pQuizCls.getChapterNo());
            pstmtInsert.setString(k++, pQuizCls.getQuestionNo());
            pstmtInsert.setString(k++, pQuizCls.getQuestion());
            
            for(int j=0; j<5; j++){
                if(j >= pQuizCls.getChoices().size())
                    pstmtInsert.setString(k++, "");
                else
                    pstmtInsert.setString(k++, pQuizCls.getChoices().get(j));
            }
            
            String answerkey = "";
            for(int j=0; j<pQuizCls.getAnswerKey().size(); j++){
                answerkey += pQuizCls.getAnswerKey().get(j);
            }
            pstmtInsert.setString(k++, answerkey);
            pstmtInsert.setString(k++, pQuizCls.getHint());
            
            try{
                pstmtInsert.executeUpdate();
            }
            catch(SQLException ex){
                int run = 1;
                
            }
        }
    }
        
    public void processData(HttpServletRequest request) throws SQLException, IOException{
                
        ServletContext application  = request.getServletContext();
        String RealPath             = application.getRealPath("/");
        String dataFile             = RealPath + "selftest/selftest11e";
        
        ResultSet set = pstmtQuery.executeQuery();
        if(!set.next()){

            CreateKeyWordHashmap();
            pQuizClsList = new ArrayList<QuizCls>();
            CollectDataFromFlatFiles(dataFile);

            AddDataToDatabase();    
        }
        
        this.chapterNo = request.getParameter("chapterNo");
        this.questionNo = request.getParameter("questionNo");
        String Title = request.getParameter("title");
        
        /**
         * This method grabs the GET query string and processes the data so that it can be read by the JSP program. 
         */
        
        choices         = new String[5]; //These are the choices we have for answers
        
        if(chapterNo != null && questionNo != null && Title != null){
            pstmtQueryQuiz.setString(1, chapterNo);
            pstmtQueryQuiz.setString(2, questionNo);
            set = pstmtQueryQuiz.executeQuery();
            if(set.next()){
                chapterNo   = set.getString(1);
                questionNo  = set.getString(2);
                question    = set.getString(3);
                choices[0]  = set.getString(4);
                choices[1]  = set.getString(5);
                choices[2]  = set.getString(6);
                choices[3]  = set.getString(7);
                choices[4]  = set.getString(8);
                answerKey   = set.getString(9);    
                hint        = set.getString(10);
            }
        }
    }
    
    /**
     * I need to know what the user selected so that I can have those options selected in GradeOneQuestion.jsp. This is only used for
     * checkboxes since I can easily grab the radio value to select. For checkboxes it is different. I grab a HashMap returned from the HttpServletRequest
     * object. This checks for the key "choices" and places all values into a string array. I then parse the string array and check each option. If I find
     * that an option is returned, then it is selected. 
     */
    public String wasSelected(HttpServletRequest request, String name){
        Map <String, String[]> ChoicesMap = request.getParameterMap();

        if(ChoicesMap.containsKey("choices")){
            String[] selections = ChoicesMap.get("choices");
            for(int i=0; i<selections.length; i++){
                
                if(selections[i].contentEquals("choiceA") && name.contentEquals("choiceA")){
                    return "checked";
                }
                else if(selections[i].contentEquals("choiceB") && name.contentEquals("choiceB")){
                    return "checked";
                }
                else if(selections[i].contentEquals("choiceC") && name.contentEquals("choiceC")){
                    return "checked";
                }
                else if(selections[i].contentEquals("choiceD") && name.contentEquals("choiceD")){
                    return "checked";
                }
                else if(selections[i].contentEquals("choiceE") && name.contentEquals("choiceE")){
                    return "checked";
                }
            }
        }
        return "";
    }

    /**
     * Checks to see if the answer is correct
     * @param request
     * @throws SQLException 
     */
    public void GradeQuestion(HttpServletRequest request) throws SQLException, UnknownHostException{
        
        //Grade and correct the question. 
        Map <String, String[]> ChoicesMap = request.getParameterMap();
        
        choices = new String[5];
        
        //Query the information for the question
        pstmtQueryQuiz.setString(1, chapterNo);
        pstmtQueryQuiz.setString(2, questionNo);
        ResultSet set = pstmtQueryQuiz.executeQuery();
        if(set.next()){
            chapterNo   = set.getString(1);
            questionNo  = set.getString(2);
            question    = set.getString(3);
            choices[0]  = set.getString(4);
            choices[1]  = set.getString(5);
            choices[2]  = set.getString(6);
            choices[3]  = set.getString(7);
            choices[4]  = set.getString(8);
            answerKey   = set.getString(9).toUpperCase();    
            hint        = set.getString(10);
        }       
        
        
        AnswersGiven = "";
        if(ChoicesMap.containsKey("choices")){
        
            //Collect the user selected and store in a string array. For each selection we concat the answer into a string called AnswersGiven. 
            String[] selections = ChoicesMap.get("choices");
            for(int i=0; i<selections.length; i++){
                
                if(selections[i].contentEquals("choiceA")){
                    AnswersGiven = AnswersGiven.concat("A");
                }
                else if(selections[i].contentEquals("choiceB")){
                    AnswersGiven = AnswersGiven.concat("B");
                }
                else if(selections[i].contentEquals("choiceC")){
                    AnswersGiven = AnswersGiven.concat("C");
                }
                else if(selections[i].contentEquals("choiceD")){
                    AnswersGiven = AnswersGiven.concat("D");
                }
                else if(selections[i].contentEquals("choiceE")){
                    AnswersGiven = AnswersGiven.concat("E");
                }
            }
        }
        
        //We compare AnswersGiven with answerKey. They should be exactly the same for the answer to be correct. If not Correct is set to false
        Correct = (answerKey.contentEquals(AnswersGiven)) ? true: false;
        SubmitAnswersToDatabase(Correct, AnswersGiven);
    }
    
    /**
     * Creates hashmap for keywords to map to <span> elements so that I can easily replace keywords strings. 
     */
    public void CreateKeyWordHashmap(){
        for(int i=0; i<KeyWords.length; i++){
            String Encoded = "<span class=\"colorForKeyword\">" + KeyWords[i] + "</span>";
            KeyWordHashMap.put(KeyWords[i], Encoded);
        }
    }
    
    public String CreateHTMLForQuestion(String Question, ArrayList<String> CodeLines){
        String HTMLStr = "<div class=\"question\">"+Question+"</div>";
        
        String CodeLinesHTML = "";
        if(CodeLines.size()>0){
            CodeLinesHTML = "<p \\><div class=\"codeForQuestion\"><pre>";
        
            for(int i=0; i<CodeLines.size(); i++){
                CodeLinesHTML += ParseLineEncodeHTML(CodeLines.get(i));
                CodeLinesHTML += "<br>";
            }            
            
            CodeLinesHTML += "</pre>";
        }
            
        return HTMLStr + CodeLinesHTML;
    }
        
    public String ParseLineEncodeHTML(String CodeLine){
        
        String str = GetLeadingSpaces(CodeLine) + CodeLine;
       
        Pattern p = Pattern.compile("(// .+)");
        Matcher m = p.matcher(str);
        StringBuffer sb = new StringBuffer();
        boolean CommentFound = false;
        
        while (m.find()) {
            m.appendReplacement(sb, "<span id=\"comment\">$0</span>");
            CommentFound = true;
        }
        m.appendTail(sb);
        
        if(!CommentFound){

            String[] strArray = str.split("\"");
            sb = new StringBuffer();
            for(int i=0; i<strArray.length;i++){
                if(i%2 == 0){
                    p = Pattern.compile(KeyWordsRegExp);
                    m = p.matcher(strArray[i]);
                    while (m.find()) {

                        m.appendReplacement(sb, "<span id=\"colorForKeyword\">$0</span>");
                    }
                    m.appendTail(sb);

                    p = Pattern.compile("(?<!Line[ :;-]|[A-Za-z0-9.])[0-9.]+(?![A-Za-z0-9.:])");
                    m = p.matcher(sb.toString());
                    String z = sb.toString();
                    sb = new StringBuffer();
                    while (m.find()) {      
                        m.appendReplacement(sb, "<span id=\"integerColor\">$0</span>");
                    }
                    m.appendTail(sb);
                }
                else
                    sb.append("<span id=\"sentenceColor\">\""+strArray[i]+"\"</span>");
            }
        }
        
        return sb.toString();
    }
    
    public void SubmitAnswersToDatabase(boolean IsCorrect, String AnswerGiven) throws SQLException, UnknownHostException{
        
        pstmtSubmitQuiz.setString(1, chapterNo);
        pstmtSubmitQuiz.setString(2, questionNo);
        pstmtSubmitQuiz.setBoolean(3, IsCorrect);
        pstmtSubmitQuiz.setDate(4, new java.sql.Date(System.currentTimeMillis()));
        pstmtSubmitQuiz.setString(5, InetAddress.getLocalHost().getHostName());
        pstmtSubmitQuiz.setBoolean(6, false);
        pstmtSubmitQuiz.setBoolean(7, false);
        pstmtSubmitQuiz.setBoolean(8, false);
        pstmtSubmitQuiz.setBoolean(9, false);
        pstmtSubmitQuiz.setBoolean(10, false);
        pstmtSubmitQuiz.setString(11, "");

        for(int i=0; i<AnswerGiven.length(); i++){
            switch(AnswerGiven.charAt(i)){
                case 'A': pstmtSubmitQuiz.setBoolean(6, true); break;
                case 'B': pstmtSubmitQuiz.setBoolean(7, true); break;
                case 'C': pstmtSubmitQuiz.setBoolean(8, true); break;
                case 'D': pstmtSubmitQuiz.setBoolean(9, true); break;
                case 'E': pstmtSubmitQuiz.setBoolean(10, true); break;
            }
        }
        pstmtSubmitQuiz.executeUpdate(); 
    }
    
    public String getCorrectAnswer(){
        return this.answerKey.toUpperCase();
    }
    
    public static boolean isInteger(String s) {
        try{
            Integer.parseInt(s); 
        } catch(NumberFormatException e) { 
            return false; 
        } catch(NullPointerException e) {
            return false;
        }
        return true;
    }  
    
    public String GetLeadingSpaces(String line){
        String newStr = "";
        for(int i=0; i<line.length(); i++){
            if(line.charAt(i) == ' ') 
                newStr += " ";
            else 
                break;
        }
        return newStr;
    }    
}
