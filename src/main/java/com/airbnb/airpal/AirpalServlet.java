/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.airbnb.airpal;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author osboxes
 */
@Slf4j
class AirpalServlet extends HttpServlet {
  static Logger loger= LoggerFactory.getLogger(AirpalServlet.class);
  String[] inputarray = new String[3];
   static String projectID;
   static String projectid1;
   static String email;
  
  @Override
  protected void service(HttpServletRequest servletRequest,
    HttpServletResponse servletResponse) throws ServletException,
    IOException {
    
    loger.info("Inside AirpalServlet ======>");
    projectID =  (String) servletRequest.getAttribute("projectID");
    inputarray = projectID.split("_");
    projectid1=inputarray[0];
     loger.info("Inside AirpalServlet projectid1    ======>"+projectid1);
    email=inputarray[1];
    loger.info("Inside AirpalServlet email    ======>"+email);

    
  }
  String getProjectId()
  {
    return projectid1;
  }
  String getemail()
  {
    return email;
  }
  
}

