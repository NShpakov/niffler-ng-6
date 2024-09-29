package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.Databases;
import guru.qa.niffler.data.dao.UserDao;
import guru.qa.niffler.data.entity.spend.UserEntity;
import guru.qa.niffler.model.CurrencyValues;

import java.sql.*;
import java.util.Optional;
import java.util.UUID;

public class UserdataUserDAOJdbc implements UserDao {
    private static final Config CFG = Config.getInstance();

    @Override
    public UserEntity createUser(UserEntity user) {
        try {
            Connection connection = connection = Databases.connection(CFG.userdataJdbcUrl());

            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO user (username,currency,firstName,surname,photo,photo_small,full_name) " + "VALUES ( ?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            {
                preparedStatement.setString(1, user.getUsername());
                preparedStatement.setObject(2, user.getCurrency());
                preparedStatement.setString(3, user.getFirstname());
                preparedStatement.setString(4, user.getSurname());
                preparedStatement.setBytes(5, user.getPhoto());
                preparedStatement.setBytes(6, user.getPhotoSmall());
                preparedStatement.setString(7, user.getFullname());

                preparedStatement.executeUpdate();

                final UUID generatedKey;
                try (ResultSet rs = preparedStatement.getGeneratedKeys()) {
                    {
                        if (rs.next()) {
                            generatedKey = rs.getObject("id", UUID.class);
                        } else {
                            throw new SQLException("Could not get generated key");
                        }
                    }
                    user.setId(generatedKey);
                    return user;

                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<UserEntity> findById(UUID id) {
        try {
            Connection connection = Databases.connection(CFG.userdataJdbcUrl());
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM user WHERE id =?");
            {
                preparedStatement.setObject(1, id);
                preparedStatement.execute();
                try (ResultSet rs = preparedStatement.getResultSet()) {
                    if (rs.next()) {
                        UserEntity user = new UserEntity();
                        user.setId(rs.getObject("id", UUID.class));
                        user.setUsername(rs.getString("username"));
                        user.setCurrency(rs.getObject("currency", CurrencyValues.class));
                        user.setFirstname(rs.getString("firstname"));
                        user.setSurname(rs.getString("surname"));
                        user.setPhoto(rs.getBytes("photo"));
                        user.setPhotoSmall(rs.getBytes("photo_small"));
                        user.setFullname(rs.getString("full_name"));
                        return Optional.of(user);
                    } else {
                        return Optional.empty();
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<UserEntity> findByUsername(String username) {
        try (Connection connection = Databases.connection(CFG.userdataJdbcUrl())) {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM user WHERE username =?");
            preparedStatement.setObject(1, username);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    UserEntity user = new UserEntity();
                    user.setId(rs.getObject("id", UUID.class));
                    user.setUsername(rs.getString("username"));
                    user.setCurrency(rs.getObject("currency", CurrencyValues.class));
                    user.setFirstname(rs.getString("firstname"));
                    user.setSurname(rs.getString("surname"));
                    user.setPhoto(rs.getBytes("photo"));
                    user.setPhotoSmall(rs.getBytes("photo_small"));
                    user.setFullname(rs.getString("full_name"));
                    return Optional.of(user);
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(UserEntity user) {
        try (Connection connection = Databases.connection(CFG.userdataJdbcUrl());
             PreparedStatement ps = connection.prepareStatement("DELETE FROM user WHERE id = ?")) {
            ps.setObject(1, user.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}