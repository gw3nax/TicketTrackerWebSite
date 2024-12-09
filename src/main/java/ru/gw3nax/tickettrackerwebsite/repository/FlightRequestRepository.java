package ru.gw3nax.tickettrackerwebsite.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.gw3nax.tickettrackerwebsite.entity.FlightRequestEntity;

import java.util.List;

@Repository
public interface FlightRequestRepository extends JpaRepository<FlightRequestEntity, Long> {

    List<FlightRequestEntity> findByUserId(Long userId);

    List<FlightRequestEntity> findAllByUserId(Long id);
}
