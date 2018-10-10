package org.teksys.pmo.controller;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.Charset;
import java.util.Arrays;
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
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.teksys.pmo.dao.EmpTimeReportRepository;
import org.teksys.pmo.domain.EmpTimeReport;
import org.teksys.pmo.model.EmpTimeReportEntity;
import org.teksys.pmo.model.EmpTimeSheetCompoundKeyEntity;
import org.teksys.pmo.service.EmpTimeReportService;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
public class EmpTimeReportControllerTest {

	@Mock
	EmpTimeReportService empTimeReportService;

	@Mock
	EmpTimeReportRepository empTimeReportRepository;

	@InjectMocks
	EmpTimeReportController controllerUnderTest;

	private MockMvc mockMvc;

	public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
			MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);

		this.mockMvc = MockMvcBuilders.standaloneSetup(controllerUnderTest).build();
	}

	@Test
	public void getAllTimeSheetReports() throws Exception {
		List<EmpTimeReportEntity> empTimeReportEntitylist = Arrays.asList(empTimeReportEntity1(),
				empTimeReportEntity2());
		when(empTimeReportRepository.findAll()).thenReturn(empTimeReportEntitylist);

		this.mockMvc.perform(get("/getAllTimeSheetReports")).andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(2))).andExpect(jsonPath("$[0].empName", is("employeeName1")))
				.andExpect(jsonPath("$[0].day2", is(20.1))).andExpect(jsonPath("$[0].projectName", is("projectName1")));

	}

	@Test
	public void searchEmpTimeSheet() throws Exception {

		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		when(empTimeReportService.searchEmpTimeSheet(any(EmpTimeReport.class)))
				.thenReturn(Arrays.asList(empTimeReport1(), empTimeReport2()));
		this.mockMvc
				.perform(post("/searchEmpTimeSheet").contentType(APPLICATION_JSON_UTF8)
						.content(mapper.writeValueAsBytes(empTimeReport1())))
				.andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(2)));

	}

	@Test()
	public void searchEmpTimeSheetExceptionTest() throws Exception {

		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		when(empTimeReportService.searchEmpTimeSheet(any(EmpTimeReport.class)))
				.thenThrow(JsonProcessingException.class);
		ResultActions result = this.mockMvc.perform(post("/searchEmpTimeSheet").contentType(APPLICATION_JSON_UTF8)
				.content(mapper.writeValueAsBytes(empTimeReport1()))).andExpect(status().isOk());
	}

	public EmpTimeReport empTimeReport1() {

		EmpTimeReport empTimeReport1 = new EmpTimeReport();
		empTimeReport1.setDay1(22.2f);
		EmpTimeSheetCompoundKeyEntity empTimeSheetCompoundKey = new EmpTimeSheetCompoundKeyEntity("type1",
				"companyEmpId1", "companyProjectId1", "8", 2012);
		empTimeReport1.setEmpTimeSheetCompoundKey(empTimeSheetCompoundKey);
		return empTimeReport1;
	}

	public EmpTimeReport empTimeReport2() {

		EmpTimeReport empTimeReport2 = new EmpTimeReport();
		empTimeReport2.setDay1(22.2f);
		EmpTimeSheetCompoundKeyEntity empTimeSheetCompoundKey = new EmpTimeSheetCompoundKeyEntity("type2",
				"companyEmpId2", "companyProjectId2", "8", 2014);
		empTimeReport2.setEmpTimeSheetCompoundKey(empTimeSheetCompoundKey);
		return empTimeReport2;
	}

	public EmpTimeReportEntity empTimeReportEntity1() {
		EmpTimeReportEntity empTimeReportEntity1 = new EmpTimeReportEntity();
		Float day1 = 20.1f;
		empTimeReportEntity1.setDay1(day1);
		empTimeReportEntity1.setDay2(day1);
		empTimeReportEntity1.setEmpName("employeeName1");
		empTimeReportEntity1.setProjectName("projectName1");
		EmpTimeSheetCompoundKeyEntity empTimeSheetCompoundKeyentity = new EmpTimeSheetCompoundKeyEntity("type1",
				"companyEmpId1", "companyProjectId1", "8", 2012);
		empTimeReportEntity1.setEmpTimeSheetCompoundKey(empTimeSheetCompoundKeyentity);
		return empTimeReportEntity1;
	}

	public EmpTimeReportEntity empTimeReportEntity2() {
		EmpTimeReportEntity empTimeReportEntity2 = new EmpTimeReportEntity();
		Float day1 = 20.1f;
		empTimeReportEntity2.setDay1(day1);
		empTimeReportEntity2.setDay2(day1);
		empTimeReportEntity2.setEmpName("employeeName2");
		empTimeReportEntity2.setProjectName("projectName2");
		EmpTimeSheetCompoundKeyEntity empTimeSheetCompoundKeyentity = new EmpTimeSheetCompoundKeyEntity("type1",
				"companyEmpId1", "companyProjectId1", "8", 2012);
		empTimeReportEntity2.setEmpTimeSheetCompoundKey(empTimeSheetCompoundKeyentity);
		return empTimeReportEntity2;
	}
}
