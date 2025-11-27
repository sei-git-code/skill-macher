package com.ses.salessupport.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * ユーザー登録リクエストDTO
 */
public class RegisterRequest {
    
    @NotBlank(message = "メールアドレスは必須です")
    @Email(message = "有効なメールアドレスを入力してください")
    private String email;
    
    @NotBlank(message = "パスワードは必須です")
    @Size(min = 6, message = "パスワードは6文字以上で入力してください")
    private String password;
    
    @NotBlank(message = "姓は必須です")
    @Size(max = 100, message = "姓は100文字以内で入力してください")
    private String lastName;
    
    @NotBlank(message = "名は必須です")
    @Size(max = 100, message = "名は100文字以内で入力してください")
    private String firstName;
    
    @Size(max = 20, message = "電話番号は20文字以内で入力してください")
    private String phone;
    
    @Size(max = 100, message = "部署名は100文字以内で入力してください")
    private String department;
    
    @Size(max = 100, message = "役職は100文字以内で入力してください")
    private String position;
    
    public RegisterRequest() {}
    
    public RegisterRequest(String email, String password, String lastName, String firstName) {
        this.email = email;
        this.password = password;
        this.lastName = lastName;
        this.firstName = firstName;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getLastName() {
        return lastName;
    }
    
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    public String getFirstName() {
        return firstName;
    }
    
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public String getDepartment() {
        return department;
    }
    
    public void setDepartment(String department) {
        this.department = department;
    }
    
    public String getPosition() {
        return position;
    }
    
    public void setPosition(String position) {
        this.position = position;
    }
}

