package dev.vjabuilds.datawavesauth.repositories;

import org.springframework.data.repository.CrudRepository;

import dev.vjabuilds.datawavesauth.models.Role;

public interface RolesRepository extends CrudRepository<Role, Long> {
    Role findByName(String name);
}
