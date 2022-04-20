package com.citalin.processor;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import com.citalin.model.StudentJdbc;
import com.citalin.model.StudentJson;

@Component
public class FirstItemProcessor implements ItemProcessor<StudentJdbc, StudentJson>{

	@Override
	public StudentJson process(StudentJdbc item) throws Exception {
		System.out.println("Inside itemp processor");		
		StudentJson studentJson = new StudentJson();
		studentJson.setId(item.getId());
		studentJson.setFirstName(item.getFirstName());
		studentJson.setLastName(item.getLastName());
		studentJson.setEmail(item.getEmail());
		
		return studentJson;
	}

}
