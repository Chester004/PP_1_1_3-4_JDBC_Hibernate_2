package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

public class UserDaoHibernateImpl implements UserDao {
    public UserDaoHibernateImpl() {

    }


    @Override
    public void createUsersTable() {
        String createTableSQL = "CREATE TABLE users("
                + "ID INT(5) NOT NULL AUTO_INCREMENT, "
                + "NAME VARCHAR(20) NOT NULL, "
                + "LASTNAME VARCHAR(20) NOT NULL, "
                + "AGE INT(3) NOT NULL, " + "PRIMARY KEY (ID) "
                + ")";
        try(Session session = Util.getHib()){
            session.beginTransaction();
            session.createSQLQuery(createTableSQL).executeUpdate();
            session.getTransaction().commit();
        } catch (PersistenceException e) {
            System.err.println("Ошибка создания таблицы");
        }
    }

    @Override
    public void dropUsersTable() {
        String dropSQL = "DROP TABLE `users`";
        try(Session session = Util.getHib()){
        session.beginTransaction();
        session.createSQLQuery(dropSQL).executeUpdate();
        session.getTransaction().commit();
        }catch (PersistenceException e){
            System.err.println("Таблица уже удалена");
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        Session session = Util.getHib();
        session.beginTransaction();
        session.save(new User(name,lastName,age));
        session.getTransaction().commit();
        session.close();
    }

    @Override
    public void removeUserById(long id) {
        try(Session session = Util.getHib();) {
            session.beginTransaction();
            String hql = "DELETE User WHERE id = :ID";
            Query query = session.createQuery(hql);
            query.setParameter("ID",id);
            query.executeUpdate();
        }catch (EntityNotFoundException e){
            System.err.printf("Id %d нет в таблице \n",id);
        }
    }

    @Override
    public List<User> getAllUsers() {
        Session session = Util.getHib();

        String hql = "FROM User";
        Query query = session.createQuery(hql);
        List<User> users = query.getResultList();
        return  users;
    }
    @Override
    public void cleanUsersTable() {
        try(Session session = Util.getHib();) {
            session.beginTransaction();
            String hql = "DELETE User ";
            Query query = session.createQuery(hql);
            query.executeUpdate();
        }catch (EntityNotFoundException e){
            System.err.println("Таблица пуста");
        }
    }
}
