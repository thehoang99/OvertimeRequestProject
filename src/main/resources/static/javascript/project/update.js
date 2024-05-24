$(document).ready(function () {

    let startDate = $('#updateProject__startDate');
    let endDate = $('#updateProject__endDate');
    let duration = $('#updateProject__duration');

    let today = new Date();
    let todayFormat = today.toISOString().substring(0, 10);

    function calculateDuration() {
        let startDateObj = new Date(startDate.val());
        let endDateObj = new Date(endDate.val());

        if (!isNaN(startDateObj.getTime()) && !isNaN(endDateObj.getTime())) {
            let timeDuration = endDateObj - startDateObj;
            let dateDuration = Math.ceil(timeDuration / (24 * 60 * 60 * 1000));
            duration.val(`${dateDuration + 1} days`);
        } else {
            duration.val('');
        }
    }
    calculateDuration();

    endDate.attr('min', startDate.val());

    startDate.change(function () {
        let newStartDate = startDate.val();
        endDate.attr('min', newStartDate);
        calculateDuration();
    });

    endDate.change(function () {
        calculateDuration();
    });

    $('#updateProjectForm').validate({
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
            code: {
                required: true,
                minlength: 3,
                maxlength: 10
            },
            name: {
                required: true
            },
            startDate: {
                required: true
            },
            endDate: {
                required: true
            }
        },
        messages: {
            code: {
                required: "Project code must be filled",
                minlength: "Project name must be at least 3 characters",
                maxlength: "Project name must be at most 10 characters"
            },
            name: {
                required: "Project name must be filled"
            },
            startDate: {
                required: "Project start date must be filled"
            },
            endDate: {
                required: "Project end date must be filled"
            }
        },
        submitHandler: function (form) {
            form.submit();
        }
    });

});