$(document).ready(function () {

    $('#updateStaffForm').validate({
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
            form.submit();
        }
    });

    $.validator.methods.isNumber = function (value) {
        return !isNaN(value);
    }

    $.validator.methods.isPositive = function (value) {
        return value > 0;
    }

});