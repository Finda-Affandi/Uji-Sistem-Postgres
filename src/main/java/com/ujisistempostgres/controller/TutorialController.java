package com.ujisistempostgres.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ujisistempostgres.model.Tutorial;
import com.ujisistempostgres.repository.TutorialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/api")
public class TutorialController {

    @Autowired
    private DataSource dataSource;

    @GetMapping("/tutorials")
    public ResponseEntity<List<Tutorial>> getAllTutorials(@RequestParam(required = false) String title) {
        try {
            List<Tutorial> tutorials = new ArrayList<>();

            Connection connection = dataSource.getConnection();
            PreparedStatement statement;
            if (title == null) {
                statement = connection.prepareStatement("SELECT * FROM tutorials");
            } else {
                statement = connection.prepareStatement("SELECT * FROM tutorials WHERE title LIKE ?");
                statement.setString(1, "%" + title + "%");
            }

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                String tutorialTitle = resultSet.getString("title");
                String description = resultSet.getString("description");
                boolean published = resultSet.getBoolean("published");
                Tutorial tutorial = new Tutorial(id, tutorialTitle, description, published);
                tutorials.add(tutorial);
            }

            connection.close();

            if (tutorials.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(tutorials, HttpStatus.OK);
        } catch (SQLException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/tutorials/{id}")
    public ResponseEntity<Tutorial> getTutorialById(@PathVariable("id") long id) {
        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM tutorials WHERE id = ?");
            statement.setLong(1, id);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String title = resultSet.getString("title");
                String description = resultSet.getString("description");
                boolean published = resultSet.getBoolean("published");
                Tutorial tutorial = new Tutorial(id, title, description, published);
                connection.close();
                return new ResponseEntity<>(tutorial, HttpStatus.OK);
            } else {
                connection.close();
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (SQLException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/tutorials")
    public ResponseEntity<String> createTutorial(@RequestBody Tutorial tutorial) {
        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement("INSERT INTO tutorials (title, description, published) VALUES (?, ?, ?)");
            statement.setString(1, tutorial.getTitle());
            statement.setString(2, tutorial.getDescription());
            statement.setBoolean(3, tutorial.isPublished());

            statement.executeUpdate();
            connection.close();

            return new ResponseEntity<>("Tutorial was created successfully.", HttpStatus.CREATED);
        } catch (SQLException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/tutorials/{id}")
    public ResponseEntity<String> updateTutorial(@PathVariable("id") long id, @RequestBody Tutorial tutorial) {
        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement("UPDATE tutorials SET title = ?, description = ?, published = ? WHERE id = ?");
            statement.setString(1, tutorial.getTitle());
            statement.setString(2, tutorial.getDescription());
            statement.setBoolean(3, tutorial.isPublished());
            statement.setLong(4, id);

            int rowsUpdated = statement.executeUpdate();
            connection.close();

            if (rowsUpdated > 0) {
                return new ResponseEntity<>("Tutorial was updated successfully.", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Cannot find Tutorial with id=" + id, HttpStatus.NOT_FOUND);
            }
        } catch (SQLException e) {
            return new ResponseEntity<>("Cannot update tutorial.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/tutorials/{id}")
    public ResponseEntity<String> deleteTutorial(@PathVariable("id") long id) {
        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement("DELETE FROM tutorials WHERE id = ?");
            statement.setLong(1, id);

            int rowsDeleted = statement.executeUpdate();
            connection.close();

            if (rowsDeleted > 0) {
                return new ResponseEntity<>("Tutorial was deleted successfully.", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Cannot find Tutorial with id=" + id, HttpStatus.NOT_FOUND);
            }
        } catch (SQLException e) {
            return new ResponseEntity<>("Cannot delete tutorial.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/tutorials")
    public ResponseEntity<String> deleteAllTutorials() {
        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement("DELETE FROM tutorials");

            int numRowsDeleted = statement.executeUpdate();
            connection.close();

            return new ResponseEntity<>("Deleted " + numRowsDeleted + " Tutorial(s) successfully.", HttpStatus.OK);
        } catch (SQLException e) {
            return new ResponseEntity<>("Cannot delete tutorials.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/tutorials/published")
    public ResponseEntity<List<Tutorial>> findByPublished() {
        try {
            List<Tutorial> tutorials = new ArrayList<>();

            Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM tutorials WHERE published = ?");
            statement.setBoolean(1, true);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                String title = resultSet.getString("title");
                String description = resultSet.getString("description");
                boolean published = resultSet.getBoolean("published");
                Tutorial tutorial = new Tutorial(id, title, description, published);
                tutorials.add(tutorial);
            }

            connection.close();

            if (tutorials.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(tutorials, HttpStatus.OK);
        } catch (SQLException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
