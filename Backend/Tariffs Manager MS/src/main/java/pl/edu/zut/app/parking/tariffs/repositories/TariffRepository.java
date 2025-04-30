package pl.edu.zut.app.parking.tariffs.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.edu.zut.app.parking.tariffs.entities.AbstractBaseEntity;
import pl.edu.zut.app.parking.tariffs.entities.Tariff;
import pl.edu.zut.app.parking.tariffs.enums.TariffClass;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TariffRepository extends JpaRepository<Tariff, UUID> {
    List<Tariff> findAllByParkingId(UUID parkingId);

    boolean existsByParkingIdAndTariffClass(UUID parkingId, TariffClass tariffClass);

    List<Tariff> findAllByParkingIdAndRecordStatus(UUID parkingId, AbstractBaseEntity.RecordStatus recordStatus);

    Optional<Tariff> findByParkingIdAndTariffClassAndRecordStatus(UUID parkingId, TariffClass tariffClass, AbstractBaseEntity.RecordStatus recordStatus);
    Optional<Tariff> findByParkingIdAndRecordStatus(UUID parkingId, AbstractBaseEntity.RecordStatus recordStatus);

    boolean existsByParkingIdAndTariffClassAndRecordStatus(UUID parkingId, TariffClass tariffClass, AbstractBaseEntity.RecordStatus recordStatus);

    Optional<Tariff> findByIdAndRecordStatus(UUID id, AbstractBaseEntity.RecordStatus recordStatus);
}
