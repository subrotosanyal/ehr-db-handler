package net.sanyal.ehr.db.repository.common;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.sanyal.ehr.model.common.Name;

@Repository
public interface NameRepository extends JpaRepository<Name, Long> {}