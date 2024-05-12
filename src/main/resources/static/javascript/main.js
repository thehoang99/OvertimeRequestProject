$(document).ready(function () {
    $('#form-login').validate({
        errorPlacement: function (error, element) {
           error.insertAfter(element);
        },

        rules: {
            username: {
                required: true
            },
            password: {
                required: true
            }
        },

        messages: {
            username: {
                required: "Please enter Username"
            },
            password: {
                required: "Please enter Password"
            }
        },

        submitHandler: function (form) {
            form.submit();
        }

    });

    $('#form-login__email, #form-login__password').change(function () {
        $('#form-login__message').children().remove();
    });

});