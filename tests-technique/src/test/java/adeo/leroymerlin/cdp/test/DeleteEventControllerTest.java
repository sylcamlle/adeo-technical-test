package adeo.leroymerlin.cdp.test;

import adeo.leroymerlin.cdp.AdeoLeroyMerlinCDPRecruitmentApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(
		webEnvironment = SpringBootTest.WebEnvironment.MOCK,
		classes = AdeoLeroyMerlinCDPRecruitmentApplication.class)
@AutoConfigureMockMvc
public class DeleteEventControllerTest {

	@Autowired
	private MockMvc mockMvc;

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
		mockMvc.perform(delete("/api/events/{id}", "1000")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content()
						.contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$", hasSize(1)))
				.andExpect(jsonPath("$[0].title", is("GrasPop Metal Meeting [1]")))
				.andExpect(jsonPath("$[0].bands[0].name", is("Metallica [1]")))
				.andExpect(jsonPath("$[0].bands[0].members[0].name", is("Queen Anika Walsh")));
	}
}
