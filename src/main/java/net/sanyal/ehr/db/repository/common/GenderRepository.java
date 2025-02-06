package net.sanyal.ehr.db.repository.common;

import net.sanyal.ehr.model.common.Gender;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

@Repository
@RepositoryRestResource
public interface GenderRepository extends JpaRepository<Gender, Long> {

}