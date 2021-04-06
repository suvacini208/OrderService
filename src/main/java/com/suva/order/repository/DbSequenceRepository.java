package com.suva.order.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.suva.order.domain.DbSequence;

public interface DbSequenceRepository extends MongoRepository<DbSequence, String>{

}
