package com.citalin.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.json.JacksonJsonObjectReader;
import org.springframework.batch.item.json.JsonItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import com.citalin.model.StudentCsv;
import com.citalin.model.StudentJson;

@Configuration
public class SampleJob {
	
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	
	@Autowired
	ItemReader<Integer> firstItemReader;
	
	@Autowired
	ItemProcessor<Integer,Long> firstItemProcessor;
	
	@Autowired
	ItemWriter<StudentJson> firstItemWriter;
	
	
	
	
	
	@Bean
	public Job chunkJob()
	{
		return jobBuilderFactory.get("Chunk Job")
				.incrementer(new RunIdIncrementer())
				.start(firstChunkStep())				
				.build();
	}
	
	private Step firstChunkStep()
	{
		return stepBuilderFactory.get("First Chunk Step")
				.<StudentJson,StudentJson>chunk(3)	
				//.reader(flatFileItemReader(null))
				.reader(jsonItemReader(null))
				//.processor(firstItemProcessor)
				.writer(firstItemWriter)
				.build();
	}
	
	// This 2 annotations are required if we want to read with @value
	// and set the FileSystemResource.
	@StepScope
	@Bean
	public FlatFileItemReader<StudentCsv> flatFileItemReader(
			@Value("#{jobParameters['inputFile']}") FileSystemResource fileSystemResource)
	{
		FlatFileItemReader<StudentCsv> flatFileItemReader =
				new FlatFileItemReader<StudentCsv>();
		//Set the location of the file we are going to read.
		flatFileItemReader.setResource(fileSystemResource);
		
		flatFileItemReader.setLineMapper(new DefaultLineMapper<StudentCsv>() {
			{
				setLineTokenizer(new DelimitedLineTokenizer() {
					{
						setNames("ID","First Name","Last Name","Email");
						setDelimiter("|");
					}
				});
				
				setFieldSetMapper(new BeanWrapperFieldSetMapper<StudentCsv>() {
					{
						setTargetType(StudentCsv.class);
					}
				});				
			}			
		});
		
		flatFileItemReader.setLinesToSkip(1);
		
		return flatFileItemReader;
	}
	
	
	@StepScope
	@Bean
	public JsonItemReader<StudentJson> jsonItemReader(
			@Value("#{jobParameters['inputFile']}") FileSystemResource fileSystemResource)
	{
		JsonItemReader<StudentJson> jsonItemReader = 
				new JsonItemReader<StudentJson>();
		
		jsonItemReader.setResource(fileSystemResource);
		jsonItemReader.setJsonObjectReader(
				new JacksonJsonObjectReader<>(StudentJson.class));
		
		// limite the numnber of records to read
		jsonItemReader.setMaxItemCount(8);
		
		// skip the first 2 items.
		jsonItemReader.setCurrentItemCount(2);
		
		return jsonItemReader;
	}
	
}
