<%-- 
    Document   : GradeOneQuestion
    Created on : Feb 15, 2018, 5:41:20 PM
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
        <title>JSP Page</title>
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
            
            #main{
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
                width:100px;
                text-align:center;
                padding:3px;
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
            <script>
                function showExplanation(Text){
                    var width = document.getElementById("main").offsetWidth;
                    width -= 50;
                    document.getElementById("explanation").innerHTML = "Explanation: " + Text;
                    document.getElementById("explanation").style.color = "blueviolet";
                    document.getElementById("explanation").style.width = width+"px";
                }
                
                function showCorrectAnswer(Text){
                    var width = document.getElementById("main").offsetWidth;
                    width -= 50;
                    document.getElementById("explanation").innerHTML = "The correct answer is " + Text;
                    document.getElementById("explanation").style.width = width+"px";
                }
             </script>
    </head>
    <body>
    <%       
        pOnLineQuiz.GradeQuestion(request);
    %>

    <div class="mainblock">
    <div id="main">
        <form method="get">
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
                            String checkboxName = "choice" + pOnLineQuiz.getLetter(i);
                            String selected = pOnLineQuiz.wasSelected(request, checkboxName);
                %>    
                            <input type="checkbox" name="<%=checkboxName%>" value="<%=checkboxName%>" <%= selected %>/> <%=pOnLineQuiz.getLetter(i)%>. 
                            &nbsp;&nbsp;<%= pOnLineQuiz.getChoices()[i] %><br>
                <%      }
                        else if(!pOnLineQuiz.getChoices()[i].isEmpty() && pOnLineQuiz.getAnswerKey().length() == 1){
                            String checkboxName = "choice" + pOnLineQuiz.getLetter(i);
                            String selected = pOnLineQuiz.wasSelected(request, checkboxName);
                %>   
                            <input type="radio" name="choice" value="<%=checkboxName%>" <%= selected %>/> <%=pOnLineQuiz.getLetter(i)%>. 
                            &nbsp;&nbsp;<%= pOnLineQuiz.getChoices()[i] %><br>
                <%     }
                    }
                %>
                </div>
                <%  if(pOnLineQuiz.getCorrect()){ %>
                        <div class="CorrectAnswer">Your Answer is correct <img src="correct.jpg"> </div>
                        <% if(!pOnLineQuiz.getHint().isEmpty()){ %>
                            <div id="explanation" onClick="showExplanation('<%= pOnLineQuiz.getHint() %>')"> Click here to show an explanation</div>
                        <% } 
                    } else {%>
                        <div class="InCorrectAnswer">Your Answer <%= pOnLineQuiz.getAnswersGiven()%> is incorrect <img src="wrong.jpg"> </div>
                        <% if(!pOnLineQuiz.getHint().isEmpty()){ %>
                            <div id="explanation" onClick="showExplanation('<%= pOnLineQuiz.getHint() %>')"> Click here to show the correct answer and an explanation</div>
                        <% } else {%>
                            <div id="explanation" onClick="showCorrectAnswer('<%= pOnLineQuiz.getCorrectAnswer() %>')"> Click here to show the correct answer</div>
                        <% }
                   }%>
                   
                   <div class="button">Get Statistics</div>
    
            </div>
        </form>
    </div>
    </div>
    </body>
</html>
