$(document).ready(function () {

    let selectedProject = $('#selectedProject');
    let startDate = selectedProject.data('start-date');
    let endDate = selectedProject.data('end-date');

    let joinDate = $('#updateWorking__joinDate');
    let leftDate = $('#updateWorking__leftDate');

    joinDate.attr('min', startDate);
    joinDate.attr('max', endDate);
    leftDate.attr('min', startDate);
    leftDate.attr('max', endDate);

    joinDate.change(function () {
       let newJoinDate = this.value;
        leftDate.attr('min', newJoinDate);
    });

    $('#updateWorkingForm').validate({
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
            staffId: {
                required: true
            },
            projectId: {
                required: true
            },
            jobRankId: {
                required: true
            },
            startDate: {
                required: true
            }
        },
        messages: {
            staffId: {
                required: "Staff Name must be filled"
            },
            projectId: {
                required: "Project Name must be filled"
            },
            jobRankId: {
                required: "Job Rank must be filled"
            },
            startDate: {
                required: "Join Date must be filled"
            }
        },
        submitHandler: function (form) {
            form.submit();
        }
    });

});