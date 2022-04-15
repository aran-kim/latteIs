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
                $("#btn-send-check").click(function(){
                    var numStr = $("#numStr").val();
                    if(data == numStr){
                        alert("성공이용!");
                        window.location.href = "/join";
                    }
                    else{
                        alert("아닌데용;");
                    }
                })
            }
        })
    }

};

main.init();