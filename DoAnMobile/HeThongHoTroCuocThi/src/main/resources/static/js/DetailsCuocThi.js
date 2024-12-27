$(document).ready(function () {
    //Hiển thị từng quy định
    var currentQuyDinhPage = 0;
    var quyDinhPages = $(".listQuyDinh");
    var totalQuyDinhPages = quyDinhPages.length;

    function showQuyDinhPage(index) {
        quyDinhPages.hide();
        quyDinhPages.eq(index).show();
    }

    $("#prevBtnQuyDinh").click(function () {
        currentQuyDinhPage = (currentQuyDinhPage > 0) ? currentQuyDinhPage - 1 : totalQuyDinhPages - 1;
        showQuyDinhPage(currentQuyDinhPage);
    });

    $("#nextBtnQuyDinh").click(function () {
        currentQuyDinhPage = (currentQuyDinhPage < totalQuyDinhPages - 1) ? currentQuyDinhPage + 1 : 0;
        showQuyDinhPage(currentQuyDinhPage);
    });

    $("#showQuyDinhBtn").click(function () {
        $("#quyDinhContainer").show();
        $("#noiDungContainer").hide();
        showQuyDinhPage(currentQuyDinhPage);
    });

    //Hiển thị từng nội dung
    var currentNoiDungPage = 0;
    var noiDungPages = $(".listNoiDung");
    var totalNoiDungPages = noiDungPages.length;

    function showNoiDungPage(index) {
        noiDungPages.hide();
        noiDungPages.eq(index).show();
    }

    $("#prevBtnNoiDung").click(function () {
        currentNoiDungPage = (currentNoiDungPage > 0) ? currentNoiDungPage - 1 : totalNoiDungPages - 1;
        showNoiDungPage(currentNoiDungPage);
    });

    $("#nextBtnNoiDung").click(function () {
        currentNoiDungPage = (currentNoiDungPage < totalNoiDungPages - 1) ? currentNoiDungPage + 1 : 0;
        showNoiDungPage(currentNoiDungPage);
    });

    $("#showNoiDungBtn").click(function () {
        $("#noiDungContainer").show();
        $("#quyDinhContainer").hide();
        showNoiDungPage(currentNoiDungPage);
    });
});