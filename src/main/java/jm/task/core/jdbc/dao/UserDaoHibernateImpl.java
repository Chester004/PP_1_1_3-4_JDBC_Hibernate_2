package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;
public class UserDaoHibernateImpl implements UserDao {
    SessionFactory sessionFactory = Util.getSessionFactory();

    public UserDaoHibernateImpl() {

    }

    @Override
    public void createUsersTable() {
        String createTableSQL = "CREATE TABLE users("
                + "id BIGINT(5) NOT NULL AUTO_INCREMENT, "
                + "name VARCHAR(20) NOT NULL, "
                + "lastname VARCHAR(20) NOT NULL, "
                + "age TINYINT(3) NOT NULL, " + "PRIMARY KEY (ID) "
                + ")";
        Session session = sessionFactory.openSession();
        try {
            session.beginTransaction();
            session.createSQLQuery(createTableSQL).executeUpdate();
            session.getTransaction().commit();
        } catch (PersistenceException e) {
            System.err.println("Ошибка создания таблицы");
        } catch (Exception e) {
            session.getTransaction().rollback();
        }
    }

    @Override
    public void dropUsersTable() {
        String dropSQL = "DROP TABLE `users`";
        Session session = sessionFactory.openSession();
        try {
            session.beginTransaction();
            session.createSQLQuery(dropSQL).executeUpdate();
            session.getTransaction().commit();
        } catch (PersistenceException e) {
            System.err.println("Таблица уже удалена");
        } catch (Exception e) {
            session.getTransaction().rollback();
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        Session session = sessionFactory.openSession();
        try {
            session.beginTransaction();
            session.save(new User(name, lastName, age));
            session.getTransaction().commit();
            System.out.printf("User с именем – %s добавлен в базу данных \n", name);
        } catch (Exception e) {
            session.getTransaction().rollback();
        }
    }

    @Override
    public void removeUserById(long id) {
        Session session = sessionFactory.openSession();
        try {
            session.beginTransaction();
            String hql = "DELETE User WHERE id = :ID";
            Query query = session.createQuery(hql);
            query.setParameter("ID", id);
            query.executeUpdate();
            session.getTransaction().commit();
        } catch (EntityNotFoundException e) {
            System.err.printf("Id %d нет в таблице \n", id);
        } catch (Exception e) {
            session.getTransaction().rollback();
        }
    }

    @Override
    public List<User> getAllUsers() {
        try {
            Session session = sessionFactory.openSession();
            String hql = "FROM User";
            Query query = session.createQuery(hql);
            List<User> users = query.getResultList();
            return users;
        } catch (Exception e) {
            System.err.println("Невозможно получить всех Юзеров");
        }
        return new ArrayList<User>();
    }

    @Override
    public void cleanUsersTable() {
        Session session = sessionFactory.openSession();
        try {
            session.beginTransaction();
            String hql = "DELETE User ";
            Query query = session.createQuery(hql);
            query.executeUpdate();
            session.getTransaction().commit();
        } catch (EntityNotFoundException e) {
            System.err.println("Таблица пуста");
        } catch (Exception e) {
            session.getTransaction().rollback();
        }
    }
}