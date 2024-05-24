$(document).ready(function () {

    $('#btn-cancelProject').click(function () {
        let projectId = $(this).attr('data-id');
        $('.btn-okModal').attr('href', '/project/cancel?id='+projectId);
    });

});