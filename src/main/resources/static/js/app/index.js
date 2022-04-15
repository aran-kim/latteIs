var main = {
    init: function(){
        var _this = this;

        $('#btn-send').on('click', function(){
            alert('인증 번호를 발송했습니다.');
            var phoneNumber = $("#phoneNumber").val();
            _this.send(phoneNumber);
        });

    },
    send : function(phoneNumber){
        $.ajax({
            url: "/check/sendSMS",
            type : "GET",
            cache : false,
            data : { phoneNumber : phoneNumber},
            success: function(data){
                if(data == "error"){
                    alert("휴대폰 번호가 올바르지 않습니다.")
                        $("#phoneNumber").attr("autofocus",true);
                }
                else{
                    $("#numStr").attr("disabled",false);
                    $("#btn-send-check").css("display","inline-block");
                    $("#phoneNumber").attr("readonly", true);
                }
            }
        })
    }

};

main.init();