if (window.console) {
  console.log("Welcome to your Play application's JavaScript!");
}

$(document).ready(function(){

alert("hey");
function refreshTable() {
$.get("getCert", function(data, status){
                $("#showCertificate").html(data)
        });
}
refreshTable();

    $("#addCert").click(function(){
       var data = {"id":parseInt($("#id").val()),"userId":$("#userId").val(),"name":$("#name").val(),"year":parseInt($("#year").val()),"description":$("#description").val()};
               $.ajax({
                           type : 'POST',
                           url : "addcertificates",
                           dataType : "text/plain",
                           data : data
                       });
                $("#certificateAddModal").modal('hide');
    $("#certificateTable").remove();
    refreshTable();
});


});