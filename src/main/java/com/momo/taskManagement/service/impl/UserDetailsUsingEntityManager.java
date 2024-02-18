package com.momo.taskManagement.service.impl;

import com.momo.taskManagement.dto.UserDetailsListDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.query.NativeQuery;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class UserDetailsUsingEntityManager {

    @PersistenceContext
    EntityManager entityManager;

    public List<UserDetailsListDto> getUserDetailDesc(){
        String queryString = "SELECT n.id as id ," +
                                    "n.first_name as firstName," +
                                    "n.last_name as lastName," +
                                    "n.username as username " +
                                    "FROM NEWUSER n " +
                                    "ORDER BY n.username DESC";
//        Query sqlQuery = entityManager.createNativeQuery(queryString);

        Query sqlQuery = entityManager.createNativeQuery(queryString, UserDetailsListDto.class);
        NativeQuery<UserDetailsListDto> nativeQuery = sqlQuery.unwrap(NativeQuery.class);

//        Query query = nativeQuery.setTupleTransformer(
//                (tuple,aliases) -> {
//                    UserDetailsListDto userDto= new UserDetailsListDto();
//                    userDto.setUserId((String) tuple[0]);
//                    userDto.setFirstName((String) tuple[1]);
//                    userDto.setLastName((String) tuple[2]);
//                    userDto.setUsername((String) tuple[3]);
//                    return userDto;
//                }
//        );

        //        List<UserDetailsListDto> usersList = query.getResultList();
        return nativeQuery.getResultList();

    }

//
//    Query sqlQuery = entityManager.createNativeQuery(queryString)
//            .setParameter("userId", userId);
//    NativeQuery nativeQuery = sqlQuery.unwrap(NativeQuery.class);
//    Query query = nativeQuery.setResultListTransformer(ResultListTransformer.aliasToBean(TxnHistoryDto.class));
//    List<TxnHistoryDto> transactions = query.getResultList();

}
