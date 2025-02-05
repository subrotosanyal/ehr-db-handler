package net.sanyal.ehr.db.repository.common;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.sanyal.ehr.model.common.Race;

@Repository
public interface RaceRepository extends JpaRepository<Race, Long> {}