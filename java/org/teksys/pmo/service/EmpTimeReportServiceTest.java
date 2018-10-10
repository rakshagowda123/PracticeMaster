package org.teksys.pmo.service;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringRunner;
import org.teksys.pmo.dao.EmpTimeReportRepository;
import org.teksys.pmo.domain.EmpTimeReport;
import org.teksys.pmo.model.EmpTimeReportEntity;
import org.teksys.pmo.model.PracticeManagementEntity;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
public class EmpTimeReportServiceTest {

	@Mock
	private EmpTimeReportRepository empTimeReportDao;

	@Mock
	private ObjectMapper objectMapper;

	@Mock
	private EntityManager entityManager;
	@Mock
	private Query query;
	@InjectMocks
	EmpTimeReportService serviceUnderTest;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void searchEmpTimeSheetTest() throws IOException {
		when(empTimeReportDao.searchEmpTimeSheet(any(EmpTimeReport.class))).thenReturn(empTimeReports());

		when(objectMapper.writeValueAsString(any(PracticeManagementEntity.class))).thenReturn("string1");

		when(objectMapper.readValue(anyString(), any(TypeReference.class))).thenReturn(empTimeReports());

		List<EmpTimeReport> expected = serviceUnderTest.searchEmpTimeSheet(empTimeReport1());

		assertThat(2, is(expected.size()));
	}

	@Test
	public void searchEmpTimeSheetExceptionTest() throws IOException {

		when(empTimeReportDao.searchEmpTimeSheet(any(EmpTimeReport.class))).thenReturn(empTimeReports());

		when(objectMapper.writeValueAsString(any(PracticeManagementEntity.class))).thenReturn("string1");

		when(objectMapper.readValue(anyString(), any(TypeReference.class))).thenThrow(IOException.class);

		List<EmpTimeReport> expected = serviceUnderTest.searchEmpTimeSheet(empTimeReport1());

	}

	@Test
	public void findAllEmpTimeReportsTest() throws IOException {
		Query query = mock(Query.class);
		when(entityManager.createNativeQuery(anyString(), eq(EmpTimeReportEntity.class))).thenReturn(query);
		when(query.getResultList()).thenReturn(empTimeReportEntityList());

		when(objectMapper.writeValueAsString(any(ArrayList.class))).thenReturn("String1");

		when(objectMapper.readValue(anyString(), any(TypeReference.class))).thenReturn(empTimeReports());

		List<EmpTimeReport> expected = serviceUnderTest.findAllEmpTimeReports();

		assertThat(2, is(expected.size()));
	}

	@Test
	public void findAllEmpTimeReportsExceptionTest() throws Exception {
		Query query = mock(Query.class);
		when(entityManager.createNativeQuery(anyString(), eq(EmpTimeReportEntity.class))).thenThrow(Exception.class);
		when(query.getResultList()).thenReturn(empTimeReportEntityList());

		when(objectMapper.writeValueAsString(any(ArrayList.class))).thenReturn("String1");

		when(objectMapper.readValue(anyString(), any(TypeReference.class))).thenReturn(empTimeReports());

		List<EmpTimeReport> expected = serviceUnderTest.findAllEmpTimeReports();
	}

	public List<EmpTimeReportEntity> empTimeReportEntityList() {
		List<EmpTimeReportEntity> list = new ArrayList<EmpTimeReportEntity>();
		list.add(empTimeReportEntity1());
		list.add(empTimeReportEntity2());
		return list;
	}

	EmpTimeReportEntity empTimeReportEntity1() {
		EmpTimeReportEntity empTimeReportEntity = new EmpTimeReportEntity();
		empTimeReportEntity.setDay1(2.22f);
		empTimeReportEntity.setProjectName("projectName");
		return empTimeReportEntity;
	}

	EmpTimeReportEntity empTimeReportEntity2() {
		EmpTimeReportEntity empTimeReportEntity = new EmpTimeReportEntity();
		empTimeReportEntity.setDay1(3.22f);
		empTimeReportEntity.setProjectName("projectName2");
		return empTimeReportEntity;
	}

	List<EmpTimeReport> empTimeReports() {
		List<EmpTimeReport> list = new ArrayList<EmpTimeReport>();
		list.add(empTimeReport1());
		list.add(empTimeReport2());
		return list;
	}

	EmpTimeReport empTimeReport1() {
		EmpTimeReport empTimeReport = new EmpTimeReport();
		empTimeReport.setDay1(2.22f);
		empTimeReport.setPracticeId("1111");
		return empTimeReport;
	}

	EmpTimeReport empTimeReport2() {
		EmpTimeReport empTimeReport = new EmpTimeReport();
		empTimeReport.setDay1(3.22f);
		empTimeReport.setPracticeId("1112");
		return empTimeReport;
	}

}
