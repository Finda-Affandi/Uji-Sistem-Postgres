package com.ujisistempostgres.repository;

import com.ujisistempostgres.model.Tutorial;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

public class JdbcTutorialRepository implements TutorialRepository {

    private DataSource dataSource;

    public JdbcTutorialRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public int save(Tutorial tutorial) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "INSERT INTO tutorials (title, description, published) VALUES (?, ?, ?)",
                     Statement.RETURN_GENERATED_KEYS
             )) {
            statement.setString(1, tutorial.getTitle());
            statement.setString(2, tutorial.getDescription());
            statement.setBoolean(3, tutorial.isPublished());

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Creating tutorial failed, no rows affected.");
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    long id = generatedKeys.getLong(1);
                    tutorial.setId(id);
                    return rowsAffected;
                } else {
                    throw new SQLException("Creating tutorial failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int update(Tutorial tutorial) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "UPDATE tutorials SET title = ?, description = ?, published = ? WHERE id = ?"
             )) {
            statement.setString(1, tutorial.getTitle());
            statement.setString(2, tutorial.getDescription());
            statement.setBoolean(3, tutorial.isPublished());
            statement.setLong(4, tutorial.getId());

            return statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public Tutorial findById(Long id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT * FROM tutorials WHERE id = ?"
             )) {
            statement.setLong(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String title = resultSet.getString("title");
                    String description = resultSet.getString("description");
                    boolean published = resultSet.getBoolean("published");
                    return new Tutorial(id, title, description, published);
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public int deleteById(Long id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "DELETE FROM tutorials WHERE id = ?"
             )) {
            statement.setLong(1, id);

            return statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public List<Tutorial> findAll() {
        List<Tutorial> tutorials = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT * FROM tutorials"
             );
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                String title = resultSet.getString("title");
                String description = resultSet.getString("description");
                boolean published = resultSet.getBoolean("published");
                Tutorial tutorial = new Tutorial(id, title, description, published);
                tutorials.add(tutorial);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tutorials;
    }

    @Override
    public List<Tutorial> findByPublished(boolean published) {
        List<Tutorial> tutorials = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT * FROM tutorials WHERE published = ?"
             )) {
            statement.setBoolean(1, published);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    long id = resultSet.getLong("id");
                    String title = resultSet.getString("title");
                    String description = resultSet.getString("description");
                    Tutorial tutorial = new Tutorial(id, title, description, published);
                    tutorials.add(tutorial);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tutorials;
    }

    @Override
    public List<Tutorial> findByTitleContaining(String title) {
        List<Tutorial> tutorials = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT * FROM tutorials WHERE title LIKE ?"
             )) {
            statement.setString(1, "%" + title + "%");

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    long id = resultSet.getLong("id");
                    String tutorialTitle = resultSet.getString("title");
                    String description = resultSet.getString("description");
                    boolean published = resultSet.getBoolean("published");
                    Tutorial tutorial = new Tutorial(id, tutorialTitle, description, published);
                    tutorials.add(tutorial);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tutorials;
    }

    @Override
    public int deleteAll() {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "DELETE FROM tutorials"
             )) {
            return statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }
}
