package net.sanyal.ehr.db.repository.common;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.sanyal.ehr.model.common.ContactDetail;

@Repository
public interface ContactDetailRepository extends JpaRepository<ContactDetail, Long> {}