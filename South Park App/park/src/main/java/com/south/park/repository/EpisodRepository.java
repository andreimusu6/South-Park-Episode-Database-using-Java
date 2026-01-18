package com.south.park.repository;

import com.south.park.entity.DataEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EpisodRepository extends JpaRepository<DataEntity, Long> {
}