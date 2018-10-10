package org.teksys.pmo.controller;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;
import org.teksys.pmo.dao.EmployeeCvRepo;
import org.teksys.pmo.dao.EmployeeRepository;
import org.teksys.pmo.domain.Employee;
import org.teksys.pmo.domain.EmployeeAllDetails;
import org.teksys.pmo.domain.EmployeeSalaryPerHourDTO;
import org.teksys.pmo.model.EmployeeCV;
import org.teksys.pmo.model.EmployeeEntity;
import org.teksys.pmo.model.EmployeeSalaryPerHour;
import org.teksys.pmo.model.ImportEmployee;
import org.teksys.pmo.service.EmployeeServiceImpl;
import org.teksys.pmo.service.UploadImportEmployeeService;
import static org.mockito.Mockito.doNothing;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
public class EmployeeControllerTest {

	@Mock
	private EmployeeServiceImpl employeeService;
	@Mock
	private UploadImportEmployeeService uploadImportEmployeeService;
	@Mock
	private EmployeeRepository employeeRepository;
	@Mock
	private EmployeeCvRepo employeeCvRepo;

	@InjectMocks
	EmployeeController controllerUnderTest;

	private MockMvc mockMvc;

	public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
			MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		this.mockMvc = MockMvcBuilders.standaloneSetup(controllerUnderTest).build();

	}

	@Test
	public void getAllEmployeesTest() throws Exception {
		List<EmployeeAllDetails> employees = Arrays.asList(employeeAllDetails1(), employeeAllDetails2());
		when(employeeService.getAllEmployees()).thenReturn(employees);

		this.mockMvc.perform(get("/getAllEmployees")).andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(2)))
				.andExpect(jsonPath("$[0].companyID", is(1))).andExpect(jsonPath("$[0].firstName", is("fname")));
				
	}

	
	@Test
	public void findAllEmployeesTest() throws Exception {
		List<EmployeeEntity> employees = Arrays.asList(employee1(), employee2());
		when(employeeService.findAllEmployees()).thenReturn(employees);

		this.mockMvc.perform(get("/findAllEmployees")).andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(2)))
				.andExpect(jsonPath("$[0].firstName", is("emp1"))).andExpect(jsonPath("$[0].lastName", is("last1")))
				.andExpect(jsonPath("$[0].empId", is("1111")));
	}

	@Test
	public void findEmpNameTest() throws Exception {
		List<EmployeeEntity> employees = Arrays.asList(employee1(), employee2());
		when(employeeRepository.findEmpName()).thenReturn(employees);
		this.mockMvc.perform(get("/findEmpName")).andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(2)))
				.andExpect(jsonPath("$[0].firstName", is("emp1"))).andExpect(jsonPath("$[0].lastName", is("last1")))
				.andExpect(jsonPath("$[0].empId", is("1111")));

	}

	@Test
	public void findAllEmployeeCvTest() throws Exception {
		List<EmployeeCV> employeeCVs = Arrays.asList(employeeCV1(), employeeCV2());
		when(employeeCvRepo.findAllEmployeeCv()).thenReturn(employeeCVs);
		this.mockMvc.perform(get("/findAllEmployeeCv")).andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(2)))
				.andExpect(jsonPath("$[0].cvId", is(1))).andExpect(jsonPath("$[0].cvName", is("cv1")))
				.andExpect(jsonPath("$[0].type", is("1")));
	}

	@Test
	public void findEmpByLocationTest() throws Exception {
		List<Employee> employees = Arrays.asList(employee3(), employee4());
		given(employeeService.findEmpByLocation("whitefield")).willReturn(employees);
		this.mockMvc.perform(get("/findEmpByLocation/whitefield")).andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(2))).andExpect(jsonPath("$[0].firstName", is("emp1")))
				.andExpect(jsonPath("$[0].lastName", is("last1"))).andExpect(jsonPath("$[0].empId", is("1111")));
	}

	@Test
	public void findEmployeeCvById() throws Exception {
		List<EmployeeCV> employeeCVs = Arrays.asList(employeeCV1(), employeeCV2());
		when(employeeCvRepo.findEmployeeCvById("3167")).thenReturn(employeeCVs);
		this.mockMvc.perform(get("/findEmployeeCvById/3167")).andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(2))).andExpect(jsonPath("$[0].cvId", is(1)))
				.andExpect(jsonPath("$[0].cvName", is("cv1"))).andExpect(jsonPath("$[0].type", is("1")));
	}

	@Test
	public void empIdExists() throws Exception {
		when(employeeCvRepo.empIdExists(anyString())).thenReturn(1);
		this.mockMvc.perform(get("/empIdExists/04195059")).andExpect(status().isOk()).andExpect(jsonPath("$", is(1)));

	}

	@Test
	public void deleteEmployeeCvTest() throws Exception {

		doNothing().when(employeeService).deleteEmployeeCv("1111");
		this.mockMvc.perform(post("/deleteCv/11").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
	}

//	@Test
//	public void deleteEmployeeCvTest() throws Exception {
//		employeeService.deleteEmployeeCv(anyString());
//		verify(employeeService.deleteEmployeeCv("1"), times(1)).deleteEmployeeCv(anyString());
//	}

	@Test
	public void downloadFileTest() throws Exception {
		when(employeeService.downloadFile(anyString())).thenReturn(empCvByte());
		this.mockMvc.perform(get("/downloadCv/04195059")).andExpect(status().isOk());
//		.andExpect(jsonPath("$", is(100)));
	}

	@Test
	public void addEmployeeTest() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

		this.mockMvc.perform(
				post("/addEmployee").contentType(APPLICATION_JSON_UTF8).content(mapper.writeValueAsBytes(employee3())))
				.andExpect(status().isOk());
	}

	@Test
	public void addEmpSalTest() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

		this.mockMvc.perform(put("/addEmpSal").contentType(APPLICATION_JSON_UTF8)
				.content(mapper.writeValueAsBytes(employeeSalaryPerHour()))).andExpect(status().isOk());

	}

	@Test
	public void findEmployeeTest() throws Exception {

		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		when(employeeService.findEmployees(any(Employee.class))).thenReturn(Arrays.asList(employee3(), employee4()));
		this.mockMvc
				.perform(post("/findEmployee").contentType(APPLICATION_JSON_UTF8)
						.content(mapper.writeValueAsBytes(employee3())))
				.andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(2)));

	}

	@Test
	public void findEmployeeExceptionTest() throws Exception {

		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		when(employeeService.findEmployees(any(Employee.class))).thenThrow(IOException.class);
		this.mockMvc.perform(
				post("/findEmployee").contentType(APPLICATION_JSON_UTF8).content(mapper.writeValueAsBytes(employee3())))
				.andExpect(status().isOk());

	}

	@Test
	public void findEmployeesWithSpecificDataTest() throws Exception {

		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		when(employeeService.findEmployeesWithSpecificData(any(Employee.class)))
				.thenReturn(Arrays.asList(employee3(), employee4()));
		this.mockMvc
				.perform(post("/findEmployeesWithSpecificData").contentType(APPLICATION_JSON_UTF8)
						.content(mapper.writeValueAsBytes(employee3())))
				.andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(2)));

	}

	@Test
	public void importFileTest() throws Exception {

		// Mock Request
		MockMultipartFile jsonFile = new MockMultipartFile("test.json", "", "application/json",
				"{\"key1\": \"value1\"}".getBytes());

		// Mock Response
		ImportEmployee response = new ImportEmployee();
		Mockito.when(uploadImportEmployeeService.storeFile(Mockito.any(MultipartFile.class))).thenReturn(response);

		mockMvc.perform(MockMvcRequestBuilders.fileUpload("/importEmployeeFile").file("file", jsonFile.getBytes())
				.characterEncoding("UTF-8")).andExpect(status().isOk());

	}

	@Test
	public void uploadFileTest() throws Exception {

		// Mock Request
		MockMultipartFile jsonFile = new MockMultipartFile("test.json", "", "application/json",
				"{\"key1\": \"value1\"}".getBytes());

		// Mock Response
		EmployeeCV response = new EmployeeCV();
		Mockito.when(employeeService.storeFile(Mockito.any(MultipartFile.class), anyString())).thenReturn(response);

		mockMvc.perform(MockMvcRequestBuilders.fileUpload("/uploadCv/1111").file("file", jsonFile.getBytes())
				.characterEncoding("UTF-8")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));

	}

	@Test
	public void employeeDLTest() throws Exception {

		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		when(employeeService.employeeDL(any(EmployeeSalaryPerHourDTO.class)))
				.thenReturn(Arrays.asList(employeeSalaryPerHourDto()));
		this.mockMvc
				.perform(post("/employeeDL").contentType(APPLICATION_JSON_UTF8)
						.content(mapper.writeValueAsBytes(employeeSalaryPerHourDto())))
				.andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(1)));

	}

	@Test
	public void historyOfDLTest() throws Exception {

		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		when(employeeService.historyOfDL(any(EmployeeSalaryPerHourDTO.class)))
				.thenReturn(Arrays.asList(employeeSalaryPerHourDto()));
		this.mockMvc
				.perform(post("/historyOfDL").contentType(APPLICATION_JSON_UTF8)
						.content(mapper.writeValueAsBytes(employeeSalaryPerHourDto())))
				.andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(1)));

	}

	@Test
	public void editEmployeeTest() throws Exception {

		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		when(employeeService.editEmployee(any(EmployeeEntity.class))).thenReturn(employee2());
		this.mockMvc
				.perform(put("/editEmployee").contentType(APPLICATION_JSON_UTF8)
						.content(mapper.writeValueAsBytes(employee2())))
				.andExpect(status().isOk()).andExpect(jsonPath("$.firstName", is("emp1")));

	}

	public ImportEmployee importEmployee() {
		ImportEmployee importEmployee = new ImportEmployee();
		importEmployee.setId(1);
		importEmployee.setFileName("fileName");
		return importEmployee;
	}

	public EmployeeSalaryPerHour employeeSalaryPerHour() {
		EmployeeSalaryPerHour employeeSalaryPerHour = new EmployeeSalaryPerHour();
		employeeSalaryPerHour.setColumnNo("1");
		employeeSalaryPerHour.setCompanyEmpId("1111");
		return employeeSalaryPerHour;
	}

	public EmployeeSalaryPerHourDTO employeeSalaryPerHourDto() {
		EmployeeSalaryPerHourDTO employeeSalaryPerHour = new EmployeeSalaryPerHourDTO();
		employeeSalaryPerHour.setId(1);
		employeeSalaryPerHour.setCompanyEmpId("1111");
		return employeeSalaryPerHour;
	}

	public byte[] empCvByte() {
		byte[] empCvByte = new byte[100];
		return empCvByte;
	}

	public EmployeeCV employeeCV1() {
		EmployeeCV employeeCV = new EmployeeCV();
		employeeCV.setCvId(1);
		employeeCV.setCvName("cv1");
		employeeCV.setType("1");
		return employeeCV;
	}

	public EmployeeCV employeeCV2() {
		EmployeeCV employeeCV = new EmployeeCV();
		employeeCV.setCvId(2);
		employeeCV.setCvName("cv2");
		employeeCV.setType("2");
		return employeeCV;
	}

	public Employee employee3() {
		Employee employee = new Employee();
		employee.setFirstName("emp1");
		employee.setLastName("last1");
		employee.setEmpId("1111");
		return employee;
	}

	public Employee employee4() {
		Employee employee = new Employee();
		employee.setFirstName("emp1");
		employee.setLastName("last1");
		employee.setEmpId("1111");
		return employee;
	}

	public EmployeeEntity employee1() {
		EmployeeEntity employee = new EmployeeEntity();
		employee.setFirstName("emp1");
		employee.setLastName("last1");
		employee.setEmpId("1111");
		return employee;
	}

	public EmployeeEntity employee2() {
		EmployeeEntity employee = new EmployeeEntity();
		employee.setFirstName("emp1");
		employee.setLastName("last1");
		employee.setEmpId("1111");
		return employee;
	}
	
	public EmployeeAllDetails employeeAllDetails1() {
		EmployeeAllDetails employeeAllDetails = new EmployeeAllDetails();
		employeeAllDetails.setCompanyID(1);
		employeeAllDetails.setFirstName("fname");
		return employeeAllDetails;
	}
	
	public EmployeeAllDetails employeeAllDetails2() {
		EmployeeAllDetails employeeAllDetails = new EmployeeAllDetails();
		employeeAllDetails.setCompanyID(2);
		employeeAllDetails.setFirstName("fname2");
		return employeeAllDetails;
	}
}
