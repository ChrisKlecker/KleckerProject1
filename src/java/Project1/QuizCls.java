/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Project1;

import java.util.ArrayList;

/**
 *
 * @author David Klecker
 */
public class QuizCls {
    
    private String m_ChapterNo;
    private String m_SectionNo;
    private String m_QuestionNo;
    private String m_Question;
    private ArrayList<String> m_Choices;
    private ArrayList<String> m_CodeLines;
    private ArrayList<Character> m_AnswerKey;
    private String m_Hint;
    
    public String getChapterNo(){return m_ChapterNo;}
    public void setChapterNo(String val){m_ChapterNo = val;}
    public String getSectionNo(){return m_SectionNo;}
    public void setSectionNo(String val){m_SectionNo = val;}
    public String getQuestionNo(){return m_QuestionNo;}
    public void setQuestionNo(String val){m_QuestionNo = val;}
    public String getQuestion(){return m_Question;}
    public void setQuestion(String val){m_Question = val;}
    public String getHint(){return m_Hint;}
    public void setHint(String val){m_Hint = val;}
    public ArrayList<String> getChoices(){return m_Choices;}
    public void setChoices(ArrayList<String> val){m_Choices = val;}
    public ArrayList<Character> getAnswerKey(){return m_AnswerKey;}
    public void setAnswerKey(ArrayList<Character> val){m_AnswerKey = val;}
    public ArrayList<String> getCodeLines(){return m_CodeLines;}
    public void setCodeLines(ArrayList<String> val){m_CodeLines = val;}
    
    public QuizCls(){
        
        m_Choices   = new ArrayList<String>();
        m_AnswerKey = new ArrayList<Character>();
        m_CodeLines = new ArrayList<String>();        
    }
    
    public QuizCls(String ChapterNumber, String SectionNumber){
        m_ChapterNo = ChapterNumber;
        m_SectionNo = SectionNumber;
        m_Choices   = new ArrayList<String>();
        m_AnswerKey = new ArrayList<Character>();
        m_CodeLines = new ArrayList<String>();
    }
    
    public void AddAnswerToList(String Answer){
        m_Choices.add(Answer);
    }
    
    public void AddKeyToList(Character Key){
        m_AnswerKey.add(Key);
    }
    
    public void AddQuestionContent(String Content){
        m_CodeLines.add(Content);
    }
    
}
