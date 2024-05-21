$(document).ready(function () {

    function calculateDuration() {
        let startDate = $('#pmReviewClaim__startDate');
        let endDate = $('#pmReviewClaim__endDate');
        let duration = $('#pmReviewClaim__duration');

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
        let dateInput = $('#pmReviewClaim__date');
        let dayInput = $('#pmReviewClaim__day');

        let selectedDateObj = new Date(dateInput.val());
        let option = {weekday: 'long'};
        let day = selectedDateObj.toLocaleDateString('en-US', option);

        dayInput.val(day);
    }

    calculateDuration();
    showDay();

    $(document).on('click', '#btn-approve',function () {
       $('#pmReviewClaimForm').attr('action', '/claim/pmReview/approve');
       $('#modal-title').html('Approve Claim');
       $('#modal-content').html(`
           <p>This action will approve Claim.</p>
           <p>Please click 'OK' to approve the claim or 'Close' to close the dialog</p>
       `);
    });

    $(document).on('click', '#btn-return', function () {
        $('#pmReviewClaimForm').attr('action', '/claim/pmReview/return');
        $('#modal-title').html('Return Claim');
        $('#modal-content').html(`
           <p>This action will return Claim.</p>
           <p>Please click 'OK' to return the claim or 'Close' to close the dialog</p>
       `);
    });

    $(document).on('click', '#btn-reject',function () {
        $('#pmReviewClaimForm').attr('action', '/claim/pmReview/reject');
        $('#modal-title').html('Reject Claim');
        $('#modal-content').html(`
           <p>This action will reject Claim.</p>
           <p>Please click 'OK' to reject the claim or 'Close' to close the dialog</p>
       `);
    });

    $('#pmReviewClaimForm').validate({
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
});





