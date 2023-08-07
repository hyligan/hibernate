package com.goit.dev10.servlets;

import com.goit.dev10.HibernateUtil;
import com.goit.dev10.entities.Person;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.FileTemplateResolver;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@WebServlet("/person")
public class PersonServlet extends HttpServlet {
  private TemplateEngine engine;

  @Override
  public void init() throws ServletException {
    engine = new TemplateEngine();

    FileTemplateResolver resolver = new FileTemplateResolver();
    resolver.setPrefix(getClass().getClassLoader().getResource("templates").getPath());
    resolver.setSuffix(".html");
    resolver.setTemplateMode("HTML5");
    resolver.setOrder(engine.getTemplateResolvers().size());
    resolver.setCacheable(false);
    engine.addTemplateResolver(resolver);
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    try(Session session = HibernateUtil.getInstance().getSessionFactory().openSession()){
      List<Person> allPersons = findAllPersons(session);
      Map<String, Object> respMap = new LinkedHashMap<>();
      respMap.put("persons", allPersons);
      engine.process("person", new Context(req.getLocale(), respMap), resp.getWriter());
    }catch (Exception e){
      log(e.getMessage(),e);
    }
    resp.getWriter().close();
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    resp.setContentType("application/json; charset=utf-8");
    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
    Person person =
        gson.fromJson(
            req.getReader()
                .lines().collect(Collectors.joining(System.lineSeparator()
                ))
            , Person.class);
    try(Session session = HibernateUtil.getInstance().getSessionFactory().openSession()){
      Transaction transaction = session.beginTransaction();
      session.persist(person);
      transaction.commit();
    }catch (Exception e){
      log(e.getMessage(),e);
    }
    doGet(req,resp);
  }

  private List<Person> findAllPersons(Session session) {
    return session.createQuery("SELECT a FROM Person a", Person.class).getResultList();
  }
}
