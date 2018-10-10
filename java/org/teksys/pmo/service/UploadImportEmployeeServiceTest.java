package org.teksys.pmo.service;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;
import org.teksys.pmo.dao.EmployeeRepository;
import org.teksys.pmo.dao.EmployeeSalaryPerHourRepository;
import org.teksys.pmo.dao.UploadImportEmployeeRepository;
import org.teksys.pmo.model.EmployeeEntity;
import org.teksys.pmo.model.EmployeeSalaryPerHour;
import org.teksys.pmo.model.ImportEmployee;

@RunWith(SpringRunner.class)
public class UploadImportEmployeeServiceTest {

	@Mock
	UploadImportEmployeeRepository uploadImportEmployeeRepository;
	@Mock
	EmployeeSalaryPerHourRepository employeeSalaryPerHourRepository;
	@Mock
	EmployeeService employeeService;
	@Mock
	EmployeeRepository employeeRepository;
	@Mock
	EmployeeRepository employeeDao;

	@InjectMocks
	UploadImportEmployeeService serviceUnderTest;

	@Before
	public void setup() throws ParseException {

		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void storeFileIfTest() throws Exception {

		when(employeeDao.findByCompanyEmpId(anyString())).thenReturn(employeeEntity());
		when(employeeSalaryPerHourRepository.findByCompanyEmpId(anyString()))
				.thenReturn(Arrays.asList(employeeSalaryPerHour1(), employeeSalaryPerHour2()));
		when(uploadImportEmployeeRepository.save(any(ImportEmployee.class))).thenReturn(importEmployee());
		ImportEmployee expected = serviceUnderTest.storeFile(file());

		assertThat("admin", is(expected.getCreatedBy()));

	}


	ImportEmployee importEmployee() {
		ImportEmployee importEmployee = new ImportEmployee();
		importEmployee.setModifiedBy("admin-modifiedBy");
		importEmployee.setCreatedBy("admin");
		importEmployee.setFileName("test.json");
		importEmployee.setDateCreated(new Date());
		return importEmployee;
	}

	public EmployeeEntity employeeEntity() {
		EmployeeEntity employeeEntity = new EmployeeEntity();
		employeeEntity.setCompanyEmpId("1");
		return employeeEntity;
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

	public MultipartFile file() throws IOException {

		FileInputStream inputFile = new FileInputStream("src/test/java/resources/importemp.xlsx");
		MockMultipartFile file = new MockMultipartFile("file", "copy.xlsx", "multipart/form-data", inputFile);

		return file;
	}

	public MultipartFile file2() throws IOException {

		FileInputStream inputFile = new FileInputStream("src/test/java/resources/importemp.xlsx");
		MockMultipartFile file = new MockMultipartFile("file", "copy.xlsx", "multipart/form-data", inputFile);

		return file;
	}

	public InputStream iStream() {

		InputStream iStream = new InputStream() {

			@Override
			public int read() throws IOException {
				// TODO Auto-generated method stub
				return 5;
			}
		};
		return iStream;
	}

}
