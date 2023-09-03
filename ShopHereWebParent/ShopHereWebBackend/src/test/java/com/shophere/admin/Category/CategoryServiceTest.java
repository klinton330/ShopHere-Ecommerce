package com.shophere.admin.Category;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.shophere.admin.Repository.CatagoryRepository;
import com.shophere.admin.service.CategoryService;
import com.shopme.common.entity.Category;


@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
public class CategoryServiceTest { 
	@MockBean
      private  CatagoryRepository catagoryRepository;
      @InjectMocks
      private CategoryService categoryService;
      
      @Test
      public void testCheckUniqueInNewModeReturnDuplicateName()
      {
    	  Integer id=null;
    	  String name="Computerss";
    	  String alias="Computer world";
    	  
    	  Category category=new Category(id, name, alias);
    	 Mockito.when(catagoryRepository.findByName(name)).thenReturn(category);
    	 Mockito.when(catagoryRepository.findByAlias(alias)).thenReturn(category);
    	  String result=categoryService.checkUniqueness(id, name, alias);
    	  assertThat(result).isEqualTo("DupliacteName");
      }
      
      @Test
      public void testCheckUniqueInNewModeReturnDuplicateAlias()
      {
    	  Integer id=null;
    	  String name="abc";
    	  String alias="Computer world";
    	  
    	  Category category=new Category(id, name, alias);
    	 Mockito.when(catagoryRepository.findByName(name)).thenReturn(null);
    	 Mockito.when(catagoryRepository.findByAlias(alias)).thenReturn(category);
    	  String result=categoryService.checkUniqueness(id, name, alias);
    	  assertThat(result).isEqualTo("DupliacteAlias");
      }
      
      @Test
      public void testCheckUniqueInNewModeReturnOk()
      {
    	  Integer id=null;
    	  String name="abc";
    	  String alias="Computer world";
    	  
    	  Category category=new Category(id, name, alias);
    	 Mockito.when(catagoryRepository.findByName(name)).thenReturn(null);
    	 Mockito.when(catagoryRepository.findByAlias(alias)).thenReturn(null);
    	  String result=categoryService.checkUniqueness(id, name, alias);
    	  assertThat(result).isEqualTo("OK");
      }
      
      @Test
      public void testCheckUniqueInEditModeReturnDuplicateName()
      {
    	  //From user
    	  Integer id=1;
    	  String name="abc";
    	  String alias="Computer world";
    	  //From DB
    	  Category category=new Category(2, name, alias);
    	 Mockito.when(catagoryRepository.findByName(name)).thenReturn(category);
    	 Mockito.when(catagoryRepository.findByAlias(alias)).thenReturn(null);
    	  String result=categoryService.checkUniqueness(id, name, alias);
    	  assertThat(result).isEqualTo("DuplicateName");
      }
      
      @Test
      public void testCheckUniqueInEditModeReturnDuplicateAlias()
      {
    	  //From user
    	  Integer id=1;
    	  String name="abc";
    	  String alias="Computer world";
    	  //From DB
    	  Category category=new Category(2, name, alias);
    	 Mockito.when(catagoryRepository.findByName(name)).thenReturn(null);
    	 Mockito.when(catagoryRepository.findByAlias(alias)).thenReturn(category);
    	  String result=categoryService.checkUniqueness(id, name, alias);
    	  assertThat(result).isEqualTo("DuplicateAlias");
      }
      
      @Test
      public void testCheckUniqueInEditModeReturnDuplicateOK()
      {
    	  //From user
    	  Integer id=1;
    	  String name="abc";
    	  String alias="Computer world";
    	  //From DB
    	  Category category=new Category(id, name, alias);
    	 Mockito.when(catagoryRepository.findByName(name)).thenReturn(null);
    	 Mockito.when(catagoryRepository.findByAlias(alias)).thenReturn(category);
    	  String result=categoryService.checkUniqueness(id, name, alias);
    	  assertThat(result).isEqualTo("OK");
      }
}
