package org.teksys.pmo.service;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;
import org.teksys.pmo.dao.BdmDao;
import org.teksys.pmo.dao.ClientRepository;
import org.teksys.pmo.dao.ClientRepositoryCustom;
import org.teksys.pmo.dao.PracticesDao;
import org.teksys.pmo.dao.ProjectPracticeMappingEntityRepository;
import org.teksys.pmo.dao.ProjectRepository;
import org.teksys.pmo.dao.ProjectRepositoryCustom;
import org.teksys.pmo.dao.RegionDao;
import org.teksys.pmo.dao.SolutionExecutiveDao;
import org.teksys.pmo.dao.DaoImpl.ProjectRepositoryImpl;
import org.teksys.pmo.domain.BDMDTO;
import org.teksys.pmo.domain.ClientDTO;
import org.teksys.pmo.domain.PracticeDTO;
import org.teksys.pmo.domain.PracticeExpenseDTO;
import org.teksys.pmo.domain.ProjectDTO;
import org.teksys.pmo.domain.ProjectPracticeMapping;
import org.teksys.pmo.domain.RegionDTO;
import org.teksys.pmo.domain.SolutionExecutiveDTO;
import org.teksys.pmo.model.BDM;
import org.teksys.pmo.model.Client;
import org.teksys.pmo.model.Practice;
import org.teksys.pmo.model.PracticeExpense;
import org.teksys.pmo.model.Project;
import org.teksys.pmo.model.ProjectPracticeMappingEntity;
import org.teksys.pmo.model.Region;
import org.teksys.pmo.model.SolutionExecutive;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
public class ProjectServiceTest {

	@Mock
	    private ProjectRepository projectRepository;

	@Mock
	    private ProjectPracticeMappingEntityRepository projectPracticeMappingEntityRepository;
	
	@Mock
	private ProjectPracticeMapping projectPracticeMapping; 
	

	@Mock
	    private ClientRepository clientDao;

	@Mock
	    private ClientRepositoryCustom clientDaoCustom;

	@Mock
	    private RegionDao regionDao;

	@Mock
	    private BdmDao bdmDao;

	@Mock
	    private SolutionExecutiveDao solutionExecutiveDao;

	@Mock
	    private PracticesDao practicesDao;

	@Mock
	    private ProjectRepositoryImpl projectRepositoryImpl;

	@Mock
	    private ProjectRepositoryCustom projectRepositoryCustom;
	
	@Mock 
	 private PracticeDTO  practiceDTO;

	@Mock
	    ObjectMapper objectMapper;
	
	@Mock
	private Project project;
	
	
	@InjectMocks
	private  ProjectService projectService;
	

	@Before
    public void setup() {
		 MockitoAnnotations.initMocks(this);
    }
	
		
	@Test
	public void testFindAllProjects() throws Exception {
		 when(projectRepository.findAll()).thenReturn(getProjectList());
		 when(objectMapper.writeValueAsString(any(Project.class))).thenReturn("list");
	       when(objectMapper.readValue(anyString(),any(TypeReference.class))).thenReturn(getProjectDTOList());
	       List<ProjectDTO> expected = projectService.findAllProjects();
	        	        assertThat(1, is(expected.size()));
	}
	@Test
	public void ExceptiontestFindAllProjects() throws Exception {
		 when(projectRepository.findAll()).thenThrow(Exception.class);
		 when(objectMapper.writeValueAsString(any(Project.class))).thenThrow(Exception.class);
	       when(objectMapper.readValue(anyString(),any(TypeReference.class))).thenThrow(Exception.class);
       List<ProjectDTO> expected = projectService.findAllProjects();
//	        	        assertThat(1, is(expected.size()));
	}
	

	@Test
	public void testFindProjects() throws Exception {
		
		ProjectDTO projectDTO = getProjectDTO();		
		 when(projectRepositoryCustom.findProjects(projectDTO)).thenReturn(getProjectList());
		 when(objectMapper.writeValueAsString(any(Project.class))).thenReturn("list");
	       when(objectMapper.readValue(anyString(),any(TypeReference.class))).thenReturn(getProjectDTOList());
	       List<ProjectDTO> expected = projectService.findProjects(projectDTO);
	        	        assertThat(1, is(expected.size()));
	}

	@Test
	public void ExceptiontestFindProjects() throws Exception {
		
		ProjectDTO projectDTO = getProjectDTO();		
		 when(projectRepositoryCustom.findProjects(projectDTO)).thenThrow(Exception.class);
		 when(objectMapper.writeValueAsString(any(Project.class))).thenThrow(Exception.class);
	       when(objectMapper.readValue(anyString(),any(TypeReference.class))).thenThrow(Exception.class);
	       List<ProjectDTO> expected = projectService.findProjects(projectDTO);
	        	      //  assertThat(1, is(expected.size()));
	}	
	
	@Test
	public void testaddProject() throws Exception{
		
		ProjectDTO pro = new ProjectDTO();
		pro.setCompanyProjectId("1");
		pro.setProjectId("1");
		pro.setProjectName("abccd");
		pro.setClientId("2");
		pro.setRegionName(1);
		pro.setSeName(2);
		pro.setBdmName(1);
		pro.setStartDate(new Date(10-2-2018));
		pro.setEndDate(new Date(2-10-2018));
		pro.setProjectAnalyst("Me");
		pro.setEmail("rgowda@gmail.com");
		pro.setContact("eee");
		pro.setType('a');
		pro.setProjectPracticeMappings(getProjectPracticeMappingSet());
		
		//when(pro.getProjectPracticeMappings()).thenReturn(getProjectPracticeMappingSet());
		ProjectPracticeMapping p = getprojectPracticeMapping();
        Set<ProjectPracticeMappingEntity> projectPracticeMappingEntities =  new HashSet<>();
		   ProjectPracticeMappingEntity projectPracticeMappingEntity = new ProjectPracticeMappingEntity();
		   projectPracticeMappingEntity.setCompanyProjectId(pro.getCompanyProjectId());
           projectPracticeMappingEntity.setPracticeId(p.getPracticeId());
           projectPracticeMappingEntities.add(projectPracticeMappingEntity);
           
           Project projectEntity = new Project();
           projectEntity.setCompanyProjectId(pro.getCompanyProjectId());
           projectEntity.setProjectId(pro.getProjectId());
           projectEntity.setProjectName(pro.getProjectName());
           projectEntity.setClientId(pro.getClientId());
           projectEntity.setRegionName(pro.getRegionName());
           projectEntity.setSeName(pro.getSeName());
           projectEntity.setBdmName(pro.getBdmName());
           projectEntity.setStartDate(pro.getStartDate());
           projectEntity.setEndDate(pro.getEndDate());
           projectEntity.setProjectAnalyst(pro.getProjectAnalyst());
           projectEntity.setEmail(pro.getEmail());
           projectEntity.setContact(pro.getContact());
           projectEntity.setType(pro.getType());
           projectEntity.setProjectPracticeMappings(projectPracticeMappingEntities);
           
           projectService.addProject(pro);
           
           verify(projectRepository,times(1)).save(any(Project.class));
          
	}
	
	@Test
	public void ExceptiontestaddProject() throws Exception{
		
		ProjectDTO pro = new ProjectDTO();
		pro.setCompanyProjectId("1");
		pro.setProjectId("1");
		pro.setProjectName("abccd");
		pro.setClientId("2");
		pro.setRegionName(1);
		pro.setSeName(2);
		pro.setBdmName(1);
		pro.setStartDate(new Date(10-2-2018));
		pro.setEndDate(new Date(2-10-2018));
		pro.setProjectAnalyst("Me");
		pro.setEmail("rgowda@gmail.com");
		pro.setContact("eee");
		pro.setType('a');
		pro.setProjectPracticeMappings(getProjectPracticeMappingSet());
		
		//when(pro.getProjectPracticeMappings()).thenReturn(getProjectPracticeMappingSet());
		ProjectPracticeMapping p = getprojectPracticeMapping();
        Set<ProjectPracticeMappingEntity> projectPracticeMappingEntities =  new HashSet<>();
		   ProjectPracticeMappingEntity projectPracticeMappingEntity = new ProjectPracticeMappingEntity();
		   projectPracticeMappingEntity.setCompanyProjectId(pro.getCompanyProjectId());
           projectPracticeMappingEntity.setPracticeId(p.getPracticeId());
           projectPracticeMappingEntities.add(projectPracticeMappingEntity);
           
           Project projectEntity = new Project();
           projectEntity.setCompanyProjectId(pro.getCompanyProjectId());
           projectEntity.setProjectId(pro.getProjectId());
           projectEntity.setProjectName(pro.getProjectName());
           projectEntity.setClientId(pro.getClientId());
           projectEntity.setRegionName(pro.getRegionName());
           projectEntity.setSeName(pro.getSeName());
           projectEntity.setBdmName(pro.getBdmName());
           projectEntity.setStartDate(pro.getStartDate());
           projectEntity.setEndDate(pro.getEndDate());
           projectEntity.setProjectAnalyst(pro.getProjectAnalyst());
           projectEntity.setEmail(pro.getEmail());
           projectEntity.setContact(pro.getContact());
           projectEntity.setType(pro.getType());
           projectEntity.setProjectPracticeMappings(projectPracticeMappingEntities);
           
           when(  projectRepository.save(projectEntity)).thenThrow(Exception.class);
           projectService.addProject(getProjectDTO());
           
//           verify(projectRepository,times(1)).save(any(Project.class));
          
	}
	
	

	@Test
	public void testUpdateProjectTrue() {
		
		Project project = new Project();
		when(projectRepository.findByCompanyProjectId(any())).thenReturn(project);
		 project.setCompanyProjectId("1");
		 String i = project.getCompanyProjectId();
		 Integer in =Integer.parseInt(i);
		 String s = project.getCompanyProjectId();
		 when(projectPracticeMappingEntityRepository.isExist(project.getCompanyProjectId())).thenReturn(1);
		 doNothing().when(projectPracticeMappingEntityRepository).removeByCompanyProjectId(s);
		
		 assertTrue(in>0);
		 projectPracticeMappingEntityRepository.removeByCompanyProjectId(s);	    
		 doNothing().when(projectPracticeMappingEntityRepository).removeByCompanyProjectId(s);
		 projectService.updateProject(getProjectDTO());
		 
	}
	
	@Test
	public void ExceptiontestUpdateProjectTrue() throws Exception {
		
		Project project = new Project();
		when(projectRepository.findByCompanyProjectId(any())).thenThrow(Exception.class);
		 project.setCompanyProjectId("1");
		 String i = project.getCompanyProjectId();
		 Integer in =Integer.parseInt(i);
		 String s = project.getCompanyProjectId();
		 when(projectPracticeMappingEntityRepository.isExist(project.getCompanyProjectId())).thenThrow(Exception.class);
		 doNothing().when(projectPracticeMappingEntityRepository).removeByCompanyProjectId(s);
		 projectService.updateProject(getProjectDTO());
	     //doThrow(new Exception()).when(projectPracticeMappingEntityRepository).removeByCompanyProjectId(s);
		// assertTrue(in>0);
		// projectPracticeMappingEntityRepository.removeByCompanyProjectId(s);	    
		 
		 
	}
	
	@Test 
	public void testUpdateProjectFalse() {
		Project project = new Project();
		when(projectRepository.findByCompanyProjectId(any())).thenReturn(project);
		project.setCompanyProjectId("-1");
		 String i = project.getCompanyProjectId();
		 Integer in =Integer.parseInt(i);
		 when(projectPracticeMappingEntityRepository.isExist(project.getCompanyProjectId())).thenReturn(-1);
			String s = project.getCompanyProjectId();
			 assertTrue(in<0);
			 
			 ProjectDTO pro = new ProjectDTO();
			 pro.setCompanyProjectId("1");
			 pro.setProjectId("1");
				pro.setProjectName("abccd");
				pro.setClientId("2");
				pro.setRegionName(1);
				pro.setSeName(2);
				pro.setBdmName(1);
				pro.setStartDate(new Date(10-2-2018));
				pro.setEndDate(new Date(2-10-2018));
				pro.setProjectAnalyst("Me");
				pro.setEmail("rgowda@gmail.com");
				pro.setContact("eee");
				pro.setType('a');
				pro.setProjectPracticeMappings(getProjectPracticeMappingSet());
				//when(pro.getProjectPracticeMappings()).thenReturn(getProjectPracticeMappingSet());
				
		           ProjectPracticeMapping practices =getprojectPracticeMapping();
		           
		            Set<ProjectPracticeMappingEntity> projectPracticeMappingEntities =  new HashSet<>();
				   ProjectPracticeMappingEntity projectPracticeMappingEntity = new ProjectPracticeMappingEntity();
	               projectPracticeMappingEntity.setProjectPracticeID(practices.getProjectPracticeID());
	               projectPracticeMappingEntity.setCompanyProjectId(pro.getCompanyProjectId());
	               projectPracticeMappingEntity.setPracticeId(practices.getPracticeId());
	               projectPracticeMappingEntities.add(projectPracticeMappingEntity);
	               project.setProjectId(pro.getProjectId());
	               project.setProjectName(pro.getProjectName());
	               project.setClientId(pro.getClientId());
	               project.setRegionName(pro.getRegionName());
	               project.setSeName(pro.getSeName());
	               project.setBdmName(pro.getBdmName());
	               project.setStartDate(pro.getStartDate());
	               project.setEndDate(pro.getEndDate());
	               project.setProjectAnalyst(pro.getProjectAnalyst());
	               project.setEmail(pro.getEmail());
	               project.setContact(pro.getContact());
	               project.setType(pro.getType());
	               project.setProjectPracticeMappings(projectPracticeMappingEntities);
	               projectService.updateProject(pro);
	               System.out.println(pro);
	               verify(projectRepository,times(1)).save(any(Project.class));
	               
		
	}
	
	
	@Test 
	public void ExceptiontestUpdateProjectFalse() {
		Project project = new Project();
		when(projectRepository.findByCompanyProjectId(any())).thenReturn(project);
		project.setCompanyProjectId("-1");
		 String i = project.getCompanyProjectId();
		 Integer in =Integer.parseInt(i);
		 when(projectPracticeMappingEntityRepository.isExist(project.getCompanyProjectId())).thenReturn(-1);
			String s = project.getCompanyProjectId();
			 assertTrue(in<0);
			 
			 ProjectDTO pro = new ProjectDTO();
			 pro.setCompanyProjectId("1");
			 pro.setProjectId("1");
				pro.setProjectName("abccd");
				pro.setClientId("2");
				pro.setRegionName(1);
				pro.setSeName(2);
				pro.setBdmName(1);
				pro.setStartDate(new Date(10-2-2018));
				pro.setEndDate(new Date(2-10-2018));
				pro.setProjectAnalyst("Me");
				pro.setEmail("rgowda@gmail.com");
				pro.setContact("eee");
				pro.setType('a');
				pro.setProjectPracticeMappings(getProjectPracticeMappingSet());
				//when(pro.getProjectPracticeMappings()).thenReturn(getProjectPracticeMappingSet());
				
		           ProjectPracticeMapping practices =getprojectPracticeMapping();
		           
		            Set<ProjectPracticeMappingEntity> projectPracticeMappingEntities =  new HashSet<>();
				   ProjectPracticeMappingEntity projectPracticeMappingEntity = new ProjectPracticeMappingEntity();
	               projectPracticeMappingEntity.setProjectPracticeID(practices.getProjectPracticeID());
	               projectPracticeMappingEntity.setCompanyProjectId(pro.getCompanyProjectId());
	               projectPracticeMappingEntity.setPracticeId(practices.getPracticeId());
	               projectPracticeMappingEntities.add(projectPracticeMappingEntity);
	               project.setProjectId(pro.getProjectId());
	               project.setProjectName(pro.getProjectName());
	               project.setClientId(pro.getClientId());
	               project.setRegionName(pro.getRegionName());
	               project.setSeName(pro.getSeName());
	               project.setBdmName(pro.getBdmName());
	               project.setStartDate(pro.getStartDate());
	               project.setEndDate(pro.getEndDate());
	               project.setProjectAnalyst(pro.getProjectAnalyst());
	               project.setEmail(pro.getEmail());
	               project.setContact(pro.getContact());
	               project.setType(pro.getType());
	               project.setProjectPracticeMappings(projectPracticeMappingEntities);
	               when(  projectRepository.save(project)).thenThrow(Exception.class);
	               projectService.updateProject(pro);
//	               System.out.println(pro);
//	               verify(projectRepository,times(1)).save(any(Project.class));
		
	}

	@Test
	public void ExceptiontestFindAllClients() throws Exception{
		
		
		 when(clientDao.findAll()).thenThrow(Exception.class);
		 when(objectMapper.writeValueAsString(any(Client.class))).thenThrow(Exception.class);
	       when(objectMapper.readValue(anyString(),any(TypeReference.class))).thenThrow(Exception.class);
	       List<ClientDTO> expected = projectService.findAllClients();
	        	        //assertThat(1, is(expected.size()));
		
		
	}

	@Test
	public void testFindAllClients() throws Exception{
		
		
		 when(clientDao.findAll()).thenReturn(getClientList());
		 when(objectMapper.writeValueAsString(any(Client.class))).thenReturn("list");
	       when(objectMapper.readValue(anyString(),any(TypeReference.class))).thenReturn(getClientDTOList());
	       List<ClientDTO> expected = projectService.findAllClients();
	        	        assertThat(1, is(expected.size()));
		
		
	}
	@Test
	public void testFindAllRegions() throws Exception{
		 when( regionDao.findAll()).thenReturn(getRegionList());
		 when(objectMapper.writeValueAsString(any(Region.class))).thenReturn("list");
	       when(objectMapper.readValue(anyString(),any(TypeReference.class))).thenReturn(getRegionDTOList());
	       List<RegionDTO> expected = projectService.findAllRegions();
	        	        assertThat(1, is(expected.size()));	}

	
	@Test
	public void ExceptiontestFindAllRegions() throws Exception{
		 when( regionDao.findAll()).thenThrow(Exception.class);
		 when(objectMapper.writeValueAsString(any(Region.class))).thenThrow(Exception.class);
	       when(objectMapper.readValue(anyString(),any(TypeReference.class))).thenThrow(Exception.class);
	       List<RegionDTO> expected = projectService.findAllRegions();
	        	      // assertThat(1, is(expected.size()));	
	        	       }
	
	
	@Test
	public void testFindAllBdm()  throws Exception {
		 when(bdmDao.findAll()).thenReturn(getBDMList());
		 when(objectMapper.writeValueAsString(any(BDM.class))).thenReturn("list");
	       when(objectMapper.readValue(anyString(),any(TypeReference.class))).thenReturn(getBDMDTOList());
	       List<BDMDTO> expected = projectService.findAllBdm();
	        	        assertThat(1, is(expected.size()));	}
	


	@Test
	public void ExceptiontestFindAllBdm()  throws Exception {
		 when(bdmDao.findAll()).thenThrow(Exception.class);
		 when(objectMapper.writeValueAsString(any(BDM.class))).thenThrow(Exception.class);
	       when(objectMapper.readValue(anyString(),any(TypeReference.class))).thenThrow(Exception.class);
	       List<BDMDTO> expected = projectService.findAllBdm();
	        	        //assertThat(1, is(expected.size()));	
	        	        }
	
	@Test
	public void testFindAllSolutionExecutive() throws Exception {
		when(solutionExecutiveDao.findAll()).thenReturn(getSolutionExecutiveList());
		 when(objectMapper.writeValueAsString(any(SolutionExecutive.class))).thenReturn("list");
	       when(objectMapper.readValue(anyString(),any(TypeReference.class))).thenReturn(getSolutionExecutiveDTOList());
	       List<SolutionExecutiveDTO> expected = projectService.findAllSolutionExecutive();
	        	        assertThat(1, is(expected.size()));
	}


	@Test
	public void ExceptiontestFindAllSolutionExecutive() throws Exception {
		when(solutionExecutiveDao.findAll()).thenThrow(Exception.class);
		 when(objectMapper.writeValueAsString(any(SolutionExecutive.class))).thenThrow(Exception.class);
	       when(objectMapper.readValue(anyString(),any(TypeReference.class))).thenThrow(Exception.class);
	       List<SolutionExecutiveDTO> expected = projectService.findAllSolutionExecutive();
	        	        //assertThat(1, is(expected.size()));
	}
	
	
	@Test
	public void testFindAllPractices() throws Exception {
		when(practicesDao.findAll()).thenReturn(getPracticeList());
		 when(objectMapper.writeValueAsString(any(Practice.class))).thenReturn("list");
	       when(objectMapper.readValue(anyString(),any(TypeReference.class))).thenReturn(getPracticeDTOList());
	       List<PracticeDTO> expected = projectService.findAllPractices();
	        	        assertThat(1, is(expected.size()));
	}

	

	@Test
	public void ExceptiontestFindAllPractices() throws Exception {
		when(practicesDao.findAll()).thenThrow(Exception.class);
		 when(objectMapper.writeValueAsString(any(Practice.class))).thenThrow(Exception.class);
	       when(objectMapper.readValue(anyString(),any(TypeReference.class))).thenThrow(Exception.class);
	       List<PracticeDTO> expected = projectService.findAllPractices();
	        	     //   assertThat(1, is(expected.size()));
	}
	
	
	@Test
	public void testAddClient() {
		ClientDTO c =  new ClientDTO();
		c.setId(1);
		c.setClientName("raksha");
		Client c1 = new Client();
		c1.setClientName(c.getClientName());
		c1.setId(c.getId());
		projectService.addClient(c);
		verify(clientDao,times(1)).save(any(Client.class));
		
		
	}
	
	@Test
	public void ExceptiontestAddClient() throws Exception {
		ClientDTO c =  new ClientDTO();
		c.setId(1);
		c.setClientName("raksha");
		//Client c1 = clientDao.findById(1);
		Client c1 = new Client();
		c1.setClientName(c.getClientName());
		c1.setId(c.getId());
		projectService.addClient(c);
		  when(clientDao.save(any(Client.class))).thenThrow(Exception.class);
		projectService.addClient(getClientDTO());
		//verify(clientDao,times(1)).save(any(Client.class));
		
		
	}
	

	@Test
	public void testUpdateClientById()  {
		ClientDTO c =  new ClientDTO();
		c.setId(1);
		c.setClientName("raksha");
		when(clientDao.findById(1)).thenReturn(new Client());
		Client c1 = clientDao.findById(1);
		c1.setClientName(c1.getClientName());
		projectService.updateClientById(c,1);
		verify(clientDao,times(1)).save(any(Client.class));
		
	}

	
	@Test
	public void ExceptiontestUpdateClientById() throws Exception{
		ClientDTO c =  new ClientDTO();
		c.setId(1);
		c.setClientName("raksha");
		when(clientDao.findById(1)).thenReturn(new Client());
		Client c1 = clientDao.findById(1);
		c1.setClientName(c1.getClientName());
		projectService.updateClientById(c,1);
		when( clientDao.save(c1)).thenThrow(Exception.class);
		projectService.updateClientById(getClientDTO(),1);
		//verify(clientDao,times(1)).save(any(Client.class));
		
	}

	
	
	Project getProject() {
		Project object = new Project();
		object.setCreatedBy("raksha");
		return object;
				
	}
			
	List<Project> getProjectList(){
		
		List<Project> list = new ArrayList<Project>();
		list.add(getProject());
		return list;
			
	}	
	
	ProjectDTO getProjectDTO() {
		
		
		ProjectDTO object = new ProjectDTO();
		object.setClientId("1");
		return object;
	}	
	
	List<ProjectDTO> getProjectDTOList(){
		
		List<ProjectDTO> list = new ArrayList<ProjectDTO>();
		list.add(getProjectDTO());
		return list;
		
	}
	
	ClientDTO getClientDTO() {
		
		ClientDTO object = new ClientDTO();
		object.setId(1);
		return object;		
		
	}
     List<ClientDTO> getClientDTOList(){
		
		List<ClientDTO> list = new ArrayList<ClientDTO>();
		list.add(getClientDTO());
		return list;
		
	 }     
     Client getClient() {
    	 Client object = new Client();
    	 object.setClientName("raksha");
    	 object.setClientName("raksha");
    	 return object; 	 
    	
     
     }     
     
     List<Client> getClientList(){
 		
 		List<Client> list = new ArrayList<Client>();
 		list.add(getClient());
 		return list;
 		
 	 }
	
     RegionDTO getRegionDTO() {
    	 RegionDTO object = new RegionDTO();
    	 object.setRegionId(1);
    	 return object; 	 
         
     }	
     List<RegionDTO> getRegionDTOList(){
  		
  		List<RegionDTO> list = new ArrayList<RegionDTO>();
  		list.add(getRegionDTO());
  		return list;
  		
  	 }          

     Region getRegion() {
    	 Region object = new Region();
    	 object.setRegionId(1);
    	 return object; 	 
         
     }	
     List<Region> getRegionList(){
   		
   		List<Region> list = new ArrayList<Region>();
   		list.add(getRegion());
   		return list;
   		  	 }
          
     
     BDMDTO getBDMDTO() {
    	 BDMDTO object = new BDMDTO();
    	 object.setBdmId(1);
    	 return object; 	 
         
     }	
     
     List<BDMDTO> getBDMDTOList(){
    		
    		List<BDMDTO> list = new ArrayList<BDMDTO>();
    		list.add(getBDMDTO());
    		return list;
    		  	 }
           
     BDM getBDM() {
    	 BDM object = new BDM();
    	 object.setBdmId(1);
    	 return object; 	 
         
     }	
     
     List<BDM> getBDMList(){
 		
 		List<BDM> list = new ArrayList<BDM>();
 		list.add(getBDM());
 		return list;
 		  	 }
     
          
     SolutionExecutiveDTO getSolutionExecutiveDTO() {
    	 SolutionExecutiveDTO object = new SolutionExecutiveDTO();
    	 object.setSeId(1);
    	 return object; 	 
         
     }	
     
     List<SolutionExecutiveDTO> getSolutionExecutiveDTOList(){
  		
  		List<SolutionExecutiveDTO> list = new ArrayList<SolutionExecutiveDTO>();
  		list.add(getSolutionExecutiveDTO());
  		return list;
  		  	 }
     
     
     SolutionExecutive getSolutionExecutive() {
    	 SolutionExecutive object = new SolutionExecutive();
    	 object.setSeId(1);
    	 return object; 	 
              }
     
     List<SolutionExecutive> getSolutionExecutiveList(){
   		
   		List<SolutionExecutive> list = new ArrayList<SolutionExecutive>();
   		list.add(getSolutionExecutive());
   		return list;
   		  	 }
     

     PracticeDTO getPracticeDTO() {
    	 PracticeDTO object = new PracticeDTO();
    	 object.setPracticeId("1");
    	 return object; 	 
              }
     
     List<PracticeDTO> getPracticeDTOList(){
    		
   		List<PracticeDTO> list = new ArrayList<PracticeDTO>();
   		list.add(getPracticeDTO());
   		return list;
   		  	 }
     
     
     Practice getPractice() {
    	 Practice object = new Practice();
    	 object.setPracticeId("1");
    	 return object; 	 
              }
     
     List<Practice> getPracticeList(){
 		
    		List<Practice> list = new ArrayList<Practice>();
    		list.add(getPractice());
    		return list;
    		  	 }
     
     
     
     ProjectPracticeMapping getprojectPracticeMapping() {
    	 ProjectPracticeMapping object = new ProjectPracticeMapping();
    	 object.setPracticeId("1");
    	 return object;    	 	 
    	 
    	 
     }
     
     
     Set<ProjectPracticeMapping> getProjectPracticeMappingSet(){
    	 
    	 Set<ProjectPracticeMapping> set = new HashSet<>();
    	 set.add(getprojectPracticeMapping());
    	 return set;
    	 
    	 
    	 
     }
}
