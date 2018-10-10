package org.teksys.pmo.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.teksys.pmo.Response.ApiResponse;
import org.teksys.pmo.domain.PracticeManagementDTO;
import org.teksys.pmo.service.PracticeManagementService;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
public class PracticeManagementControllerTest {

	@Mock
	private PracticeManagementService practiceManagementService;

	@InjectMocks
	private PracticeManagementController practiceManagementController;

	private MockMvc mockMvc;

	public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
			MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

	@Test
	public void contextLoads() {
	}

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);

		this.mockMvc = MockMvcBuilders.standaloneSetup(practiceManagementController).build();
	}

	@Test
	public void testsaveProjectContentisTrue() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

		when(practiceManagementService.isPracticeExist(any(PracticeManagementDTO.class))).thenReturn(true);
		doNothing().when(practiceManagementService).saveProjectContent(getPracticeManagement());

		this.mockMvc
				.perform(post("/saveProjectContent").contentType(APPLICATION_JSON_UTF8)
						.content(mapper.writeValueAsBytes(getPracticeManagement())))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.status" ,is("SUCESS")))
				.andDo(print());
	}

	@Test
	public void testsaveProjectContentisFalse() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

		when(practiceManagementService.isPracticeExist(getPracticeManagement())).thenReturn(false);
		doNothing().when(practiceManagementService).saveProjectContent(getPracticeManagement());

		this.mockMvc
				.perform(post("/saveProjectContent").contentType(APPLICATION_JSON_UTF8)
						.content(mapper.writeValueAsBytes(getPracticeManagement())))
				.andExpect(status().isOk()).andExpect(jsonPath("$.status", is("FAILURE"))).andDo(print());
	}

	@Test
	public void testfindAllProjectInfo() throws Exception {

		when(practiceManagementService.findAllProjectInfo()).thenReturn(getListPracticeExpenses());
		this.mockMvc.perform(get("/findAllProjectInfo")).andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(1)))
				.andExpect(jsonPath("$[0].createdBy", is("ashwin")))
				.andExpect(jsonPath("$[0].modifiedBy", is("raksha")));

	}

	@Test
	public void testupdatePracticeTrue() throws Exception {

		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		
		when(practiceManagementService.isPracticeExist(any(PracticeManagementDTO.class))).thenReturn(true);
//		when(practiceManagementService.isPracticeExist(getPracticeManagement())).thenReturn(true);
		
		doNothing().when(practiceManagementService).updateById(getPracticeManagement());
		this.mockMvc
				.perform(post("/updatePractice").contentType(APPLICATION_JSON_UTF8)
						.content(mapper.writeValueAsBytes(getPracticeManagement())))
				.andExpect(status().isOk()).andExpect(jsonPath("$.status", is("FAILURE"))).andDo(print());

	}

	@Test
	public void testupdatePracticeFalse() throws Exception {

		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		when(practiceManagementService.isPracticeExist(getPracticeManagement())).thenReturn(false);

		this.mockMvc
				.perform(post("/updatePractice").contentType(APPLICATION_JSON_UTF8)
						.content(mapper.writeValueAsBytes(getPracticeManagement())))
				.andExpect(status().isOk()).andExpect(jsonPath("$.status", is("SUCESS"))).andDo(print());

	}

	List<PracticeManagementDTO> getListPracticeExpenses() {

		List<PracticeManagementDTO> toReturn = new ArrayList<>();
		toReturn.add(getPracticeManagement());
		return toReturn;

	}

	ApiResponse getApiFalse() {
		ApiResponse at = new ApiResponse("FAILURE", "", "iNTERNAL ERROR : PracticeId doesn't exist");
		return at;

	}

	PracticeManagementDTO getPracticeManagement() {

		PracticeManagementDTO object1 = new PracticeManagementDTO();
		object1.setId(1);
		object1.setCreatedBy("ashwin");
		object1.setModifiedBy("raksha");
		return object1;

	}

	PracticeManagementDTO getPracticeManagement1()

	{
		PracticeManagementDTO object1 = new PracticeManagementDTO();
		object1.setCreatedBy("ashwin");
		object1.setModifiedBy("raksha");
		return null;

	}
}
