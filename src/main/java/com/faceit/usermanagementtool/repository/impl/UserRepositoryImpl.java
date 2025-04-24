package com.faceit.usermanagementtool.repository.impl;

import com.faceit.usermanagementtool.model.User;
import com.faceit.usermanagementtool.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepositoryImpl implements UserRepository {
    
    @Autowired
    @Qualifier("mongoTemplate")
    private MongoTemplate mongoTemplate;
    
    @Override
    public List<User> findByLastName(String name) {
        return null;
    }

    @Override
    public boolean removeUserById(String id) {
        Query query = Query.query(Criteria.where("id").is(id));
        return Optional.ofNullable(this.mongoTemplate.findAndRemove(query, User.class)).isPresent();
    }

    @Override
    public boolean userCreation(User user) {
        return Optional.ofNullable(this.mongoTemplate.insert(user)).isPresent();
    }

    @Override
    public boolean updateUser(String id, User user) {
        Query query = Query.query(Criteria.where("id").is(id));
        return Optional.ofNullable(this.mongoTemplate.findAndReplace(query, User.class)).isPresent();
    }

    @Override
    public List<User> findByFilters(User user) {
        Criteria idCriteria = Criteria.where("id").is(user.id());
        Collection<Criteria> criteriasCollection = new ArrayList<>();
        criteriasCollection.add(idCriteria);
        Criteria criteria = new Criteria();
        criteria.andOperator(criteriasCollection);
        Query query = new Query();
        query.addCriteria(criteria);
        return this.mongoTemplate.find(query, User.class);
    }
}
