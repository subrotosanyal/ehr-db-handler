package net.sanyal.ehr.db.repository.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import net.sanyal.ehr.model.service.ServiceType;

@Repository
@RepositoryRestResource
public interface ServiceTypeRespository extends JpaRepository<ServiceType, Long> {
    
}
