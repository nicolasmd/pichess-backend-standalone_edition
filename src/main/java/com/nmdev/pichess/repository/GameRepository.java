package com.nmdev.pichess.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.nmdev.pichess.model.Game;

@Repository
public interface GameRepository extends MongoRepository<Game,String> {
    @Query("{$and :[{active: true},{$or :[{whiteUser: ?0},{blackUser: ?0}]} ]}")
    Game getActiveGame(String userId);
}