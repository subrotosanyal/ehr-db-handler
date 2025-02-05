package net.sanyal.ehr.db.repository.patient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import net.sanyal.ehr.model.patient.Immunization;
import net.sanyal.ehr.model.patient.InsuranceDetail;
import net.sanyal.ehr.model.patient.MedicalHistory;
import net.sanyal.ehr.model.patient.Medication;
import net.sanyal.ehr.model.patient.MenstruationPregnancyRecord;
import net.sanyal.ehr.model.patient.SubstanceConsumption;

@Component
public class PatientRepositoryManager {

    public final PatientRepository patientRepository;
    public final ImmunizationRepository immunizationRepository;
    public final InsuranceDetailRepository insuranceDetailRepository;
    public final MedicalHistoryRepository medicalHistoryRepository;
    public final MedicationRepository medicationRepository;
    public final MenstruationPregnancyRecordRepository menstruationPregnancyRecordRepository;
    public final SubstanceConsumptionRepository substanceConsumptionRepository;

    @Autowired
    public PatientRepositoryManager(
        PatientRepository patientRepository,
        ImmunizationRepository immunizationRepository,
        InsuranceDetailRepository insuranceDetailRepository,
        MedicalHistoryRepository medicalHistoryRepository,
        MedicationRepository medicationRepository,
        MenstruationPregnancyRecordRepository menstruationPregnancyRecordRepository,
        SubstanceConsumptionRepository substanceConsumptionRepository
    ) {
        this.patientRepository = patientRepository;
        this.immunizationRepository = immunizationRepository;
        this.insuranceDetailRepository = insuranceDetailRepository;
        this.medicalHistoryRepository = medicalHistoryRepository;
        this.medicationRepository = medicationRepository;
        this.menstruationPregnancyRecordRepository = menstruationPregnancyRecordRepository;
        this.substanceConsumptionRepository = substanceConsumptionRepository;
    }
}

@Repository
interface ImmunizationRepository extends JpaRepository<Immunization, Long> {}

@Repository
interface InsuranceDetailRepository extends JpaRepository<InsuranceDetail, Long> {}

@Repository
interface MedicalHistoryRepository extends JpaRepository<MedicalHistory, Long> {}

@Repository
interface MedicationRepository extends JpaRepository<Medication, Long> {}

@Repository
interface MenstruationPregnancyRecordRepository extends JpaRepository<MenstruationPregnancyRecord, Long> {}

@Repository
interface SubstanceConsumptionRepository extends JpaRepository<SubstanceConsumption, Long> {}
