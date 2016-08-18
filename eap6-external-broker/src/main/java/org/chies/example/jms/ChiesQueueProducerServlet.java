package org.chies.example.jms;

import java.io.IOException;
import java.io.PrintWriter;

import javax.annotation.Resource;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/producer")
public class ChiesQueueProducerServlet extends HttpServlet {

    private static final long serialVersionUID = -8314035702649252239L;

    @Resource(mappedName = "java:/ConnectionFactory")
    private ConnectionFactory connectionFactory;

    @Resource(lookup = "java:/queue/ChiesQueue")
    private Queue queue;


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
        Connection connection = null;
        String message = (req.getParameter("message") != null) ? req.getParameter("message") : "sample message"; 
        try {
            connection = connectionFactory.createConnection();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            MessageProducer messageProducer = session.createProducer(queue);
            connection.start();
            messageProducer.send(session.createTextMessage(message));
            connection.close();
            out.write("<p><i>Message '" + message +"' enviada com sucesso</i></p>");
            out.write("<p><i>Go to your A-MQ console and look for enqueue and dequeue statistics</i></p>");
            out.write("<p><i>Go to EAP log and check the received messages from MDB</i></p>");
        } catch (Exception e) {
        	throw new RuntimeException(e);
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
