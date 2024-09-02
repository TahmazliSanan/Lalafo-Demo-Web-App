package org.pronet.lalafodemo.repositories;

import org.pronet.lalafodemo.entities.Role;
import org.pronet.lalafodemo.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmailContainingIgnoreCase(String email);
    User findByResetToken(String resetToken);
    Boolean existsByEmailContainingIgnoreCase(String email);
    Boolean existsByRole(Role role);
}
