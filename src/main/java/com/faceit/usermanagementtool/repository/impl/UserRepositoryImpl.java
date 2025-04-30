package com.faceit.usermanagementtool.repository.impl;

import com.faceit.usermanagementtool.model.User;
import com.faceit.usermanagementtool.repository.UserRepository;
import com.mongodb.DuplicateKeyException;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Repository
@AllArgsConstructor
@NoArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private static final Logger LOG = LoggerFactory.getLogger(UserRepositoryImpl.class);


    @Autowired
    @Qualifier("mongoTemplate")
    private MongoTemplate mongoTemplate;

    @Override
    public boolean removeUserById(final String id) {
            return Optional.ofNullable(this.mongoTemplate.findAndRemove(Query.query(Criteria.where("id").is(id)), User.class))
                    .map(user -> true)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found."));
    }

    @Override
    public boolean userCreation(final User user) {
        try{
            LOG.info("[REST-Creation] MongoDB insert one document {}", user);
            return Optional.ofNullable(this.mongoTemplate.insert(user)).isPresent();          
        }catch (DuplicateKeyException e){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Duplicated key with User=" + user);
        }
    }

    @Override
    public boolean updateUser(final String id, final User user) {
        LOG.info("[REST-Update] MongoDB updating document ID={} with the new information {}", id, user);
        return Optional.ofNullable(this.mongoTemplate.findAndReplace(Query.query(Criteria.where("id").is(id)), user))
                .map(userMongo -> true)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found."));
    }

    @Override
    public List<User> findByFilters(Map<String, String> filters) {
        Collection<Criteria> criteriasCollection = this.generateReadCriterias(filters);
        Criteria completeSearchCriteria = new Criteria();
        completeSearchCriteria.andOperator(criteriasCollection);
        Query query = new Query(completeSearchCriteria);
        List<User> returnedListByFilters = this.mongoTemplate.find(query, User.class);
        LOG.info("[REST-Read] MongoDB find {} with filters {}.", returnedListByFilters, filters);
        if(returnedListByFilters.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Filters did not match.");
        }
        return returnedListByFilters;
    }

    private Collection<Criteria> generateReadCriterias(Map<String, String> filters) {
        Collection<Criteria> criterias = new ArrayList<>();        
        filters.keySet().forEach(key -> {
            if(!filters.get(key).isEmpty()) {
                criterias.add(Criteria.where(key).is(filters.get(key)));
            } 
        });
        return criterias;
    }
}
