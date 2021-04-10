package adeo.leroymerlin.cdp;

import org.springframework.data.repository.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface EventRepository extends Repository<Event, Long> {

    void delete(Long eventId);

    @Transactional(readOnly = true)
    List<Event> findAllBy();

    void save(Event event);
}
