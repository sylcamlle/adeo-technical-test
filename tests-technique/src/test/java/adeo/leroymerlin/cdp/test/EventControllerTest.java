package adeo.leroymerlin.cdp.test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;

import adeo.leroymerlin.cdp.AdeoLeroyMerlinCDPRecruitmentApplication;
import adeo.leroymerlin.cdp.Event;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(
		webEnvironment = SpringBootTest.WebEnvironment.MOCK,
		classes = AdeoLeroyMerlinCDPRecruitmentApplication.class)
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class EventControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	public void getAllEvents() throws Exception {
		mockMvc.perform(get("/api/events/")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content()
						.contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$", hasSize(5)))
				.andExpect(jsonPath("$[0].title", is("GrasPop Metal Meeting")));
	}

	@Test
	public void searchByQuery() throws Exception {
		mockMvc.perform(get("/api/events/search/{query}", "Wa")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content()
						.contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$", hasSize(1)))
				.andExpect(jsonPath("$[0].title", is("GrasPop Metal Meeting [1]")))
				.andExpect(jsonPath("$[0].bands[0].name", is("Metallica [1]")))
				.andExpect(jsonPath("$[0].bands[0].members[0].name", is("Queen Anika Walsh")));
	}

	@Test
	public void deleteEvent() throws Exception {
		mockMvc.perform(delete("/api/events/{id}", "1001")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
		mockMvc.perform(get("/api/events/")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content()
						.contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$", hasSize(4)));
	}

	@Test
	public void updateEvent() throws Exception {
		MvcResult mvcResult = mockMvc.perform(get("/api/events/")
				.contentType(MediaType.APPLICATION_JSON)).andReturn();
		List<Event> eventList = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<List<Event>>() {});

		eventList.get(0).setNbStars(4);
		eventList.get(0).setComment("test");

		mockMvc.perform(put("/api/events/{id}", eventList.get(0).getId().toString())
				.content(objectMapper.writeValueAsString(eventList.get(0)))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
		mockMvc.perform(get("/api/events/search/{query}", "Wa")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content()
						.contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$", hasSize(1)))
				.andExpect(jsonPath("$[0].title", is("GrasPop Metal Meeting [1]")))
				.andExpect(jsonPath("$[0].nbStars", is(4)))
				.andExpect(jsonPath("$[0].comment", is("test")));
	}
}
