package com.momo.taskManagement.userFolder.repository;

import com.momo.taskManagement.userFolder.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);
//
//    @Query(nativeQuery = true,
//            value = "SELECT * FROM NEWUSER n ORDER BY n.username DESC "
//    )
//    List<User> findAllUserDesc();
//

//
//    @Query(
//            value = "SELECT * FROM User n ORDER BY n.username ASC "
//    )
//    List<User> findAllUserDescHQL();



}
