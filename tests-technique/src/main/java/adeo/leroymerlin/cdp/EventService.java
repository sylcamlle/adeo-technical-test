package adeo.leroymerlin.cdp;

import adeo.leroymerlin.cdp.utils.EventUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventService {

    private final EventRepository eventRepository;
    private final EventUtils eventUtils;

    @Autowired
    public EventService(EventRepository eventRepository, EventUtils eventUtils) {
        this.eventRepository = eventRepository;
        this.eventUtils = eventUtils;
    }

    public List<Event> getEvents() {
        return eventRepository.findAllBy();
    }

    public void delete(Long id) {
        eventRepository.delete(id);
    }

    public void updateEvent(Long id, Event event) {
        event.setId(id);
        eventRepository.save(event);
    }

    public List<Event> getFilteredEvents(String query) {
        List<Event> events = eventRepository.findAllBy();
        // Filter the events list in pure JAVA here
        return events.stream()
                .filter(eventUtils.getEventBandMemberNameFilter(query))
                .map(eventUtils.filteringEventBandMemberByName(query))
                .map(eventUtils::setEventCountNumberChildItem)
                .collect(Collectors.toList());
    }
}
