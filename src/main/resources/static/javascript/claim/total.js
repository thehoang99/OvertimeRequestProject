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

});