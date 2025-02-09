package net.sanyal.ehr.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import net.sanyal.ehr.model.appointment.AppointmentPriority;
import net.sanyal.ehr.model.appointment.AppointmentStatus;
import net.sanyal.ehr.model.appointment.AppointmentType;
import net.sanyal.ehr.model.common.Ethnicity;
import net.sanyal.ehr.model.common.Gender;
import net.sanyal.ehr.model.common.Race;
import net.sanyal.ehr.model.service.ServiceCategory;
import net.sanyal.ehr.model.service.ServiceType;

@Configuration
public class RestConfiguration implements RepositoryRestConfigurer {

    @Override
    public void configureRepositoryRestConfiguration(
            RepositoryRestConfiguration config, CorsRegistry cors) {
        config.exposeIdsFor(Race.class, Gender.class, Ethnicity.class, ServiceType.class, ServiceCategory.class,
                AppointmentType.class, AppointmentStatus.class, AppointmentPriority.class);
    }
}
