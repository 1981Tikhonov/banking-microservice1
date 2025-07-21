package com.bank.project.dto;

import com.bank.project.entity.Client;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Data
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class CreateClientRequest {
    @NotBlank(message = "Имя не может быть пустым")
    @Size(min = 2, max = 50, message = "Имя должно содержать от 2 до 50 символов")
    private String firstName;

    @NotBlank(message = "Фамилия не может быть пустой")
    @Size(min = 2, max = 50, message = "Фамилия должна содержать от 2 до 50 символов")
    private String lastName;

    @Size(max = 50, message = "Отчество должно содержать до 50 символов")
    private String middleName;

    @NotNull(message = "Дата рождения обязательна")
    @Past(message = "Дата рождения должна быть в прошлом")
    private LocalDate birthDate;

    @NotBlank(message = "Email обязателен")
    @Email(message = "Некорректный формат email")
    private String email;

    @NotBlank(message = "Телефон обязателен")
    @Pattern(regexp = "^\\+7\\d{10}$", message = "Телефон должен быть в формате +7XXXXXXXXXX")
    private String phone;

    @NotBlank(message = "Серия паспорта обязательна")
    @Pattern(regexp = "\\d{4}", message = "Серия паспорта должна состоять из 4 цифр")
    private String passportSeries;

    @NotBlank(message = "Номер паспорта обязателен")
    @Pattern(regexp = "\\d{6}", message = "Номер паспорта должен состоять из 6 цифр")
    private String passportNumber;

    @NotBlank(message = "Код подразделения обязателен")
    @Pattern(regexp = "\\d{3}-\\d{3}", message = "Код подразделения должен быть в формате XXX-XXX")
    private String departmentCode;

    @NotBlank(message = "Кем выдан паспорт - обязательное поле")
    @Size(max = 255, message = "Поле 'Кем выдан' должно содержать до 255 символов")
    private String issuedBy;

    @NotNull(message = "Дата выдачи паспорта обязательна")
    @Past(message = "Дата выдачи должна быть в прошлом")
    private LocalDate issueDate;

    @NotBlank(message = "Адрес регистрации обязателен")
    @Size(max = 500, message = "Адрес регистрации должен содержать до 500 символов")
    private String registrationAddress;

    private String taxCode;
    
    // Additional getters required by other classes
    public String getAddress() {
        return registrationAddress;
    }
    
    public String getStatus() {
        return "ACTIVE"; // Default status
    }


    public void setAddress(String s) {

    }
}