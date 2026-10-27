package com.bloodbank.repository;

import com.bloodbank.model.Donor;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DonorRepository extends MongoRepository<Donor, String> {

    Optional<Donor> findByPhone(String phone);

    Optional<Donor> findByEmail(String email);

    boolean existsByPhone(String phone);

    boolean existsByEmail(String email);

    long countByAvailability(String availability);

    List<Donor> findByAvailability(String availability);

    @Query("{ 'availability': 'YES', 'blood_group': ?0, 'city': { $regex: ?1, $options: 'i' } }")
    List<Donor> findAvailableByBloodGroupAndCity(String bloodGroup, String cityRegex);

    @Query("{ 'availability': 'YES', 'blood_group': ?0 }")
    List<Donor> findAvailableByBloodGroup(String bloodGroup);

    @Query("{ 'availability': 'YES', 'city': { $regex: ?0, $options: 'i' } }")
    List<Donor> findAvailableByCity(String cityRegex);

    @Query("{ 'availability': 'YES' }")
    List<Donor> findAllAvailable();
}
