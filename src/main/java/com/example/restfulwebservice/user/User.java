package com.example.restfulwebservice.user;

import com.fasterxml.jackson.annotation.JsonFilter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.util.Date;
//@JsonFilter("UserInfo")  //controller || service 클래스에서 사용
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "사용자 객체를 위한 도메인 객체")
public class User {

    private Integer id;

    @Size(min = 2 , message = "Name은 2글자 이상 입력해주세요")
    @ApiModelProperty(notes = "사용자 이름을 입력해주세요")
    private String name;

    //현재 날짜는 안되고 과거만 볼 수 있음
    @Past
    @ApiModelProperty(notes = "사용자 등록일")
    private Date joinDate;

    @ApiModelProperty(notes = "사용자 패스워드")
    private String password;
    @ApiModelProperty(notes = "사용자 주민번호")
    private String ssn;
}
