package com.goit.dev10;

import com.goit.dev10.entities.Person;
import lombok.Getter;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
  private static final HibernateUtil INSTANCE;

  @Getter
  private SessionFactory sessionFactory;

  static {
    INSTANCE = new HibernateUtil();
  }

  private HibernateUtil() {
    sessionFactory = new Configuration()
        .addAnnotatedClass(Person.class)
        .buildSessionFactory();
  }

  public static HibernateUtil getInstance() {
    return INSTANCE;
  }

  public void close() {
    sessionFactory.close();
  }
}