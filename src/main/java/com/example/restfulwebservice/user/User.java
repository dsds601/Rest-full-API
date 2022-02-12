package com.example.restfulwebservice.user;

import com.fasterxml.jackson.annotation.JsonFilter;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.util.Date;

@Data
@AllArgsConstructor
@JsonFilter("UserInfo")  //controller || service 클래스에서 사용
public class User {

    private Integer id;

    @Size(min = 2 , message = "Name은 2글자 이상 입력해주세요")
    private String name;

    //현재 날짜는 안되고 과거만 볼 수 있음
    @Past
    private Date joinDate;

    private String password;
    private String ssn;
}
