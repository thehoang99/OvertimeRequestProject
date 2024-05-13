$(document).ready(function () {

    let staffNameField = $('#staffName');
    loadDetailInfo(staffNameField.val());

    staffNameField.change(function () {
        let staffId = this.value;
        loadDetailInfo(staffId);
    })

    function loadDetailInfo(staffId) {
        $.ajax({
           url:  "/staff/viewDetail?id="+staffId,
            success: function (data) {
               $('#viewDetail').html(data);
            }
        });
    }

});