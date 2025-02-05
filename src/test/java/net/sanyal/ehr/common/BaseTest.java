package net.sanyal.ehr.common;

import net.sanyal.ehr.db.repository.appointment.AppointmentPriorityRepository;
import net.sanyal.ehr.db.repository.appointment.AppointmentRepository;
import net.sanyal.ehr.db.repository.appointment.AppointmentStatusRepository;
import net.sanyal.ehr.db.repository.appointment.AppointmentTypeRepository;
import net.sanyal.ehr.db.repository.common.EthnicityRepository;
import net.sanyal.ehr.db.repository.common.GenderRepository;
import net.sanyal.ehr.db.repository.common.RaceRepository;
import net.sanyal.ehr.db.repository.patient.PatientRepository;
import net.sanyal.ehr.db.repository.practitioner.PractitionerRepository;
import net.sanyal.ehr.db.repository.service.ServiceCategoryRepository;
import net.sanyal.ehr.db.repository.service.ServiceTypeRespository;
import net.sanyal.ehr.model.appointment.AppointmentPriority;
import net.sanyal.ehr.model.appointment.AppointmentStatus;
import net.sanyal.ehr.model.appointment.AppointmentType;
import net.sanyal.ehr.model.common.Ethnicity;
import net.sanyal.ehr.model.common.Gender;
import net.sanyal.ehr.model.common.Race;
import net.sanyal.ehr.model.service.ServiceCategory;
import net.sanyal.ehr.model.service.ServiceType;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@Slf4j
public abstract class BaseTest {

    @Autowired
    protected ObjectMapper objectMapper;
    @Autowired
    protected MockMvc mockMvc;

    protected static GenderRepository genderRepo;
    protected static RaceRepository raceRepo;
    protected static PatientRepository patientRepository;
    protected static EthnicityRepository ethnicityRepo;
    protected static PractitionerRepository practitionerRepository;
    protected static AppointmentRepository appointmentRepository;
    protected static AppointmentPriorityRepository appointmentPriorityRepo;
    protected static AppointmentStatusRepository appointmentStatusRepo;
    protected static AppointmentTypeRepository appointmentTypeRepo;
    protected static ServiceCategoryRepository serviceCategoryRepo;
    protected static ServiceTypeRespository serviceTypeRepo;

    protected static Gender male;
    protected static Gender female;
    protected static Race asian;
    protected static Ethnicity other;
    protected static AppointmentType routineAppointmentType;

    @SuppressWarnings("resource")
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
            .withCommand("postgres", "-c", "fsync=off", "-c", "log_statement=all")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass")
            .withUrlParam("autocommit", "true")
            .withReuse(true);

    @BeforeAll
    public static void setup(@Autowired GenderRepository genderRepo,
            @Autowired RaceRepository raceRepo,
            @Autowired EthnicityRepository ethnicityRepo,
            @Autowired PatientRepository patientRepository,
            @Autowired PractitionerRepository practitionerRepository,
            @Autowired AppointmentRepository appointmentRepository,
            @Autowired AppointmentPriorityRepository appointmentPriorityRepo,
            @Autowired AppointmentStatusRepository appointmentStatusRepo,
            @Autowired AppointmentTypeRepository appointmentTypeRepo,
            @Autowired ServiceTypeRespository serviceTypeRepo,
            @Autowired ServiceCategoryRepository serviceCategoryRepo) {
        log.info("Setting up test environment");
        startPostgresIfNotRunning();
        seedInitialDataToStaticTables(genderRepo, raceRepo, ethnicityRepo, appointmentPriorityRepo,
                appointmentStatusRepo,
                appointmentTypeRepo, serviceTypeRepo, serviceCategoryRepo);
        BaseTest.patientRepository = patientRepository;
        BaseTest.genderRepo = genderRepo;
        BaseTest.raceRepo = raceRepo;
        BaseTest.ethnicityRepo = ethnicityRepo;
        BaseTest.practitionerRepository = practitionerRepository;
        BaseTest.appointmentRepository = appointmentRepository;
        BaseTest.appointmentPriorityRepo = appointmentPriorityRepo;
        BaseTest.appointmentStatusRepo = appointmentStatusRepo;
        BaseTest.appointmentTypeRepo = appointmentTypeRepo;
        BaseTest.serviceCategoryRepo = serviceCategoryRepo;
        BaseTest.serviceTypeRepo = serviceTypeRepo;
        initializeLooksUps();
    }

    private static void initializeLooksUps() {
        log.info("Initializing looks ups");
        male = genderRepo.findById(1L)
                .orElseThrow(() -> new RuntimeException("Male Gender not found"));
        female = genderRepo.findById(2L)
                .orElseThrow(() -> new RuntimeException("Female Gender not found"));
        asian = raceRepo.findById(1L).orElseThrow(() -> new RuntimeException("Asian Race not found"));
        other = ethnicityRepo.findById(3L)
                .orElseThrow(() -> new RuntimeException("Other Ethnicity not found"));
        routineAppointmentType = appointmentTypeRepo.findById(1L)
                .orElseThrow(() -> new RuntimeException("Female Gender not found"));
    }

    private static void seedInitialDataToStaticTables(GenderRepository genderRepo, RaceRepository raceRepo,
            EthnicityRepository ethnicityRepo, AppointmentPriorityRepository appointmentPriorityRepo,
            AppointmentStatusRepository appointmentStatusRepo, AppointmentTypeRepository appointmentTypeRepo,
            ServiceTypeRespository serviceTypeRepo, ServiceCategoryRepository serviceCategoryRepo) {
        seedGenderTable(genderRepo);
        seedRaceTable(raceRepo);
        seedEthnicityTable(ethnicityRepo);
        seedAppointmentStatusTable(appointmentStatusRepo);
        seedAppointmentPriorityTable(appointmentPriorityRepo);
        seedAppointmentTypeTable(appointmentTypeRepo);
        seedServiceCategoryTable(serviceCategoryRepo);
        seedServiceTypeTable(serviceTypeRepo);
        log.info("Seeding of data in the lookup tables completed");
    }

    @AfterAll
    static void cleanUp() {
        log.info("Cleaning up all the resources/entities created for testing");
        patientRepository.deleteAll();
        practitionerRepository.deleteAll();
        appointmentRepository.deleteAll();
    }

    private static void startPostgresIfNotRunning() {
        if (!postgres.isRunning()) {
            postgres.start();
            log.info("Started PostgreSQL container " + postgres.getMappedPort(5432));
        }
    }

    @DynamicPropertySource
    static void configureTestProperties(DynamicPropertyRegistry registry) {
        startPostgresIfNotRunning();
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.datasource.driver-class-name", postgres::getDriverClassName);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
        registry.add("spring.jpa.show-sql", () -> "true");
        registry.add("spring.jpa.properties.hibernate.format_sql", () -> "true");
        registry.add("logging.level.org.hibernate.orm.jdbc.bind", () -> "trace");
        registry.add("spring.jpa.properties.hibernate.dialect", () -> "org.hibernate.dialect.PostgreSQLDialect");
        log.info("Configuring spring test properties: {}", registry);
    }

    private static void seedGenderTable(GenderRepository genderRepo) {
        if (genderRepo.count() == 0) {
            genderRepo.save(Gender.builder().genderId(1L).genderName("Male").build());
            genderRepo.save(Gender.builder().genderId(2L).genderName("Female").build());
            genderRepo.save(Gender.builder().genderId(3L).genderName("Other").build());
        }
    }

    private static void seedRaceTable(RaceRepository raceRepo) {
        if (raceRepo.count() == 0) {
            raceRepo.save(Race.builder().raceId(1L).raceName("Asian").build());
            raceRepo.save(Race.builder().raceId(2L).raceName("Black or African American").build());
            raceRepo.save(Race.builder().raceId(3L).raceName("White").build());
        }
    }

    private static void seedEthnicityTable(EthnicityRepository ethnicityRepo) {
        if (ethnicityRepo.count() == 0) {
            ethnicityRepo.save(Ethnicity.builder().ethnicityId(1L).ethnicityName("Hispanic or Latino").build());
            ethnicityRepo.save(Ethnicity.builder().ethnicityId(2L).ethnicityName("Non-Hispanic or Latino").build());
            ethnicityRepo.save(Ethnicity.builder().ethnicityId(3L).ethnicityName("Other").build());
        }
    }

    private static void seedAppointmentStatusTable(AppointmentStatusRepository appointmentStatusRepo) {
        if (appointmentStatusRepo.count() == 0) {
            appointmentStatusRepo.save(
                    AppointmentStatus.builder().appointmentStatusId(1L).appointmentStatusName("Scheduled").build());
            appointmentStatusRepo.save(
                    AppointmentStatus.builder().appointmentStatusId(2L).appointmentStatusName("Completed").build());
            appointmentStatusRepo.save(
                    AppointmentStatus.builder().appointmentStatusId(3L).appointmentStatusName("Cancelled").build());
        }
    }

    private static void seedAppointmentPriorityTable(AppointmentPriorityRepository appointmentPriorityRepo) {
        if (appointmentPriorityRepo.count() == 0) {
            appointmentPriorityRepo.save(AppointmentPriority.builder().appointmentPriorityId(1L)
                    .appointmentPriorityName("Low").build());
            appointmentPriorityRepo.save(AppointmentPriority.builder().appointmentPriorityId(2L)
                    .appointmentPriorityName("Medium").build());
            appointmentPriorityRepo.save(AppointmentPriority.builder().appointmentPriorityId(3L)
                    .appointmentPriorityName("High").build());
        }
    }

    private static void seedAppointmentTypeTable(AppointmentTypeRepository appointmentTypeRepo) {
        if (appointmentTypeRepo.count() == 0) {
            appointmentTypeRepo
                    .save(AppointmentType.builder().appointmentTypeId(1L).appointmentTypeName("Routine").build());
            appointmentTypeRepo
                    .save(AppointmentType.builder().appointmentTypeId(2L).appointmentTypeName("Checkup").build());
            appointmentTypeRepo
                    .save(AppointmentType.builder().appointmentTypeId(3L).appointmentTypeName("Emergency").build());
        }
    }

    private static void seedServiceTypeTable(ServiceTypeRespository serviceTypeRepo) {
        if (serviceTypeRepo.count() == 0) {
            serviceTypeRepo.save(ServiceType.builder().serviceTypeId(1L).serviceTypeName("Consultation").build());
            serviceTypeRepo.save(ServiceType.builder().serviceTypeId(2L).serviceTypeName("MRI").build());
            serviceTypeRepo.save(ServiceType.builder().serviceTypeId(3L).serviceTypeName("ECG").build());
        }
    }

    private static void seedServiceCategoryTable(ServiceCategoryRepository serviceCategoryRepo) {
        if (serviceCategoryRepo.count() == 0) {
            serviceCategoryRepo
                    .save(ServiceCategory.builder().serviceCategoryId(1L).serviceCategoryName("Opthalmology").build());
            serviceCategoryRepo
                    .save(ServiceCategory.builder().serviceCategoryId(2L).serviceCategoryName("Cardiology").build());
            serviceCategoryRepo
                    .save(ServiceCategory.builder().serviceCategoryId(3L).serviceCategoryName("Oncology").build());
        }
    }
}
