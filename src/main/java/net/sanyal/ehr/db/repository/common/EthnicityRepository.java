package net.sanyal.ehr.db.repository.common;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.sanyal.ehr.model.common.Ethnicity;

@Repository
public interface EthnicityRepository extends JpaRepository<Ethnicity, Long> {}