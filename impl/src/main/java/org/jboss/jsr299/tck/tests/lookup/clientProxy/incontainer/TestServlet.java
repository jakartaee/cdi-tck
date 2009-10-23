package org.jboss.jsr299.tck.tests.lookup.clientProxy.incontainer;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TestServlet extends HttpServlet
{
   private static final long serialVersionUID = -4722487503814381947L;
   @Inject
   private Car car;
   @Inject
   private Garage garage;

   @Override
   protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
   {
      if (car.getMake().equals("unknown"))
      {
         // set the make of the car
         car.setMake(req.getParameter("make"));
         // make sure that the garage contains the current instance
         resp.getWriter().append(garage.getMakeOfTheParkedCar());
         resp.setContentType("text/plain");
         resp.setStatus(200);
      }
      else
      {
         resp.setStatus(500);
      }
   }
}
