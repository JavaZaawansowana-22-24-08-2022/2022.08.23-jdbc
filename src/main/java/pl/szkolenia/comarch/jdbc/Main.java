package pl.szkolenia.comarch.jdbc;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static Connection connection;

    public static void main(String[] args) {
        connect();

        User user = new User(1, "mateusz", "mateusz1234", "Janusz", "Kowalski");
        //persistUser(user);
        //updateUser(user);
        //deleteUser(2);

        //User userFromDB = getUserById(2);
        //System.out.println(userFromDB);

        System.out.println(getAllUsers());
        disconnect();
    }

    public static void connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Main.connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/test1", "root", "");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static void persistUser(User user) {
        try {
            String sql = "INSERT INTO tuser (login, password, name, surname) VALUES (?,?,?,?)";

            PreparedStatement statement = Main.connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, user.getLogin());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getName());
            statement.setString(4, user.getSurname());

            statement.executeUpdate();

            ResultSet resultSet = statement.getGeneratedKeys();
            resultSet.next();
            user.setId(resultSet.getInt(1));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void updateUser(User user) {
        try {
            String sql = "UPDATE tuser SET login = ?, password = ?, name = ?, surname = ? WHERE id = ?";

            PreparedStatement preparedStatement = Main.connection.prepareStatement(sql);
            preparedStatement.setString(1, user.getLogin());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getName());
            preparedStatement.setString(4, user.getSurname());
            preparedStatement.setInt(5, user.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static User getUserById(int id) {
        try {
            String sql = "SELECT * FROM tuser WHERE id = ?";

            PreparedStatement preparedStatement = Main.connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);

            ResultSet rs = preparedStatement.executeQuery();

            if(rs.next()) {
                return new User(
                        rs.getInt("id"),
                        rs.getString("login"),
                        rs.getString("password"),
                        rs.getString("name"),
                        rs.getString("surname")
                );
            }

            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        try {
            String sql = "SELECT * FROM tuser";

            PreparedStatement preparedStatement = Main.connection.prepareStatement(sql);

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                users.add(new User(
                        rs.getInt("id"),
                        rs.getString("login"),
                        rs.getString("password"),
                        rs.getString("name"),
                        rs.getString("surname")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return users;
    }

    public static void deleteUser(int id) {
        try {
            String sql = "DELETE FROM tuser WHERE id = ?";

            PreparedStatement preparedStatement = Main.connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void disconnect() {
        try {
            Main.connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
