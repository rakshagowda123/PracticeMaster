package org.teksys.pmo.service;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringRunner;
import org.teksys.pmo.dao.ExpensesDao;
import org.teksys.pmo.dao.PracticeDao;
import org.teksys.pmo.domain.ExpensesDTO;
import org.teksys.pmo.domain.PracticeExpenseDTO;
import org.teksys.pmo.model.Expenses;
import org.teksys.pmo.model.PracticeExpense;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
public class PracticeServiceTest {

	
	@Mock
	 private PracticeDao practiceDao;
	
	@Mock
	 private ExpensesDao expensesDao;
	
	@Mock
	private ObjectMapper objectMapper;
	
	@InjectMocks
	PracticeService practiceService;
	
	@Before
    public void setup() {
		 MockitoAnnotations.initMocks(this);
    }
	
	
	@Test
	public void savePracticeExpensetest() throws Exception {
		
		practiceService.savePracticeExpense(getPracticeExpenseDTO());
		  verify(practiceDao,times(1)).save(any(PracticeExpense.class));
	}
	

	@Test
	public void savePracticeExpenseExceptiontest() throws Exception {
		
		 //when( practiceDao.save(getPracticeExpense())).thenThrow(Exception.class);
		 when(practiceDao.save(any(PracticeExpense.class))).thenThrow(Exception.class);
		 // verify(practiceDao,times(1)).save(any(PracticeExpense.class));
		 practiceService.savePracticeExpense(getPracticeExpenseDTO());
	}
	
	
	@Test
	public void findAllExpensestest() throws Exception{
		
		 when(expensesDao.findAll()).thenReturn(getExpensesList());
		 when(objectMapper.writeValueAsString(any(Expenses.class))).thenReturn("list");
	       when(objectMapper.readValue(anyString(),any(TypeReference.class))).thenReturn(getExpensesDTOList());
	       List<ExpensesDTO> expected = practiceService.findAllExpenses();
	        	        assertThat(1, is(expected.size()));
	}
	
	
	@Test
	public void findAllExpensesExceptiontest() throws Exception{
		
		 when(expensesDao.findAll()).thenThrow(Exception.class);
		 when(objectMapper.writeValueAsString(any(Expenses.class))).thenThrow(Exception.class);
	       when(objectMapper.readValue(anyString(),any(TypeReference.class))).thenThrow(Exception.class);
	       List<ExpensesDTO> expected = practiceService.findAllExpenses();
	        	        //assertThat(1, is(expected.size()));
	}
	
	
	
	@Test
	public void findAllPracticeExpensesExceptiontest() throws Exception{
		
		 when(practiceDao.findAll()).thenThrow(Exception.class);
		 when(objectMapper.writeValueAsString(any(PracticeExpense.class))).thenThrow(Exception.class);
	       when(objectMapper.readValue(anyString(),any(TypeReference.class))).thenThrow(Exception.class);
	       List<PracticeExpenseDTO> expected = practiceService.findAllPracticeExpenses();
	        	      //  assertThat(1, is(expected.size()));
	}
	
	@Test
	public void findAllPracticeExpensestest() throws Exception{
		
		 when(practiceDao.findAll()).thenReturn(getPracticeExpenseList());
		 when(objectMapper.writeValueAsString(any(PracticeExpense.class))).thenReturn("list");
	       when(objectMapper.readValue(anyString(),any(TypeReference.class))).thenReturn(getPracticeExpenseDTOList());
	       List<PracticeExpenseDTO> expected = practiceService.findAllPracticeExpenses();
	        	        assertThat(1, is(expected.size()));
	}
	
	@Test
	public void updatePracticeExpenseByIdtest() throws Exception{
		PracticeExpenseDTO  practiceExpenseDTO = new PracticeExpenseDTO();		
		practiceExpenseDTO.setPracticeId("1");
		practiceExpenseDTO.setExpensesId("2");
		practiceExpenseDTO.setCurrency_name("Dollar");
		practiceExpenseDTO.setAmount(100.0);
		practiceExpenseDTO.setPlanned("abc");
		practiceExpenseDTO.setExpenses_description("high");
		practiceExpenseDTO.setDateOfExpense(new Date(12-1-1998));
		
		PracticeExpense practiceExpense= new PracticeExpense();
		 when(practiceDao.findByPracticeExpenseId(any())).thenReturn(practiceExpense);
		 practiceExpense = practiceDao.findByPracticeExpenseId(practiceExpenseDTO.getPracticeExpenseId());
		 practiceExpense.setPracticeId(practiceExpenseDTO.getPracticeId());
         practiceExpense.setExpensesId(practiceExpenseDTO.getExpensesId());
         practiceExpense.setCurrency_name(practiceExpenseDTO.getCurrency_name());
         practiceExpense.setAmount(practiceExpenseDTO.getAmount());
         practiceExpense.setPlanned(practiceExpenseDTO.getPlanned());
         practiceExpense.setExpenses_description(practiceExpenseDTO.getExpenses_description());
         practiceExpense.setDateOfExpense(practiceExpenseDTO.getDateOfExpense());

         practiceService.updatePracticeExpenseById(practiceExpenseDTO);
         verify(practiceDao,times(1)).save(any(PracticeExpense.class));
	}
	
	
	@Test
	public void updatePracticeExpenseByIdExceptiontest() throws Exception {
//		PracticeExpenseDTO  practiceExpenseDTO = new PracticeExpenseDTO();		
//		practiceExpenseDTO.setPracticeId("1");
//		practiceExpenseDTO.setExpensesId("2");
//		practiceExpenseDTO.setCurrency_name("Dollar");
//		practiceExpenseDTO.setAmount(100.0);
//		practiceExpenseDTO.setPlanned("abc");
//		practiceExpenseDTO.setExpenses_description("high");
//		practiceExpenseDTO.setDateOfExpense(new Date(12-1-1998));
//		practiceExpenseDTO.setPracticeExpenseId(1);
//		PracticeExpense practiceExpense= new PracticeExpense();
//		//when(practiceDao.findByPracticeExpenseId(any())).thenThrow(Exception.class);
//		 when( practiceDao.findByPracticeExpenseId(practiceExpenseDTO.getPracticeExpenseId())).thenThrow(Exception.class);
//		 practiceExpense = practiceDao.findByPracticeExpenseId(practiceExpenseDTO.getPracticeExpenseId());
//		 practiceExpense.setPracticeId(practiceExpenseDTO.getPracticeId());
//         practiceExpense.setExpensesId(practiceExpenseDTO.getExpensesId());
//         practiceExpense.setCurrency_name(practiceExpenseDTO.getCurrency_name());
//         practiceExpense.setAmount(practiceExpenseDTO.getAmount());
//         practiceExpense.setPlanned(practiceExpenseDTO.getPlanned());
//         practiceExpense.setExpenses_description(practiceExpenseDTO.getExpenses_description());
//         practiceExpense.setDateOfExpense(practiceExpenseDTO.getDateOfExpense());

         when(practiceDao.save(any(PracticeExpense.class))).thenThrow(Exception.class);
        practiceService.updatePracticeExpenseById(getPracticeExpenseDTO());
        //practiceService.updatePracticeExpenseById(practiceExpenseDTO);
         //verify(practiceDao,times(1)).save(any(PracticeExpense.class));
	}
	
	
	PracticeExpenseDTO getPracticeExpenseDTO() {
		PracticeExpenseDTO object=new PracticeExpenseDTO();
		object.setPracticeExpenseId(1);
		return object;
	
				
	}
	
List<PracticeExpenseDTO> getPracticeExpenseDTOList(){
		
		List<PracticeExpenseDTO> list = new ArrayList<PracticeExpenseDTO>();
		list.add(getPracticeExpenseDTO());
		return list;
		
	}
	
	
	PracticeExpense getPracticeExpense() {
		
		PracticeExpense object = new PracticeExpense();
		object.setPracticeId("1");
		object.setPracticeExpenseId(1);
		return object;
		
		
	}
	
	List<PracticeExpense> getPracticeExpenseList(){
		
		List<PracticeExpense> list = new ArrayList<PracticeExpense>();
		list.add(getPracticeExpense());
		return list;
		
	}
	
	List<ExpensesDTO> getExpensesDTOList(){
		
		List<ExpensesDTO> list = new ArrayList<ExpensesDTO>();
		list.add(getExpensesDTO());
		return list;
					
		}
	
	ExpensesDTO getExpensesDTO() {
		
		ExpensesDTO  object = new ExpensesDTO ();
		object.setExpensesId("2");
		return object;
				
	}

	Expenses getExpenses()
	{
		
		Expenses object = new Expenses();
		object.setExpensesId("2");
		return object;
	
	}
	
	List<Expenses> getExpensesList(){
		
	List<Expenses> list = new ArrayList<Expenses>();
	list.add(getExpenses());
	return list;
	
		
	}
	
}
