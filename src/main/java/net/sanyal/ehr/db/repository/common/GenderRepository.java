package net.sanyal.ehr.db.repository.common;

import net.sanyal.ehr.model.common.Gender;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMethod;

@Repository
@RepositoryRestResource
@CrossOrigin(origins = { "http://localhost:3000", "http://localhost:5173" }, methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.PATCH, RequestMethod.OPTIONS })
public interface GenderRepository extends JpaRepository<Gender, Long> {

}