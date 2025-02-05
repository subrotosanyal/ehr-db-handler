package net.sanyal.ehr.db.repository.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CommonRepositoryManager {

    public final GenderRepository genderRepository;
    public final ContactDetailRepository contactDetailRepository;
    public final RaceRepository raceRepository;
    public final EthnicityRepository ethnicityRepository;
    public final AddressRepository addressRepository;
    public final NameRepository nameRepository;

    @Autowired
    public CommonRepositoryManager(
        GenderRepository genderRepository,
        ContactDetailRepository contactDetailRepository,
        RaceRepository raceRepository,
        EthnicityRepository ethnicityRepository,
        AddressRepository addressRepository,
        NameRepository nameRepository
    ) {
        this.genderRepository = genderRepository;
        this.contactDetailRepository = contactDetailRepository;
        this.raceRepository = raceRepository;
        this.ethnicityRepository = ethnicityRepository;
        this.addressRepository = addressRepository;
        this.nameRepository = nameRepository;
    }
}

