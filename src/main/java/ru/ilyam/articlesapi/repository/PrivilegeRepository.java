package ru.ilyam.articlesapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.ilyam.articlesapi.entity.Privilege;

import java.util.Set;

public interface PrivilegeRepository extends JpaRepository<Privilege, Long> {

    @Query("select priv from Privilege priv join priv.roles r where r.id = :id")
    Set<Privilege> findAllByRoleId(@Param("id") Long roleId);

    @Query("select priv from Privilege priv join priv.roles r where r.name = :name")
    Set<Privilege> findAllByRoleName(@Param("name") String name);
}