package org.pronet.lalafodemo.repositories;

import org.pronet.lalafodemo.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findByName(String name);
    Boolean existsByName(String name);
}
