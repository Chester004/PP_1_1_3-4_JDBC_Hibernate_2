package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    public UserDaoJDBCImpl() {

    }

    public void createUsersTable() {

        String createTableSQL = "CREATE TABLE users("
                + "ID INT(5) NOT NULL AUTO_INCREMENT, "
                + "NAME VARCHAR(20) NOT NULL, "
                + "LASTNAME VARCHAR(20) NOT NULL, "
                + "AGE INT(3) NOT NULL, " + "PRIMARY KEY (ID) "
                + ")";
        try(Statement statement = Util.getConnection().createStatement();) {
            statement.execute(createTableSQL);
        } catch (SQLSyntaxErrorException e) {
            System.err.println("Таблица уже создана");
        } catch (SQLException e ){
            throw new RuntimeException(e);
        }
    }

    private void executeUpdate(String sql, String okMessage) {
        try (Statement statement = Util.getConnection().createStatement()) {
            int res = statement.executeUpdate(sql);
            if (res > 0) {
                System.out.println(okMessage);
            }
        } catch (SQLSyntaxErrorException e) {
            System.out.println("Не получилось создать/удалить таблицу");
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void dropUsersTable() {
        String sql = "DROP TABLE `users`";
        executeUpdate(sql,"Таблица удалена");
    }

    public void saveUser(String name, String lastName, byte age) {
        String sql = String.format("Insert into users(name,lastname,age) VALUES ('%s','%s',%d)", name, lastName, age);
        executeUpdate(sql,String.format("User с именем %s успешно добавлен в базу данных",name));
    }

    public void removeUserById(long id) {
        String sql = String.format("DELETE FROM users WHERE id = %d",id);
        executeUpdate(sql,String.format("User с %d успешно удален \n",id));
    }

    public List<User> getAllUsers() {
        try(Statement statement = Util.getConnection().createStatement();) {
            ResultSet res = statement.executeQuery("SELECT * FROM users");
            List<User> users = new ArrayList<>();
            while (res.next()){
                users.add(new User(res.getString(2),res.getString(3),res.getByte(4)));
            }
            return users;
        } catch (SQLException e ){
            throw new RuntimeException(e);
        }
    }

    public void cleanUsersTable() {
        String sql = "DELETE FROM users";
        executeUpdate(sql, "БД успешно очищена");
    }

}
