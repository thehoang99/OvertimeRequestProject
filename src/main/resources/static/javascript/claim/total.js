$(document).ready(function () {
    $(document).on('click', '#btn-claimDetail', function () {
        let recordId = $(this).data('id');
       $.ajax({
           url: '/claim/detail',
           type: 'GET',
           data: {id: recordId},
           success: function (data) {
               $('#modal-body-content').html(data);
           }
       })
    });

    $(document).on('click', '#btn-cancelClaim', function () {
        let claimId = $(this).attr('data-id');
        let cancelAndSubmitModal = $('#cancelAndSubmitClaim');

        cancelAndSubmitModal.find('.modal-title').text('Cancel Claim');
        cancelAndSubmitModal.find('.modal-body').html(`
            <p>This action will Cancel Claim.</p>
            <p>Please click ‘OK’ to cancel the claim or ‘Close’ to close the dialog.</p
       `)

       $('.btn-okModal').attr('href', '/claim/myClaim/cancel?id='+claimId);
    });

    $(document).on('click', '#btn-submitClaim', function () {
        let claimId = $(this).attr('data-id');
        let cancelAndSubmitModal = $('#cancelAndSubmitClaim');

        cancelAndSubmitModal.find('.modal-title').text('Submit Claim');
        cancelAndSubmitModal.find('.modal-body').html(`
            <p>This action will Submit Claim.</p>
            <p>Please click ‘OK’ to submit the claim or ‘Close’ to close the dialog.</p
       `)

        $('.btn-okModal').attr('href', '/claim/myClaim/submit?id='+claimId);
    });

});