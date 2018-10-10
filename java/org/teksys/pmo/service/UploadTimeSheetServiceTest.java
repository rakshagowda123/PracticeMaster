package org.teksys.pmo.service;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.io.FileInputStream;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;
import org.teksys.pmo.dao.EmpTimeReportRepository;
import org.teksys.pmo.dao.UploadTimeSheetRepository;
import org.teksys.pmo.model.ImportTimeSheetEntity;

@RunWith(SpringRunner.class)
public class UploadTimeSheetServiceTest {

	@Mock
	UploadTimeSheetRepository uploadTimeSheetRepository;
	@Mock
	EmpTimeReportRepository empTimeReportRepository;

	@InjectMocks
	UploadTimeSheetService serviceUnderTest;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void storeFileIfTest() throws IOException {
		when(uploadTimeSheetRepository.save(any(ImportTimeSheetEntity.class))).thenReturn(importTimeSheetEntity());
		ImportTimeSheetEntity expected = serviceUnderTest.storeFile(file());
		assertThat("ashwin", is(expected.getCreatedBy()));
	}

	@Test
	public void storeFileElseTest() throws IOException {
		when(uploadTimeSheetRepository.save(any(ImportTimeSheetEntity.class))).thenReturn(importTimeSheetEntity());
		ImportTimeSheetEntity expected = serviceUnderTest.storeFile(file2());
	}


	public MultipartFile file() throws IOException {

		FileInputStream inputFile = new FileInputStream("src/test/java/resources/timesheet1.xlsx");
		MockMultipartFile file = new MockMultipartFile("file", "s.xlsx", "multipart/form-data", inputFile);

		return file;
	}

	public MultipartFile file2() throws IOException {

		FileInputStream inputFile = new FileInputStream("src/test/java/resources/timesheet2.xlsx");
		MockMultipartFile file = new MockMultipartFile("file", "s2.xlsx", "multipart/form-data", inputFile);

		return file;
	}

	public ImportTimeSheetEntity importTimeSheetEntity() {
		ImportTimeSheetEntity importTimeSheetEntity = new ImportTimeSheetEntity();
		importTimeSheetEntity.setCreatedBy("ashwin");
		importTimeSheetEntity.setFileName("filename");
		return importTimeSheetEntity;
	}
}
