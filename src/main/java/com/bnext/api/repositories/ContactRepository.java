package com.bnext.api.repositories;

import com.bnext.api.repositories.models.ContactModel;
import org.springframework.data.mongodb.repository.DeleteQuery;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactRepository extends MongoRepository<ContactModel, Long>{
    @Query(value="{ 'userId' : ?0 }")
    List<ContactModel> findContactsByUser(Long userId);

    @DeleteQuery(value="{ 'userId' : ?0 }")
    void deleteContactsByUser(Long userId);
};