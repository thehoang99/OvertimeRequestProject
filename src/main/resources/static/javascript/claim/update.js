$(document).ready(function () {

    let dateInput = $('#updateClaim__date');

    function showDay() {
        let dayInput = $('#updateClaim__day');
        let selectedDateObj = new Date(dateInput.val());
        let option = {weekday: 'long'};
        let day = selectedDateObj.toLocaleDateString('en-US', option);

        dayInput.val(day);
    }

    function calculateDuration() {
        let startDate = $('#updateClaim__startDate');
        let endDate = $('#updateClaim__endDate');
        let duration = $('#updateClaim__duration');

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

    function calculateTotalHours() {
        let fromTime = $('#updateClaim__fromTime');
        let toTime = $('#updateClaim__toTime');
        let totalHours = $('#updateClaim__totalHours');

        let fromTimeObj = new Date('1900-01-01T' + fromTime.val() + 'Z');
        let toTimeObj = new Date('1900-01-01T' + toTime.val() + 'Z');
        let timeDiff = toTimeObj - fromTimeObj;
        let totalHoursVal = Math.round(timeDiff / (60 * 60 * 1000));

        totalHours.val(totalHoursVal);
    }

    showDay();
    calculateDuration();

    dateInput.change(function () {
       showDay();
    });

    $('#updateClaim__fromTime, #updateClaim__toTime').change(function () {
       calculateTotalHours();
    });

    $('#updateClaimForm').validate({
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
            status: {
                required: true
            },
            date: {
                required: true,
                isInDuration: true,
                isNotExpired: true
            },
            fromTime: {
                required: true
            },
            toTime: {
                required: true,
                isAfterFromTime: true
            },
            totalHours: {
                required: true
            },
            remarks: {
                required: true
            }
        },
        messages: {
            status: {
                required: "Claim status must be filled"
            },
            date: {
                required: "Claim date must be filled",
                isInDuration: "Claim date must be within duration of project",
                isNotExpired:  "Claim date is only created while you are in the project"
            },
            fromTime: {
                required: "From time must be filled"
            },
            toTime: {
                required: "To time must be filled",
                isAfterFromTime: "To time must be after From time",
            },
            totalHours: {
                required: "Total Overtime Hours must be filled"
            },
            remarks: {
                required: "Remarks must be filled"
            }
        },
        submitHandler: function (form) {
            form.submit();
        }
    });

    $.validator.methods.isInDuration = function (value) {
        let startDate = $('#updateClaim__startDate').val();
        let endDate = $('#updateClaim__endDate').val();
        return value >= startDate && value <= endDate;
    }

    $.validator.methods.isNotExpired = function (value) {
        let joinDate = $('#updateClaim__joinedDate').val();
        let leftDate = $('#updateClaim__leftDate').val();
        return value >= joinDate && (leftDate === "" || value <= leftDate);
    }

    $.validator.methods.isAfterFromTime = function (value) {
        let fromTime = $('#updateClaim__fromTime').val();
        return value > fromTime;
    }

});