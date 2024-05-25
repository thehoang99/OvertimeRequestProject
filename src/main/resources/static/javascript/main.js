$(document).ready(function () {
    $('#form-login').validate({
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

    function highlightSelectedButton() {
        let buttons = document.querySelectorAll('#leftNavBar .btn');
        let currentURL = window.location.href;
        buttons.forEach(function (btn) {
            let hrefOfButton = btn.getAttribute('href');
            if (currentURL.includes(hrefOfButton)) {
                btn.classList.add('highlightedButton');
            } else {
                btn.classList.remove('highlightedButton');
            }
        })
    }
    highlightSelectedButton();

    const patterns = {
        "btn-myClaims": [/\/claim\/my/, /\/claim\/update/],
        "btn-claimsForApproval": [/\/claim\/pendingApproval/, /\/claim\/approvedOrPaid/],
        "btn-claimsForFinance": [/\/claim\/approved/, /\/claim\/paid/],
        "btn-adminManagement": [/\/staff/, /\/project/, /\/working/],
        "btn-staffInformation": [/\/staff/],
        "btn-projectInformation": [/\/project/],
        "btn-workingInformation": [/\/working/]
    };

    Object.entries(patterns).forEach(([buttonId, patternURLs]) => {
        let currentURL = window.location.href;
        patternURLs.forEach(patternURL => {
            if (patternURL.test(currentURL)) {
                $("#" + buttonId).click();
            }
        });
    });

    // const myClaimsPattern = [/\/claim\/my/, /\/claim\/update/];
    // const approvalPatterns = [/\/claim\/pendingApproval/, /\/claim\/approvedOrPaid/];
    // const financePatterns = [/\/claim\/approved/, /\/claim\/paid/];
    // const adminPatterns = [/\/staff/, /\/project/];
    // const staffPatterns = [/\/staff/];
    // const projectPatterns = [/\/project/];

    // showCurrentLeftBar(myClaimsPattern, "btn-myClaims");
    // showCurrentLeftBar(approvalPatterns, "btn-claimsForApproval");
    // showCurrentLeftBar(financePatterns, "btn-claimsForFinance");
    // showCurrentLeftBar(adminPatterns, "btn-adminManagement");
    // showCurrentLeftBar(staffPatterns, "btn-staffInformation");
    // showCurrentLeftBar(projectPatterns, "btn-projectInformation");

    // function showCurrentLeftBar(patternURLs, buttonId) {
    //     let currentURL = window.location.href;
    //     patternURLs.forEach(function (patternURL) {
    //         if (patternURL.test(currentURL)) {
    //             $("#" + buttonId).click();
    //         }
    //     })
    // }


    function calculateDuration() {
        $('.myClaim-row').each(function () {
            let startDateVal = $(this).find('.startDate').val();
            let endDateVal = $(this).find('.endDate').val();
            let duration = $(this).find('.duration');

            let startDateObj = new Date(startDateVal);
            let endDateObj = new Date(endDateVal);

            if (!isNaN(startDateObj.getTime()) && !isNaN(endDateObj.getTime())) {
                let timeDuration = endDateObj - startDateObj;
                let dateDuration = Math.ceil(timeDuration / (24 * 60 * 60 * 1000));
                duration.text(`${dateDuration + 1} days`);
            } else {
                duration.text('');
            }

        });
    }
    calculateDuration();

});