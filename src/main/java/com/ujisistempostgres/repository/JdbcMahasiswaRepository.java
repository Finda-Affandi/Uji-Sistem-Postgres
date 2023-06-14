package com.ujisistempostgres.repository;

import com.ujisistempostgres.entity.Mahasiswa;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcMahasiswaRepository implements MahasiswaRepository {
    private DataSource dataSource;

    public JdbcMahasiswaRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    @Override
    public int save (Mahasiswa mahasiswa) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "INSERT INTO mahasiswa (nama, alamat) VALUES (?, ?)",
                     Statement.RETURN_GENERATED_KEYS
             )
        ) {
            statement.setString(1, mahasiswa.getNama());
            statement.setString(2, mahasiswa.getAlamat());

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Creating tutorial failed, no rows affected.");
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int id = generatedKeys.getInt(1);
                    mahasiswa.setNim(id);
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
    public int update(Mahasiswa mahasiswa) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "UPDATE mahasiswa SET nama = ?, alamat = ? WHERE nim = ?"
             )) {
            statement.setString(1, mahasiswa.getNama());
            statement.setString(2, mahasiswa.getAlamat());
            statement.setInt(4, mahasiswa.getNim());

            return statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public Mahasiswa findByNim(int nim) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT * FROM mahasiswa WHERE nim = ?"
             )) {
            statement.setLong(1, nim);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String nama = resultSet.getString("nama");
                    String alamat = resultSet.getString("alamat");
                    return new Mahasiswa(nim, nama, alamat);
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
    public int deleteByNim(int nim) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "DELETE FROM mahasiswa WHERE nim = ?"
             )) {
            statement.setInt(1, nim);

            return statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public List<Mahasiswa> findAll() {
        List<Mahasiswa> mahasiswas = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT * FROM mahasiswa"
             );
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int nim = resultSet.getInt("nim");
                String nama = resultSet.getString("nama");
                String alamat = resultSet.getString("alamat");
                Mahasiswa mahasiswa = new Mahasiswa(nim, nama, alamat);
                mahasiswas.add(mahasiswa);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return mahasiswas;
    }

    @Override
    public List<Mahasiswa> findByNama(String nama) {
        List<Mahasiswa> mahasiswas = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT * FROM mahasiswa WHERE nama = ?"
             )) {
            statement.setString(1, nama);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int nim = resultSet.getInt("nim");
                    String alamat = resultSet.getString("alamat");
                    Mahasiswa mahasiswa = new Mahasiswa(nim, nama, alamat);
                    mahasiswas.add(mahasiswa);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return mahasiswas;
    }

    @Override
    public List<Mahasiswa> findByAlamat(String alamat) {
        List<Mahasiswa> mahasiswas = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT * FROM mahasiswa WHERE alamat LIKE ?"
             )) {
            statement.setString(1, "%" + alamat + "%");

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int nim = resultSet.getInt("nim");
                    String nama = resultSet.getString("nama");
                    String newAlamat = resultSet.getString("alamat");
                    Mahasiswa mahasiswa = new Mahasiswa(nim, nama, newAlamat);
                    mahasiswas.add(mahasiswa);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return mahasiswas;
    }

    @Override
    public int deleteAll() {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "DELETE FROM mahasiswa"
             )) {
            return statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }
}
