package com.momo.taskManagement.libraryFolder.repository;

import com.momo.taskManagement.libraryFolder.model.LibraryUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LibraryUserRespository extends JpaRepository<LibraryUser,Long> {

}
