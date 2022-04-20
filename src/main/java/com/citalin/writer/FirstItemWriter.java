package com.citalin.writer;

import java.util.List;

import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import com.citalin.model.StudentXml;

@Component
public class FirstItemWriter implements ItemWriter<StudentXml>{

	@Override
	public void write(List<? extends StudentXml> items) throws Exception {
		System.out.println("Inside item writer");
		
		items.stream()
		.forEach(System.out::println);
		
	}

}
