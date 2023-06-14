package com.ujisistempostgres.controller;

import com.ujisistempostgres.entity.Mahasiswa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/api")
public class MahasiswaController {
    @Autowired
    private DataSource dataSource;

    @GetMapping("/mahasiswa")
    public ResponseEntity<List<Mahasiswa>> getAllMahasiswa(@RequestParam(required = false) String alamat) {
        try {
            List<Mahasiswa> mahasiswas = new ArrayList<>();

            Connection connection = dataSource.getConnection();
            PreparedStatement statement;
            if (alamat == null) {
                statement = connection.prepareStatement("SELECT * FROM mahasiswa");
            } else {
                statement = connection.prepareStatement("SELECT * FROM mahasiswa WHERE alamat LIKE ?");
                statement.setString(1, "%" + alamat + "%");
            }

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int nim = resultSet.getInt("nim");
                String newAlamat = resultSet.getString("alamat");
                String nama = resultSet.getString("nama");
                Mahasiswa mahasiswa = new Mahasiswa(nim, newAlamat, nama);
                mahasiswas.add(mahasiswa);
            }

            connection.close();

            if (mahasiswas.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(mahasiswas, HttpStatus.OK);
        } catch (SQLException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/mahasiswa/{nim}")
    public ResponseEntity<Mahasiswa> getMahasiswaByNim(@PathVariable("nim") int nim) {
        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM mahasiswa WHERE nim = ?");
            statement.setInt(1, nim);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String alamat = resultSet.getString("alamat");
                String nama = resultSet.getString("nama");
                Mahasiswa mahasiswa = new Mahasiswa(nim, nama, alamat);
                connection.close();
                return new ResponseEntity<>(mahasiswa, HttpStatus.OK);
            } else {
                connection.close();
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (SQLException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/mahasiswa")
    public ResponseEntity<String> createMahasiswa(@RequestBody Mahasiswa mahasiswa) {
        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement("INSERT INTO mahasiswa (nama, alamat) VALUES (?, ?)");
            statement.setString(1, mahasiswa.getNama());
            statement.setString(2, mahasiswa.getAlamat());

            statement.executeUpdate();
            connection.close();

            return new ResponseEntity<>("Mahasiswa was created successfully.", HttpStatus.CREATED);
        } catch (SQLException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/mahasiswa/{nim}")
    public ResponseEntity<String> updateMahasiswa(@PathVariable("nim") int nim, @RequestBody Mahasiswa mahasiswa) {
        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement("UPDATE mahasisaw SET nama = ?, alamat = ? WHERE nim = ?");
            statement.setString(1, mahasiswa.getNama());
            statement.setString(2, mahasiswa.getAlamat());
            statement.setInt(4, nim);

            int rowsUpdated = statement.executeUpdate();
            connection.close();

            if (rowsUpdated > 0) {
                return new ResponseEntity<>("Mahasiswa was updated successfully.", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Cannot find Mahasiswa with nim=" + nim, HttpStatus.NOT_FOUND);
            }
        } catch (SQLException e) {
            return new ResponseEntity<>("Cannot update mahasiswa.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/mahasiswa/{nim}")
    public ResponseEntity<String> deleteMahasiswa(@PathVariable("nim") int nim) {
        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement("DELETE FROM mahasiswa WHERE nim = ?");
            statement.setInt(1, nim);

            int rowsDeleted = statement.executeUpdate();
            connection.close();

            if (rowsDeleted > 0) {
                return new ResponseEntity<>("Mahasiswa was deleted successfully.", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Cannot find Mahasiswa with nim=" + nim, HttpStatus.NOT_FOUND);
            }
        } catch (SQLException e) {
            return new ResponseEntity<>("Cannot delete mahasiswa.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/mahasiswa")
    public ResponseEntity<String> deleteAllMahasiswa() {
        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement("DELETE FROM mahasiswa");

            int numRowsDeleted = statement.executeUpdate();
            connection.close();

            return new ResponseEntity<>("Deleted " + numRowsDeleted + " Mahasiswa(s) successfully.", HttpStatus.OK);
        } catch (SQLException e) {
            return new ResponseEntity<>("Cannot delete mahasiswa.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
