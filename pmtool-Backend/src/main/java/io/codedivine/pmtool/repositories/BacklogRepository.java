package io.codedivine.pmtool.repositories;

import io.codedivine.pmtool.domain.Backlog;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BacklogRepository extends CrudRepository<Backlog,Long> {

      Backlog findByProjectIdentifier(String Identifier);


}
