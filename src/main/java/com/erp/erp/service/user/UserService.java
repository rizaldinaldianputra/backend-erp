package com.erp.erp.service.user;

import com.erp.erp.model.User;
import java.util.List;
import java.util.Optional;

public interface UserService {
    User createUser(User user);
    User updateUser(Long id, User user);
    void deleteUser(Long id);
    Optional<User> getUserById(Long id);
    List<User> getAllUsers();

    // Tambahkan untuk keperluan login (AuthController)
    Optional<User> findByUsername(String username);
}
