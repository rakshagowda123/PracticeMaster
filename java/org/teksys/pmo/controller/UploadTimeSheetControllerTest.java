package org.teksys.pmo.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;
import org.teksys.pmo.model.ImportTimeSheetEntity;
import org.teksys.pmo.service.UploadTimeSheetService;

@RunWith(SpringRunner.class)
public class UploadTimeSheetControllerTest {

	@Mock
	UploadTimeSheetService uploadTimeSheetService;

	@InjectMocks
	UploadTimeSheetController controllerUnderTest;

	private MockMvc mockMvc;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		this.mockMvc = MockMvcBuilders.standaloneSetup(controllerUnderTest).build();

	}

	@Test
	public void uploadFileTest() throws Exception {

		// Mock Request
		MockMultipartFile jsonFile = new MockMultipartFile("test.json", "", "application/json",
				"{\"key1\": \"value1\"}".getBytes());

		// Mock Response
		ImportTimeSheetEntity response = new ImportTimeSheetEntity();
		Mockito.when(uploadTimeSheetService.storeFile(Mockito.any(MultipartFile.class))).thenReturn(response);

		mockMvc.perform(MockMvcRequestBuilders.fileUpload("/uploadFile").file("file", jsonFile.getBytes())
				.characterEncoding("UTF-8")).andExpect(status().isOk());

	}
}
