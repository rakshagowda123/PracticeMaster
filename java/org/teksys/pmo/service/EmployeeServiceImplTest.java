package org.teksys.pmo.service;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;
import org.teksys.pmo.dao.EmployeeCvRepo;
import org.teksys.pmo.dao.EmployeeRepository;
import org.teksys.pmo.dao.EmployeeSalaryPerHourRepository;
import org.teksys.pmo.dao.EmployeeSalaryPerHourRepositoryCustom;
import org.teksys.pmo.domain.Employee;
import org.teksys.pmo.domain.EmployeeAllDetails;
import org.teksys.pmo.domain.EmployeeCV;
import org.teksys.pmo.domain.EmployeeSalaryPerHourDTO;
import org.teksys.pmo.model.EmployeeEntity;
import org.teksys.pmo.model.EmployeeSalaryPerHour;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
public class EmployeeServiceImplTest {

	@Mock
	private EmployeeRepository employeeDao;
	@Mock
	EmployeeSalaryPerHourRepository empSalDao;
	@Mock
	private EmployeeCvRepo employeeCvRepo;
	@Mock
	private EmployeeSalaryPerHourRepositoryCustom empSalCustDao;
	@Mock
	ObjectMapper objectMapper;
	@InjectMocks
	EmployeeServiceImpl serviceUnderTest;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void findAllEmployeesTest() {
		when(employeeDao.findAll()).thenReturn(getEmployeeEntities());
		List<EmployeeEntity> expected = serviceUnderTest.findAllEmployees();
		assertThat(2, is(expected.size()));
		assertThat("fname2", is(expected.get(1).getFirstName()));
	}

	@Test
	public void getAllEmployeesTest() {
		when(employeeDao.getAllEmployees()).thenReturn(employeeAllDetails());
		List<EmployeeAllDetails> expected = serviceUnderTest.getAllEmployees();
		assertThat(2, is(expected.size()));
		assertThat("fname", is(expected.get(0).getFirstName()));
	}
	
	@Test
	public void findAllEmployeesTestException() {
		when(employeeDao.findAll()).thenThrow(Exception.class);
		List<EmployeeEntity> expected = serviceUnderTest.findAllEmployees();
		assertThat(0, is(expected.size()));

	}

	@Test
	public void findEmpByLocationTest() {
		when(employeeDao.searchEmployee(anyString())).thenReturn(getEmployees());
		List<Employee> expected = serviceUnderTest.findEmpByLocation("location");
		assertThat(2, is(expected.size()));
	}

	@Test
	public void addEmployeeTest() {
		serviceUnderTest.addEmployee(employee3());
		verify(employeeDao, times(1)).save(any(EmployeeEntity.class));
	}

	@Test
	public void addEmployeeExceptionTest() throws Exception {

		when(employeeDao.save(any(EmployeeEntity.class))).thenThrow(Exception.class);
		serviceUnderTest.addEmployee(employee3());
	}

	@Test
	public void editEmployeeTest() {
		when(employeeDao.findByCompanyEmpId(anyString())).thenReturn(employeeEntity1());
		serviceUnderTest.editEmployee(employeeEntity1());
		verify(employeeDao, times(1)).save(any(EmployeeEntity.class));
	}

	@Test
	public void searchEmployeeTest() throws IOException {
		when(employeeDao.findEmployeeByAll(any(Employee.class))).thenReturn(Arrays.asList(employee3(), employee4()));
		List<Employee> expected = serviceUnderTest.searchEmployee(employee3());
		assertThat(2, is(expected.size()));
		assertThat("emp2", is(expected.get(1).getFirstName()));
	}

	// this
	@Test
	public void findEmployeesIfTest() throws IOException {

		when(employeeDao.findEmployeeByAll(any(Employee.class))).thenReturn(getEmployees());

		List<Employee> expected = serviceUnderTest.findEmployees(employee3());

		assertThat(2, is(expected.size()));
	}

	@Test
	public void findEmployeesElseTest() throws Exception {

		when(employeeDao.findEmployees(any(Employee.class))).thenReturn(getEmployeeEntities());

		when(objectMapper.writeValueAsString(any(List.class))).thenReturn("Sahib SIngh");

		when(objectMapper.readValue(anyString(), any(TypeReference.class))).thenReturn(getEmployees());

		when(employeeDao.findEmployeeByAll(any(Employee.class))).thenReturn(Arrays.asList(employee3(), employee4()));
		List<Employee> expected = serviceUnderTest.findEmployees(employee4());

		assertThat(2, is(expected.size()));

	}

	@Test
	public void employeeDLTest() throws Exception {

		when(empSalCustDao.employeeDL(any(EmployeeSalaryPerHourDTO.class))).thenReturn(getEmployeeSalaryPerHourList());

		when(objectMapper.writeValueAsString(any(List.class))).thenReturn("string1");

		when(objectMapper.readValue(anyString(), any(TypeReference.class)))
				.thenReturn(getEmployeeSalaryPerHourDtoList());

		List<EmployeeSalaryPerHourDTO> expected = serviceUnderTest.employeeDL(employeeSalaryPerHourDTO1());

		assertThat(2, is(expected.size()));

	}

	@Test
	public void employeeDLExceptionTest() throws Exception {

		when(empSalCustDao.employeeDL(any(EmployeeSalaryPerHourDTO.class))).thenThrow(Exception.class);

		when(objectMapper.writeValueAsString(any(List.class))).thenThrow(Exception.class);

		when(objectMapper.readValue(anyString(), any(TypeReference.class))).thenThrow(Exception.class);

		List<EmployeeSalaryPerHourDTO> expected = serviceUnderTest.employeeDL(employeeSalaryPerHourDTO1());

	}

	@Test
	public void addEmpSalTest() {
		when(empSalDao.findByCompanyEmpId(anyString())).thenReturn(getEmployeeSalaryPerHourList());

		serviceUnderTest.addEmpSal(employeeSalaryPerHour1());
//		verify(employeeDao, times(1)).save(anyList());
	}

	@Test
	public void findEmployeesWithSpecificData() throws IOException {
		when(employeeDao.findEmployeesWithSpecificData(any(Employee.class))).thenReturn(getEmployeeEntities());
		when(objectMapper.writeValueAsString(getEmployeeEntities().getClass())).thenReturn("string 1");
		when(objectMapper.readValue(anyString(), any(TypeReference.class))).thenReturn(getEmployees());
		List<Employee> employees = serviceUnderTest.findEmployeesWithSpecificData(employee3());
		assertThat(2, is(employees.size()));
	}

	@Test
	public void findEmployeesWithSpecificDataException() throws IOException {
		when(employeeDao.findEmployeesWithSpecificData(any(Employee.class))).thenThrow(IOException.class);
		when(objectMapper.writeValueAsString(any(List.class))).thenThrow(IOException.class);
		when(objectMapper.readValue(anyString(), any(TypeReference.class))).thenThrow(IOException.class);
		List<Employee> employees = serviceUnderTest.findEmployeesWithSpecificData(employee3());

	}

	@Test
	public void historyOfDLTest() throws IOException {
		when(empSalCustDao.historyOfDL(any(EmployeeSalaryPerHourDTO.class))).thenReturn(getEmployeeSalaryPerHourList());

		when(objectMapper.writeValueAsString(getEmployeeSalaryPerHourList().getClass())).thenReturn("String");

		when(objectMapper.readValue(anyString(), any(TypeReference.class)))
				.thenReturn(getEmployeeSalaryPerHourDtoList());

		List<EmployeeSalaryPerHourDTO> expected = serviceUnderTest.historyOfDL(employeeSalaryPerHourDTO1());
		assertThat(2, is(expected.size()));
	}

	@Test
	public void historyOfDLExceptionTest() throws Exception {
		when(empSalCustDao.historyOfDL(any(EmployeeSalaryPerHourDTO.class))).thenThrow(Exception.class);

		when(objectMapper.writeValueAsString(getEmployeeSalaryPerHourList().getClass())).thenThrow(Exception.class);

		when(objectMapper.readValue(anyString(), any(TypeReference.class))).thenThrow(Exception.class);

		List<EmployeeSalaryPerHourDTO> expected = serviceUnderTest.historyOfDL(employeeSalaryPerHourDTO1());

	}

	@Test
	public void deleteEmployeeCvTest() {
		serviceUnderTest.deleteEmployeeCv(anyString());
		verify(employeeCvRepo, times(1)).deleteEmployeeCv(anyString());
	}

	@Test
	public void downloadFileTest() {
		when(employeeCvRepo.findCvByEmpId(anyString())).thenReturn(getByteArray());
		byte[] expected = serviceUnderTest.downloadFile(anyString());
		assertThat(100, is(expected.length));
	}


	@Test
	public void storeFileElseElseTest() {
		when(employeeCvRepo.empIdExists(anyString())).thenReturn(0);
		serviceUnderTest.storeFile(multipartFile2(), "1111");
		// verify
	}

	@Test
	public void storeFileElseExceptionTest() throws Exception {
		when(employeeCvRepo.empIdExists(anyString())).thenReturn(0);
		MultipartFile mockM = mock(MultipartFile.class);
		when(mockM.getContentType()).thenThrow(Exception.class);

		// doNothing().when(any(EmployeeCvRepo.class).save(anyList()));
//		doThrow(Exception.class).when(employeeCvRepo).save(any(Iterable.class));

//        thrown.expectMessage("Name must not be null");
//		when(any(MultipartFile.class).getBytes()).thenThrow(Exception.class);
		serviceUnderTest.storeFile(multipartFile2(), "1111");
	}

	public List<EmployeeCV> employeeCVs() {
		List<EmployeeCV> list = new ArrayList<EmployeeCV>();
		list.add(employeeCV1());
		list.add(employeeCV2());
		return list;
	}

	public EmployeeCV employeeCV1() {
		EmployeeCV employeeCV = new EmployeeCV();
		employeeCV.setCvId(1);
		return employeeCV;
	}

	public EmployeeCV employeeCV2() {
		EmployeeCV employeeCV = new EmployeeCV();
		employeeCV.setCvId(2);
		return employeeCV;
	}

	public MultipartFile multipartFile1() {
		MultipartFile multipartFile = new MultipartFile() {

			@Override
			public void transferTo(File dest) throws IOException, IllegalStateException {
				// TODO Auto-generated method stub

			}

			@Override
			public boolean isEmpty() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public long getSize() {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public String getOriginalFilename() {
				// TODO Auto-generated method stub
				return "originalName";
			}

			@Override
			public String getName() {
				// TODO Auto-generated method stub
				return "name";
			}

			@Override
			public InputStream getInputStream() throws IOException {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public String getContentType() {
				// TODO Auto-generated method stub
				return "application/msword";
			}

			@Override
			public byte[] getBytes() throws IOException {
				// TODO Auto-generated method stub
				return null;
			}
		};
		return multipartFile;
	}

	public MultipartFile multipartFile2() {
		MultipartFile multipartFile = new MultipartFile() {

			@Override
			public void transferTo(File dest) throws IOException, IllegalStateException {
				// TODO Auto-generated method stub

			}

			@Override
			public boolean isEmpty() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public long getSize() {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public String getOriginalFilename() {
				// TODO Auto-generated method stub
				return "originalName";
			}

			@Override
			public String getName() {
				// TODO Auto-generated method stub
				return "name";
			}

			@Override
			public InputStream getInputStream() throws IOException {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public String getContentType() {
				// TODO Auto-generated method stub
				return "notapplication/msword";
			}

			@Override
			public byte[] getBytes() throws IOException {
				// TODO Auto-generated method stub
				return getByteArray();
			}
		};
		return multipartFile;
	}

	
	List<Employee> getEmployees() {
		List<Employee> employees = new ArrayList<Employee>();
		employees.add(employee4());
		employees.add(employee3());
		return employees;
	}

	List<EmployeeEntity> getEmployeeEntities() {
		List<EmployeeEntity> employeeEntities = new ArrayList<EmployeeEntity>();
		employeeEntities.add(employeeEntity1());
		employeeEntities.add(employeeEntity2());
		return employeeEntities;
	}

	List<EmployeeSalaryPerHour> getEmployeeSalaryPerHourList() {
		List<EmployeeSalaryPerHour> list = new ArrayList<EmployeeSalaryPerHour>();
		list.add(employeeSalaryPerHour1());
		list.add(employeeSalaryPerHour2());
		return list;
	}

	List<EmployeeSalaryPerHourDTO> getEmployeeSalaryPerHourDtoList() {
		List<EmployeeSalaryPerHourDTO> list = new ArrayList<EmployeeSalaryPerHourDTO>();
		list.add(employeeSalaryPerHourDTO2());
		list.add(employeeSalaryPerHourDTO2());
		return list;
	}

	public EmployeeSalaryPerHourDTO employeeSalaryPerHourDTO1() {
		EmployeeSalaryPerHourDTO employeeSalaryPerHourDTO = new EmployeeSalaryPerHourDTO();
		employeeSalaryPerHourDTO.setCompanyEmpId("1111");
		employeeSalaryPerHourDTO.setId(1);
		return employeeSalaryPerHourDTO;
	}

	public EmployeeSalaryPerHourDTO employeeSalaryPerHourDTO2() {
		EmployeeSalaryPerHourDTO employeeSalaryPerHourDTO = new EmployeeSalaryPerHourDTO();
		employeeSalaryPerHourDTO.setCompanyEmpId("1111");
		employeeSalaryPerHourDTO.setId(1);
		return employeeSalaryPerHourDTO;
	}

	public EmployeeSalaryPerHour employeeSalaryPerHour1() {
		EmployeeSalaryPerHour employeeSalaryPerHour = new EmployeeSalaryPerHour();
		employeeSalaryPerHour.setColumnNo("1");
		employeeSalaryPerHour.setCompanyEmpId("1111");
		employeeSalaryPerHour.setErrorMsg("errormsg");
		return employeeSalaryPerHour;
	}

	public EmployeeSalaryPerHour employeeSalaryPerHour2() {
		EmployeeSalaryPerHour employeeSalaryPerHour = new EmployeeSalaryPerHour();
		employeeSalaryPerHour.setColumnNo("2");
		employeeSalaryPerHour.setCompanyEmpId("2222");
		employeeSalaryPerHour.setErrorMsg("errormsg");
		return employeeSalaryPerHour;
	}

	EmployeeEntity employeeEntity1() {
		EmployeeEntity employeeEntity = new EmployeeEntity();
		employeeEntity.setEmpId("1");
		employeeEntity.setColumnNo("1");
		employeeEntity.setFirstName("fname1");
		return employeeEntity;
	}

	EmployeeEntity employeeEntity2() {
		EmployeeEntity employeeEntity = new EmployeeEntity();
		employeeEntity.setEmpId("2");
		employeeEntity.setColumnNo("1");
		employeeEntity.setFirstName("fname2");
		return employeeEntity;
	}

	public Employee employee3() {
		Employee employee = new Employee();
		employee.setFirstName("emp1");
		employee.setLastName("last1");
		employee.setEmpId("1111");
		employee.setProjectName("project1");
		return employee;
	}

	public Employee employee4() {
		Employee employee = new Employee();
		employee.setFirstName("emp2");
		employee.setLastName("last1");
		employee.setEmpId("1111");
		return employee;
	}

	public byte[] getByteArray() {
		byte[] array = new byte[100];
		return array;
	}
	
	public List<EmployeeAllDetails> employeeAllDetails() {
		List<EmployeeAllDetails> list= new ArrayList<>();
		list.add(employeeAllDetails1());
		list.add(employeeAllDetails2());
		return list;
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
