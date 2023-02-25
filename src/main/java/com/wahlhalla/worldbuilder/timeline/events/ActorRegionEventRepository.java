package com.wahlhalla.worldbuilder.timeline.events;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActorRegionEventRepository extends JpaRepository<ActorRegionEvent, Long> {

}