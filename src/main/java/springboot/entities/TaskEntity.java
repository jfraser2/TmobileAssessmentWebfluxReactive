package springboot.entities;

import java.time.ZonedDateTime;

import org.hibernate.annotations.TimeZoneStorage;
import org.hibernate.annotations.TimeZoneStorageType;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import springboot.errorHandling.helpers.ZonedDateTimeConverter;

@Table(name = "Tasks")
@Entity
public class TaskEntity {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "int")
    private Long id;
    
    @Column(name = "task_name", columnDefinition = "VARCHAR(55)", nullable = false)
    private String taskName;
    
    @Column(name = "task_description", columnDefinition = "VARCHAR(55)", nullable = true)
    private String taskDescription;
    
    @Column(name = "task_status", columnDefinition = "VARCHAR(55)", nullable = false)
    private String taskStatus;
    
	@JsonSerialize(using = ZonedDateTimeConverter.class)
    @Column(name = "task_create_date", nullable = false)
    @TimeZoneStorage(TimeZoneStorageType.NATIVE)
    private ZonedDateTime  taskCreateDate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getTaskDescription() {
		return taskDescription;
	}

	public void setTaskDescription(String taskDescription) {
		this.taskDescription = taskDescription;
	}

	public String getTaskStatus() {
		return taskStatus;
	}

	public void setTaskStatus(String taskStatus) {
		this.taskStatus = taskStatus;
	}

	public ZonedDateTime getTaskCreateDate() {
		return taskCreateDate;
	}

	public void setTaskCreateDate(ZonedDateTime taskCreateDate) {
		this.taskCreateDate = taskCreateDate;
	}
    

}
