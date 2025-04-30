package pl.edu.zut.app.parking.owners.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import pl.edu.zut.app.parking.owners.entities.Address;
import pl.edu.zut.app.parking.owners.repositories.AddressRepository;
import pl.edu.zut.app.parking.owners.services.AddressRepositoryService;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class AddressRepositoryServiceImpl implements AddressRepositoryService {

    private final AddressRepository repository;

    @CachePut(value = "addresses", key = "#address.id")
    @Override
    public Address save(Address address) {
        return repository.save(address);
    }

    @Cacheable(value = "addresses", key = "#id")
    @Override
    public Address findById(UUID id) {
        return repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Address not found"));
    }
}
