package net.sanyal.ehr.db.repository.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.sanyal.ehr.model.service.ServiceType;

@Repository
public interface ServiceTypeRespository extends JpaRepository<ServiceType, Long> {
    
}
