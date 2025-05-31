package com.shail.practice.SpringBatchProcessing.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

@Configuration
public class BatchConfig {

    //job launcher uses this job to create job
    //jobRepository available as interface
    @Bean
    public Job jobBean(JobRepository jobrepository,JobCompletionNotificationImpl listner, Step steps){
        //creating job
        //jobbuilder//we have to create job repository here as well
        //job listner job is to do anything before job or after job so we need a listner also

        return new JobBuilder("job",jobrepository)
                .listener(listner)
                .start(steps)
                .build();

    }
    @Bean
    public Step steps(JobRepository jobRepository, DataSourceTransactionManager transactionManager, ItemReader<Product> reader
    ,ItemProcessor<Product,Product> processor,ItemWriter<Product> writer){
        return new StepBuilder("jobStep",jobRepository)
                .<Product,Product>chunk(5, transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    //reader
    @Bean
    public FlatFileItemReader<Product> reader(){
        return new FlatFileItemReaderBuilder<Product>()
                .name("itemReader")
                .resource(new ClassPathResource("SPRING_BATCH_PROCESSING_FILE.CSV"))
                .linesToSkip(1) // Skip header manually
                .delimited()
                .names("productId","title","description","price","discount")
                .targetType(Product.class)
                .build();


    }

    //processor

    @Bean
    public ItemProcessor<Product,Product> itemProcessor(){
        //no implementaion class exist for this so we have to create

        return new CustomItemProcessor();
    }

    //writer

    @Bean
    public ItemWriter<Product> itemWriter(DataSource dataSource){
        //class available here
        //jdbc and jpa both available
        return new JdbcBatchItemWriterBuilder<Product>()
                .sql("insert into batch_file_product(product_id,title,description,discount,price,discounted_price)values(:product_id,:title,:description,:discount,:price,:discountedPrice)")
                .dataSource(dataSource)
                .beanMapped()
                .build();
        //beanMapped read about
        //itemsqlparametersouceprovider

    }
}

