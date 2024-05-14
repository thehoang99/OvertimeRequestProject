$(document).ready(function () {
    function showWorkingDetail(workingId, callback) {
        $.ajax({
           url: '/claim/workingDetail?workingId='+workingId,
            success: function (data) {
               $('#workingDetail').html(data);
               callback();
            }
        });
    }

    function calculateDuration() {
        let startDate = $('#createClaim__startDate');
        let endDate = $('#createClaim__endDate');
        let duration = $('#createClaim__duration');

        let startDateObj = new Date(startDate.val());
        let endDateObj = new Date(endDate.val());

        if (!isNaN(startDateObj.getDate()) && !isNaN(endDateObj.getDate())) {
            let timeDuration = endDateObj - startDateObj;
            let dateDuration = Math.ceil(timeDuration / (24 * 60 * 60 * 1000));
            duration.val(`${dateDuration + 1} days`);
        } else {
            duration.val('');
        }
    }

    let dateInput = $('#creatClaim__date');

    function showDay() {
        let dayInput = $('#creatClaim__day');
        let selectedDateObj = new Date(dateInput.val());
        let option = {weekday: 'long'};
        let day = selectedDateObj.toLocaleDateString('en-US', option);

        dayInput.val(day);
    }

    function calculateTotalHours() {
        let fromTimeVal = $('#creatClaim__fromTime').val();
        let toTimeVal = $('#creatClaim__toTime').val();
        let totalHoursInput = $('#creatClaim__totalHours');

        let fromTimeObj = new Date('1900-01-01T' + fromTimeVal + 'Z');
        let toTimeObj = new Date('1900-01-01T' + toTimeVal + 'Z');
        let totalHours = (toTimeObj - fromTimeObj) / (60 * 60 * 1000);
        totalHours = Math.round(totalHours);

        totalHoursInput.val(totalHours);
    }

    let selectedWorking = $('#createClaim__projectName');
    showWorkingDetail(selectedWorking.val(), calculateDuration);

    selectedWorking.change(function () {
        let workingId = this.value;
        showWorkingDetail(workingId, calculateDuration);
    });

    dateInput.change(function () {
        showDay();
    });

    $('#creatClaim__fromTime, #creatClaim__toTime').change(function () {
        calculateTotalHours();
    });

    $('#createClaimForm').validate({
        errorPlacement: function (error, element) {
            error.insertAfter(element);
        },
        rules: {
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
            remarks: {
                required: true
            }
        },
        messages: {
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
            remarks: {
                required: "Remarks must be filled"
            }
        },
        submitHandler: function (form) {
            form.submit();
        }
    });

    $.validator.methods.isInDuration = function (value) {
        let startDate = $('#createClaim__startDate').val();
        let endDate = $('#createClaim__endDate').val();
        return value >= startDate && value <= endDate;
    }

    $.validator.methods.isNotExpired = function (value) {
        let joinDate = $('#createClaim__joinedDate').val();
        let leftDate = $('#createClaim__leftDate').val();
        return value >= joinDate && (leftDate === "" || value <= leftDate);
    }

    $.validator.methods.isAfterFromTime = function (value) {
        let fromTime = $('#creatClaim__fromTime').val();
        return value > fromTime;
    }

});