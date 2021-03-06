package org.vstu.compprehension.models.repository;


import org.vstu.compprehension.models.entities.BackendEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BackendRepository extends CrudRepository<BackendEntity, Long> {
}
