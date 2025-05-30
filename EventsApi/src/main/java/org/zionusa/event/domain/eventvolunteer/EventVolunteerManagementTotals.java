package org.zionusa.event.domain.eventvolunteer;

import lombok.Data;

import javax.persistence.Id;

@Data
public class EventVolunteerManagementTotals {
    @Id
    private Integer eventProposalId;

    private Integer femaleAdultContacts = 0;
    private Integer femaleAdultMembers = 0;
    private Integer femaleAdultTotals = 0;
    private Integer femaleAdultVips = 0;
    private Integer femaleCollegeStudentContacts = 0;
    private Integer femaleCollegeStudentMembers = 0;
    private Integer femaleCollegeStudentTotals = 0;
    private Integer femaleCollegeStudentVips = 0;
    private Integer femaleTeenagerContacts = 0;
    private Integer femaleTeenagerMembers = 0;
    private Integer femaleTeenagerTotals = 0;
    private Integer femaleTeenagerVips = 0;
    private Integer femaleYoungAdultContacts = 0;
    private Integer femaleYoungAdultMembers = 0;
    private Integer femaleYoungAdultTotals = 0;
    private Integer femaleYoungAdultVips = 0;
    private Integer maleAdultContacts = 0;
    private Integer maleAdultMembers = 0;
    private Integer maleAdultTotals = 0;
    private Integer maleAdultVips = 0;
    private Boolean isForResultsSurvey = false;
    private Integer maleCollegeStudentContacts = 0;
    private Integer maleCollegeStudentMembers = 0;
    private Integer maleCollegeStudentTotals = 0;
    private Integer maleCollegeStudentVips = 0;
    private Integer maleTeenagerContacts = 0;
    private Integer maleTeenagerMembers = 0;
    private Integer maleTeenagerTotals = 0;
    private Integer maleTeenagerVips = 0;
    private Integer maleYoungAdultContacts = 0;
    private Integer maleYoungAdultMembers = 0;
    private Integer maleYoungAdultTotals = 0;
    private Integer maleYoungAdultVips = 0;

    // Totals
    private Integer adults = 0;
    private Integer collegeStudents = 0;
    private Integer teenagers = 0;
    private Integer youngAdults = 0;

    private Integer femaleContacts = 0;
    private Integer femaleMembers = 0;
    private Integer femaleVips = 0;
    private Integer maleContacts = 0;
    private Integer maleMembers = 0;
    private Integer maleVips = 0;

    private Integer contacts = 0;
    private Integer members = 0;
    private Integer vips = 0;
    private Integer total = 0;

    private Integer resultsSurveyId;
}
