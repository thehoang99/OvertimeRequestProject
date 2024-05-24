$(document).ready(function (){

    $('#createStaffForm').validate({
        errorClass: "is-invalid",
        validClass: "is-valid",
        errorElement: "div",
        errorPlacement: function (error, element) {
            error.addClass("invalid-feedback");
            if (element.prop("type") === "checkbox") {
                error.insertAfter(element.next("label"));
            } else {
                error.insertAfter(element);
            }
        },
        highlight: function (element, errorClass, validClass) {
            $(element).addClass(errorClass).removeClass(validClass);
        },
        unhighlight: function (element, errorClass, validClass) {
            $(element).addClass(validClass).removeClass(errorClass);
        },
        rules: {
            name: {
                required: true
            },
            email: {
                required: true,
                email: true
            },
            password: {
                required: true,
                password: true
            },
            departmentId: {
                required: true
            },
            roleId: {
                required: true
            },
            salary: {
                required: true,
                isNumber: true,
                isPositive: true
            }
        },
        messages: {
            name: {
                required: "Name must be filled"
            },
            email: {
                required: "Email must be filled",
                email: "Email is not match format (abc@1234...)"
            },
            password: {
                required: "Password must be filled",
                password: "Password must have at least 8 characters and include number, uppercase and lowercase letters"
            },
            departmentId: {
                required: "Department must be filled"
            },
            roleId: {
                required: "Role must be filled"
            },
            salary: {
                required: "Salary must be filled",
                isNumber: "Salary must be a number",
                isPositive: "Salary must be a positive number"
            }
        },
        submitHandler: function (form) {
            form.submit()
        }
    });

    $.validator.methods.password = function (value) {
        let regexPassword = /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$/;
        return regexPassword.test(value);
    }

    $.validator.methods.isNumber = function (value) {
        return !isNaN(value);
    }

    $.validator.methods.isPositive = function (value) {
        return value > 0;
    }

});