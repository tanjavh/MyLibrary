package com.example.onlineLibrary.userLoanMicroservice.repository;

import com.example.onlineLibrary.userLoanMicroservice.model.entity.Role;
import com.example.onlineLibrary.userLoanMicroservice.model.enums.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName name);
}
