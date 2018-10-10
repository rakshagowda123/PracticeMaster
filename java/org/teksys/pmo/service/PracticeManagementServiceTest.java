package org.teksys.pmo.service;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringRunner;
import org.teksys.pmo.dao.PracticeManagementRepository;
import org.teksys.pmo.domain.PracticeManagementDTO;
import org.teksys.pmo.model.PracticeManagementEntity;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
public class PracticeManagementServiceTest {

	@Mock
	private PracticeManagementRepository practiceManagementRepository;
	@Mock
	ObjectMapper objectMapper;

	@InjectMocks
	PracticeManagementService serviceUnderTest;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void saveProjectContentTest() {

		serviceUnderTest.saveProjectContent(getPracticeManagementDTO());

		verify(practiceManagementRepository, times(1)).save(any(PracticeManagementEntity.class));

	}

	@Test
	public void saveProjectContentExceptionTest() throws Exception {

		when(practiceManagementRepository.save(any(ArrayList.class))).thenThrow(Exception.class);

		serviceUnderTest.saveProjectContent(getPracticeManagementDTO());

	}

	@Test
	public void updateByIdTest() {
		PracticeManagementDTO practiceManagementDTO = new PracticeManagementDTO();
		practiceManagementDTO.setId(1);
		practiceManagementDTO.setIsDeleted(false);
		practiceManagementDTO.setActive(true);
		practiceManagementDTO.setModifiedDate(new Date());

		PracticeManagementEntity practiceManagementEntity = new PracticeManagementEntity();
		when(practiceManagementRepository.findById(any())).thenReturn(practiceManagementEntity);

		practiceManagementEntity = practiceManagementRepository.findById(practiceManagementDTO.getId());
		practiceManagementEntity.setDeleted(practiceManagementDTO.isDeleted());
		practiceManagementEntity.setActive(practiceManagementDTO.getActive());
		practiceManagementEntity.setModifiedDate(practiceManagementDTO.getModifiedDate());

		serviceUnderTest.updateById(practiceManagementDTO);
		verify(practiceManagementRepository, times(1)).save(any(PracticeManagementEntity.class));

	}

	@Test
	public void findAllProjectInfoTest() throws IOException {

		when(practiceManagementRepository.findAllThoseNotDeleted()).thenReturn(getPracticeManagementEntitiesList());

		when(objectMapper.writeValueAsString(any(PracticeManagementEntity.class))).thenReturn("Sahib SIngh");

		when(objectMapper.readValue(anyString(), any(TypeReference.class))).thenReturn(getList());

		List<PracticeManagementDTO> expected = serviceUnderTest.findAllProjectInfo();

		assertThat(1, is(expected.size()));

	}

	@Test
	public void findAllProjectInfoExceptionTest() throws IOException {

		when(practiceManagementRepository.findAllThoseNotDeleted()).thenThrow(Exception.class);

		when(objectMapper.writeValueAsString(any(PracticeManagementEntity.class))).thenThrow(Exception.class);

		when(objectMapper.readValue(anyString(), any(TypeReference.class))).thenThrow(Exception.class);

		List<PracticeManagementDTO> expected = serviceUnderTest.findAllProjectInfo();

	}

	@Test
	public void isPracticeExistTestTrue() {
		when(practiceManagementRepository.findById(anyInt())).thenReturn(getyPracticeManagementEntity());
		boolean expected = serviceUnderTest.isPracticeExist(getPracticeManagementDTO());
		assertFalse(expected);

	}

	@Test
	public void isPracticeExistTestFalse() {
		when(practiceManagementRepository.findById(anyInt())).thenReturn(null);
		boolean expected = serviceUnderTest.isPracticeExist(getPracticeManagementDTO());
		assertTrue(expected);

	}

	List<PracticeManagementEntity> getPracticeManagementEntitiesList() {
		List<PracticeManagementEntity> toReturn = new ArrayList<>();
		toReturn.add(getyPracticeManagementEntity());
		toReturn.add(getyPracticeManagementEntity2());

		return toReturn;
	}

	PracticeManagementEntity getyPracticeManagementEntity() {
		PracticeManagementEntity practiceManagementEntity = new PracticeManagementEntity();
		practiceManagementEntity.setId(1);
		practiceManagementEntity.setActive(true);
		practiceManagementEntity.setCreatedBy("name");
		practiceManagementEntity.setCreatedDate(new Date());

		return practiceManagementEntity;
	}

	PracticeManagementEntity getyPracticeManagementEntity2() {
		PracticeManagementEntity practiceManagementEntity = new PracticeManagementEntity();
		practiceManagementEntity.setId(2);
		practiceManagementEntity.setActive(true);
		practiceManagementEntity.setCreatedBy("name 2");
		practiceManagementEntity.setCreatedDate(new Date());

		return practiceManagementEntity;
	}

	List<PracticeManagementDTO> getList() {
		List<PracticeManagementDTO> toReturnDtos = new ArrayList<PracticeManagementDTO>();
		toReturnDtos.add(getPracticeManagementDTO());
		return toReturnDtos;
	}

	PracticeManagementDTO getPracticeManagementDTO() {
		PracticeManagementDTO practiceManagementDTO = new PracticeManagementDTO();
		practiceManagementDTO.setId(1);
		return practiceManagementDTO;
	}

//	@Test
//	public void isPracticeExistTrueTest() {
//		PracticeManagementEntity practiceManagementEntity = new PracticeManagementEntity();
//		practiceManagementEntity.setId(1);
//        practiceManagementEntity.setDeleted(true);
//        practiceManagementEntity.setActive(true);
//        practiceManagementEntity.setModifiedDate(new Date());
//		when(practiceManagementRepository.findById(any())).thenReturn(practiceManagementEntity);
//		 assertTrue(practiceManagementEntity.equals(practiceManagementEntity));
//	}
//	
//	@Test
//	public void isPracticeExistFalseTest() {
//		PracticeManagementEntity practiceManagementEntity = new PracticeManagementEntity();
//		practiceManagementEntity.setId(1);
//        practiceManagementEntity.setDeleted(true);
//        practiceManagementEntity.setActive(true);
//        practiceManagementEntity.setModifiedDate(new Date());
//		when(practiceManagementRepository.findById(any())).thenReturn(practiceManagementEntity);
//		 assertFalse(practiceManagementEntity.equals(new PracticeManagementEntity()));
//	}
}
