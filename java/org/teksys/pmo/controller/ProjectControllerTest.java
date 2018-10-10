package org.teksys.pmo.controller;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.is;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.teksys.pmo.domain.BDMDTO;
import org.teksys.pmo.domain.ClientDTO;
import org.teksys.pmo.domain.PracticeDTO;
import org.teksys.pmo.domain.ProjectDTO;
import org.teksys.pmo.domain.RegionDTO;
import org.teksys.pmo.domain.SolutionExecutiveDTO;
import org.teksys.pmo.service.ProjectService;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
public class ProjectControllerTest {

	@Mock
	ProjectService projectService;

	@InjectMocks
	ProjectController controllerUnderTest;

	private MockMvc mockMvc;

	public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
			MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		this.mockMvc = MockMvcBuilders.standaloneSetup(controllerUnderTest).build();
	}

	@Test
	public void findAllClientsTest() throws Exception {
		List<ClientDTO> clientDTOs = Arrays.asList(clientDTO1(), clientDTO2());
		when(projectService.findAllClients()).thenReturn(clientDTOs);

		this.mockMvc.perform(get("/findAllClients")).andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(2)))
				.andExpect(jsonPath("$[0].clientName", is("client1")))
				.andExpect(jsonPath("$[0].message", is("message1")));
	}

	@Test
	public void findAllProjectsTest() throws Exception {
		List<ProjectDTO> projectDTOs = Arrays.asList(projectDTO());
		when(projectService.findAllProjects()).thenReturn(projectDTOs);

		this.mockMvc.perform(get("/findAllProjects")).andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(1)))
				.andExpect(jsonPath("$[0].projectId", is("1")))
				.andExpect(jsonPath("$[0].projectName", is("projectName")));
	}

	@Test
	public void findAllRegionsTest() throws Exception {    
		List<RegionDTO> regionDTOs = Arrays.asList(regionDto1(), regionDto2());
		when(projectService.findAllRegions()).thenReturn(regionDTOs);

		this.mockMvc.perform(get("/findAllRegions")).andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(2)))
				.andExpect(jsonPath("$[0].name", is("regionName1"))).andExpect(jsonPath("$[0].regionId", is(1)));
	}

	@Test
	public void findAllBdmTest() throws Exception {
		List<BDMDTO> bdmdtos = Arrays.asList(bdmDto1(), bdmDto2());
		when(projectService.findAllBdm()).thenReturn(bdmdtos);

		this.mockMvc.perform(get("/findAllBdm")).andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(2)))
				.andExpect(jsonPath("$[0].name", is("bdm1"))).andExpect(jsonPath("$[0].bdmId", is(1)));
	}

	@Test
	public void findAllSolutionExecutiveTest() throws Exception {
		List<SolutionExecutiveDTO> solutionExecutiveDTOs = Arrays.asList(solutionExecutiveDTO1(),
				solutionExecutiveDTO2());
		when(projectService.findAllSolutionExecutive()).thenReturn(solutionExecutiveDTOs);

		this.mockMvc.perform(get("/findAllSolutionExecutive")).andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(2))).andExpect(jsonPath("$[0].name", is("sol1")))
				.andExpect(jsonPath("$[0].seId", is(1)));
	}

	@Test
	public void findAllPracticesTest() throws Exception {
		List<PracticeDTO> practiceDTOs = Arrays.asList(practiceDTO1(), practiceDTO2());
		when(projectService.findAllPractices()).thenReturn(practiceDTOs);

		this.mockMvc.perform(get("/findAllPractices")).andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(2)))
				.andExpect(jsonPath("$[0].companyId", is("1111"))).andExpect(jsonPath("$[0].practiceHead", is("head1")))
				.andExpect(jsonPath("$[0].practiceId", is("1")))
				.andExpect(jsonPath("$[0].practiceName", is("practice1")));
	}

	@Test
	public void addProjectTest() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

		this.mockMvc.perform(
				post("/addProject").contentType(APPLICATION_JSON_UTF8).content(mapper.writeValueAsBytes(projectDTO())))
				.andExpect(status().isOk());

	}

	@Test
	public void updateProjectTest() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

		this.mockMvc.perform(post("/updateProject").contentType(APPLICATION_JSON_UTF8)
				.content(mapper.writeValueAsBytes(projectDTO()))).andExpect(status().isOk());

	}

	@Test
	public void findProjectsTest() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

		this.mockMvc.perform(
				post("/findProject").contentType(APPLICATION_JSON_UTF8).content(mapper.writeValueAsBytes(projectDTO())))
				.andExpect(status().isOk());

	}

	@Test
	public void addClientTest() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

		this.mockMvc.perform(
				post("/addClient").contentType(APPLICATION_JSON_UTF8).content(mapper.writeValueAsBytes(clientDTO1())))
				.andExpect(status().isOk());

	}

	@Test
	public void editClientTest() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

		this.mockMvc.perform(post("/editClient/1").contentType(APPLICATION_JSON_UTF8)
				.content(mapper.writeValueAsBytes(clientDTO1()))).andExpect(status().isOk());

	}

	public ProjectDTO projectDTO() {
		ProjectDTO projectDTO = new ProjectDTO();
		projectDTO.setProjectId("1");
		projectDTO.setProjectName("projectName");
		return projectDTO;
	}

	public ClientDTO clientDTO1() {
		ClientDTO clientDTO = new ClientDTO();
		clientDTO.setClientName("client1");
		clientDTO.setId(1);
		clientDTO.setMessage("message1");
		return clientDTO;
	}

	public ClientDTO clientDTO2() {
		ClientDTO clientDTO = new ClientDTO();
		clientDTO.setClientName("client2");
		clientDTO.setId(2);
		clientDTO.setMessage("message2");
		return clientDTO;
	}

	public PracticeDTO practiceDTO1() {
		PracticeDTO practiceDTO = new PracticeDTO();
		practiceDTO.setCompanyId("1111");
		practiceDTO.setPracticeHead("head1");
		practiceDTO.setPracticeId("1");
		practiceDTO.setPracticeName("practice1");
		return practiceDTO;
	}

	public PracticeDTO practiceDTO2() {
		PracticeDTO practiceDTO = new PracticeDTO();
		practiceDTO.setCompanyId("1112");
		practiceDTO.setPracticeHead("head2");
		practiceDTO.setPracticeId("2");
		practiceDTO.setPracticeName("practice2");
		return practiceDTO;
	}

	public SolutionExecutiveDTO solutionExecutiveDTO1() {
		SolutionExecutiveDTO solutionExecutiveDTO = new SolutionExecutiveDTO();
		solutionExecutiveDTO.setName("sol1");
		solutionExecutiveDTO.setSeId(1);
		return solutionExecutiveDTO;
	}

	public SolutionExecutiveDTO solutionExecutiveDTO2() {
		SolutionExecutiveDTO solutionExecutiveDTO = new SolutionExecutiveDTO();
		solutionExecutiveDTO.setName("sol2");
		solutionExecutiveDTO.setSeId(2);
		return solutionExecutiveDTO;
	}

	public BDMDTO bdmDto1() {
		BDMDTO bdmdto = new BDMDTO();
		bdmdto.setBdmId(1);
		bdmdto.setName("bdm1");
		return bdmdto;
	}

	public BDMDTO bdmDto2() {
		BDMDTO bdmdto = new BDMDTO();
		bdmdto.setBdmId(2);
		bdmdto.setName("bdm2");
		return bdmdto;
	}

	public RegionDTO regionDto1() {
		RegionDTO regionDTO = new RegionDTO();
		regionDTO.setName("regionName1");
		regionDTO.setRegionId(1);
		return regionDTO;
	}

	public RegionDTO regionDto2() {
		RegionDTO regionDTO = new RegionDTO();
		regionDTO.setName("regionName2");
		regionDTO.setRegionId(2);
		return regionDTO;
	}

}
