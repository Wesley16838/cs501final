package com.example.taiwanets;

public class PeopleModel {
    private String Fullname;
    private String Image;
    private String Status;
    private String School;
    private String CompanyName;
    private String JobTitle;

    private  PeopleModel(){

    }

    private PeopleModel(String Fullname, String Image, String Status, String School, String CompanyName, String JobTitle){
        this.Fullname = Fullname;
        this.Image =  Image;
        this.Status = Status;
        this.School = School;
        this.CompanyName = CompanyName;
        this.JobTitle = JobTitle;
    }

    public void setFullname(String Fullname) {
        this.Fullname = Fullname;
    }

    public String getFullname() {
        return Fullname;
    }

    public void setImage(String Image) {
        this.Image = Image;
    }

    public String getImage() {
        return Image;
    }

    public void setStatus(String Status) {
        this.Status = Status;
    }

    public String getStatus() {
        return Status;
    }

    public void setSchool(String School) {
        this.School = School;
    }

    public String getSchool() {
        return School;
    }

    public void setCompanyName(String CompanyName) {
        this.CompanyName = CompanyName;
    }

    public String getCompanyName() {
        return CompanyName;
    }

    public void setJobTitle(String JobTitle) {
        this.JobTitle = JobTitle;
    }

    public String getJobTitle() {
        return JobTitle;
    }


}
