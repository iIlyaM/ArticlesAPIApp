package ru.ilyam.articlesapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.ilyam.articlesapi.entity.Role;

import java.util.List;
import java.util.Set;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);

    Role findRoleById(Long id);

    @Query("SELECT r FROM Role r WHERE r.id IN :ids")
    Set<Role> findRolesByIds(@Param("ids") List<Long> ids);

    @Modifying
    @Query(value = "INSERT INTO users_roles (user_id, role_id) SELECT :userId, id FROM roles WHERE id IN (:roleIds)",
            nativeQuery = true)
    void saveUserRoles(@Param("userId") Long userId, @Param("roleIds") List<Long> roleIds);
}