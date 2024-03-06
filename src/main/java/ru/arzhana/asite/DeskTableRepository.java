package ru.arzhana.asite;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeskTableRepository extends JpaRepository<DeskTable, Long> {

    List<DeskTable> findAllByIdStation(Long idStation);
}
