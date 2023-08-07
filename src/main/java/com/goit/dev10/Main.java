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
      for (Person person : findAllPersons(session)) {
        transaction.rollback();
        System.out.println(person);
      }

    }catch (Exception e){
      e.printStackTrace();
    }
  }

  private  static  List<Person> findAllPersons(Session session) {
    return session.createQuery("SELECT a FROM Person a", Person.class).getResultList();
  }
}