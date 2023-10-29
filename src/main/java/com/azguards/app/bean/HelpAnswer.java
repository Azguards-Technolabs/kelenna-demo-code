package com.azguards.app.bean;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@Entity
@ToString
@EqualsAndHashCode
@Table(name = "help_answer", uniqueConstraints = @UniqueConstraint(columnNames = { "help_id", "user_id", "answer" }, 
	   name = "UK_HELP_ANSWER_HID_UID_ANSWER"), indexes = {@Index(name = "IDX_HELP_ID", columnList = "help_id", unique = false),
			   @Index(name = "IDX_USER_ID", columnList = "user_id", unique = false)})
public class HelpAnswer implements Serializable {

    private static final long serialVersionUID = 6922844940897956622L;
    
    @Id
    @GenericGenerator(name = "generator", strategy = "guid", parameters = {})
	@GeneratedValue(generator = "generator")
	@Column(name = "id", unique = true, nullable = false, length=36)
    private String id;
    
    @Column(name = "user_id", nullable = false, length = 36)
    private String user;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "help_id", nullable = false)
    private Help help;
    
    @Column(name = "answer", nullable = false)
    private String answer;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_on", length = 19)
    private Date createdOn;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_on", length = 19)
    private Date updatedOn;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "deleted_on", length = 19)
    private Date deletedOn;
    
    @Column(name = "created_by", length = 50)
    private String createdBy;
    
    @Column(name = "updated_by", length = 50)
    private String updatedBy;
    
    @Column(name = "is_deleted")
    private Boolean isDeleted;
    
    @Column(name = "file_name")
    private String fileName;
}
