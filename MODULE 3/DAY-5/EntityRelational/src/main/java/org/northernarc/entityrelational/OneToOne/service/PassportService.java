package org.northernarc.entityrelational.OneToOne.service;

import org.northernarc.entityrelational.OneToOne.model.Passport;

import java.util.List;

public interface PassportService {

    Passport addPassport(Passport passport);

    List<Passport> getAllPassports();

    Passport getPassportById(Long passportNumber);

    Passport updatePassport(Long passportNumber, Passport passport);

    void deletePassport(Long passportNumber);

}
