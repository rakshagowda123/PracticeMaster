package org.teksys.pmo.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.Charset;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.teksys.pmo.domain.ExpensesDTO;
import org.teksys.pmo.domain.PracticeDTO;
import org.teksys.pmo.domain.PracticeExpenseDTO;
import org.teksys.pmo.service.PracticeService;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;



@RunWith(SpringRunner.class)
public class PracticeControllerTest {

	@Mock
    private PracticeService practiceService;
	
	@InjectMocks
	PracticeController practiceController;
	
     private MockMvc mockMvc;
	
     public static final MediaType APPLICATION_JSON_UTF8 =
    	        new MediaType(MediaType.APPLICATION_JSON.getType(),
    	                MediaType.APPLICATION_JSON.getSubtype(),
    	                Charset.forName("utf8"));
	@Test
	public void contextLoads() {
	}

	@Before
    public void setUp() {
		  MockitoAnnotations.initMocks(this);
		  
		  this.mockMvc = MockMvcBuilders.standaloneSetup(practiceController).build();
	}

	@Test
	public void testfindAllPracticeExpenses() throws Exception
	{	System.out.println(getListPracticeExpenses().toString());
		
		when(practiceService.findAllPracticeExpenses()).thenReturn(getListPracticeExpenses());
		this.mockMvc.perform(get("/findAllPracticeExpenses"))
		.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(1))).andExpect(jsonPath("$[0].practiceId", is("abc")))
        .andExpect(jsonPath("$[0].currency_name", is("Dollar")));
		
	}
	
	@Test
	public void testfindAllExpenses() throws Exception
	{
		
		when(practiceService.findAllExpenses()).thenReturn(getListExpenses());
		this.mockMvc.perform(get("/findAllExpenses"))
      .andExpect(status().isOk());
		
	}
	
	
	@Test
	public void testsavePracticeExpense() throws Exception {

		 ObjectMapper mapper = new ObjectMapper();
		 mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		    
		
		this.mockMvc.perform(post("/savePracticeExpense")
				.contentType(APPLICATION_JSON_UTF8)
	            .content(mapper.writeValueAsBytes(getPracticeExpense())))
		.andExpect(status().isOk());
	}
	
	
	@Test
	public void testupdatePracticeExpense() throws Exception
	{
		
		 ObjectMapper mapper = new ObjectMapper();
		 mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		    
		
		this.mockMvc.perform(post("/updatePracticeExpense")
				.contentType(APPLICATION_JSON_UTF8)
	            .content(mapper.writeValueAsBytes(getPracticeExpense())))
		.andExpect(status().isOk());
		
		
	}
	
 List<PracticeExpenseDTO> getListPracticeExpenses(){
		
		List<PracticeExpenseDTO> toReturn =new ArrayList<>();
		toReturn.add(getPracticeExpense());
		return toReturn;
		
	}
 
 List<ExpensesDTO> getListExpenses(){
	 List<ExpensesDTO> toReturn =new ArrayList<>();
		toReturn.add(getExpenses());
		return toReturn;
 }
	
	 PracticeExpenseDTO getPracticeExpense() {
		
		PracticeExpenseDTO object1 = new PracticeExpenseDTO();
		object1.setPracticeExpenseId(5);
		object1.setPracticeId("abc");
		object1.setExpensesId("a");
		object1.setAmount(1000.0);
		object1.setCurrency_name("Dollar");
		object1.setExpenses_description("xyz");
		object1.setDateOfExpense(new Date(10-2-2018));
		object1.setPlanned("acv");
		object1.setExpenses(null);
		object1. setPractice(getPracticeDTO());
		object1.setExpenses(getExpenses());
		return object1;
			
		
	}
	
	 ExpensesDTO getExpenses() {
		ExpensesDTO e = new ExpensesDTO();
		e.setExpensesId("aaa");
		e.setExpensesName("ddd");
		return e;
		
		
	}
	
	static PracticeDTO getPracticeDTO() {
		PracticeDTO p = new PracticeDTO();
		p.setCompanyId("xxx");
		p.setPracticeHead("madaraj");
		p.setPracticeId("id");
		p.setPracticeName("ADM");
		return p;
		
		
	}
	
	
	
}
