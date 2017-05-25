package com.airbnb.airpal;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Slf4j
class AirpalServlet extends HttpServlet {
  static Logger loger= LoggerFactory.getLogger(AirpalServlet.class);
  String[] inputarray = new String[3];
  @Override
  protected void service(HttpServletRequest servletRequest,
    HttpServletResponse servletResponse) throws ServletException,
    IOException {
    
    loger.info("Inside AirpalServlet ======>");
    String projectID =  (String) servletRequest.getAttribute("projectID");
    inputarray = projectID.split("_");
    String projectid1=inputarray[0];
     loger.info("Inside AirpalServlet projectid1    ======>"+projectid1);
    String email=inputarray[1];
    loger.info("Inside AirpalServlet email    ======>"+email);

    
  }
  
}


