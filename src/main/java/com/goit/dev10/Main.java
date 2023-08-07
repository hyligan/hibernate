package com.goit.dev10;

import com.goit.dev10.entities.Person;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class Main {
  public static void main(String[] args) {
    try(Session session = HibernateUtil.getInstance().getSessionFactory().openSession()){
      Transaction transaction = session.beginTransaction();
      session.persist(new Person("test"));
      transaction.commit();
      for (Person person : findAllStudentsWithJpql(session)) {
        transaction.rollback();
        System.out.println(person);
      }

    }catch (Exception e){
      e.printStackTrace();
    }
  }

  public static List<Person> findAllStudentsWithJpql(Session session) {
    return session.createQuery("SELECT a FROM Person a", Person.class).getResultList();
  }
}