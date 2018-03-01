<%-- 
    Document   : OneQuestion
    Created on : Feb 15, 2018, 11:32:47 AM
    Author     : David Klecker
--%>

<!DOCTYPE html>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import = "java.sql.*" %> 
<%@ page import="javax.servlet.http.HttpServletRequest" %>
<%@ page import = "Project1.OnLineQuiz" %>
<jsp:useBean id = "pOnLineQuiz" class = "Project1.OnLineQuiz" scope = "session" ></jsp:useBean>
<jsp:setProperty name = "pOnLineQuiz" property = "*" /> 

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Online Quiz</title>
        <style>
            #colorForKeyword{
                color:green;
            }

            .codeForQuestion{
                font-family:"Courier New", Courier, Verdana, sans-serif;
                font-size:18px;
                padding-left:20px;
            }

            .question{
                font-size:18px;
                font-family:"Times New Roman";
            }

            #sentenceColor{
                color:blue;
            }

            #integerColor{
                color:teal;
            }
            
            .mainblock{
                display:block;
                width:100%;
                text-align:center;
            }
            
            .main{
                display: inline-block;
                border:1px solid orange; 
                text-align:left;
            }
            
            .form{
                display: inline-block;
            }
            
            .title{
                background-color:#6193cb;
                color:white;
                font-weight:bold;
                font-family:arial;
                padding:3px; 
                text-align:center;
            }
            
            .mainquestionarea{
                background-color:white;
                padding:10px 15px;
            }
            
            .button{
                margin-bottom: 0px; 
                margin-top: 10px; 
                margin-left: 5px;
                border: 1px solid black; 
                font-family: Helvetica, monospace; 
                font-size: .9em;
                background-color: lightgreen;
                border-radius: 5px; 
                color:black;
            }
            #comment{
                color:maroon;
            }
            
            #explanation{
                color:green;
                font-family:arial,sans-serif;
            }
           .CorrectAnswer{
                color:green;
                font-family:arial,sans-serif;
            }
            
            .InCorrectAnswer{
                color:red;
                font-family:arial,sans-serif;
            } 
                       
        </style>
    </head>
    <body>
        
    <% 
        pOnLineQuiz.processData(request);
    %>
    
    <div class="mainblock">
        <div class="main">
            <form method="post" action="GradeOneQuestion.jsp" name="quiz" class="form">
                <div class="title">
                    Multiple-Choice Question <%= pOnLineQuiz.getTitle()%>
                </div>
                <div class="mainquestionarea">
                    <div>
                        <%= pOnLineQuiz.getQuestion() %>
                    </div>
                    <p>
                    <div>
                    <% 
                        for(int i=0; i<pOnLineQuiz.getChoices().length; i++){ 
                            if(!pOnLineQuiz.getChoices()[i].isEmpty() && pOnLineQuiz.getAnswerKey().length()>1){ 
                    %>    
                                <input class="checkboxButtons" type="checkbox" name="choices" value="choice<%=pOnLineQuiz.getLetter(i)%>" /> <%=pOnLineQuiz.getLetter(i)%>. 
                                &nbsp;&nbsp;<%= pOnLineQuiz.getChoices()[i] %><br>
                    <%      }
                            else if(!pOnLineQuiz.getChoices()[i].isEmpty() && pOnLineQuiz.getAnswerKey().length() == 1){
                    %>   
                                <input class="radioButtons" type="radio" name="choices" value="choice<%=pOnLineQuiz.getLetter(i)%>" /> <%=pOnLineQuiz.getLetter(i)%>. 
                                &nbsp;&nbsp;<%= pOnLineQuiz.getChoices()[i] %><br>
                    <%     }
                        }
                    %>
                    </div>
                <input name="buttonName" class="button" type="submit" value="Check My Answer">

                </div>
            </form>
        </div>
    </div>
    </body>
</html>
