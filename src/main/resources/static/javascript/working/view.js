$(document).ready(function () {

    let projectEle = $('#viewWorking__project');
    let projectId = projectEle.val();

    function sendProjectId(projectId) {
        $.ajax({
            url: '/working/workingDetail?projectId='+projectId,
            success: function(data) {
                $('#working-detail').html(data);
            }
        });
    }

    sendProjectId(projectId);
    projectEle.change(function () {
       let projectId = this.value;
       sendProjectId(projectId);
    });

    $(document).on('click', '#btn-deleteWorking', function () {
        let workingId = $(this).attr('data-id');
        $('.btn-okModal').attr('href', '/working/delete?id='+workingId);
    });


});