$(document).ready(function () {

    function calculateDuration() {
        let startDate = $('#financeReviewClaim__startDate');
        let endDate = $('#financeReviewClaim__endDate');
        let duration = $('#financeReviewClaim__duration');

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

    function showDay() {
        let dateInput = $('#financeReviewClaim__date');
        let dayInput = $('#financeReviewClaim__day');

        let selectedDateObj = new Date(dateInput.val());
        let option = {weekday: 'long'};
        let day = selectedDateObj.toLocaleDateString('en-US', option);

        dayInput.val(day);
    }

    calculateDuration();
    showDay();

    $(document).on('click', '#btn-paid',function () {
        $('#financeReviewClaimForm').attr('action', '/claim/financeReview/paid');
        $('#modal-title').html('Paid Claim');
        $('#modal-content').html(`
           <p>This action will paid Claim.</p>
           <p>Please click 'OK' to paid the claim or 'Close' to close the dialog</p>
       `);
    });

    $(document).on('click', '#btn-reject',function () {
        $('#financeReviewClaimForm').attr('action', '/claim/financeReview/reject');
        $('#modal-title').html('Reject Claim');
        $('#modal-content').html(`
           <p>This action will reject Claim.</p>
           <p>Please click 'OK' to reject the claim or 'Close' to close the dialog</p>
       `);
    });
});

$('#financeReviewClaimForm').validate({
    errorPlacement: function (error, element) {
        error.insertAfter(element);
    },
    rules: {
        remarks: {
            required: true
        }
    },
    messages: {
        remarks: {
            required: "Remarks must be filled"
        }
    },
    submitHandler: function (form) {
        form.submit();
    }
});



