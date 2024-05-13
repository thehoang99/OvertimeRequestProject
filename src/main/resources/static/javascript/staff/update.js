$(document).ready(function () {

    $('#updateStaffForm').validate({
        errorPlacement: function (error, element) {
            error.insertBefore(element);
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