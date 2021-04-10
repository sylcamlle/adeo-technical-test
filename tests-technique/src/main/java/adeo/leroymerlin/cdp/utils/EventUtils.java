package adeo.leroymerlin.cdp.utils;

import adeo.leroymerlin.cdp.Band;
import adeo.leroymerlin.cdp.Event;
import adeo.leroymerlin.cdp.Member;
import org.springframework.stereotype.Component;

import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Component
public class EventUtils {

	public Event setEventCountNumberChildItem(Event event) {
		event.setTitle(event.getTitle() + " [" + event.getBands().size() + "]");
		event.getBands().forEach(band -> band.setName(band.getName() + " [" + band.getMembers().size() + "]"));
		return event;
	}

	public Predicate<Event> getEventBandMemberNameFilter(String query) {
		return event -> event.getBands().stream()
				.anyMatch(getBandMemberNameFilter(query));
	}

	public Function<Event, Event> filteringEventBandMemberByName(String query) {
		return event -> {
			event.setBands(event.getBands().stream()
					.filter(getBandMemberNameFilter(query))
					.map(filteringBandMemberByName(query))
					.collect(Collectors.toSet()));
			return event;
		};
	}

	private Function<Band, Band> filteringBandMemberByName(String query) {
		return band -> {
			band.setMembers(band.getMembers().stream()
					.filter(getMemberNameFilter(query))
					.collect(Collectors.toSet()));
			return band;
		};
	}

	private Predicate<Band> getBandMemberNameFilter(String query) {
		return band -> band.getMembers().stream()
				.anyMatch(getMemberNameFilter(query));
	}

	private Predicate<Member> getMemberNameFilter(String query) {
		return member -> member.getName().toUpperCase().contains(query.toUpperCase());
	}
}
