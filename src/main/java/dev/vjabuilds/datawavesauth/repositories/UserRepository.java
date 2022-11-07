package dev.vjabuilds.datawavesauth.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.vjabuilds.datawavesauth.models.DatawavesUser;

public interface UserRepository extends JpaRepository<DatawavesUser, Long> {
    DatawavesUser findByEmail(String email);
}