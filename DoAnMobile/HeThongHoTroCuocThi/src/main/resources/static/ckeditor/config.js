

CKEDITOR.editorConfig = function( config ) {
    config.filebrowserBrowseUrl = "/ckfinder/ckfinder.html";
    config.filebrowserImageUrl = "/ckfinder/ckfinder.html?type=Images";
    config.filebrowserFlashUrl = "/ckfinder/ckfinder.html?type=Flash";
    config.filebrowserUploadUrl = "/ckfinder/core/connector/aspx/connector.aspx?command=QuickUpload&type=Files";
    config.filebrowserImageUploadUrl = "/ckfinder/core/connector/aspx/connector.aspx?command=QuickUpload&type=Images";
    config.filebrowserFlashUploadUrl = "/ckfinder/core/connector/aspx/connector.aspx?command=QuickUpload&type=Flash";

    config.extraPlugins = 'youtube';
    config.youtube_responsive = true;
};
