package com.citalin.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.citalin.model.StudentResponse;

@Service
public class StudentService {
	
	@Value("${service.url}")
	private String serviceUrl;
	
	List<StudentResponse> studentsList;
	int index = 0;
	
	public List<StudentResponse> restCallToGetStudents()
	{
		RestTemplate restTemplate = new RestTemplate();
		
		StudentResponse [] studentResponseArray = 
				restTemplate.getForObject(serviceUrl, StudentResponse[].class);
		
		studentsList = Arrays.asList(studentResponseArray);
		
		return studentsList;
		
	}
	
	public StudentResponse getStudent()
	{
		
		if(studentsList == null)
		{
			restCallToGetStudents();
		} 
		
		if(studentsList != null && !studentsList.isEmpty() &&
				index < studentsList.size())
		{
			StudentResponse stu = studentsList.get(index);
			index++;
			return stu;
		}
		
		return null;
		
	}

}
